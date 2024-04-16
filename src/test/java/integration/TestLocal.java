package integration;

import org.junit.jupiter.api.Test;
import org.ndts.optalgj.algs.LocalSearch;
import org.ndts.optalgj.problems.rect.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static org.ndts.optalgj.problems.rect.InstanceGenerator.generateInstances;

class RunSearch implements Callable<String> {
	private final String name;
	private final LocalSearch<Input, Output> search;
	private final Input input;

	public RunSearch(final String name, final LocalSearch<Input, Output> search,
					 final Input input) {
		this.name = name;
		this.search = search;
		this.input = input;
	}

	@Override
	public String call() {
		search.initialize(input);
		final var start = LocalDateTime.now();
		//noinspection StatementWithEmptyBody
		while (search.iteration()) {}
		final var end = LocalDateTime.now();
		final var duration = Duration.between(start, end);
		final var output = search.bestOutput();
		return """
			%s
			Box Count: %d
			Used Space: %s%%
			Duration: %ds
			Iterations: %d
			Overlaps: %s
			""".formatted(name, output.boxes().size(), output.usedSpace() * 100,
			duration.toSeconds(), search.currentIteration(), output.firstBoxWithOverlap().map((
				"Has" +
				" Overlap at: %d")::formatted).orElse("Has No " + "Overlaps"));
	}
}

public class TestLocal {
	private static Input newInput() {
		int boxLength = 20;
		return new Input(generateInstances(1000, 1, 7, 1, 7), boxLength);
	}

	private static ArrayList<RunSearch> getRunSearches() {
		final var input = newInput();
		return new ArrayList<>(3) {{
			add(new RunSearch("Geometric", new LocalSearch<>(new SimpleSolutionConstructor(),
				new BoxCountMinimization(), new GeometricNeighborhood()), input));
			add(new RunSearch("Rule", new LocalSearch<>(new SimpleSolutionConstructor(),
				new BoxCountMinimization(), new RuleNeighborhood()), input));
			add(new RunSearch("Overlap", new LocalSearch<>(new SimpleSolutionConstructor(),
				new BoxCountAndOverlaps(), new OverlapNeighborhood()), input));
		}};
	}

	@Test
	public void testAll() {
		final var executor = Executors.newFixedThreadPool(3);
		final var futures =
			getRunSearches().stream().map(s -> CompletableFuture.supplyAsync(s::call, executor).thenAccept(System.out::println)).collect(Collectors.toSet());
		futures.forEach(fut -> {
			try {
				fut.get();
			} catch (ExecutionException | InterruptedException e) {
				throw new RuntimeException(e);
			}
		});
	}
}
