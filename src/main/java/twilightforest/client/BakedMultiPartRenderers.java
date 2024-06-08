package twilightforest.client;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.LazyLoadedValue;
import twilightforest.client.event.JappaPackReloadListener;
import twilightforest.client.renderer.entity.*;
import twilightforest.client.renderer.entity.newmodels.NewHydraHeadRenderer;
import twilightforest.client.renderer.entity.newmodels.NewHydraNeckRenderer;
import twilightforest.client.renderer.entity.newmodels.NewNagaSegmentRenderer;
import twilightforest.entity.TFPart;
import twilightforest.entity.boss.HydraHead;
import twilightforest.entity.boss.HydraNeck;
import twilightforest.entity.boss.NagaSegment;
import twilightforest.entity.boss.SnowQueenIceShield;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BooleanSupplier;

@SuppressWarnings("deprecation")
public class BakedMultiPartRenderers {
	private static final Map<ResourceLocation, LazyLoadedValue<EntityRenderer<?>>> renderers = new HashMap<>();

	public static void bakeMultiPartRenderers(EntityRendererProvider.Context context) {
		BooleanSupplier jappa = JappaPackReloadListener.INSTANCE.uncachedJappaPackCheck();
		renderers.put(TFPart.RENDERER, new LazyLoadedValue<>(() -> new NoopRenderer<>(context)));
		renderers.put(HydraHead.RENDERER, new LazyLoadedValue<>(() -> !jappa.getAsBoolean() ? new HydraHeadRenderer(context) : new NewHydraHeadRenderer(context)));
		renderers.put(HydraNeck.RENDERER, new LazyLoadedValue<>(() -> !jappa.getAsBoolean() ? new HydraNeckRenderer(context) : new NewHydraNeckRenderer(context)));
		renderers.put(SnowQueenIceShield.RENDERER, new LazyLoadedValue<>(() -> new SnowQueenIceShieldLayer<>(context)));
		renderers.put(NagaSegment.RENDERER, new LazyLoadedValue<>(() -> !jappa.getAsBoolean() ? new NagaSegmentRenderer<>(context) : new NewNagaSegmentRenderer<>(context)));
	}

	public static EntityRenderer<?> lookup(ResourceLocation location) {
		return renderers.get(location).get();
	}
}
