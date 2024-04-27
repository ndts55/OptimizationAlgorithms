package org.ndts.optalgj.algs;

public interface Oracle<Solution, Node extends GreedyNode<Solution>> {
	boolean isLeaf(Node node);
}
