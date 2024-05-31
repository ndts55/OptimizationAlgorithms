package org.ndts.optalgj.problems.rect;

import org.ndts.optalgj.algs.GreedySearchVariant;
import org.ndts.optalgj.algs.LocalSearchVariant;
import org.ndts.optalgj.algs.ObjectiveFunction;
import org.ndts.optalgj.problems.rect.domain.Output;

import java.util.stream.IntStream;

public class MinBoxObjs {
	private static double boxCount(Output output) {
		return output.boxes().size();
	}

	private static double rectsWithOverlapCount(Output output) {
		return output.boxes().stream().mapToLong(b -> IntStream.range(0, b.size()).filter(b::overlapExistsAt).count()).sum();
	}

	private static double lastOccupiedArea(Output output) {
		return ((double) output.boxes().getLast().occupiedArea()) / ((double) output.boxLength() * output.boxLength());
	}

	public static ObjectiveFunction<Output> regular() {
		return MinBoxObjs::boxCount;
	}

	public static ObjectiveFunction<Output> punishOverlaps() {
		return output -> boxCount(output) + 2 * rectsWithOverlapCount(output);
	}

	public static ObjectiveFunction<Output> punishLastBoxFullness() {
		return output -> boxCount(output) + lastOccupiedArea(output);
	}

	public static ObjectiveFunction<Output> getRecommended(LocalSearchVariant variant) {
		return switch (variant) {
			case Geometric -> regular();
			case Rules -> punishLastBoxFullness();
			case Overlap -> punishOverlaps();
		};
	}

	public static ObjectiveFunction<Output> getRecommended(GreedySearchVariant variant) {
		return regular();
	}
}
