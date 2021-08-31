package org.opencds.cqf.cql.engine.retrieve;

import java.util.ArrayList;
import java.util.List;

public class Request {
    private String dataType;
    public String getDataType() {
        return dataType;
    }
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    private String templateId;
    public String getTemplateId() {
        return templateId;
    }
    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    private ContextFilter context;
    public ContextFilter getContext() {
        return this.context;
    }
    public void setContext(ContextFilter context) {
        this.context = context;
    }

    private String includePath;
    public String getIncludePath() {
        return this.includePath;
    }
    public void setIncludePath(String includePath) {
        this.includePath = includePath;
    }

    private boolean includeReverse;
    public boolean getIncludeReverse() {
        return this.includeReverse;
    }
    public void setIncludeReverse(boolean includeReverse) {
        this.includeReverse = includeReverse;
    }

    private List<TerminologyFilter> codes = new ArrayList<TerminologyFilter>();
    public Iterable<TerminologyFilter> getCodes() {
        return codes;
    }
    public void addCodes(TerminologyFilter tf) {
        codes.add(tf);
    }

    private List<DateRangeFilter> dateRanges = new ArrayList<DateRangeFilter>();
    public Iterable<DateRangeFilter> getDateRanges() {
        return dateRanges;
    }
    public void addDateRange(DateRangeFilter drf) {
        dateRanges.add(drf);
    }

    private List<OtherFilter> values = new ArrayList<OtherFilter>();
    public Iterable<OtherFilter> getValues() {
        return values;
    }
    public void addValue(OtherFilter of) {
        values.add(of);
    }

    private List<Request> includes = new ArrayList<Request>();
    public Iterable<Request> getIncludes() {
        return includes;
    }
    public void addInclude(Request request) {
        includes.add(request);
    }
}
