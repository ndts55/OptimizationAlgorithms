package org.ndts.optalgj.problems.rect;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestPositionedRectangle {
	final PositionedRectangle p0 = new PositionedRectangle(new Rectangle(0, 5, 5), 0, 0, false);
	final PositionedRectangle p1 = new PositionedRectangle(new Rectangle(1, 5, 5), 1, 0, false);
	final PositionedRectangle p2 = new PositionedRectangle(new Rectangle(2, 1, 1), 5, 0, false);
	final PositionedRectangle p3 = new PositionedRectangle(new Rectangle(3, 1, 1), 6, 5, false);
	final PositionedRectangle p4 = new PositionedRectangle(new Rectangle(4, 1, 15), 0, 4, false);
	final PositionedRectangle p5 = new PositionedRectangle(new Rectangle(5, 10, 10), 0, 0, false);

	@Test
	public void testOverlapsWith() {
		assertTrue(p0.overlapsWith(p1), "p0 should overlap with p1");
		assertFalse(p0.overlapsWith(p2), "p0 should not overlap with p2");
		assertTrue(p1.overlapsWith(p2), "p1 should overlap with p2");
		assertFalse(p0.overlapsWith(p3), "p0 should not overlap with p3");
		assertFalse(p1.overlapsWith(p3), "p1 should not overlap with p3");
		assertFalse(p2.overlapsWith(p3), "p2 should not overlap with p3");
		assertTrue(p0.overlapsWith(p4), "p0 should overlap with p4");
		assertTrue(p1.overlapsWith(p4), "p1 should overlap with p4");
		assertFalse(p2.overlapsWith(p4), "p2 should not overlap with p4");
		assertFalse(p3.overlapsWith(p4), "p3 should not overlap with p4");
		assertTrue(p0.overlapsWith(p5), "p0 should overlap with p5");
		assertTrue(p1.overlapsWith(p5), "p1 should overlap with p5");
		assertTrue(p2.overlapsWith(p5), "p2 should overlap with p5");
		assertTrue(p3.overlapsWith(p5), "p3 should overlap with p5");
		assertTrue(p4.overlapsWith(p5), "p4 should overlap with p5");
	}

	@Test
	public void testOverlapArea() {
		assertEquals(20, p0.overlapArea(p1), "p0 and p1 overlap area should be 20");
		assertEquals(0, p0.overlapArea(p2), "p0 and p2 overlap area should be 0");
		assertEquals(0, p0.overlapArea(p3), "p0 and p3 overlap area should be 0");
		assertEquals(5, p0.overlapArea(p4), "p0 and p4 overlap area should be 5");
		assertEquals(25, p0.overlapArea(p5), "p0 and p5 overlap area should be 25");
		assertEquals(1, p1.overlapArea(p2), "p1 and p2 overlap area should be 1");
		assertEquals(0, p1.overlapArea(p3), "p1 and p3 overlap area should be 0");
		assertEquals(5, p1.overlapArea(p4), "p1 and p4 overlap area should be 5");
		assertEquals(25, p1.overlapArea(p5), "p1 and p5 overlap area should be 25");
		assertEquals(0, p2.overlapArea(p3), "p2 and p3 overlap area should be 0");
		assertEquals(0, p2.overlapArea(p4), "p2 and p4 overlap area should be 0");
		assertEquals(1, p2.overlapArea(p5), "p2 and p5 overlap area should be 1");
		assertEquals(0, p3.overlapArea(p4), "p3 and p4 overlap area should be 0");
		assertEquals(1, p3.overlapArea(p5), "p3 and p5 overlap area should be 1");
		assertEquals(10, p4.overlapArea(p5), "p4 and p5 overlap area should be 10");
	}

	@Test
	public void testOverlapPercentage() {
		assertEquals(0.8, p0.overlapPercentage(p1), "p0 and p1 overlap percentage should be 0.8");
		assertEquals(0.0, p0.overlapPercentage(p2), "p0 and p2 overlap percentage should be 0.0");
		assertEquals(0.0, p0.overlapPercentage(p3), "p0 and p3 overlap percentage should be 0.0");
		assertEquals(0.2, p0.overlapPercentage(p4), "p0 and p4 overlap percentage should be 0.2");
		assertEquals(1.0, p0.overlapPercentage(p5), "p0 and p5 overlap percentage should be 1.0");
		assertEquals(0.8, p1.overlapPercentage(p0), "p1 and p0 overlap percentage should be 0.8");
		assertEquals(0.04, p1.overlapPercentage(p2), "p1 and p2 overlap percentage should be 0" +
			".04");
		assertEquals(0.0, p1.overlapPercentage(p3), "p1 and p3 overlap percentage should be 0.0");
		assertEquals(0.2, p1.overlapPercentage(p4), "p1 and p4 overlap percentage should be 0.2");
		assertEquals(1.0, p1.overlapPercentage(p5), "p1 and p5 overlap percentage should be 1.0");
		assertEquals(0.0, p2.overlapPercentage(p0), "p2 and p0 overlap percentage should be 0.0");
		assertEquals(1.0, p2.overlapPercentage(p1), "p2 and p1 overlap percentage should be 1.0");
		assertEquals(0.0, p2.overlapPercentage(p3), "p2 and p3 overlap percentage should be 0.0");
		assertEquals(0.0, p2.overlapPercentage(p4), "p2 and p4 overlap percentage should be 0.0");
		assertEquals(1.0, p2.overlapPercentage(p5), "p2 and p5 overlap percentage should be 1.0");
		assertEquals(0.0, p3.overlapPercentage(p0), "p3 and p0 overlap percentage should be 0.0");
		assertEquals(0.0, p3.overlapPercentage(p1), "p3 and p1 overlap percentage should be 0.0");
		assertEquals(0.0, p3.overlapPercentage(p2), "p3 and p2 overlap percentage should be 0.0");
		assertEquals(0.0, p3.overlapPercentage(p4), "p3 and p4 overlap percentage should be 0.0");
		assertEquals(1.0, p3.overlapPercentage(p5), "p3 and p5 overlap percentage should be 1.0");
		assertEquals(1.0 / 3.0, p4.overlapPercentage(p0), "p3 and p0 overlap percentage should " +
			"be" +
			" " +
			"1/3");
		assertEquals(1.0 / 3.0, p4.overlapPercentage(p1), "p3 and p1 overlap percentage should " +
			"be" +
			" " +
			"1/3");
		assertEquals(0.0, p4.overlapPercentage(p2), "p4 and p2 overlap percentage should be 0.0");
		assertEquals(0.0, p4.overlapPercentage(p3), "p4 and p3 overlap percentage should be 0.0");
		assertEquals(2.0 / 3.0, p4.overlapPercentage(p5), "p4 and p5 overlap percentage should " +
			"be" +
			" " +
			"2/3");
		assertEquals(0.25, p5.overlapPercentage(p0), "p5 and p0 overlap percentage should be 0" +
			".25");
		assertEquals(0.25, p5.overlapPercentage(p1), "p5 and p1 overlap percentage should be 0" +
			".25");
		assertEquals(0.01, p5.overlapPercentage(p2), "p5 and p2 overlap percentage should be 0" +
			".01");
		assertEquals(0.01, p5.overlapPercentage(p3), "p5 and p3 overlap percentage should be 0" +
			".01");
		assertEquals(0.1, p5.overlapPercentage(p4), "p5 and p4 overlap percentage should be 0.1");
	}
}
