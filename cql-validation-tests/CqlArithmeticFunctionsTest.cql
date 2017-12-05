library ArithmeticFunctions version '1'

context Population

define TestPlan: Plan(162)


//Abs
define AbsNull: Ok(Equivalent(Abs(null as Integer), null), 'AbsNull')
define Abs0: Ok(Equivalent(Abs(0), 0), 'Abs0')
define AbsNeg1: Ok(Equivalent(Abs(-1), 1), 'AbsNeg1')
define AbsNeg1Dec: Ok(Equivalent(Abs(-1.0), 1.0), 'AbsNeg1Dec')
define Abs0Dec: Ok(Equivalent(Abs(0.0), 0.0), 'Abs0Dec')
define Abs1cm: Ok(Equivalent(Abs(-1.0'cm'), 1.0'cm'), 'Abs1cm')

//Add
define AddNull: Ok(Equivalent(1 + null, null), 'AddNull')
define Add11: Ok(Equivalent(1 + 1, 2), 'Add11')
define Add1D1D: Ok(Equivalent(1.0 + 1.0, 2.0), 'Add1D1D')
define Add1Q1Q: Ok(Equivalent(1'g/cm3' + 1'g/cm3', 2.0'g/cm3'), 'Add1Q1Q')
define AddIAndD: Ok(Equivalent(1 + 2.0, 3.0), 'AddIAndD')

//Ceiling
define CeilingNull: Ok(Equivalent(Ceiling(null as Decimal), null), 'CeilingNull')
define Ceiling1D: Ok(Equivalent(Ceiling(1.0), 1), 'Ceiling1D')
define Ceiling1D1: Ok(Equivalent(Ceiling(1.1), 2), 'Ceiling1D1')
define CeilingNegD1: Ok(Equivalent(Ceiling(-0.1), 0), 'CeilingNegD1')
define CeilingNeg1: Ok(Equivalent(Ceiling(-1.0), -1), 'CeilingNeg1')
define CeilingNeg1D1: Ok(Equivalent(Ceiling(-1.1), -1), 'CeilingNeg1D1')
define Ceiling1I: Ok(Equivalent(Ceiling(1), 1), 'Ceiling1I')

//Divide
define DivideNull: Ok(Equivalent(1 / null, null), 'DivideNull')
define Divide10: Ok(Equivalent(1 / 0, null), 'Divide10')
define Divide01: Ok(Equivalent(0 / 1, 0.0), 'Divide01')
define Divide11: Ok(Equivalent(1 / 1, 1.0), 'Divide11')
define Divide1d1d: Ok(Equivalent(1.0 / 1.0, 1.0), 'Divide1d1d')
define Divide103: Ok(Equivalent(10 / 3, 3.33333333), 'Divide103')
define Divide1Q1: Ok(Equivalent(1'g/cm3' / 1.0, 1.0'g/cm3'), 'Divide1Q1')
//define Divide1Q1Q: Ok(Equivalent(1'g/cm3' / 1'g/cm3', 1.0), 'Divide1Q1Q')
define Divide1Q1Q: Todo(false, 'Divide1Q1Q', 'make Translator recognize unit changes on Divide()')
define Divide10I5D: Ok(Equivalent(10 / 5.0, 2.0), 'Divide10I5D')
define Divide10I5I: Ok(Equivalent(10 / 5, 2.0), 'Divide10I5I')
define Divide10Q5I: Ok(Equivalent(10.0 'g' / 5, 2.0'g'), 'Divide10Q5I')

//Floor
define FloorNull: Ok(Equivalent(Floor(null as Decimal), null), 'FloorNull')
define Floor1: Ok(Equivalent(Floor(1), 1), 'Floor1')
define Floor1D: Ok(Equivalent(Floor(1.0), 1), 'Floor1D')
define Floor1D1: Ok(Equivalent(Floor(1.1), 1), 'Floor1D1')
define FloorNegD1: Ok(Equivalent(Floor(-0.1), -1), 'FloorNegD1')
define FloorNeg1: Ok(Equivalent(Floor(-1.0), -1), 'FloorNeg1')
define FloorNeg1D1: Ok(Equivalent(Floor(-1.1), -2), 'FloorNeg1D1')
define Floor2I: Ok(Equivalent(Floor(2), 2), 'Floor2I')

