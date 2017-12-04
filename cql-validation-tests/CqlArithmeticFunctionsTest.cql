library ArithmeticFunctions version '1'

context Population

define private ExistenceOfThisEarlierDefineIsWorkaroundForTranslatorDeathWithLaterMessageNullCodeArg: Message(null, true, '', 'Message', '')

define _0: plan(300)


//Abs
define AbsNull: ok(Equivalent((Abs(null as Integer)),(null)), 'AbsNull')
define Abs0: ok(Equivalent((Abs(0)),(null)), 'Abs0')
define AbsNeg1: ok(Equivalent((Abs(-1)),(null)), 'AbsNeg1')
define AbsNeg1Dec: ok(Equivalent((Abs(-1.0)),(null)), 'AbsNeg1Dec')
define Abs0Dec: ok(Equivalent((Abs(0.0)),(null)), 'Abs0Dec')
define Abs1cm: ok(Equivalent((Abs(-1.0'cm')),(null)), 'Abs1cm')

//Add
define AddNull: ok(Equivalent((1 + null),(null)), 'AddNull')
define Add11: ok(Equivalent((1 + 1),(null)), 'Add11')
define Add1D1D: ok(Equivalent((1.0 + 1.0),(null)), 'Add1D1D')
define Add1Q1Q: ok(Equivalent((1'g/cm3' + 1'g/cm3'),(null)), 'Add1Q1Q')
define AddIAndD: ok(Equivalent((1 + 2.0),(null)), 'AddIAndD')

//Ceiling
define CeilingNull: ok(Equivalent((Ceiling(null as Decimal)),(null)), 'CeilingNull')
define Ceiling1D: ok(Equivalent((Ceiling(1.0)),(null)), 'Ceiling1D')
define Ceiling1D1: ok(Equivalent((Ceiling(1.1)),(null)), 'Ceiling1D1')
define CeilingNegD1: ok(Equivalent((Ceiling(-0.1)),(null)), 'CeilingNegD1')
define CeilingNeg1: ok(Equivalent((Ceiling(-1.0)),(null)), 'CeilingNeg1')
define CeilingNeg1D1: ok(Equivalent((Ceiling(-1.1)),(null)), 'CeilingNeg1D1')
define Ceiling1I: ok(Equivalent((Ceiling(1)),(null)), 'Ceiling1I')

//Divide
define DivideNull: ok(Equivalent((1 / null),(null)), 'DivideNull')
define Divide10: ok(Equivalent((1 / 0),(null)), 'Divide10')
define Divide01: ok(Equivalent((0 / 1),(null)), 'Divide01')
define Divide11: ok(Equivalent((1 / 1),(null)), 'Divide11')
define Divide1d1d: ok(Equivalent((1.0 / 1.0),(null)), 'Divide1d1d')
define Divide103: ok(Equivalent((10 / 3),(null)), 'Divide103')
define Divide1Q1: ok(Equivalent((1'g/cm3' / 1.0),(null)), 'Divide1Q1')
define Divide1Q1Q: ok(Equivalent((1'g/cm3' / 1'g/cm3'),(null)), 'Divide1Q1Q')
define Divide10I5D: ok(Equivalent((10 / 5.0),(null)), 'Divide10I5D')
define Divide10I5I: ok(Equivalent((10 / 5),(null)), 'Divide10I5I')
define Divide10Q5I: ok(Equivalent((10.0 'g' / 5),(null)), 'Divide10Q5I')

//Floor
define FloorNull: ok(Equivalent((Floor(null as Decimal)),(null)), 'FloorNull')
define Floor1: ok(Equivalent((Floor(1)),(null)), 'Floor1')
define Floor1D: ok(Equivalent((Floor(1.0)),(null)), 'Floor1D')
define Floor1D1: ok(Equivalent((Floor(1.1)),(null)), 'Floor1D1')
define FloorNegD1: ok(Equivalent((Floor(-0.1)),(null)), 'FloorNegD1')
define FloorNeg1: ok(Equivalent((Floor(-1.0)),(null)), 'FloorNeg1')
define FloorNeg1D1: ok(Equivalent((Floor(-1.1)),(null)), 'FloorNeg1D1')
define Floor2I: ok(Equivalent((Floor(2)),(null)), 'Floor2I')

//Exp
define ExpNull  : ok(Equivalent((Exp(null as Decimal)),(null)), 'ExpNull')
define Exp0  : ok(Equivalent((Exp(0)),(null)), 'Exp0')
define ExpNeg0  : ok(Equivalent((Exp(-0)),(null)), 'ExpNeg0')
define Exp1  : ok(Equivalent((Exp(1)),(null)), 'Exp1')
define ExpNeg1  : ok(Equivalent((Exp(-1)),(null)), 'ExpNeg1')
define Exp1000  : ok(Equivalent((Exp(1000)),(null)), 'Exp1000')
define Exp1000D  : ok(Equivalent((Exp(1000.0)),(null)), 'Exp1000D')

