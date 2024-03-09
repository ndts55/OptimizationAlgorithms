package org.ndts.optalgj;

public enum GreedyNeighborhoodVariant {
	VariantA,
	VariantB;

	@Override
	public String toString() {
		return switch (this) {
			case VariantA -> "Variant A";
			case VariantB -> "Variant B";
		};
	}
}
