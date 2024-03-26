package org.ndts.optalgj.algs;

public interface Neighborhood<T> {
	T betterNeighbor(T initial, ObjectiveFunction<T> obj);
}
