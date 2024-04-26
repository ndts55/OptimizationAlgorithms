package org.ndts.optalgj.problems.rect.utils;

import org.ndts.optalgj.problems.rect.domain.Box;
import org.ndts.optalgj.problems.rect.domain.Input;
import org.ndts.optalgj.problems.rect.domain.Output;
import org.ndts.optalgj.problems.rect.domain.PositionedRectangle;

import java.util.ArrayList;
import java.util.List;

public class SolutionConstructor {
	public static Output forLocal(Input input) {
		List<Box> boxes = new ArrayList<>(input.rectangles().size());
		for (var rectangle : input.rectangles())
			boxes.add(new Box(new ArrayList<>(1) {{
				add(new PositionedRectangle(rectangle));
			}}));
		return new Output(input.boxLength(), boxes);
	}
}