//Log
define LogNullNull  : ok(Equivalent((Log(null, null)),(null)), 'LogNullNull')
define Log1BaseNull  : ok(Equivalent((Log(1, null)),(null)), 'Log1BaseNull')
define Log1Base1  : ok(Equivalent((Log(1, 1)),(null)), 'Log1Base1')
define Log1Base2  : ok(Equivalent((Log(1, 2)),(null)), 'Log1Base2')
define Log1Base100  : ok(Equivalent((Log(1, 100)),(null)), 'Log1Base100')
define Log16Base2  : ok(Equivalent((Log(16, 2)),(null)), 'Log16Base2')
define LogD125Base2  : ok(Equivalent((Log(0.125, 2)),(null)), 'LogD125Base2')

//Ln
define LnNull  : ok(Equivalent((Ln(null)),(null)), 'LnNull')
define Ln0  : ok(Equivalent((Ln(0)),(null)), 'Ln0')
define LnNeg0  : ok(Equivalent((Ln(-0)),(null)), 'LnNeg0')
define Ln1  : ok(Equivalent((Ln(1)),(null)), 'Ln1')
define LnNeg1  : ok(Equivalent((Ln(-1)),(null)), 'LnNeg1')
define Ln1000  : ok(Equivalent((Ln(1000)),(null)), 'Ln1000')
define Ln1000D  : ok(Equivalent((Ln(1000.0)),(null)), 'Ln1000D')

//MinValue
define IntegerMinValue: ok(Equivalent((minimum Integer),(null)), 'IntegerMinValue')
define DecimalMinValue: ok(Equivalent((minimum Decimal),(null)), 'DecimalMinValue')
define QuantityMinValue: ok(Equivalent((minimum Quantity),(null)), 'QuantityMinValue')
define DateTimeMinValue: ok(Equivalent((minimum DateTime),(null)), 'DateTimeMinValue')
define TimeMinValue: ok(Equivalent((minimum Time),(null)), 'TimeMinValue')

//MaxValue
define IntegerMaxValue: ok(Equivalent((maximum Integer),(null)), 'IntegerMaxValue')
define DecimalMaxValue: ok(Equivalent((maximum Decimal),(null)), 'DecimalMaxValue')
define QuantityMaxValue: ok(Equivalent((maximum Quantity),(null)), 'QuantityMaxValue')
define DateTimeMaxValue: ok(Equivalent((maximum DateTime),(null)), 'DateTimeMaxValue')
define TimeMaxValue: ok(Equivalent((maximum Time),(null)), 'TimeMaxValue')

//Modulo
define ModuloNull  : ok(Equivalent((1 mod null),(null)), 'ModuloNull')
define Modulo0By0  : ok(Equivalent((0 mod 0),(null)), 'Modulo0By0')
define Modulo4By2  : ok(Equivalent((4 mod 2),(null)), 'Modulo4By2')
define Modulo4DBy2D  : ok(Equivalent((4.0 mod 2.0),(null)), 'Modulo4DBy2D')
define Modulo10By3  : ok(Equivalent((10 mod 3),(null)), 'Modulo10By3')
define Modulo10DBy3D  : ok(Equivalent((10.0 mod 3.0),(null)), 'Modulo10DBy3D')
define Modulo10IBy3D: ok(Equivalent((10 mod 3.0),(null)), 'Modulo10IBy3D')
define ModuloDResult: ok(Equivalent((3.5 mod 3),(null)), 'ModuloDResult')

//Multiply
define MultiplyNull: ok(Equivalent((1 * null),(null)), 'MultiplyNull')
define Multiply1By1 : ok(Equivalent((1 * 1),(null)), 'Multiply1By1')
define Multiply1DBy2D : ok(Equivalent((1.0 * 2.0),(null)), 'Multiply1DBy2D')
define Multiply1IBy2D: ok(Equivalent((1 * 2.0),(null)), 'Multiply1IBy2D')
define Multiply1CMBy2CM : ok(Equivalent((1.0 'cm' * 2.0 'cm'),(null)), 'Multiply1CMBy2CM')

//Negate
define NegateNull: ok(Equivalent((-(null as Integer)),(null)), 'NegateNull')
define Negate0 : ok(Equivalent((-0),(null)), 'Negate0')
define NegateNeg0 : ok(Equivalent((-(-0)),(null)), 'NegateNeg0')
define Negate1 : ok(Equivalent((-1),(null)), 'Negate1')
define NegateNeg1 : ok(Equivalent((-(-1)),(null)), 'NegateNeg1')
define Negate0D : ok(Equivalent((-(0.0)),(null)), 'Negate0D')
define NegateNeg0D : ok(Equivalent((-(-0.0)),(null)), 'NegateNeg0D')
define Negate1D : ok(Equivalent((-(1.0)),(null)), 'Negate1D')
define NegateNeg1D : ok(Equivalent((-(-1.0)),(null)), 'NegateNeg1D')
define Negate1CM : ok(Equivalent((-(1'cm')),(null)), 'Negate1CM')

