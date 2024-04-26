package org.ndts.optalgj.problems.rect.objs;

import org.ndts.optalgj.algs.ObjectiveFunction;
import org.ndts.optalgj.problems.rect.domain.Output;

import java.util.stream.IntStream;

public class BoxCountAndOverlaps implements ObjectiveFunction<Output> {
	@Override
	public Double evaluate(Output output) {
		final var boxCount = output.boxes().size();
		final var rectanglesWithOverlap =
			output.boxes().stream().mapToLong(b -> IntStream.range(0, b.size()).filter(b::overlapExistsAt).count()).sum();
		return (double) (boxCount + 2 * rectanglesWithOverlap);
	}
}
