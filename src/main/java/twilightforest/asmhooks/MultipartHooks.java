package twilightforest.asmhooks;

import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.Lightmap;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import tamaized.beanification.Autowired;
import twilightforest.asm.transformers.multipart.ResolveEntityStateRendererTransformer;
import twilightforest.asm.transformers.multipart.SendDirtyEntityDataTransformer;
import twilightforest.util.multiparts.MultipartEntityUtil;

import java.util.Iterator;

@SuppressWarnings({"JavadocReference", "unused"})
public class MultipartHooks {

	@Autowired
	private static MultipartEntityUtil multipartEntityUtil;

	/**
	 * {@link twilightforest.asm.transformers.multipart.ResolveEntitiesForRendereringTransformer}<p/>
	 *
	 * Injection Point:<br/>
	 * {@link net.minecraft.client.renderer.LevelRenderer#renderLevel(DeltaTracker, boolean, Camera, GameRenderer, Lightmap, Matrix4f, Matrix4f)}<br/>
	 * [Targets: {@link net.minecraft.client.multiplayer.ClientLevel#entitiesForRendering}]
	 */
	public static Iterator<Entity> resolveEntitiesForRendering(Iterator<Entity> iter) {
		return multipartEntityUtil.injectTFPartEntities(iter);
	}

	/**
	 * {@link twilightforest.asm.transformers.multipart.ResolveEntityRendererTransformer}<p/>
	 *
	 * Injection Point:<br/>
	 * {@link net.minecraft.client.renderer.entity.EntityRenderDispatcher#getRenderer(Entity)}<br/>
	 * Targets: {@link net.minecraft.client.renderer.entity.EntityRenderDispatcher#renderers}
	 */
	@Nullable
	public static EntityRenderer<?, ?> resolveEntityRenderer(@Nullable EntityRenderer<?, ?> renderer, Entity entity) {
		return multipartEntityUtil.tryLookupTFPartRenderer(renderer, entity);
	}

	/**
	 * {@link ResolveEntityStateRendererTransformer}<p/>
	 *
	 * Injection Point:<br/>
	 * {@link net.minecraft.client.renderer.entity.EntityRenderDispatcher#getRenderer(EntityRenderState)}<br/>
	 * Targets: the renderer lookup result for PartEntity render states in the submit phase
	 */
	@Nullable
	public static EntityRenderer<?, ?> resolveEntityStateRenderer(@Nullable EntityRenderer<?, ?> renderer, EntityRenderState state) {
		return multipartEntityUtil.tryLookupPartStateRenderer(renderer, state);
	}

	/**
	 * Register a renderer override for a non-PartEntityState render state.
	 * Call this from extractRenderState() for renderers that don't extend TFPartRenderer
	 * but still need PartEntity renderer redirection in the submit phase.
	 */
	public static void registerStateRenderer(EntityRenderState state, Identifier rendererId) {
		multipartEntityUtil.registerStateRenderer(state, rendererId);
	}

	/**
	 * {@link SendDirtyEntityDataTransformer}<p/>
	 *
	 * Injection Point:<br/>
	 * {@link net.minecraft.server.level.ServerEntity#sendDirtyEntityData}
	 */
	public static void sendDirtyEntityData(Entity entity) {
		multipartEntityUtil.sendDirtyMultipartEntityData(entity);
	}

}
