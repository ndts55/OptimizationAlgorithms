package org.ndts.optalgj.problems.rect;

import org.ndts.optalgj.algs.Neighborhood;

import java.util.List;

/*
Ein Nachbar lässt sich erzeugen, indem Rechtecke direkt verschoben werden, sowohl innerhalb einer
Box als auch von einer Box zur anderen. Im Prinzip sind Sie frei darin, wie Sie das machen. Sie sind
auch frei darin, die Zielfunktion so abzuändern, dass Nachbarn auch dann besser sind, wenn die
eigentlich zu minimierende Zielfunktion nicht besser ist, der Nachbar aber nach heuristischen
Überlegungen näher an einer Verbesserung dran ist. Zum Beispiel könnte es sinnvoll sein, einen
Schritt zu belohnen, bei dem die Anzahl Rechtecke in einer Box, in der ohnehin nur wenige Rechtecke
in dieser Box sind, weiter verringert wird, auch wenn die Box damit (noch) nicht leer ist.
 */
public class GeometricNeighborhood implements Neighborhood<Output> {
	@Override
	public List<Output> neighborhood(Output positionedRectangles) {
		throw new UnsupportedOperationException();
	}
}
