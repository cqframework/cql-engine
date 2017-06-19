package org.opencds.cqf.cql.data.fhir;

import org.hl7.fhir.dstu3.model.*;
import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.runtime.DateTime;
import org.opencds.cqf.cql.runtime.Interval;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;


/**
 * Created by Bryn on 5/7/2016.
 */
public class FhirMeasureEvaluator {
    public MeasureReport evaluate(Context context, Measure measure, Patient patient, Date periodStart, Date periodEnd) {
        MeasureReport report = new MeasureReport();
        report.setMeasure(new Reference(measure));
        report.setPatient(new Reference(patient));
        Period reportPeriod = new Period();
        reportPeriod.setStart(periodStart);
        reportPeriod.setEnd(periodEnd);
        report.setPeriod(reportPeriod);
        report.setType(MeasureReport.MeasureReportType.INDIVIDUAL);

        Interval measurementPeriod = new Interval(DateTime.fromJavaDate(periodStart), true, DateTime.fromJavaDate(periodEnd), true);
        context.setParameter(null, "MeasurementPeriod", measurementPeriod);

        HashMap<String,Resource> resources = new HashMap<String, Resource>();

        // for each measure group
        for (Measure.MeasureGroupComponent group : measure.getGroup()) {
            MeasureReport.MeasureReportGroupComponent reportGroup = new MeasureReport.MeasureReportGroupComponent();
            reportGroup.setIdentifier(group.getIdentifier().copy()); // TODO: Do I need to do this copy? Will HAPI FHIR do this automatically?
            report.getGroup().add(reportGroup);
            for (Measure.MeasureGroupPopulationComponent population : group.getPopulation()) {
                // evaluate the criteria expression, should return true/false, translate to 0/1 for report
                Object result = context.resolveExpressionRef(population.getCriteria()).evaluate(context);
                int count = 0;
                if (result instanceof Boolean) {
                    count = (Boolean)result ? 1 : 0;
                }
                else if (result instanceof Iterable) {
                    for (Object item : (Iterable<Object>)result) {
                        count++;
                        if (item instanceof Resource) {
                            resources.put(((Resource)item).getId(), (Resource)item);
                        }
                    }
                }
                else if (result instanceof Resource) {
                	count++;
                	resources.put(((Resource)result).getId(), (Resource)result);
                }
                
                MeasureReport.MeasureReportGroupPopulationComponent populationReport = new MeasureReport.MeasureReportGroupPopulationComponent();
                populationReport.setCount(count);
                populationReport.setCode(population.getCode());
                reportGroup.getPopulation().add(populationReport);
            }
        }

        ArrayList<String> expressionNames = new ArrayList<String>();
        // HACK: Hijacking Supplemental data to specify the evaluated resources
        // In reality, this should be specified explicitly, but I'm not sure what else to do here....
        for (Measure.MeasureSupplementalDataComponent supplementalData : measure.getSupplementalData()) {
            expressionNames.add(supplementalData.getCriteria());
        }

        // TODO: Need to return both the MeasureReport and the EvaluatedResources Bundle
        FhirMeasureBundler bundler = new FhirMeasureBundler();
        //String[] expressionNameArray = new String[expressionNames.size()];
        //expressionNameArray = expressionNames.toArray(expressionNameArray);
        //org.hl7.fhir.dstu3.model.Bundle evaluatedResources = bundler.bundle(context, expressionNameArray);
        org.hl7.fhir.dstu3.model.Bundle evaluatedResources = bundler.bundle(resources.values());
        evaluatedResources.setId(UUID.randomUUID().toString());
        //String jsonString = fhirClient.getFhirContext().newJsonParser().encodeResourceToString(evaluatedResources);
        //ca.uhn.fhir.rest.api.MethodOutcome result = fhirClient.create().resource(evaluatedResources).execute();
        report.setEvaluatedResources(new Reference('#' + evaluatedResources.getId()));
        report.addContained(evaluatedResources);
        return report;
    }
}
