package org.ndts.optalgj;

import java.util.List;
import java.util.function.Function;

public interface Neighborhood<T> extends Function<T, List<T>> {
}
