package twilightforest.test.util.multiparts;

import twilightforest.util.multiparts.MultipartEntityUtil;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import tamaized.beanification.junit.MockitoFixer;
import twilightforest.client.BakedMultiPartRenderers;
import twilightforest.entity.TFPart;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoFixer.class)
public class MultipartEntityUtilTests {

	private MultipartEntityUtil instance;

	@BeforeEach
	public void setup() {
		instance = new MultipartEntityUtil();
	}

	@Test
	public void tryLookupTFPartRenderer() {
		try (MockedStatic<BakedMultiPartRenderers> lookup = mockStatic(BakedMultiPartRenderers.class)) {
			Identifier location = Identifier.withDefaultNamespace("test");
			EntityRenderer<?, ?> partRenderer = mock(EntityRenderer.class);
			lookup.when(() -> BakedMultiPartRenderers.lookup(location)).thenReturn(partRenderer);

			Object part = mock(TFPart.class);
			doReturn(location).when((TFPart<?>) part).renderer();

			EntityRenderer<?, ?> originalRenderer = mock(EntityRenderer.class);

			EntityRenderer<?, ?> result = instance.tryLookupTFPartRenderer(originalRenderer, (Entity) part);

			assertNotNull(result);
			assertSame(partRenderer, result);
			assertNotSame(originalRenderer, result);
		}
	}

	@Test
	public void tryLookupTFPartRendererNonTFPart() {
		try (MockedStatic<BakedMultiPartRenderers> lookup = mockStatic(BakedMultiPartRenderers.class)) {
			EntityRenderer<?, ?> partRenderer = mock(EntityRenderer.class);
			lookup.when(() -> BakedMultiPartRenderers.lookup(any(Identifier.class))).thenReturn(partRenderer);

			EntityRenderer<?, ?> originalRenderer = mock(EntityRenderer.class);

			EntityRenderer<?, ?> result = instance.tryLookupTFPartRenderer(originalRenderer, mock(Entity.class));

			assertNotNull(result);
			assertNotSame(partRenderer, result);
			assertSame(originalRenderer, result);
		}
	}

}