//Exp
define ExpNull  : Ok(Equivalent(Exp(null as Decimal), null), 'ExpNull')
define Exp0  : Ok(Equivalent(Exp(0), 1.0), 'Exp0')
define ExpNeg0  : Ok(Equivalent(Exp(-0), 1.0), 'ExpNeg0')
define Exp1  : Ok(Equivalent(Exp(1), 2.718281828459045090795598298427648842334747314453125), 'Exp1')
define ExpNeg1  : Ok(Equivalent(Exp(-1), 0.367879441171442334024277442949824035167694091796875), 'ExpNeg1')
//define Exp1000  : Ok(Equivalent(Exp(1000), ...), 'Exp1000')
define Exp1000  : Skipped(true, 'Exp1000', 'can not trap expected error: Results in positive infinity')
//define Exp1000D  : Ok(Equivalent(Exp(1000.0), ...), 'Exp1000D')
define Exp1000D  : Skipped(true, 'Exp1000D', 'can not trap expected error: Results in positive infinity')

//Log
define LogNullNull  : Ok(Equivalent(Log(null, null), null), 'LogNullNull')
define Log1BaseNull  : Ok(Equivalent(Log(1, null), null), 'Log1BaseNull')
define Log1Base1  : Ok(Equivalent(Log(1, 1), 0.0), 'Log1Base1')
define Log1Base2  : Ok(Equivalent(Log(1, 2), 0.0), 'Log1Base2')
define Log1Base100  : Ok(Equivalent(Log(1, 100), 0.0), 'Log1Base100')
define Log16Base2  : Ok(Equivalent(Log(16, 2), 4.0), 'Log16Base2')
define LogD125Base2  : Ok(Equivalent(Log(0.125, 2), -3.0), 'LogD125Base2')

//Ln
define LnNull  : Ok(Equivalent(Ln(null), null), 'LnNull')
//define Ln0  : Ok(Equivalent(Ln(0), ...), 'Ln0')
define Ln0  : Skipped(true, 'Ln0', 'can not trap expected error: Results in negative infinity')
//define LnNeg0  : Ok(Equivalent(Ln(-0), ...), 'LnNeg0')
define LnNeg0  : Skipped(true, 'LnNeg0', 'can not trap expected error: Results in negative infinity')
define Ln1  : Ok(Equivalent(Ln(1), 0.0), 'Ln1')
define LnNeg1  : Ok(Equivalent(Ln(-1), null), 'LnNeg1')
define Ln1000  : Ok(Equivalent(Ln(1000), 6.907755278982136815102421678602695465087890625), 'Ln1000')
define Ln1000D  : Ok(Equivalent(Ln(1000.0), 6.907755278982136815102421678602695465087890625), 'Ln1000D')

//MinValue
//define IntegerMinValue: Ok(Equivalent(minimum Integer, -2147483648), 'IntegerMinValue')
define IntegerMinValue: Todo(false, 'IntegerMinValue', 'make Engine parse -2147483648 holistically not as a negated positive')
define DecimalMinValue: Ok(Equivalent(minimum Decimal, -9999999999999999999999999999.99999999), 'DecimalMinValue')
//OBSOLETE define QuantityMinValue: Ok(Equivalent((minimum Quantity),(-9999999999999999999999999999.99999999)), 'QuantityMinValue')
define DateTimeMinValue: Ok(Equivalent(minimum DateTime, @0001-01-01T00:00:00.000), 'DateTimeMinValue')
define TimeMinValue: Ok(Equivalent(minimum Time, @T00:00:00.000), 'TimeMinValue')

//MaxValue
define IntegerMaxValue: Ok(Equivalent(maximum Integer, 2147483647), 'IntegerMaxValue')
define DecimalMaxValue: Ok(Equivalent(maximum Decimal, 9999999999999999999999999999.99999999), 'DecimalMaxValue')
//OBSOLETE define QuantityMaxValue: Ok(Equivalent((maximum Quantity),(9999999999999999999999999999.99999999)), 'QuantityMaxValue')
define DateTimeMaxValue: Ok(Equivalent(maximum DateTime, @9999-12-31T23:59:59.999), 'DateTimeMaxValue')
define TimeMaxValue: Ok(Equivalent(maximum Time, @T23:59:59.999), 'TimeMaxValue')

