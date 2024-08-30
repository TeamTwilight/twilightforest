package twilightforest.util.multiparts;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import twilightforest.client.BakedMultiPartRenderers;
import twilightforest.entity.TFPart;
import twilightforest.junit.MockitoFixer;

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
			ResourceLocation location = ResourceLocation.withDefaultNamespace("test");
			EntityRenderer<?> partRenderer = mock(EntityRenderer.class);
			lookup.when(() -> BakedMultiPartRenderers.lookup(location)).thenReturn(partRenderer);

			TFPart<?> part = mock(TFPart.class);
			when(part.renderer()).thenReturn(location);

			EntityRenderer<?> originalRenderer = mock(EntityRenderer.class);

			EntityRenderer<?> result = instance.tryLookupTFPartRenderer(originalRenderer, part);

			assertNotNull(result);
			assertSame(partRenderer, result);
			assertNotSame(originalRenderer, result);
		}
	}

	@Test
	public void tryLookupTFPartRendererNonTFPart() {
		try (MockedStatic<BakedMultiPartRenderers> lookup = mockStatic(BakedMultiPartRenderers.class)) {
			EntityRenderer<?> partRenderer = mock(EntityRenderer.class);
			lookup.when(() -> BakedMultiPartRenderers.lookup(any(ResourceLocation.class))).thenReturn(partRenderer);

			EntityRenderer<?> originalRenderer = mock(EntityRenderer.class);

			EntityRenderer<?> result = instance.tryLookupTFPartRenderer(originalRenderer, mock(Entity.class));

			assertNotNull(result);
			assertNotSame(partRenderer, result);
			assertSame(originalRenderer, result);
		}
	}

}
