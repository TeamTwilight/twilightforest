package twilightforest.util.multiparts;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;
import tamaized.beanification.Component;
import twilightforest.client.BakedMultiPartRenderers;
import twilightforest.client.state.entity.PartEntityState;
import twilightforest.entity.TFPart;
import twilightforest.network.UpdateTFMultipartPacket;

import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;

@Component
public class MultipartEntityUtil {

	// Map for non-PartEntityState render states (e.g. FallingBlockRenderState from SnowQueenIceShield)
	// that still need renderer redirection in the submit phase.
	private final Map<EntityRenderState, Identifier> stateRenderers = new WeakHashMap<>();

	public Iterator<Entity> injectTFPartEntities(Iterator<Entity> iter) {
		return new MultipartEntityIteratorWrapper(iter);
	}

	@Nullable
	public EntityRenderer<?,?> tryLookupTFPartRenderer(@Nullable EntityRenderer<?,?> renderer, Entity entity) {
		if (entity instanceof TFPart<?> part) {
			EntityRenderer<?, ?> partRenderer = BakedMultiPartRenderers.lookup(part.renderer());
			return partRenderer != null ? partRenderer : renderer;
		}
		return renderer;
	}

	/**
	 * Register a renderer override for a non-PartEntityState render state.
	 * Used by renderers like SnowQueenIceShieldRenderer that create FallingBlockRenderState
	 * but are rendered through the PartEntity ASM system.
	 */
	public void registerStateRenderer(EntityRenderState state, Identifier rendererId) {
		stateRenderers.put(state, rendererId);
	}

	@Nullable
	public EntityRenderer<?, ?> tryLookupPartStateRenderer(@Nullable EntityRenderer<?, ?> renderer, EntityRenderState state) {
		if (state instanceof PartEntityState partState && partState.partRendererId != null) {
			EntityRenderer<?, ?> partRenderer = BakedMultiPartRenderers.lookup(partState.partRendererId);
			return partRenderer != null ? partRenderer : renderer;
		}
		// Handle non-PartEntityState renderers (e.g. SnowQueenIceShieldRenderer using FallingBlockRenderState)
		Identifier rendererId = stateRenderers.remove(state);
		if (rendererId != null) {
			EntityRenderer<?, ?> partRenderer = BakedMultiPartRenderers.lookup(rendererId);
			return partRenderer != null ? partRenderer : renderer;
		}
		return renderer;
	}

	public Entity sendDirtyMultipartEntityData(Entity entity) {
		if (entity.isMultipartEntity())
			PacketDistributor.sendToPlayersTrackingEntity(entity, new UpdateTFMultipartPacket(entity));
		return entity;
	}

}
