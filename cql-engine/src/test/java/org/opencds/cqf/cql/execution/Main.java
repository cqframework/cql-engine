package org.opencds.cqf.cql.execution;

import org.opencds.cqf.cql.elm.execution.EquivalentEvaluator;
import org.opencds.cqf.cql.runtime.DateTime;
import org.opencds.cqf.cql.runtime.TemporalHelper;
import org.testng.Assert;

import javax.xml.bind.JAXBException;
import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) throws JAXBException {
        try{
        CqlDateTimeOperatorsTest test =new CqlDateTimeOperatorsTest();
     //   CqlStringOperatorsTest test =new CqlStringOperatorsTest();
        test.beforeEachTestMethod();
        test.testBefore();
        test.testAdd();
       // test.testUpper();
       //     test.testToString();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

//    public void test(){
//        try {
//            beforeEachTestMethod();
//            Context context = new Context(library);
//
//            // simple cache test -- watching behavior while stepping through tests
//            context.setExpressionCaching(true);
//            BigDecimal offset = TemporalHelper.getDefaultOffset();
//
//            Object result = context.resolveExpressionRef("DateTimeAdd2YearsByDays").evaluate(context);
//            System.out.println("Result:" + result);
//            System.out.println("Expected:" + new DateTime(offset, 2016));
//            // Assert.assertTrue(EquivalentEvaluator.equivalent(result, new DateTime(offset, 2016)));
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//
//    }
}
