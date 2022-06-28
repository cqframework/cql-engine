package org.opencds.cqf.cql.engine.serializing.jaxb;

import org.opencds.cqf.cql.engine.elm.execution.ObjectFactoryEx;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

public class XmlCqlMapper {

    private static JAXBContext jaxbContext;

    public static JAXBContext getJaxbContext() {
        if (jaxbContext == null) {
            try {
                jaxbContext = JAXBContext.newInstance(ObjectFactoryEx.class);
            } catch (JAXBException e) {
                e.printStackTrace();
                throw new RuntimeException("Error creating JAXBContext - " + e.getMessage());
            }
        }
        return jaxbContext;
    }
}
