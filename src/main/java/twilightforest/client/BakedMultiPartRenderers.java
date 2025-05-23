package twilightforest.client;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.LazyLoadedValue;
import twilightforest.client.model.TFModelLayers;
import twilightforest.client.model.entity.HydraHeadModel;
import twilightforest.client.model.entity.HydraNeckModel;
import twilightforest.client.model.entity.NagaModel;
import twilightforest.client.renderer.entity.*;
import twilightforest.entity.TFPart;
import twilightforest.entity.boss.HydraHead;
import twilightforest.entity.boss.HydraNeck;
import twilightforest.entity.boss.NagaSegment;
import twilightforest.entity.boss.SnowQueenIceShield;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("deprecation")
public class BakedMultiPartRenderers {
	private static final Map<ResourceLocation, LazyLoadedValue<EntityRenderer<?, ?>>> renderers = new HashMap<>();

	public static void bakeMultiPartRenderers(EntityRendererProvider.Context context) {
		renderers.put(TFPart.RENDERER, new LazyLoadedValue<>(() -> new NoopRenderer<>(context)));
		renderers.put(HydraHead.RENDERER, new LazyLoadedValue<>(() -> new HydraHeadRenderer(context, new HydraHeadModel(context.bakeLayer(TFModelLayers.HYDRA_HEAD)))));
		renderers.put(HydraNeck.RENDERER, new LazyLoadedValue<>(() -> new HydraNeckRenderer(context, new HydraNeckModel(context.bakeLayer(TFModelLayers.HYDRA_NECK)))));
		renderers.put(SnowQueenIceShield.RENDERER, new LazyLoadedValue<>(() -> new SnowQueenIceShieldRenderer(context)));
		renderers.put(NagaSegment.RENDERER, new LazyLoadedValue<>(() -> new NagaSegmentRenderer(context, new NagaModel<>(context.bakeLayer(TFModelLayers.NAGA_BODY)))));
	}

	public static EntityRenderer<?, ?> lookup(ResourceLocation location) {
		return renderers.get(location).get();
	}
}
