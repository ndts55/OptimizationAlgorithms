package org.ndts.optalgj.algs;

public interface Oracle<Solution, Wrapper extends GreedyWrapper<Solution>> {
	boolean isLeaf(Wrapper wrapper);
}
