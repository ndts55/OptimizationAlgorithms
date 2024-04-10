package org.ndts.optalgj.problems.rect;

import org.ndts.optalgj.algs.Neighborhood;
import org.ndts.optalgj.algs.ObjectiveFunction;

import java.util.concurrent.atomic.AtomicBoolean;

/*
Anstelle von zulässigen Lösungen, arbeitet die lokale Suche hier auf Permutationen von Rechtecken.
Analog zum Greedy-Algorithmus werden die Rechtecke in der Reihenfolge der Permutation in den Boxen
platziert. Als Regel: Die Nachbarschaft definieren Sie durch kleine Modifikationsschritte auf der
Permutation. Auch hier könnte es sinnvoll sein, Rechtecke in relativ leeren Boxen anderswo in der
Permutation zu platzieren.
 */
public class RuleNeighborhood implements Neighborhood<Output> {
	// region Class Attributes

	protected final AtomicBoolean cancelled = new AtomicBoolean(false);

	// endregion

	// region Cancellation
	@Override
	public void cancel() {
		cancelled.setRelease(true);
	}

	@Override
	public boolean isCancelled() {
		return cancelled.getAcquire();
	}
	// endregion

	// region Better Neighbor

	@Override
	public Output betterNeighbor(Output initial, ObjectiveFunction<Output> obj) {
		// TODO implement rule-based neighborhood for local search
		return null;
	}

	// endregion
}
