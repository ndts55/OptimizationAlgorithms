package org.ndts.optalgj;

public enum AlgorithmVariant {
	Local,
	Greedy;


	@Override
	public String toString() {
		return switch (this) {
			case Local -> "Local";
			case Greedy -> "Greedy";
		};
	}
}
