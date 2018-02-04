library ArithmeticFunctions version '1'

context Population

//Abs
define AbsNull: Abs(null as Integer)
define Abs0: Abs(0)
define AbsNeg1: Abs(-1)
define AbsNeg1Dec: Abs(-1.0)
define Abs0Dec: Abs(0.0)
define Abs1cm: Abs(-1.0'cm')

//Add
define AddNull: 1 + null
define Add11: 1 + 1
define Add1D1D: 1.0 + 1.0
define Add1Q1Q: 1'g/cm3' + 1'g/cm3'
define AddIAndD: 1 + 2.0

//Ceiling
define CeilingNull: Ceiling(null as Decimal)
define Ceiling1D: Ceiling(1.0)
define Ceiling1D1: Ceiling(1.1)
define CeilingNegD1: Ceiling(-0.1)
define CeilingNeg1: Ceiling(-1.0)
define CeilingNeg1D1: Ceiling(-1.1)
define Ceiling1I: Ceiling(1)

//Divide
define DivideNull: 1 / null
define Divide10: 1 / 0
define Divide01: 0 / 1
define Divide11: 1 / 1
define Divide1d1d: 1.0 / 1.0
define Divide103: 10 / 3
define Divide1Q1: 1'g/cm3' / 1.0
define Divide1Q1Q: 1'g/cm3' / 1'g/cm3'
define Divide10I5D: 10 / 5.0
define Divide10I5I: 10 / 5
define Divide10Q5I: 10.0 'g' / 5

//Floor
define FloorNull: Floor(null as Decimal)
define Floor1: Floor(1)
define Floor1D: Floor(1.0)
define Floor1D1: Floor(1.1)
define FloorNegD1: Floor(-0.1)
define FloorNeg1: Floor(-1.0)
define FloorNeg1D1: Floor(-1.1)
define Floor2I: Floor(2)

//Exp
define ExpNull  : Exp(null as Decimal)
define Exp0  : Exp(0)
define ExpNeg0  : Exp(-0)
define Exp1  : Exp(1)
define ExpNeg1  : Exp(-1)
define Exp1000  : Exp(1000)
define Exp1000D  : Exp(1000.0)

//Log
define LogNullNull  : Log(null, null)
define Log1BaseNull  : Log(1, null)
define Log1Base1  : Log(1, 1)
define Log1Base2  : Log(1, 2)
define Log1Base100  : Log(1, 100)
define Log16Base2  : Log(16, 2)
define LogD125Base2  : Log(0.125, 2)

//Ln
define LnNull  : Ln(null)
define Ln0  : Ln(0)
define LnNeg0  : Ln(-0)
define Ln1  : Ln(1)
define LnNeg1  : Ln(-1)
define Ln1000  : Ln(1000)
define Ln1000D  : Ln(1000.0)

//MinValue
define IntegerMinValue: minimum Integer
define DecimalMinValue: minimum Decimal
define QuantityMinValue: minimum Quantity
define DateTimeMinValue: minimum DateTime
define TimeMinValue: minimum Time

//MaxValue
define IntegerMaxValue: maximum Integer
define DecimalMaxValue: maximum Decimal
define QuantityMaxValue: maximum Quantity
define DateTimeMaxValue: maximum DateTime
define TimeMaxValue: maximum Time

//Modulo
define ModuloNull  : 1 mod null
define Modulo0By0  : 0 mod 0
define Modulo4By2  : 4 mod 2
define Modulo4DBy2D  : 4.0 mod 2.0
define Modulo10By3  : 10 mod 3
define Modulo10DBy3D  : 10.0 mod 3.0
define Modulo10IBy3D: 10 mod 3.0
define ModuloDResult: 3.5 mod 3

//Multiply
define MultiplyNull: 1 * null
define Multiply1By1 : 1 * 1
define Multiply1DBy2D : 1.0 * 2.0
define Multiply1IBy2D: 1 * 2.0
define Multiply1CMBy2CM : 1.0 'cm' * 2.0 'cm'

//Negate
define NegateNull: -(null as Integer)
define Negate0 : -0
define NegateNeg0 : -(-0)
define Negate1 : -1
define NegateNeg1 : -(-1)
define Negate0D : -(0.0)
define NegateNeg0D : -(-0.0)
define Negate1D : -(1.0)
define NegateNeg1D : -(-1.0)
define Negate1CM : -(1'cm')

