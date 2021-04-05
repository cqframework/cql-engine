package org.opencds.cqf.cql.engine.fhir.terminology;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.ValueSet;
import org.opencds.cqf.cql.engine.fhir.R4FhirTest;
import org.opencds.cqf.cql.engine.runtime.Code;
import org.opencds.cqf.cql.engine.terminology.CodeSystemInfo;
import org.opencds.cqf.cql.engine.terminology.ValueSetInfo;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


public class TestR4FhirTerminologyProvider extends R4FhirTest {

	private static final String TEST_DISPLAY = "Display";
	private static final String TEST_CODE = "425178004";
	private static final String TEST_SYSTEM = "http://snomed.info/sct";
	private static final String TEST_SYSTEM_VERSION = "2013-09";
	R4FhirTerminologyProvider provider;
	
	@BeforeMethod
	public void initializeProvider() {
		provider = new R4FhirTerminologyProvider(newClient());
	}
	
	@Test(expectedExceptions = UnsupportedOperationException.class)
	public void nonNullVersionUnsupported() {
		ValueSetInfo info = new ValueSetInfo();
		info.setId("urn:oid:Test");
		info.setVersion("1.0.0.");
		
		provider.resolveByUrl(info);
	}
	
	@Test(expectedExceptions = UnsupportedOperationException.class)
	public void nonNullCodesystemsUnsupported() {
		CodeSystemInfo codeSystem = new CodeSystemInfo();
		codeSystem.setId("SNOMED-CT");
		codeSystem.setVersion("2013-09");
		
		ValueSetInfo info = new ValueSetInfo();
		info.setId("urn:oid:Test");
		info.getCodeSystems().add(codeSystem);
		
		provider.resolveByUrl(info);
	}
	
	@Test
	public void urnOidPrefixIsStripped() {
		ValueSetInfo info = new ValueSetInfo();
		info.setId("urn:oid:Test");
		
		provider.resolveByUrl(info);
		assertEquals( info.getId(), "Test" );
	}
	
	@Test( expectedExceptions = IllegalArgumentException.class )
	public void moreThanOneURLSearchResultIsError() throws Exception {
		ValueSetInfo info = new ValueSetInfo();
		info.setId("http://localhost/fhir/ValueSet/1.2.3.4");
		
		ValueSet firstSet = new ValueSet();
		firstSet.setId("1");
		firstSet.setUrl(info.getId());
		
		ValueSet secondSet = new ValueSet();
		secondSet.setId("1");
		secondSet.setUrl(info.getId());
		
		mockFhirSearch("/ValueSet?url=" + urlencode(info.getId()) , firstSet, secondSet);
		
		provider.resolveByUrl(info);
	}
	
	@Test( expectedExceptions = IllegalArgumentException.class )
	public void zeroURLSearchResultIsError() throws Exception {
		ValueSetInfo info = new ValueSetInfo();
		info.setId("http://localhost/fhir/ValueSet/1.2.3.4");
		
		mockFhirSearch("/ValueSet?url=" + urlencode(info.getId()));
		
		provider.resolveByUrl(info);
	}
	
	@Test
	public void expandOperationReturnsCorrectCodesMoreThanZero() {
		ValueSetInfo info = new ValueSetInfo();
		info.setId("urn:oid:Test");
		
		ValueSet valueSet = new ValueSet();
		valueSet.setId("Test");
		valueSet.getExpansion().getContainsFirstRep().setSystem(TEST_SYSTEM).setCode(TEST_CODE);
		
		Parameters parameters = new Parameters();
		parameters.getParameterFirstRep().setName("return").setResource(valueSet);
		
		mockFhirPost("/ValueSet/Test/$expand", parameters);
		
		Iterable<Code> codes = provider.expand(info);
		
		List<Code> list = StreamSupport.stream(codes.spliterator(), false).collect(Collectors.toList());
		assertEquals( list.size(), 1 );
		assertEquals( list.get(0).getSystem(), TEST_SYSTEM );
		assertEquals( list.get(0).getCode(), TEST_CODE );
	}
	
	@Test
	public void inOperationReturnsTrueWhenFhirReturnsTrue() throws Exception {
		ValueSetInfo info = new ValueSetInfo();
		info.setId("urn:oid:Test");
		
		Code code = new Code();
		code.setSystem(TEST_SYSTEM);
		code.setCode(TEST_CODE);
		code.setDisplay(TEST_DISPLAY);
		
		Parameters parameters = new Parameters();
		parameters.getParameterFirstRep().setName("result").setValue(new BooleanType(true));
		
		mockFhirRead("/ValueSet/Test/$validate-code?code=" + urlencode(code.getCode()) + "&system=" + urlencode(code.getSystem()), parameters);
		
		boolean result = provider.in(code, info);
		assertTrue( result );
	}
	
	@Test
	public void inOperationReturnsFalseWhenFhirReturnsFalse() throws Exception {
		ValueSetInfo info = new ValueSetInfo();
		info.setId("urn:oid:Test");
		
		Code code = new Code();
		code.setSystem(TEST_SYSTEM);
		code.setCode(TEST_CODE);
		code.setDisplay(TEST_DISPLAY);
		
		Parameters parameters = new Parameters();
		parameters.getParameterFirstRep().setName("result").setValue(new BooleanType(false));
		
		mockFhirRead("/ValueSet/Test/$validate-code?code=" + urlencode(code.getCode()) + "&system=" + urlencode(code.getSystem()), parameters);
		
		boolean result = provider.in(code, info);
		assertFalse( result );
	}
	
	@Test
	public void inOperationHandlesNullSystem() throws Exception {
		ValueSetInfo info = new ValueSetInfo();
		info.setId("urn:oid:Test");
		
		Code code = new Code();
		code.setCode(TEST_CODE);
		code.setDisplay(TEST_DISPLAY);
		
		Parameters parameters = new Parameters();
		parameters.getParameterFirstRep().setName("result").setValue(new BooleanType(true));
		
		mockFhirRead("/ValueSet/Test/$validate-code?code=" + urlencode(code.getCode()), parameters);
		
		boolean result = provider.in(code, info);
		assertTrue( result );
	}
	
	@Test
	public void lookupOperationSuccess() throws Exception {
		CodeSystemInfo info = new CodeSystemInfo();
		info.setId(TEST_SYSTEM);
		info.setVersion(TEST_SYSTEM_VERSION);
		
		Code code = new Code();
		code.setCode(TEST_CODE);
		code.setSystem(TEST_SYSTEM);
		code.setDisplay(TEST_DISPLAY);
		
		Parameters parameters = new Parameters();
		parameters.addParameter().setName("name").setValue(new StringType(code.getCode()));
		parameters.addParameter().setName("version").setValue(new StringType(info.getVersion()));
		parameters.addParameter().setName("display").setValue(new StringType(code.getDisplay()));
		
		mockFhirPost("/CodeSystem/$lookup", parameters);
		
		Code result = provider.lookup(code, info);
		assertNotNull( result );
		assertEquals( result.getSystem(), code.getSystem() );
		assertEquals( result.getCode(), code.getCode() );
		assertEquals( result.getDisplay(), code.getDisplay() );
	}
	
	protected String urlencode(String value) throws UnsupportedEncodingException {
		return URLEncoder.encode(value, "utf-8");
	}
}
