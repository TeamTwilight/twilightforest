package twilightforest.junit;


import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class MockitoFixer implements BeforeEachCallback {

	@Override
	public void beforeEach(ExtensionContext context) {
		Thread.currentThread().setContextClassLoader(ClassLoader.getSystemClassLoader());
	}

}
