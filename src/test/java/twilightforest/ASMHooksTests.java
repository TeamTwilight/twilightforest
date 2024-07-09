package twilightforest;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.entity.PartEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import twilightforest.client.TFClientSetup;
import twilightforest.entity.TFPart;
import twilightforest.junit.MockitoFixer;

import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoFixer.class)
public class ASMHooksTests {

	@Test
	public void resolveEntityRenderer() {
		try (MockedStatic<TFClientSetup.BakedMultiPartRenderers> lookup = mockStatic(TFClientSetup.BakedMultiPartRenderers.class)) {
			ResourceLocation location = ResourceLocation.withDefaultNamespace("test");
			EntityRenderer<?> partRenderer = mock(EntityRenderer.class);
			lookup.when(() -> TFClientSetup.BakedMultiPartRenderers.lookup(location)).thenReturn(partRenderer);

			TFPart<?> part = mock(TFPart.class);
			when(part.renderer()).thenReturn(location);

			EntityRenderer<?> originalRenderer = mock(EntityRenderer.class);

			EntityRenderer<?> result = ASMHooks.resolveEntityRenderer(originalRenderer, part);

			assertNotNull(result);
			assertSame(partRenderer, result);
			assertNotSame(originalRenderer, result);
		}
	}

	@Test
	public void resolveEntityRendererNonTFPart() {
		try (MockedStatic<TFClientSetup.BakedMultiPartRenderers> lookup = mockStatic(TFClientSetup.BakedMultiPartRenderers.class)) {
			EntityRenderer<?> partRenderer = mock(EntityRenderer.class);
			lookup.when(() -> TFClientSetup.BakedMultiPartRenderers.lookup(any(ResourceLocation.class))).thenReturn(partRenderer);

			EntityRenderer<?> originalRenderer = mock(EntityRenderer.class);

			EntityRenderer<?> result = ASMHooks.resolveEntityRenderer(originalRenderer, mock(Entity.class));

			assertNotNull(result);
			assertNotSame(partRenderer, result);
			assertSame(originalRenderer, result);
		}
	}

	private Entity mockEntity(PartEntity<?>... parts) {
		Entity entity = mock(Entity.class);
		when(entity.isMultipartEntity()).thenReturn(parts.length > 0);
		when(entity.getParts()).thenReturn(parts);
		return entity;
	}

	@Test
	public void resolveEntitiesForRendering() {
		Iterator<Entity> result = ASMHooks.resolveEntitiesForRendering(List.of(
			mockEntity(),
			mockEntity(mock(PartEntity.class)),
			mockEntity(mock(TFPart.class)),
			mockEntity(),
			mockEntity(mock(TFPart.class), mock(TFPart.class)),
			mockEntity()
		).iterator());

		assertNotNull(result);
		// Since these are mocks, we need to use #getSuperclass
		assertEquals(Entity.class, assertDoesNotThrow(result::next).getClass().getSuperclass());
		assertEquals(Entity.class, assertDoesNotThrow(result::next).getClass().getSuperclass());
		assertEquals(Entity.class, assertDoesNotThrow(result::next).getClass().getSuperclass());
		assertEquals(TFPart.class, assertDoesNotThrow(result::next).getClass().getSuperclass());
		assertEquals(Entity.class, assertDoesNotThrow(result::next).getClass().getSuperclass());
		assertEquals(Entity.class, assertDoesNotThrow(result::next).getClass().getSuperclass());
		assertEquals(TFPart.class, assertDoesNotThrow(result::next).getClass().getSuperclass());
		assertEquals(TFPart.class, assertDoesNotThrow(result::next).getClass().getSuperclass());
		assertEquals(Entity.class, assertDoesNotThrow(result::next).getClass().getSuperclass());
	}

}
