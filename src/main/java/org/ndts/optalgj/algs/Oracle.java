package org.ndts.optalgj.algs;

public interface Oracle<Solution, Wrapper extends GreedyNode<Solution>> {
	boolean isLeaf(Wrapper wrapper);
}
