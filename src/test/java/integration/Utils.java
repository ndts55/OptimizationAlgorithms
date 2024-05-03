package integration;

import org.ndts.optalgj.algs.OptimizationAlgorithm;
import org.ndts.optalgj.problems.rect.domain.Input;
import org.ndts.optalgj.problems.rect.domain.Output;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.ndts.optalgj.problems.rect.utils.InstanceGenerator.generateInstances;

public class Utils {
	private static final Input input = newInput();

	private static Input newInput() {
		int boxLength = 20;
		return new Input(generateInstances(400, 1, 7, 1, 7), boxLength);
	}

	public static Input getInput() {return new Input(input);}

	private static void printOutput(final Output output, final Duration duration,
									final OptimizationAlgorithm<Output> search) {
		System.out.printf("""
				Box Count: %d
				Used Space: %s%%
				Duration: %dms
				Iterations: %d
				Overlaps: %s
				%n%n""", output.boxes().size(), output.usedSpace() * 100, duration.toMillis(),
			search.iteration(),
			output.firstBoxWithOverlap().map(("Has Overlap at: %d")::formatted).orElse("Has No " + "Overlaps"));
	}

	public static void runSearch(final String name, final OptimizationAlgorithm<Output> search) {
		System.out.println(name);
		final var start = LocalDateTime.now();
		//noinspection StatementWithEmptyBody
		while (search.iterate()) ;
		final var end = LocalDateTime.now();
		final var duration = Duration.between(start, end);
		final var output = search.best();
		printOutput(output, duration, search);
	}
}
