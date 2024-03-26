package org.ndts.optalgj.problems.rect;

import org.ndts.optalgj.algs.CopyConstructible;

import java.util.ArrayList;
import java.util.List;

public record Output(int boxLength, List<Box> boxes) implements CopyConstructible<Output> {

	public Output(final Output output) {
		this(output.boxLength, new ArrayList<>(output.boxes.size()));
		for (var box : output.boxes) boxes.add(new Box(box));
	}

	@Override
	public Output copy() {
		return new Output(this);
	}
}
