package twilightforest.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.debug.DebugScreenEntries;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.gizmos.GizmoStyle;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.util.ARGB;
import net.minecraft.util.context.ContextKey;
import net.minecraft.util.debug.DebugValueAccess;
import net.minecraft.world.TickRateManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.ExtractLevelRenderStateEvent;
import net.neoforged.neoforge.client.event.RegisterDebugRenderersEvent;
import net.neoforged.neoforge.client.event.SubmitCustomGeometryEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.Nullable;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import twilightforest.TwilightForestMod;
import twilightforest.entity.TFPart;

import java.util.ArrayList;
import java.util.List;

@Component(dist = Dist.CLIENT)
public class MultipartRenderDispatcher implements DebugRenderer.SimpleDebugRenderer {

	private final ContextKey<List<PartRender>> partRenders = new ContextKey<>(TwilightForestMod.prefix("multipart_renders"));
	private final int hitboxColor = ARGB.colorFromFloat(1.0F, 0.25F, 1.0F, 0.0F);

	@PostConstruct
	private void setup(IEventBus modBus, IEventBus gameBus) {
		modBus.addListener(this::registerDebugRenderers);

		gameBus.addListener(this::extractPartRenderStates);
		gameBus.addListener(this::submitPartRenderStates);
	}

	private void registerDebugRenderers(RegisterDebugRenderersEvent event) {
		event.register(this);
	}

	private void extractPartRenderStates(ExtractLevelRenderStateEvent event) {
		ClientLevel level = event.getLevel();
		LevelRenderState renderState = event.getRenderState();
		DeltaTracker deltaTracker = event.getDeltaTracker();
		TickRateManager tickRateManager = level.tickRateManager();
		Frustum frustum = event.getFrustum();
		Vec3 cameraPos = event.getCamera().position();
		boolean showOutlines = event.getLevelRenderer().shouldShowEntityOutlines();
		List<PartRender> renders = new ArrayList<>();

		TFPart.forEachPart(level.entitiesForRendering(), part -> {
			EntityRenderer<Entity, EntityRenderState> renderer = BakedMultiPartRenderers.lookup(part.renderer());
			if (!renderer.shouldRender(part, frustum, cameraPos.x(), cameraPos.y(), cameraPos.z()))
				return;

			EntityRenderState state = renderer.createRenderState(part, deltaTracker.getGameTimeDeltaPartialTick(!tickRateManager.isEntityFrozen(part)));
			renders.add(new PartRender(renderer, state));
			if (showOutlines && state.appearsGlowing())
				renderState.haveGlowingEntities = true;
		});

		renderState.setRenderData(this.partRenders, renders);
	}

	private void submitPartRenderStates(SubmitCustomGeometryEvent event) {
		LevelRenderState renderState = event.getLevelRenderState();
		@Nullable List<PartRender> renders = renderState.getRenderData(this.partRenders);
		if (renders == null || renders.isEmpty())
			return;

		CameraRenderState cameraRenderState = renderState.cameraRenderState;
		Vec3 cameraPos = cameraRenderState.pos;
		PoseStack stack = event.getPoseStack();

		for (PartRender render : renders) {
			EntityRenderState state = render.state();
			if (!renderState.haveGlowingEntities)
				state.outlineColor = 0;

			Vec3 offset = render.renderer().getRenderOffset(state);
			stack.pushPose();
			stack.translate(state.x - cameraPos.x() + offset.x(), state.y - cameraPos.y() + offset.y(), state.z - cameraPos.z() + offset.z());
			render.renderer().submit(state, stack, event.getSubmitNodeCollector(), cameraRenderState);
			stack.popPose();
		}
	}

	@Override
	public void emitGizmos(double camX, double camY, double camZ, DebugValueAccess debugValues, Frustum frustum, float partialTicks) {
		Minecraft minecraft = Minecraft.getInstance();
		if (minecraft.level == null || !minecraft.debugEntries.isCurrentlyEnabled(DebugScreenEntries.ENTITY_HITBOXES))
			return;

		TFPart.forEachPart(minecraft.level.entitiesForRendering(), part -> {
			if (part.isInvisible())
				return;

			AABB box = part.getBoundingBox();
			if (!frustum.isVisible(box))
				return;

			Gizmos.cuboid(box.move(part.getPosition(partialTicks).subtract(part.position())), GizmoStyle.stroke(this.hitboxColor));
		});
	}

	private record PartRender(EntityRenderer<Entity, EntityRenderState> renderer, EntityRenderState state) {
	}
}
