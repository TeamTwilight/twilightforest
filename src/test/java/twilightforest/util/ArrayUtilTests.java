package twilightforest.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ArrayUtilTests {

	@Test
	public void clampedBeyondLength() {
		Object filler = new Object();
		Object check = new Object();

		Object result = ArrayUtil.clamped(new Object[]{filler, filler, check}, 20);

		assertSame(check, result);
	}

	@Test
	public void clampedAtLength() {
		Object filler = new Object();
		Object check = new Object();

		Object result = ArrayUtil.clamped(new Object[]{filler, filler, check}, 2);

		assertSame(check, result);
	}

	@Test
	public void clampedBeforeLength() {
		Object filler = new Object();
		Object check = new Object();

		Object result = ArrayUtil.clamped(new Object[]{filler, check, filler}, 1);

		assertSame(check, result);
	}

	@Test
	public void clampedZero() {
		Object filler = new Object();
		Object check = new Object();

		Object result = ArrayUtil.clamped(new Object[]{check, filler, filler}, 0);

		assertSame(check, result);
	}

	@Test
	public void clampedNegative() {
		Object filler = new Object();
		Object check = new Object();

		Object result = ArrayUtil.clamped(new Object[]{check, filler, filler}, -20);

		assertSame(check, result);
	}

	@Test
	public void wrappedBeyondLength() {
		Object filler = new Object();
		Object check = new Object();

		Object result = ArrayUtil.wrapped(new Object[]{filler, filler, check}, 5);

		assertSame(check, result);
	}

	@Test
	public void wrappedAtLength() {
		Object filler = new Object();
		Object check = new Object();

		Object result = ArrayUtil.wrapped(new Object[]{filler, filler, check}, 2);

		assertSame(check, result);
	}

	@Test
	public void wrappedBeforeLength() {
		Object filler = new Object();
		Object check = new Object();

		Object result = ArrayUtil.wrapped(new Object[]{filler, check, filler}, 1);

		assertSame(check, result);
	}

	@Test
	public void wrappedZero() {
		Object filler = new Object();
		Object check = new Object();

		Object result = ArrayUtil.wrapped(new Object[]{check, filler, filler}, 0);

		assertSame(check, result);
	}

	@Test
	public void wrappedNegative() {
		Object filler = new Object();

		assertThrows(ArrayIndexOutOfBoundsException.class, () -> ArrayUtil.wrapped(new Object[]{filler, filler, filler}, -2));
	}

}
