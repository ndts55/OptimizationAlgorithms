package org.ndts.optalgj.problems.rect;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class TestBox {
	final PositionedRectangle p0 = new PositionedRectangle(new Rectangle(0, 5, 5), 0, 0, false);
	final PositionedRectangle p1 = new PositionedRectangle(new Rectangle(1, 5, 5), 1, 0, false);
	final PositionedRectangle p2 = new PositionedRectangle(new Rectangle(2, 1, 1), 5, 0, false);
	final PositionedRectangle p3 = new PositionedRectangle(new Rectangle(3, 1, 1), 6, 5, false);
	final PositionedRectangle p4 = new PositionedRectangle(new Rectangle(4, 1, 15), 0, 4, false);
	final PositionedRectangle p5 = new PositionedRectangle(new Rectangle(5, 10, 10), 0, 0, false);
	final PositionedRectangle p6 = new PositionedRectangle(new Rectangle(6, 4, 2), 3, 3, false);
	final Box box0 = new Box(new ArrayList<>(Arrays.asList(p0, p1, p2, p3, p4, p5)));
	final Box box1 = new Box(new ArrayList<>(Arrays.asList(p0, p2, p3)));
	final Box box2 = new Box(new ArrayList<>(Arrays.asList(p1, p3, p4)));

	@Test
	public void testOverlapsExistAt() {
		assertTrue(box0.overlapExistsAt(0), "p0 in box0 should have overlaps");
		assertTrue(box0.overlapExistsAt(1), "p1 in box0 should have overlaps");
		assertTrue(box0.overlapExistsAt(2), "p2 in box0 should have overlaps");
		assertTrue(box0.overlapExistsAt(3), "p3 in box0 should have overlaps");
		assertTrue(box0.overlapExistsAt(4), "p4 in box0 should have overlaps");
		assertTrue(box0.overlapExistsAt(5), "p5 in box0 should have overlaps");
		assertFalse(box1.overlapExistsAt(0), "p0 in box1 should not have overlaps");
		assertFalse(box1.overlapExistsAt(1), "p2 in box1 should not have overlaps");
		assertFalse(box1.overlapExistsAt(2), "p3 in box1 should not have overlaps");
		assertTrue(box2.overlapExistsAt(0), "p1 in box2 should have overlaps");
		assertFalse(box2.overlapExistsAt(1), "p3 in box2 should not have overlaps");
		assertTrue(box2.overlapExistsAt(2), "p4 in box2 should have overlaps");
	}

	@Test
	public void testHasOverlaps() {
		assertTrue(box0.hasOverlaps(), "box0 should have overlaps");
		assertFalse(box1.hasOverlaps(), "box1 should not have overlaps");
		assertTrue(box2.hasOverlaps(), "box2 should have overlaps");
	}

	@Test
	public void testOverlapPercentageAt() {
		assertEquals(2.0, box0.overlapPercentageAt(0),
			"p0 in box0 should have overlap percentage of 2.0");
		assertEquals(2.04, box0.overlapPercentageAt(1),
			"p1 in box0 should have overlap percentage of 2.04");
		assertEquals(2.0, box0.overlapPercentageAt(2),
			"p2 in box0 should have overlap percentage of 2.0");
		assertEquals(1.0, box0.overlapPercentageAt(3),
			"p3 in box0 should have overlap percentage of 1.0");
		assertEquals(4.0 / 3.0, box0.overlapPercentageAt(4),
			"p4 in box0 should have overlap percentage of 4/3");
		assertEquals(0.62, box0.overlapPercentageAt(5),
			"p5 in box0 should have overlap percentage of 0.62");
		for (var i = 0; i < box1.size(); i += 1)
			assertEquals(0.0, box1.overlapPercentageAt(i),
				"Overlap percentage should be 0.0 in box1");
		assertEquals(0.2, box2.overlapPercentageAt(0),
			"p1 in box2 should have overlap percentage of 0.2");
		assertEquals(0.0, box2.overlapPercentageAt(1),
			"p3 in box2 should have overlap percentage of 0.0");
		assertEquals(1.0 / 3.0, box2.overlapPercentageAt(2),
			"p4 in box2 should have overlap percentage of 1/3");
	}

	@Test
	public void testWouldOverlap() {
		assertTrue(box0.wouldOverlap(p6), "p6 in box0 should have overlap");
		assertTrue(box1.wouldOverlap(p6), "p6 in box1 should have overlap");
		assertTrue(box2.wouldOverlap(p6), "p6 in box2 should have overlap");
	}

	@Test
	public void testPossibleOverlapPercentage() {
		assertEquals(2.25, box0.possibleOverlapPercentage(p6),
			"p6 in box0 should have overlap percentage of 2.5");
		assertEquals(0.5, box1.possibleOverlapPercentage(p6),
			"p6 in box1 should have overlap percentage of 0.5");
		assertEquals(0.75, box2.possibleOverlapPercentage(p6),
			"p6 in box2 should have overlap percentage of 0.75");
	}
}
