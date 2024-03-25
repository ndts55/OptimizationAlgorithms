package org.ndts.optalgj.algs;

import java.util.List;

public interface Neighborhood<T> {
	List<T> neighborhood(T t);
}
