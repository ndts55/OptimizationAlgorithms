package org.ndts.optalgj.problems.rect;

/*
Algorithmische Problemstellung:
Sie wenden Ihre Algorithmen auf folgendes konkretes Optimierungsproblem an:
Gegeben ist eine endliche Menge von Rechtecken mit ganzzahligen Seitenlängen sowie eine
ganzzahlige Boxlänge L. Sie können sich auf den Fall beschränken, dass keine Seitenlänge eines
Rechtecks größer als L ist. Gesucht ist eine achsenparallele Platzierung aller dieser Rechtecke in
der Ebene so, dass je zwei Rechtecke offen disjunkt platziert sind, das heißt, zwei Rechtecke dürfen
nur Eckpunkte und (Segmente der) Randkanten gemeinsam haben, keine inneren Punkte. Rechtecke dürfen
in beiden möglichen achsenparallelen Orientierungen platziert sein, also auch um 90 Grad rotiert
werden. Die Rechtecke dürfen nicht beliebig platziert sein, sondern jedes Rechteck muss vollständig
in einem Quadrat der Länge L platziert sein. Diese Quadrate heißen Boxen im Folgenden. Zu minimieren
ist die Anzahl der Boxen, die Sie benötigen, um alle Rechtecke darin zu platzieren.
*/

import org.ndts.optalgj.algs.FeasibleSolutions;

import java.util.ArrayList;
import java.util.List;

public class SimpleSolutionConstructor implements FeasibleSolutions<Input, Output> {
	@Override
	public Output arbitrarySolution(Input rectangleInput) {
		// simplest valid solution is to put each rectangle in its own box
		List<Box> boxes = new ArrayList<>(rectangleInput.rectangles().size());
		for (var rectangle : rectangleInput.rectangles())
			boxes.add(new Box(new ArrayList<>(1) {{
				add(new PositionedRectangle(rectangle));
			}}));
		return new Output(rectangleInput.boxLength(), boxes);
	}
}