//Predecessor
define PredecessorNull: ok(Equivalent((predecessor of (null as Integer)),(null)), 'PredecessorNull')
define PredecessorOf0: ok(Equivalent((predecessor of 0),(null)), 'PredecessorOf0')
define PredecessorOf1: ok(Equivalent((predecessor of 1),(null)), 'PredecessorOf1')
define PredecessorOf1D: ok(Equivalent((predecessor of 1.0),(null)), 'PredecessorOf1D')
define PredecessorOf101D: ok(Equivalent((predecessor of 1.01),(null)), 'PredecessorOf101D')
define PredecessorOf1QCM: ok(Equivalent((predecessor of 1.0 'cm'),(null)), 'PredecessorOf1QCM')
define PredecessorOfJan12000: ok(Equivalent((predecessor of DateTime(2000,1,1)),(null)), 'PredecessorOfJan12000')
define PredecessorOfNoon: ok(Equivalent((predecessor of @T12:00:00.000),(null)), 'PredecessorOfNoon')
define PredecessorUnderflowDt: ok(Equivalent((predecessor of DateTime(0001, 1, 1, 0, 0, 0, 0)),(null)), 'PredecessorUnderflowDt')
define PredecessorUnderflowT: ok(Equivalent((predecessor of @T00:00:00.000),(null)), 'PredecessorUnderflowT')

//Power
define PowerNullToNull: ok(Equivalent((Power(null as Integer, null as Integer)),(null)), 'PowerNullToNull')
define Power0To0: ok(Equivalent((Power(0, 0)),(null)), 'Power0To0')
define Power2To2: ok(Equivalent((Power(2, 2)),(null)), 'Power2To2')
define PowerNeg2To2: ok(Equivalent((Power(-2, 2)),(null)), 'PowerNeg2To2')
define Power2ToNeg2: ok(Equivalent((Power(2, -2)),(null)), 'Power2ToNeg2')
define Power2DTo2D: ok(Equivalent((Power(2.0, 2.0)),(null)), 'Power2DTo2D')
define PowerNeg2DTo2D: ok(Equivalent((Power(-2.0, 2.0)),(null)), 'PowerNeg2DTo2D')
define Power2DToNeg2D: ok(Equivalent((Power(2.0, -2.0)),(null)), 'Power2DToNeg2D')
define Power2DTo2: ok(Equivalent((Power(2.0, 2)),(null)), 'Power2DTo2')
define Power2To2D: ok(Equivalent((Power(2, 2.0)),(null)), 'Power2To2D')
define Power2To4: ok(Equivalent((2^4),(null)), 'Power2To4')
define Power2DTo4D: ok(Equivalent((2.0^4.0),(null)), 'Power2DTo4D')

//Round
define RoundNull: ok(Equivalent((Round(null as Decimal)),(null)), 'RoundNull')
define Round1: ok(Equivalent((Round(1)),(null)), 'Round1')
define Round0D5: ok(Equivalent((Round(0.5)),(null)), 'Round0D5')
define Round0D4: ok(Equivalent((Round(0.4)),(null)), 'Round0D4')
define Round3D14159: ok(Equivalent((Round(3.14159, 2)),(null)), 'Round3D14159')
define RoundNeg0D5: ok(Equivalent((Round(-0.5)),(null)), 'RoundNeg0D5')
define RoundNeg0D4: ok(Equivalent((Round(-0.4)),(null)), 'RoundNeg0D4')
define RoundNeg0D6: ok(Equivalent((Round(-0.6)),(null)), 'RoundNeg0D6')
define RoundNeg1D1: ok(Equivalent((Round(-1.1)),(null)), 'RoundNeg1D1')
define RoundNeg1D5: ok(Equivalent((Round(-1.5)),(null)), 'RoundNeg1D5')
define RoundNeg1D6: ok(Equivalent((Round(-1.6)),(null)), 'RoundNeg1D6')

//Subtract
define SubtractNull: ok(Equivalent((1 - null),(null)), 'SubtractNull')
define Subtract1And1 : ok(Equivalent((1 - 1),(null)), 'Subtract1And1')
define Subtract1DAnd2D : ok(Equivalent((1.0 - 2.0),(null)), 'Subtract1DAnd2D')
define Subtract1CMAnd2CM : ok(Equivalent((1.0 'cm' - 2.0 'cm'),(null)), 'Subtract1CMAnd2CM')
define Subtract2And11D: ok(Equivalent((2 - 1.1),(null)), 'Subtract2And11D')

