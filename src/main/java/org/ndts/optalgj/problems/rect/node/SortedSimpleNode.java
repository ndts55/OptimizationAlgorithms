package org.ndts.optalgj.problems.rect.node;

import org.ndts.optalgj.problems.rect.domain.Input;
import org.ndts.optalgj.problems.rect.domain.Rectangle;

import java.util.Comparator;

public class SortedSimpleNode extends SimpleNode {
	public SortedSimpleNode(Input input) {
		super(input);
		rectangles.sort(Comparator.comparingInt(Rectangle::area).reversed());
	}
}