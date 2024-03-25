package org.ndts.optalgj.problems.rect;

import org.ndts.optalgj.algs.Neighborhood;

import java.util.List;

/*
Die geometriebasierte Nachbarschaft wird angepasst auf die Situation, dass Rechtecke sich zu einem
gewissen Prozentsatz überlappen dürfen. Die Überlappung zweier Rechtecke ist dabei die gemeinsame
Fläche geteilt durch das Maximum der beiden Rechteckflächen. Dieser Prozentsatz ist zu Beginn 100
(so, dass eine Optimallösung einfach zu finden ist). Im Laufe der Zeit reduziert sich der
Prozentsatz, und Verletzungen werden hart in der Zielfunktion bestraft. Am Ende müssen Sie natürlich
dafür sorgen, dass schlussendlich eine garantiert überlappungsfreie Lösung entsteht.
 */

public class OverlapNeighborhood implements Neighborhood<List<PositionedRectangle>> {
	@Override
	public List<List<PositionedRectangle>> neighborhood(List<PositionedRectangle> positionedRectangleList) {
		throw new UnsupportedOperationException();
	}
}
