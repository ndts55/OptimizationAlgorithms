package org.ndts.optalgj.problems.rect;

import org.ndts.optalgj.algs.ObjectiveFunction;

import java.util.List;

public class Obj implements ObjectiveFunction<List<PositionedRectangle>> {
	@Override
	public Double obj(List<PositionedRectangle> positionedRectangles) {
		return (double) positionedRectangles.size();
	}
}
