package integration;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestJava {
	@Test
	public void testNestedArraylistCopy() {
		final List<List<Integer>> original = new ArrayList<>() {{
			add(new ArrayList<>() {{add(1);}});
		}};

		final var copy = new ArrayList<>(original);

		copy.getFirst().add(2);

		assertEquals(2, original.getFirst().size());
		assertEquals(2, copy.getFirst().size());

		final var deepCopy =
			original.stream().map(ArrayList::new).collect(Collectors.toCollection(ArrayList::new));
		deepCopy.getFirst().add(3);

		assertEquals(2, original.getFirst().size());
		assertEquals(2, copy.getFirst().size());
		assertEquals(3, deepCopy.getFirst().size());
	}

	@Test
	public void testFilterMutation() {
		final var ns = List.of(1, 2, 3, 4, 5);
		final var fs = ns.stream().filter(n -> {
			n += 10;
			return n % 2 == 0;
		}).collect(Collectors.toCollection(ArrayList::new));
		assertEquals(4, fs.getLast());
	}
}
