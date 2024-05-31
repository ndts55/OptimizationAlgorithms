package integration;

import org.junit.jupiter.api.Test;
import org.ndts.optalgj.algs.*;
import org.ndts.optalgj.problems.rect.MinBoxObjs;
import org.ndts.optalgj.problems.rect.domain.Input;
import org.ndts.optalgj.problems.rect.domain.Output;
import org.ndts.optalgj.problems.rect.neighborhood.GeometricNeighborhood;
import org.ndts.optalgj.problems.rect.neighborhood.OverlapNeighborhood;
import org.ndts.optalgj.problems.rect.neighborhood.RuleNeighborhood;
import org.ndts.optalgj.problems.rect.node.LegoNode;
import org.ndts.optalgj.problems.rect.node.SimpleNode;
import org.ndts.optalgj.problems.rect.node.SortedLegoNode;
import org.ndts.optalgj.problems.rect.node.SortedSimpleNode;
import org.ndts.optalgj.problems.rect.utils.SolutionConstructor;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.ndts.optalgj.problems.rect.utils.InstanceGenerator.generateInstances;

enum Variant {
	Geometric, Rules, Overlap, VariantA, VariantA2, VariantB, VariantB2;

	static Variant from(LocalSearchVariant localSearchVariant) {
		return switch (localSearchVariant) {
			case Geometric -> Geometric;
			case Rules -> Rules;
			case Overlap -> Overlap;
		};
	}

	static Variant from(GreedySearchVariant greedySearchVariant) {
		return switch (greedySearchVariant) {
			case VariantA -> VariantA;
			case VariantA2 -> VariantA2;
			case VariantB -> VariantB;
			case VariantB2 -> VariantB2;
		};
	}

	@Override
	public String toString() {
		return switch (this) {
			case Geometric -> "Geometric";
			case Rules -> "Rules";
			case Overlap -> "Overlap";
			case VariantA -> "Simple";
			case VariantA2 -> "Simple (sorted)";
			case VariantB -> "Lego";
			case VariantB2 -> "Lego (sorted)";
		};
	}
}

record TestInput(int index, Input input) {
}

record RunResult(long duration, int boxCount) {
}

record TestResult(int index, Map<Variant, RunResult> runResultsPerVariant) {

	TestResult(TestInput input) {
		this(input.index(), input.input());
	}

	private TestResult(int index, Input input) {
		this(index, Stream.concat(Arrays.stream(LocalSearchVariant.values()).map(Variant::from),
			Arrays.stream(GreedySearchVariant.values()).map(Variant::from)).map(variant -> Map.entry(variant, runAndTime(constructSearch(variant, input)))).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
	}

	private static RunResult runAndTime(final OptimizationAlgorithm<Output> search) {
		final var start = System.currentTimeMillis();
		//noinspection StatementWithEmptyBody
		while (search.iterate()) ;
		final var end = System.currentTimeMillis();
		return new RunResult(end - start, search.best().boxes().size());
	}

	private static OptimizationAlgorithm<Output> constructSearch(final Variant variant,
																 final Input input) {
		return switch (variant) {
			case Geometric ->
				new LocalSearch<>(MinBoxObjs.getRecommended(LocalSearchVariant.Geometric),
					new GeometricNeighborhood(), SolutionConstructor.forLocal(input));
			case Rules -> new LocalSearch<>(MinBoxObjs.getRecommended(LocalSearchVariant.Rules),
				new RuleNeighborhood(), SolutionConstructor.forLocal(input));
			case Overlap -> new LocalSearch<>(MinBoxObjs.getRecommended(LocalSearchVariant.Overlap),
				new OverlapNeighborhood(), SolutionConstructor.forLocal(input));
			case VariantA ->
				new GreedySearch<>(MinBoxObjs.getRecommended(GreedySearchVariant.VariantA),
					new ArrayDeque<>(), new SimpleNode(input));
			case VariantA2 ->
				new GreedySearch<>(MinBoxObjs.getRecommended(GreedySearchVariant.VariantA2),
					new ArrayDeque<>(), new SortedSimpleNode(input));
			case VariantB ->
				new GreedySearch<>(MinBoxObjs.getRecommended(GreedySearchVariant.VariantB),
					new ArrayDeque<>(), new LegoNode(input));
			case VariantB2 ->
				new GreedySearch<>(MinBoxObjs.getRecommended(GreedySearchVariant.VariantB2),
					new ArrayDeque<>(), new SortedLegoNode(input));
		};
	}

	@Override
	public String toString() {
		final var sb = new StringBuilder();
		sb.append("Run %d\n".formatted(index));
		Arrays.stream(Variant.values()).forEach(variant -> {
			final var result = runResultsPerVariant.get(variant);
			sb.append("%s: %dms\t%d boxes\n".formatted(variant.toString(), result.duration(),
				result.boxCount()));
		});
		return sb.toString();
	}
}

public class TestAll {
	private static final int instanceCount = 20;
	private static final int boxLength = 20;
	private static final int rectangleCount = 1000;
	private static final int minWidth = 1;
	private static final int minHeight = 1;
	private static final int maxWidth = 7;
	private static final int maxHeight = 7;

	private static void printArguments() {
		System.out.printf("""
				Executing %d runs. Each with the following parameters:
				- Box Length:      %d
				- Rectangle Count: %d
				- Min. Width:      %d
				- Min. Height:     %d
				- Max. Width:      %d
				- Max. Height:     %d
				""", instanceCount, boxLength, rectangleCount, minWidth, minHeight, maxWidth,
			maxHeight);
	}

	private static void printStatistics(ArrayList<TestResult> testResults) {
		final var sb = new StringBuilder();
		testResults.stream().reduce(Arrays.stream(Variant.values()).collect(Collectors.toMap(v -> v, v -> new ArrayList<RunResult>())), (acc, elm) -> {
			elm.runResultsPerVariant().forEach((variant, runResult) -> acc.get(variant).add(runResult));
			return acc;
		}, (left, right) -> {
			right.forEach((variant, runResults) -> left.get(variant).addAll(runResults));
			return left;
		}).forEach((variant, runResults) -> {
			final var durAvgOpt = runResults.stream().mapToLong(RunResult::duration).average();
			final var boxAvgOpt = runResults.stream().mapToLong(RunResult::boxCount).average();
			if (durAvgOpt.isPresent() && boxAvgOpt.isPresent())
				sb.append("%s: %fms\t%f boxes\n".formatted(variant.toString(),
					durAvgOpt.getAsDouble(), boxAvgOpt.getAsDouble()));
		});
		System.out.println(sb);
	}

	@Test
	public void testAll() {
		printArguments();
		final var results = IntStream.range(0, instanceCount).mapToObj(i -> new TestInput(i,
			new Input(generateInstances(rectangleCount, minWidth, maxWidth, minHeight, maxHeight),
				boxLength))).map(TestResult::new).collect(Collectors.toCollection(ArrayList<TestResult>::new));
		results.forEach(tr -> System.out.println(tr.toString()));
		printStatistics(results);
	}
}
