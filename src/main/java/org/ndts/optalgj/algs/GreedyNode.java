package org.ndts.optalgj.algs;

import java.util.List;

public interface GreedyNode<Solution, Child extends GreedyNode<Solution, Child>> {
	Solution solution();

	List<Child> descend();

	boolean isLeaf();
}
