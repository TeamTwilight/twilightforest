package twilightforest.junit;


import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;
import org.mockito.MockedStatic;
import twilightforest.beans.TFBeanContext;

import javax.annotation.Nullable;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

public class TFBeanContextJunitExtension implements BeforeAllCallback, AfterAllCallback, TestInstancePostProcessor {

	@Nullable
	private MockedStatic<TFBeanContext> beanContext;

	private <T> T mockBean(Class<T> type) {
		return mockBean(type, null);
	}

	private  <T> T mockBean(Class<T> type, @Nullable String name) {
		assertNotNull(beanContext);
		T bean = mock(type);
		if (name == null)
			beanContext.when(() -> TFBeanContext.inject(type)).thenReturn(bean);
		beanContext.when(() -> TFBeanContext.inject(type, name)).thenReturn(bean);
		return bean;
	}

	@Override
	public void beforeAll(ExtensionContext context) {
		beanContext = mockStatic(TFBeanContext.class);
	}

	@Override
	public void afterAll(ExtensionContext context) {
		assertNotNull(beanContext);
		beanContext.close();
	}

	@Override
	public void postProcessTestInstance(Object testInstance, ExtensionContext context) throws Exception {
		for (Field field : AnnotationSupport.findAnnotatedFields(testInstance.getClass(), MockBean.class)) {
			field.trySetAccessible();
			if (field.get(testInstance) == null) {
				String name = field.getAnnotation(MockBean.class).value();
				field.set(testInstance, name.equals(MockBean.DEFAULT_VALUE) ? mockBean(field.getType()) : mockBean(field.getType(), name));
			}
		}
	}
}
