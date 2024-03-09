package org.ndts.optalgj;

public enum LocalNeighborhoodVariant {
	Geometric,
	Rules,
	Overlap;

	@Override
	public String toString() {
		return switch (this) {
			case Geometric -> "Geometric";
			case Rules -> "Rule-Based";
			case Overlap -> "Partial Overlap";
		};
	}
}
