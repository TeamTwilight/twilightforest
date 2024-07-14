package twilightforest.beans;

import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.neoforgespi.language.ModFileScanData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.objectweb.asm.Type;
import twilightforest.TwilightForestMod;
import twilightforest.junit.MockitoFixer;
import twilightforest.util.junit.DummyClass;

import java.lang.annotation.ElementType;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoFixer.class)
public class TFBeanContextTests {

	@BeforeEach
	public void setup() {
		DummyClass.reset();
	}

	@Test
	public void init() {
		try (MockedStatic<ModList> modList = mockStatic(ModList.class, RETURNS_DEEP_STUBS)) {
			ModFileScanData scanData = mock(ModFileScanData.class);

			Type clazzDefaultName = mock(Type.class);
			when(clazzDefaultName.getClassName()).thenReturn(DummyClass.class.getCanonicalName());

			ModFileScanData.AnnotationData annotationDataDefaultName = mock(ModFileScanData.AnnotationData.class);
			when(annotationDataDefaultName.clazz()).thenReturn(clazzDefaultName);
			when(annotationDataDefaultName.annotationData()).thenReturn(Map.of("value", Component.DEFAULT_VALUE));

			ModFileScanData.AnnotationData annotationDataCustomName = mock(ModFileScanData.AnnotationData.class);
			when(annotationDataCustomName.clazz()).thenReturn(clazzDefaultName);
			when(annotationDataCustomName.annotationData()).thenReturn(Map.of("value", "name"));

			ModFileScanData.AnnotationData annotationDataBeanAnnotation = mock(ModFileScanData.AnnotationData.class);
			when(annotationDataBeanAnnotation.clazz()).thenReturn(clazzDefaultName);
			when(annotationDataBeanAnnotation.memberName()).thenReturn("test");
			when(annotationDataBeanAnnotation.annotationData()).thenReturn(Map.of("value", "bean_method"));

			when(scanData.getAnnotatedBy(Component.class, ElementType.TYPE)).thenReturn(Stream.of(annotationDataDefaultName, annotationDataCustomName));
			when(scanData.getAnnotatedBy(Bean.class, ElementType.METHOD)).thenReturn(Stream.of(annotationDataBeanAnnotation));

			ModContainer container = mock(ModContainer.class, RETURNS_DEEP_STUBS);
			when(container.getModInfo().getOwningFile().getFile().getScanResult()).thenReturn(scanData);
			modList.when(() -> ModList.get().getModContainerById(TwilightForestMod.ID)).thenReturn(Optional.of(container));

			TFBeanContext.init();
			TFBeanContext.register(DummyClass.class, "manual", new DummyClass());

			DummyClass resultNamed = TFBeanContext.inject(DummyClass.class, "name");
			assertNotNull(resultNamed);
			assertEquals(1, resultNamed.id);

			DummyClass result = TFBeanContext.inject(DummyClass.class);
			assertNotNull(result);
			assertEquals(0, result.id);

			DummyClass resultBeanMethod = TFBeanContext.inject(DummyClass.class, "bean_method");
			assertNotNull(resultBeanMethod);
			assertEquals(2, resultBeanMethod.id);
			assertInstanceOf(DummyClass.ExtendedDummyClass.class, resultBeanMethod);

			DummyClass resultManual = TFBeanContext.inject(DummyClass.class, "manual");
			assertNotNull(resultManual);
			assertEquals(3, resultManual.id);
		}
	}

}
