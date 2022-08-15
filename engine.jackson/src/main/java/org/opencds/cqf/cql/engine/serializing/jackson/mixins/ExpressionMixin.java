package org.opencds.cqf.cql.engine.serializing.jackson.mixins;

import org.opencds.cqf.cql.engine.elm.execution.*;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type", defaultImpl = ExpressionDefEvaluator.class)
@JsonSubTypes({
    @Type(value = AbsEvaluator.class, name = "Abs"),
    @Type(value = AddEvaluator.class, name = "Add"),
    @Type(value = AfterEvaluator.class, name = "After"),
    @Type(value = AliasRefEvaluator.class, name = "AliasRef"),
    @Type(value = AllTrueEvaluator.class, name = "AllTrue"),
    @Type(value = AndEvaluator.class, name = "And"),
    @Type(value = AnyInCodeSystemEvaluator.class, name = "AnyInCodeSystem"),
    @Type(value = AnyInValueSetEvaluator.class, name = "AnyInValueSet"),
    @Type(value = AnyTrueEvaluator.class, name = "AnyTrue"),
    @Type(value = AsEvaluator.class, name = "As"),
    @Type(value = AvgEvaluator.class, name = "Avg"),
    @Type(value = BeforeEvaluator.class, name = "Before"),
    @Type(value = CalculateAgeEvaluator.class, name = "CalculateAge"),
    @Type(value = CalculateAgeAtEvaluator.class, name = "CalculateAgeAt"),
    @Type(value = CaseEvaluator.class, name = "Case"),
    @Type(value = CeilingEvaluator.class, name = "Ceiling"),
    @Type(value = ChildrenEvaluator.class, name = "Children"),
    @Type(value = CoalesceEvaluator.class, name = "Coalesce"),
    @Type(value = CodeEvaluator.class, name = "Code"),
    @Type(value = CodeRefEvaluator.class, name = "CodeRef"),
    @Type(value = CodeSystemRefEvaluator.class, name = "CodeSystemRef"),
    @Type(value = CollapseEvaluator.class, name = "Collapse"),
    @Type(value = CombineEvaluator.class, name = "Combine"),
    @Type(value = ConcatenateEvaluator.class, name = "Concatenate"),
    @Type(value = ConceptEvaluator.class, name = "Concept"),
    @Type(value = ConceptRefEvaluator.class, name = "ConceptRef"),
    @Type(value = ContainsEvaluator.class, name = "Contains"),
    @Type(value = ConvertEvaluator.class, name = "Convert"),
    @Type(value = ConvertQuantityEvaluator.class, name = "ConvertQuantity"),
    @Type(value = ConvertsToBooleanEvaluator.class, name = "ConvertsToBoolean"),
    @Type(value = ConvertsToDateEvaluator.class, name = "ConvertsToDate"),
    @Type(value = ConvertsToDateTimeEvaluator.class, name = "ConvertsToDateTime"),
    @Type(value = ConvertsToDecimalEvaluator.class, name = "ConvertsToDecimal"),
    @Type(value = ConvertsToIntegerEvaluator.class, name = "ConvertsToInteger"),
    @Type(value = ConvertsToLongEvaluator.class, name = "ConvertsToLong"),
    @Type(value = ConvertsToQuantityEvaluator.class, name = "ConvertsToQuantity"),
    @Type(value = ConvertsToStringEvaluator.class, name = "ConvertsToString"),
    @Type(value = ConvertsToTimeEvaluator.class, name = "ConvertsToTime"),
    @Type(value = CountEvaluator.class, name = "Count"),
    @Type(value = DateEvaluator.class, name = "Date"),
    @Type(value = DateFromEvaluator.class, name = "DateFrom"),
    @Type(value = DateTimeEvaluator.class, name = "DateTime"),
    @Type(value = DateTimeComponentFromEvaluator.class, name = "DateTimeComponentFrom"),
    @Type(value = DescendentsEvaluator.class, name = "Descendents"),
    @Type(value = DifferenceBetweenEvaluator.class, name = "DifferenceBetween"),
    @Type(value = DistinctEvaluator.class, name = "Distinct"),
    @Type(value = DivideEvaluator.class, name = "Divide"),
    @Type(value = DurationBetweenEvaluator.class, name = "DurationBetween"),
    @Type(value = EndEvaluator.class, name = "End"),
    @Type(value = EndsEvaluator.class, name = "Ends"),
    @Type(value = EndsWithEvaluator.class, name = "EndsWith"),
    @Type(value = EqualEvaluator.class, name = "Equal"),
    @Type(value = EquivalentEvaluator.class, name = "Equivalent"),
    @Type(value = ExceptEvaluator.class, name = "Except"),
    @Type(value = ExistsEvaluator.class, name = "Exists"),
    @Type(value = ExpEvaluator.class, name = "Exp"),
    @Type(value = ExpandEvaluator.class, name = "Expand"),
    @Type(value = ExpressionDefEvaluator.class, name = "ExpressionDef"),
    @Type(value = ExpressionRefEvaluator.class, name = "ExpressionRef"),
    // @Type(value = FunctionDef.class, name = "FunctionDef"),
    @Type(value = FilterEvaluator.class, name = "Filter"),
    @Type(value = FirstEvaluator.class, name = "First"),
    @Type(value = FlattenEvaluator.class, name = "Flatten"),
    @Type(value = FloorEvaluator.class, name = "Floor"),
    @Type(value = ForEachEvaluator.class, name = "ForEach"),
    @Type(value = FunctionRefEvaluator.class, name = "FunctionRef"),
    @Type(value = GeometricMeanEvaluator.class, name = "GeometricMean"),
    @Type(value = GreaterEvaluator.class, name = "Greater"),
    @Type(value = GreaterOrEqualEvaluator.class, name = "GreaterOrEqual"),
    @Type(value = HighBoundaryEvaluator.class, name = "HighBoundary"),
    @Type(value = IdentifierRefEvaluator.class, name = "IdentifierRef"),
    @Type(value = IfEvaluator.class, name = "If"),
    @Type(value = ImpliesEvaluator.class, name = "Implies"),
    @Type(value = InEvaluator.class, name = "In"),
    @Type(value = IncludedInEvaluator.class, name = "IncludedIn"),
    @Type(value = IncludesEvaluator.class, name = "Includes"),
    @Type(value = InCodeSystemEvaluator.class, name = "InCodeSystem"),
    @Type(value = IndexerEvaluator.class, name = "Indexer"),
    @Type(value = IndexOfEvaluator.class, name = "IndexOf"),
    @Type(value = InstanceEvaluator.class, name = "Instance"),
    @Type(value = IntersectEvaluator.class, name = "Intersect"),
    @Type(value = IntervalEvaluator.class, name = "Interval"),
    @Type(value = InValueSetEvaluator.class, name = "InValueSet"),
    @Type(value = IsEvaluator.class, name = "Is"),
    @Type(value = IsFalseEvaluator.class, name = "IsFalse"),
    @Type(value = IsNullEvaluator.class, name = "IsNull"),
    @Type(value = IsTrueEvaluator.class, name = "IsTrue"),
    @Type(value = LastEvaluator.class, name = "Last"),
    @Type(value = LastPositionOfEvaluator.class, name = "LastPositionOf"),
    @Type(value = LengthEvaluator.class, name = "Length"),
    @Type(value = LessEvaluator.class, name = "Less"),
    @Type(value = LessOrEqualEvaluator.class, name = "LessOrEqual"),
    @Type(value = ListEvaluator.class, name = "List"),
    @Type(value = LiteralEvaluator.class, name = "Literal"),
    @Type(value = LnEvaluator.class, name = "Ln"),
    @Type(value = LogEvaluator.class, name = "Log"),
    @Type(value = LowBoundaryEvaluator.class, name = "LowBoundary"),
    @Type(value = LowerEvaluator.class, name = "Lower"),
    @Type(value = MatchesEvaluator.class, name = "Matches"),
    @Type(value = MaxEvaluator.class, name = "Max"),
    @Type(value = MaxValueEvaluator.class, name = "MaxValue"),
    @Type(value = MedianEvaluator.class, name = "Median"),
    @Type(value = MeetsEvaluator.class, name = "Meets"),
    @Type(value = MeetsAfterEvaluator.class, name = "MeetsAfter"),
    @Type(value = MeetsBeforeEvaluator.class, name = "MeetsBefore"),
    @Type(value = MessageEvaluator.class, name = "Message"),
    @Type(value = MinEvaluator.class, name = "Min"),
    @Type(value = MinValueEvaluator.class, name = "MinValue"),
    @Type(value = ModeEvaluator.class, name = "Mode"),
    @Type(value = ModuloEvaluator.class, name = "Modulo"),
    @Type(value = MultiplyEvaluator.class, name = "Multiply"),
    @Type(value = NegateEvaluator.class, name = "Negate"),
    @Type(value = NotEvaluator.class, name = "Not"),
    @Type(value = NotEqualEvaluator.class, name = "NotEqual"),
    @Type(value = NowEvaluator.class, name = "Now"),
    @Type(value = NullEvaluator.class, name = "Null"),
    @Type(value = OperandRefEvaluator.class, name = "OperandRef"),
    @Type(value = OrEvaluator.class, name = "Or"),
    @Type(value = OverlapsEvaluator.class, name = "Overlaps"),
    @Type(value = OverlapsAfterEvaluator.class, name = "OverlapsAfter"),
    @Type(value = OverlapsBeforeEvaluator.class, name = "OverlapsBefore"),
    @Type(value = ParameterRefEvaluator.class, name = "ParameterRef"),
    @Type(value = PointFromEvaluator.class, name = "PointFrom"),
    @Type(value = PopulationStdDevEvaluator.class, name = "PopulationStdDev"),
    @Type(value = PopulationVarianceEvaluator.class, name = "PopulationVariance"),
    @Type(value = PositionOfEvaluator.class, name = "PositionOf"),
    @Type(value = PowerEvaluator.class, name = "Power"),
    @Type(value = PrecisionEvaluator.class, name = "Precision"),
    @Type(value = PredecessorEvaluator.class, name = "Predecessor"),
    @Type(value = ProductEvaluator.class, name = "Product"),
    @Type(value = ProperContainsEvaluator.class, name = "ProperContains"),
    @Type(value = ProperInEvaluator.class, name = "ProperIn"),
    @Type(value = ProperIncludedInEvaluator.class, name = "ProperIncludedIn"),
    @Type(value = ProperIncludesEvaluator.class, name = "ProperIncludes"),
    @Type(value = PropertyEvaluator.class, name = "Property"),
    @Type(value = QuantityEvaluator.class, name = "Quantity"),
    @Type(value = QueryEvaluator.class, name = "Query"),
    @Type(value = QueryLetRefEvaluator.class, name = "QueryLetRef"),
    @Type(value = RatioEvaluator.class, name = "Ratio"),
    @Type(value = RepeatEvaluator.class, name = "Repeat"),
    @Type(value = ReplaceMatchesEvaluator.class, name = "ReplaceMatches"),
    @Type(value = RetrieveEvaluator.class, name = "Retrieve"),
    @Type(value = RoundEvaluator.class, name = "Round"),
    @Type(value = SameAsEvaluator.class, name = "SameAs"),
    @Type(value = SameOrAfterEvaluator.class, name = "SameOrAfter"),
    @Type(value = SameOrBeforeEvaluator.class, name = "SameOrBefore"),
    @Type(value = SingletonFromEvaluator.class, name = "SingletonFrom"),
    @Type(value = SizeEvaluator.class, name = "Size"),
    @Type(value = SliceEvaluator.class, name = "Slice"),
    @Type(value = SplitEvaluator.class, name = "Split"),
    @Type(value = SplitOnMatchesEvaluator.class, name = "SplitOnMatches"),
    @Type(value = StartEvaluator.class, name = "Start"),
    @Type(value = StartsEvaluator.class, name = "Starts"),
    @Type(value = StartsWithEvaluator.class, name = "StartsWith"),
    @Type(value = StdDevEvaluator.class, name = "StdDev"),
    @Type(value = SubstringEvaluator.class, name = "Substring"),
    @Type(value = SubtractEvaluator.class, name = "Subtract"),
    @Type(value = SuccessorEvaluator.class, name = "Successor"),
    @Type(value = SumEvaluator.class, name = "Sum"),
    @Type(value = TimeEvaluator.class, name = "Time"),
    @Type(value = TimeFromEvaluator.class, name = "TimeFrom"),
    @Type(value = TimeOfDayEvaluator.class, name = "TimeOfDay"),
    @Type(value = TimezoneFromEvaluator.class, name = "TimezoneFrom"),
    @Type(value = TimezoneOffsetFromEvaluator.class, name = "TimezoneOffsetFrom"),
    @Type(value = ToBooleanEvaluator.class, name = "ToBoolean"),
    @Type(value = ToConceptEvaluator.class, name = "ToConcept"),
    @Type(value = ToDateEvaluator.class, name = "ToDate"),
    @Type(value = ToDateTimeEvaluator.class, name = "ToDateTime"),
    @Type(value = TodayEvaluator.class, name = "Today"),
    @Type(value = ToDecimalEvaluator.class, name = "ToDecimal"),
    @Type(value = ToIntegerEvaluator.class, name = "ToInteger"),
    @Type(value = ToListEvaluator.class, name = "ToList"),
    @Type(value = ToLongEvaluator.class, name = "ToLong"),
    @Type(value = ToQuantityEvaluator.class, name = "ToQuantity"),
    @Type(value = ToRatioEvaluator.class, name = "ToRatio"),
    @Type(value = ToStringEvaluator.class, name = "ToString"),
    @Type(value = ToTimeEvaluator.class, name = "ToTime"),
    @Type(value = TruncateEvaluator.class, name = "Truncate"),
    @Type(value = TruncatedDivideEvaluator.class, name = "TruncatedDivide"),
    @Type(value = TupleEvaluator.class, name = "Tuple"),
    @Type(value = UnionEvaluator.class, name = "Union"),
    @Type(value = UpperEvaluator.class, name = "Upper"),
    @Type(value = ValueSetRefEvaluator.class, name = "ValueSetRef"),
    @Type(value = VarianceEvaluator.class, name = "Variance"),
    @Type(value = WidthEvaluator.class, name = "Width"),
    @Type(value = XorEvaluator.class, name = "Xor"),
})
public interface ExpressionMixin {
}