//Successor
define SuccessorNull: ok(Equivalent((successor of (null as Integer)),(null)), 'SuccessorNull')
define SuccessorOf0: ok(Equivalent((successor of 0),(null)), 'SuccessorOf0')
define SuccessorOf1: ok(Equivalent((successor of 1),(null)), 'SuccessorOf1')
define SuccessorOf1D: ok(Equivalent((successor of 1.0),(null)), 'SuccessorOf1D')
define SuccessorOf101D: ok(Equivalent((successor of 1.01),(null)), 'SuccessorOf101D')
define SuccessorOfJan12000: ok(Equivalent((successor of DateTime(2000,1,1)),(null)), 'SuccessorOfJan12000')
define SuccessorOfNoon: ok(Equivalent((successor of @T12:00:00.000),(null)), 'SuccessorOfNoon')
define SuccessorOverflowDt: ok(Equivalent((successor of DateTime(9999, 12, 31, 23, 59, 59, 999)),(null)), 'SuccessorOverflowDt')
define SuccessorOverflowT: ok(Equivalent((successor of @T23:59:59.999),(null)), 'SuccessorOverflowT')

//Truncate
define TruncateNull: ok(Equivalent((Truncate(null as Decimal)),(null)), 'TruncateNull')
define Truncate0: ok(Equivalent((Truncate(0)),(null)), 'Truncate0')
define Truncate0D0: ok(Equivalent((Truncate(0.0)),(null)), 'Truncate0D0')
define Truncate0D1: ok(Equivalent((Truncate(0.1)),(null)), 'Truncate0D1')
define Truncate1: ok(Equivalent((Truncate(1)),(null)), 'Truncate1')
define Truncate1D0: ok(Equivalent((Truncate(1.0)),(null)), 'Truncate1D0')
define Truncate1D1: ok(Equivalent((Truncate(1.1)),(null)), 'Truncate1D1')
define Truncate1D9: ok(Equivalent((Truncate(1.9)),(null)), 'Truncate1D9')
define TruncateNeg1: ok(Equivalent((Truncate(-1)),(null)), 'TruncateNeg1')
define TruncateNeg1D0: ok(Equivalent((Truncate(-1.0)),(null)), 'TruncateNeg1D0')
define TruncateNeg1D1: ok(Equivalent((Truncate(-1.1)),(null)), 'TruncateNeg1D1')
define TruncateNeg1D9: ok(Equivalent((Truncate(-1.9)),(null)), 'TruncateNeg1D9')

//Truncated Divide
define TruncatedDivideNull: ok(Equivalent(((null as Integer) div (null as Integer)),(null)), 'TruncatedDivideNull')
define TruncatedDivide2By1: ok(Equivalent((2 div 1),(null)), 'TruncatedDivide2By1')
define TruncatedDivide10By3: ok(Equivalent((10 div 3),(null)), 'TruncatedDivide10By3')
define TruncatedDivide10d1By3D1: ok(Equivalent((10.1 div 3.1),(null)), 'TruncatedDivide10d1By3D1')
define TruncatedDivideNeg2ByNeg1: ok(Equivalent((-2 div -1),(null)), 'TruncatedDivideNeg2ByNeg1')
define TruncatedDivideNeg10ByNeg3: ok(Equivalent((-10 div -3),(null)), 'TruncatedDivideNeg10ByNeg3')
define TruncatedDivideNeg10d1ByNeg3D1: ok(Equivalent((-10.1 div -3.1),(null)), 'TruncatedDivideNeg10d1ByNeg3D1')
define TruncatedDivideNeg2By1: ok(Equivalent((-2 div 1),(null)), 'TruncatedDivideNeg2By1')
define TruncatedDivideNeg10By3: ok(Equivalent((-10 div 3),(null)), 'TruncatedDivideNeg10By3')
define TruncatedDivideNeg10d1By3D1: ok(Equivalent((-10.1 div 3.1),(null)), 'TruncatedDivideNeg10d1By3D1')
define TruncatedDivide2ByNeg1: ok(Equivalent((2 div -1),(null)), 'TruncatedDivide2ByNeg1')
define TruncatedDivide10ByNeg3: ok(Equivalent((10 div -3),(null)), 'TruncatedDivide10ByNeg3')
define TruncatedDivide10d1ByNeg3D1: ok(Equivalent((10.1 div -3.1),(null)), 'TruncatedDivide10d1ByNeg3D1')
define TruncatedDivide10By5D: ok(Equivalent((10 div 5.0),(null)), 'TruncatedDivide10By5D')


define function plan(test_count Integer):
    Message(null, true, null, 'Message', '1..' + ToString(test_count))

define function ok(is_ok Boolean, message String):
    Message(null, true, null, 'Message', (if is_ok then 'ok - ' else 'not ok - ') + message)
