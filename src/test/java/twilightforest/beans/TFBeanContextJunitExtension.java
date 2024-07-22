package twilightforest.beans;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;
import org.mockito.MockedStatic;

import javax.annotation.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class TFBeanContextJunitExtension implements BeforeAllCallback, AfterAllCallback, TestInstancePostProcessor {

	private static final Logger LOGGER = LogManager.getLogger(TFBeanContextJunitExtension.class);

	@Nullable
	private MockedStatic<TFBeanContext> beanContext;

	@Nullable
	private TFBeanContext beanContextInstance;

	private Map<TFBeanContext.BeanDefinition<?>, Object> mockedBeans = new HashMap<>();

	private <T> T mockBean(Class<T> type) {
		return mockBean(type, null);
	}

	private <T> T mockBean(Class<T> type, @Nullable String name) {
		assertNotNull(beanContext);
		assertNotNull(beanContextInstance);
		T bean = mock(type);
		if (name == null)
			beanContext.when(() -> TFBeanContext.inject(type)).thenReturn(bean);
		beanContext.when(() -> TFBeanContext.inject(type, name)).thenReturn(bean);
		doReturn(bean).when(beanContextInstance).injectInternal(type, name);
		return bean;
	}

	@Override
	public void beforeAll(ExtensionContext context) {
		LOGGER.info("Dirtied TFBeanContext");
		beanContextInstance = spy(TFBeanContext.class);
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
				field.set(testInstance, mockedBeans.computeIfAbsent(
					new TFBeanContext.BeanDefinition<>(field.getType(), name),
					k -> name.equals(MockBean.DEFAULT_VALUE) ? mockBean(field.getType()) : mockBean(field.getType(), name)
				));
			}
		}

		assertNotNull(beanContextInstance);
		if (beanContextInstance.isFrozen())
			return;
		Field f = TFBeanContext.class.getDeclaredField("INSTANCE");
		f.trySetAccessible();
		f.set(null, beanContextInstance);
		Method init = TFBeanContext.class.getDeclaredMethod("initInternal", Consumer.class, boolean.class);
		init.trySetAccessible();
		init.invoke(beanContextInstance, null, true);
	}
}
