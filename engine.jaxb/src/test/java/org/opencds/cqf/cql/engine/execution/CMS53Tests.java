package org.opencds.cqf.cql.engine.execution;

import org.testng.annotations.Test;

import javax.xml.bind.JAXBException;
import java.io.IOException;

public class CMS53Tests {
    @Test
    public void testLibraryLoadXML() {
        try {
            CqlLibraryReader.read(CMS53Tests.class.getResourceAsStream("CMS53Draft/PrimaryPCIReceivedWithin90MinutesofHospitalArrival-7.0.001.xml"));
        } catch (IOException | JAXBException e) {
            throw new IllegalArgumentException("Error reading ELM: " + e.getMessage());
        }
    }

}