//Modulo
define ModuloNull  : Ok(Equivalent(1 mod null, null), 'ModuloNull')
define Modulo0By0  : Ok(Equivalent(0 mod 0, null), 'Modulo0By0')
define Modulo4By2  : Ok(Equivalent(4 mod 2, 0), 'Modulo4By2')
define Modulo4DBy2D  : Ok(Equivalent(4.0 mod 2.0, 0.0), 'Modulo4DBy2D')
define Modulo10By3  : Ok(Equivalent(10 mod 3, 1), 'Modulo10By3')
define Modulo10DBy3D  : Ok(Equivalent(10.0 mod 3.0, 1.0), 'Modulo10DBy3D')
define Modulo10IBy3D: Ok(Equivalent(10 mod 3.0, 1.0), 'Modulo10IBy3D')
define ModuloDResult: Ok(Equivalent(3.5 mod 3, 0.5), 'ModuloDResult')

//Multiply
define MultiplyNull: Ok(Equivalent(1 * null, null), 'MultiplyNull')
define Multiply1By1 : Ok(Equivalent(1 * 1, 1), 'Multiply1By1')
define Multiply1DBy2D : Ok(Equivalent(1.0 * 2.0, 2.0), 'Multiply1DBy2D')
define Multiply1IBy2D: Ok(Equivalent(1 * 2.0, 2.0), 'Multiply1IBy2D')
define Multiply1CMBy2CM : Todo(Equivalent((1.0 'cm' * 2.0 'cm'),(2.0'cm2')), 'Multiply1CMBy2CM', 'make the units multiply')

//Negate
define NegateNull: Ok(Equivalent(-(null as Integer), null), 'NegateNull')
define Negate0 : Ok(Equivalent(-0, 0), 'Negate0')
define NegateNeg0 : Ok(Equivalent(-(-0), 0), 'NegateNeg0')
define Negate1 : Ok(Equivalent(-1, -1), 'Negate1')
define NegateNeg1 : Ok(Equivalent(-(-1), 1), 'NegateNeg1')
define Negate0D : Ok(Equivalent(-(0.0), 0.0), 'Negate0D')
define NegateNeg0D : Ok(Equivalent(-(-0.0), 0.0), 'NegateNeg0D')
define Negate1D : Ok(Equivalent(-(1.0), -1.0), 'Negate1D')
define NegateNeg1D : Ok(Equivalent(-(-1.0), 1.0), 'NegateNeg1D')
define Negate1CM : Ok(Equivalent(-(1'cm'), -1.0'cm'), 'Negate1CM')

//Predecessor
define PredecessorNull: Ok(Equivalent(predecessor of (null as Integer), null), 'PredecessorNull')
define PredecessorOf0: Ok(Equivalent(predecessor of 0, -1), 'PredecessorOf0')
define PredecessorOf1: Ok(Equivalent(predecessor of 1, 0), 'PredecessorOf1')
define PredecessorOf1D: Ok(Equivalent(predecessor of 1.0, 0.99999999), 'PredecessorOf1D')
define PredecessorOf101D: Ok(Equivalent(predecessor of 1.01, 1.00999999), 'PredecessorOf101D')
define PredecessorOf1QCM: Ok(Equivalent(predecessor of 1.0 'cm', 0.99999999'cm'), 'PredecessorOf1QCM')
define PredecessorOfJan12000: Ok(Equivalent(predecessor of DateTime(2000,1,1), @1999-12-31), 'PredecessorOfJan12000')
define PredecessorOfNoon: Ok(Equivalent(predecessor of @T12:00:00.000, @T11:59:59.999), 'PredecessorOfNoon')
//define PredecessorUnderflowDt: Ok(Equivalent(predecessor of DateTime(0001, 1, 1, 0, 0, 0, 0), ...), 'PredecessorUnderflowDt')
define PredecessorUnderflowDt : Skipped(true, 'PredecessorUnderflowDt', 'can not trap expected error: The result of the predecessor operation precedes the minimum value allowed for the type')
//define PredecessorUnderflowT: Ok(Equivalent(predecessor of @T00:00:00.000, ...), 'PredecessorUnderflowT')
define PredecessorUnderflowT : Skipped(true, 'PredecessorUnderflowT', 'can not trap expected error: The result of the predecessor operation precedes the minimum value allowed for the type')