//Predecessor
define PredecessorNull: predecessor of (null as Integer)
define PredecessorOf0: predecessor of 0
define PredecessorOf1: predecessor of 1
define PredecessorOf1D: predecessor of 1.0
define PredecessorOf101D: predecessor of 1.01
define PredecessorOf1QCM: predecessor of 1.0 'cm'
define PredecessorOfJan12000: predecessor of DateTime(2000,1,1)
define PredecessorOfNoon: predecessor of @T12:00:00.000
define PredecessorUnderflowDt: predecessor of DateTime(0001, 1, 1, 0, 0, 0, 0)
define PredecessorUnderflowT: predecessor of @T00:00:00.000

//Power
define PowerNullToNull: Power(null as Integer, null as Integer)
define Power0To0: Power(0, 0)
define Power2To2: Power(2, 2)
define PowerNeg2To2: Power(-2, 2)
define Power2ToNeg2: Power(2, -2)
define Power2DTo2D: Power(2.0, 2.0)
define PowerNeg2DTo2D: Power(-2.0, 2.0)
define Power2DToNeg2D: Power(2.0, -2.0)
define Power2DTo2: Power(2.0, 2)
define Power2To2D: Power(2, 2.0)
define Power2To4: 2^4
define Power2DTo4D: 2.0^4.0
define Power2DToNeg2DEquivalence: Power(2, -2) ~ 0.25

//Round
define RoundNull: Round(null as Decimal)
define Round1: Round(1)
define Round0D5: Round(0.5)
define Round0D4: Round(0.4)
define Round3D14159: Round(3.14159, 2)
define RoundNeg0D5: Round(-0.5)
define RoundNeg0D4: Round(-0.4)
define RoundNeg0D6: Round(-0.6)
define RoundNeg1D1: Round(-1.1)
define RoundNeg1D5: Round(-1.5)
define RoundNeg1D6: Round(-1.6)

//Subtract
define SubtractNull: 1 - null
define Subtract1And1 : 1 - 1
define Subtract1DAnd2D : 1.0 - 2.0
define Subtract1CMAnd2CM : 1.0 'cm' - 2.0 'cm'
define Subtract2And11D: 2 - 1.1

//Successor
define SuccessorNull: successor of (null as Integer)
define SuccessorOf0: successor of 0
define SuccessorOf1: successor of 1
define SuccessorOf1D: successor of 1.0
define SuccessorOf101D: successor of 1.01
define SuccessorOfJan12000: successor of DateTime(2000,1,1)
define SuccessorOfNoon: successor of @T12:00:00.000
define SuccessorOverflowDt: successor of DateTime(9999, 12, 31, 23, 59, 59, 999)
define SuccessorOverflowT: successor of @T23:59:59.999

//Truncate
define TruncateNull: Truncate(null as Decimal)
define Truncate0: Truncate(0)
define Truncate0D0: Truncate(0.0)
define Truncate0D1: Truncate(0.1)
define Truncate1: Truncate(1)
define Truncate1D0: Truncate(1.0)
define Truncate1D1: Truncate(1.1)
define Truncate1D9: Truncate(1.9)
define TruncateNeg1: Truncate(-1)
define TruncateNeg1D0: Truncate(-1.0)
define TruncateNeg1D1: Truncate(-1.1)
define TruncateNeg1D9: Truncate(-1.9)

//Truncated Divide
define TruncatedDivideNull: (null as Integer) div (null as Integer)
define TruncatedDivide2By1: 2 div 1
define TruncatedDivide10By3: 10 div 3
define TruncatedDivide10d1By3D1: 10.1 div 3.1
define TruncatedDivideNeg2ByNeg1: -2 div -1
define TruncatedDivideNeg10ByNeg3: -10 div -3
define TruncatedDivideNeg10d1ByNeg3D1: -10.1 div -3.1
define TruncatedDivideNeg2By1: -2 div 1
define TruncatedDivideNeg10By3: -10 div 3
define TruncatedDivideNeg10d1By3D1: -10.1 div 3.1
define TruncatedDivide2ByNeg1: 2 div -1
define TruncatedDivide10ByNeg3: 10 div -3
define TruncatedDivide10d1ByNeg3D1: 10.1 div -3.1
define TruncatedDivide10By5D: 10 div 5.0
