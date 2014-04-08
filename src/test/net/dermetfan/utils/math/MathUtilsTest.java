package net.dermetfan.utils.math;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import net.dermetfan.utils.ArrayUtils;

import org.junit.Test;

public class MathUtilsTest {

	@Test
	public void normalize() {
		assertEquals(-com.badlogic.gdx.math.MathUtils.PI, MathUtils.normalize(-com.badlogic.gdx.math.MathUtils.PI2 * 2.5f, -com.badlogic.gdx.math.MathUtils.PI2), com.badlogic.gdx.math.MathUtils.PI / 20f);
		assertEquals(-270, MathUtils.normalize(-360 + 90, 360), 0);
		assertEquals(90, MathUtils.normalize(360 + 90, -360), 0);
	}

	@Test
	public void nearest() {
		float[] values = {-32.1f, -53424.23f, 83232, -1, 20, 1.1f, 0};
		assertEquals(1.1f, MathUtils.nearest(3, values), 0);
		assertEquals(83232, MathUtils.nearest(Float.POSITIVE_INFINITY, values), 0);
		assertEquals(-53424.23f, MathUtils.nearest(Float.NEGATIVE_INFINITY, values, 100), 0);
		assertEquals(180, MathUtils.nearest(100, new float[] {-300, 200, 180}, 100), 0);
		assertTrue(Float.isNaN(MathUtils.nearest(0, new float[] {-300, 200, 180}, 100)));
	}

	@Test
	public void amplitude() {
		float[] values = {-5, 1, 3, 2, 4, 1, -4, 3.2f, 5};
		assertEquals(10, MathUtils.amplitude(values), 0);
	}

	@Test
	public void scale() {
		float[] values = {-50, -25, 0, 25, 50};
		assertEquals(1, MathUtils.amplitude(MathUtils.scale(values, 0, 1, false)), 0);
	}

	@Test
	public void abs() {
		assertArrayEquals(new Float[] {1f, 1f, 0f}, ArrayUtils.box(MathUtils.abs(new float[] {1, -1, 0})));
	}

	@Test
	public void add() {
		assertArrayEquals(new Float[] {0f, 1f, 2f}, ArrayUtils.box(MathUtils.add(new float[] {-1, 0, 1}, 1)));
	}

	@Test
	public void sub() {
		assertArrayEquals(new Float[] {0f, 1f, 2f}, ArrayUtils.box(MathUtils.sub(new float[] {1, 2, 3}, 1)));
	}

	@Test
	public void mul() {
		assertArrayEquals(new Float[] {0f, 1f, 2f}, ArrayUtils.box(MathUtils.mul(new float[] {0, .5f, 1}, 2)));
	}

	@Test
	public void div() {
		assertArrayEquals(new Float[] {0f, 1f, 2f}, ArrayUtils.box(MathUtils.div(new float[] {0, 2, 4}, 2)));
	}

}