//Power
define PowerNullToNull: Ok(Equivalent(Power(null as Integer, null as Integer), null), 'PowerNullToNull')
define Power0To0: Ok(Equivalent(Power(0, 0), 1), 'Power0To0')
define Power2To2: Ok(Equivalent(Power(2, 2), 4), 'Power2To2')
define PowerNeg2To2: Ok(Equivalent(Power(-2, 2), 4), 'PowerNeg2To2')
//define Power2ToNeg2: Ok(Equivalent(Power(2, -2), 0.25), 'Power2ToNeg2')
define Power2ToNeg2 : Todo(false, 'Power2ToNeg2', 'fix Engine error: Cannot call ToDecimal operator with argument of type java.math.BigDecimal')
define Power2DTo2D: Ok(Equivalent(Power(2.0, 2.0), 4.0), 'Power2DTo2D')
define PowerNeg2DTo2D: Ok(Equivalent(Power(-2.0, 2.0), 4.0), 'PowerNeg2DTo2D')
define Power2DToNeg2D: Ok(Equivalent(Power(2.0, -2.0), 0.25), 'Power2DToNeg2D')
define Power2DTo2: Ok(Equivalent(Power(2.0, 2), 4.0), 'Power2DTo2')
define Power2To2D: Ok(Equivalent(Power(2, 2.0), 4.0), 'Power2To2D')
define Power2To4: Ok(Equivalent(2^4, 16), 'Power2To4')
define Power2DTo4D: Ok(Equivalent(2.0^4.0, 16.0), 'Power2DTo4D')

//Round
define RoundNull: Ok(Equivalent(Round(null as Decimal), null), 'RoundNull')
define Round1: Ok(Equivalent(Round(1), 1.0), 'Round1')
define Round0D5: Ok(Equivalent(Round(0.5), 1.0), 'Round0D5')
define Round0D4: Ok(Equivalent(Round(0.4), 0.0), 'Round0D4')
define Round3D14159: Ok(Equivalent(Round(3.14159, 2), 3.14), 'Round3D14159')
define RoundNeg0D5: Ok(Equivalent(Round(-0.5), 0.0), 'RoundNeg0D5')
define RoundNeg0D4: Ok(Equivalent(Round(-0.4), 0.0), 'RoundNeg0D4')
define RoundNeg0D6: Ok(Equivalent(Round(-0.6), -1.0), 'RoundNeg0D6')
define RoundNeg1D1: Ok(Equivalent(Round(-1.1), -1.0), 'RoundNeg1D1')
define RoundNeg1D5: Ok(Equivalent(Round(-1.5), -1.0), 'RoundNeg1D5')
define RoundNeg1D6: Ok(Equivalent(Round(-1.6), -2.0), 'RoundNeg1D6')

//Subtract
define SubtractNull: Ok(Equivalent(1 - null, null), 'SubtractNull')
define Subtract1And1 : Ok(Equivalent(1 - 1, 0), 'Subtract1And1')
define Subtract1DAnd2D : Ok(Equivalent(1.0 - 2.0, -1.0), 'Subtract1DAnd2D')
define Subtract1CMAnd2CM : Ok(Equivalent(1.0 'cm' - 2.0 'cm', -1.0'cm'), 'Subtract1CMAnd2CM')
define Subtract2And11D: Ok(Equivalent(2 - 1.1, 0.9), 'Subtract2And11D')

//Successor
define SuccessorNull: Ok(Equivalent(successor of (null as Integer), null), 'SuccessorNull')
define SuccessorOf0: Ok(Equivalent(successor of 0, 1), 'SuccessorOf0')
define SuccessorOf1: Ok(Equivalent(successor of 1, 2), 'SuccessorOf1')
define SuccessorOf1D: Ok(Equivalent(successor of 1.0, 1.00000001), 'SuccessorOf1D')
define SuccessorOf101D: Ok(Equivalent(successor of 1.01, 1.01000001), 'SuccessorOf101D')
define SuccessorOfJan12000: Ok(Equivalent(successor of DateTime(2000,1,1), @2000-01-02), 'SuccessorOfJan12000')
define SuccessorOfNoon: Ok(Equivalent(successor of @T12:00:00.000, @T12:00:00.001), 'SuccessorOfNoon')
//define SuccessorOverflowDt: Ok(Equivalent(successor of DateTime(9999, 12, 31, 23, 59, 59, 999), ...), 'SuccessorOverflowDt')
define SuccessorOverflowDt : Skipped(true, 'SuccessorOverflowDt', 'can not trap expected error: The result of the successor operation exceeds the maximum value allowed for the type')
//define SuccessorOverflowT: Ok(Equivalent(successor of @T23:59:59.999, ...), 'SuccessorOverflowT')
define SuccessorOverflowT : Skipped(true, 'SuccessorOverflowT', 'can not trap expected error: The result of the successor operation exceeds the maximum value allowed for the type')

