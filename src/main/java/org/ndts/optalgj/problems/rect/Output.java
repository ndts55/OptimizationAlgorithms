package org.ndts.optalgj.problems.rect;

import org.ndts.optalgj.algs.DeepCopy;

import java.util.List;

public record Output(List<Box> boxes) implements DeepCopy<Output> {
	@Override
	public Output deepCopy() {
		// TODO implement deep copy of Output
		throw new UnsupportedOperationException();
	}
}
