package twilightforest.util.junit;

/**
 * Dummy class used for JUnit tests on the main sourceset classloader<br/>
 * Make sure to invoke {@link #reset()} before each test!
 */
public class DummyClass {

	static int incr = 0;

	public final int id = incr++;

	public static void reset() {
		incr = 0;
	}

	public static DummyClass test() {
		return new ExtendedDummyClass();
	}

	public static class ExtendedDummyClass extends DummyClass {

	}
}
