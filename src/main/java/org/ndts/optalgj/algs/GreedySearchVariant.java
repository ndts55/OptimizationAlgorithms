package org.ndts.optalgj.algs;

public enum GreedySearchVariant {
	VariantA, VariantA2, VariantB, VariantB2;

	@Override
	public String toString() {
		return switch (this) {
			case VariantA -> "Simple";
			case VariantA2 -> "Simple (sorted)";
			case VariantB -> "Lego";
			case VariantB2 -> "Lego (sorted)";
		};
	}
}