//Truncate
define TruncateNull: Ok(Equivalent(Truncate(null as Decimal), null), 'TruncateNull')
define Truncate0: Ok(Equivalent(Truncate(0), 0), 'Truncate0')
define Truncate0D0: Ok(Equivalent(Truncate(0.0), 0), 'Truncate0D0')
define Truncate0D1: Ok(Equivalent(Truncate(0.1), 0), 'Truncate0D1')
define Truncate1: Ok(Equivalent(Truncate(1), 1), 'Truncate1')
define Truncate1D0: Ok(Equivalent(Truncate(1.0), 1), 'Truncate1D0')
define Truncate1D1: Ok(Equivalent(Truncate(1.1), 1), 'Truncate1D1')
define Truncate1D9: Ok(Equivalent(Truncate(1.9), 1), 'Truncate1D9')
define TruncateNeg1: Ok(Equivalent(Truncate(-1), -1), 'TruncateNeg1')
define TruncateNeg1D0: Ok(Equivalent(Truncate(-1.0), -1), 'TruncateNeg1D0')
define TruncateNeg1D1: Ok(Equivalent(Truncate(-1.1), -1), 'TruncateNeg1D1')
define TruncateNeg1D9: Ok(Equivalent(Truncate(-1.9), -1), 'TruncateNeg1D9')

//Truncated Divide
define TruncatedDivideNull: Ok(Equivalent((null as Integer) div (null as Integer), null), 'TruncatedDivideNull')
define TruncatedDivide2By1: Ok(Equivalent(2 div 1, 2), 'TruncatedDivide2By1')
define TruncatedDivide10By3: Ok(Equivalent(10 div 3, 3), 'TruncatedDivide10By3')
define TruncatedDivide10d1By3D1: Ok(Equivalent(10.1 div 3.1, 3.0), 'TruncatedDivide10d1By3D1')
define TruncatedDivideNeg2ByNeg1: Ok(Equivalent(-2 div -1, 2), 'TruncatedDivideNeg2ByNeg1')
define TruncatedDivideNeg10ByNeg3: Ok(Equivalent(-10 div -3, 3), 'TruncatedDivideNeg10ByNeg3')
define TruncatedDivideNeg10d1ByNeg3D1: Ok(Equivalent(-10.1 div -3.1, 3.0), 'TruncatedDivideNeg10d1ByNeg3D1')
define TruncatedDivideNeg2By1: Ok(Equivalent(-2 div 1, -2), 'TruncatedDivideNeg2By1')
define TruncatedDivideNeg10By3: Ok(Equivalent(-10 div 3, -3), 'TruncatedDivideNeg10By3')
define TruncatedDivideNeg10d1By3D1: Ok(Equivalent(-10.1 div 3.1, -3.0), 'TruncatedDivideNeg10d1By3D1')
define TruncatedDivide2ByNeg1: Ok(Equivalent(2 div -1, -2), 'TruncatedDivide2ByNeg1')
define TruncatedDivide10ByNeg3: Ok(Equivalent(10 div -3, -3), 'TruncatedDivide10ByNeg3')
define TruncatedDivide10d1ByNeg3D1: Ok(Equivalent(10.1 div -3.1, -3.0), 'TruncatedDivide10d1ByNeg3D1')
define TruncatedDivide10By5D: Ok(Equivalent(10 div 5.0, 2.0), 'TruncatedDivide10By5D')


define function Plan(testCount Integer):
    Message(null, true, null as String, 'Message', '1..' + ToString(testCount))

define function Ok(isOk Boolean, message String):
    Message(null, true, null as String, 'Message', (if isOk then 'ok - ' else 'not ok - ') + message)

define function Todo(isOk2 Boolean, message2 String, whyTodo String):
    Ok(isOk2, message2 + ' # TODO - ' + whyTodo)
    // Note: parser/runtime has a scope-breaking bug manifesting if Todo() arg named "message" also.

define function Skipped(isOk2 Boolean, message2 String, whySkipped String):
    Ok(isOk2, message2 + ' # SKIPPED - ' + whySkipped)
    // Note: parser/runtime has a scope-breaking bug manifesting if Skipped() arg named "message" also.
