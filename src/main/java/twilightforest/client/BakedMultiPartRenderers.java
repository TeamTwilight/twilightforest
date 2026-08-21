package twilightforest.client;

import com.google.common.base.Suppliers;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.resources.Identifier;
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
import java.util.function.Supplier;

@SuppressWarnings("deprecation")
public class BakedMultiPartRenderers {
	private static final Map<Identifier, Supplier<EntityRenderer<?,?>>> renderers = new HashMap<>();

	public static void bakeMultiPartRenderers(EntityRendererProvider.Context context) {
		renderers.put(TFPart.RENDERER, Suppliers.memoize(() -> new NoopRenderer<>(context)));
		renderers.put(HydraHead.RENDERER, Suppliers.memoize(() -> new HydraHeadRenderer(context, new HydraHeadModel(context.bakeLayer(TFModelLayers.HYDRA_HEAD)))));
		renderers.put(HydraNeck.RENDERER, Suppliers.memoize(() -> new HydraNeckRenderer(context, new HydraNeckModel(context.bakeLayer(TFModelLayers.HYDRA_NECK)))));
		renderers.put(SnowQueenIceShield.RENDERER, Suppliers.memoize(() -> new SnowQueenIceShieldRenderer(context)));
		renderers.put(NagaSegment.RENDERER, Suppliers.memoize(() -> new NagaSegmentRenderer(context, new NagaModel<>(context.bakeLayer(TFModelLayers.NAGA_BODY)))));
	}

	public static EntityRenderer<?,?> lookup(Identifier location) {
		return renderers.get(location).get();
	}
}
