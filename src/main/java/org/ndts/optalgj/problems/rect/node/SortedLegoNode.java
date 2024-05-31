package org.ndts.optalgj.problems.rect.node;

import org.ndts.optalgj.problems.rect.domain.Input;
import org.ndts.optalgj.problems.rect.domain.Rectangle;

import java.util.Comparator;

public class SortedLegoNode extends LegoNode {
	public SortedLegoNode(Input input) {
		super(input);
		rectangles.sort(Comparator.comparingInt(Rectangle::area).reversed());
	}
}
