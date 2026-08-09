package twilightforest.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import twilightforest.block.entity.ReactorDebrisBlockEntity;
import twilightforest.client.state.block.ReactorDebrisRenderState;

import java.util.HashMap;
import java.util.Map;

public class ReactorDebrisRenderer implements BlockEntityRenderer<ReactorDebrisBlockEntity, ReactorDebrisRenderState> {

	private static final Map<Identifier, Material.Baked> spriteCache = new HashMap<>();

	public ReactorDebrisRenderer(BlockEntityRendererProvider.Context context) {
	}

	@Override
	public void submit(ReactorDebrisRenderState state, PoseStack stack, SubmitNodeCollector collector, CameraRenderState camera) {
		float minX = state.minPos.x;
		float minY = state.minPos.y;
		float minZ = state.minPos.z;
		float maxX = state.maxPos.x;
		float maxY = state.maxPos.y;
		float maxZ = state.maxPos.z;

		RenderType type = RenderTypes.entityTranslucent(TextureAtlas.LOCATION_BLOCKS);

		maybeRenderDoubleSide(stack, type, collector, getSprite(state.textures[0]).sprite(), Direction.Axis.X, minX, minY, minZ, maxY, maxZ, -1, state.lightCoords);
		maybeRenderDoubleSide(stack, type, collector, getSprite(state.textures[1]).sprite(), Direction.Axis.X, maxX, minY, maxZ, maxY, minZ, 1, state.lightCoords);
		maybeRenderDoubleSide(stack, type, collector, getSprite(state.textures[2]).sprite(), Direction.Axis.Y, minY, minX, maxZ, maxX, minZ, -1, state.lightCoords);
		maybeRenderDoubleSide(stack, type, collector, getSprite(state.textures[3]).sprite(), Direction.Axis.Y, maxY, minX, minZ, maxX, maxZ, 1, state.lightCoords);
		maybeRenderDoubleSide(stack, type, collector, getSprite(state.textures[4]).sprite(), Direction.Axis.Z, minZ, minX, minY, maxX, maxY, -1, state.lightCoords);
		maybeRenderDoubleSide(stack, type, collector, getSprite(state.textures[5]).sprite(), Direction.Axis.Z, maxZ, maxX, minY, minX, maxY, 1, state.lightCoords);
	}

	private static void maybeRenderDoubleSide(PoseStack stack, RenderType type, SubmitNodeCollector collector, TextureAtlasSprite sprite, Direction.Axis axis, float c1, float min1, float min2, float max1, float max2, float n, int light) {
		renderSide(stack, type, collector, sprite, axis, c1, min1, min2, max1, max2, n, light);
		if (!sprite.transparency().isOpaque()) {
			renderSide(stack, type, collector, sprite, axis, c1, min1, min2, max1, max2, -n, light);
		}
	}

	private static void renderSide(PoseStack stack, RenderType type, SubmitNodeCollector collector, TextureAtlasSprite sprite, Direction.Axis axis, float c1, float min1, float min2, float max1, float max2, float n, int light) {
		collector.submitCustomGeometry(stack, type, (pose, consumer) -> {
			float u0 = Mth.lerp(min1, sprite.getU0(), sprite.getU1());
			float v0 = Mth.lerp(min2, sprite.getV0(), sprite.getV1());
			float u1 = Mth.lerp(max1, sprite.getU0(), sprite.getU1());
			float v1 = Mth.lerp(max2, sprite.getV0(), sprite.getV1());

			switch (axis) {
				case X -> {
					vertex(consumer, pose, c1, min1, min2, u0, v0, n, 0, 0, light);
					vertex(consumer, pose, c1, min1, max2, u0, v1, n, 0, 0, light);
					vertex(consumer, pose, c1, max1, max2, u1, v1, n, 0, 0, light);
					vertex(consumer, pose, c1, max1, min2, u1, v0, n, 0, 0, light);
				}
				case Y -> {
					vertex(consumer, pose, min1, c1, min2, u0, v0, 0, n, 0, light);
					vertex(consumer, pose, min1, c1, max2, u0, v1, 0, n, 0, light);
					vertex(consumer, pose, max1, c1, max2, u1, v1, 0, n, 0, light);
					vertex(consumer, pose, max1, c1, min2, u1, v0, 0, n, 0, light);
				}
				case Z -> {
					vertex(consumer, pose, min1, min2, c1, u0, v0, 0, 0, n, light);
					vertex(consumer, pose, min1, max2, c1, u0, v1, 0, 0, n, light);
					vertex(consumer, pose, max1, max2, c1, u1, v1, 0, 0, n, light);
					vertex(consumer, pose, max1, min2, c1, u1, v0, 0, 0, n, light);
				}
			}
		});
	}

	public static Material.Baked getSprite(Identifier location) {
		if (location == null)  // Handles cases with too many debris placed at once
			return getSprite(ReactorDebrisBlockEntity.DEFAULT_TEXTURE);
		return spriteCache.computeIfAbsent(location, loc ->
			new Material.Baked(Minecraft.getInstance().getAtlasManager().getAtlasOrThrow(TextureAtlas.LOCATION_BLOCKS).getSprite(loc), false)
		);
	}

	private static void vertex(VertexConsumer consumer, PoseStack.Pose pose, float x, float y, float z, float u, float v, float nx, float ny, float nz, int light) {
		consumer.addVertex(pose, x, y, z).setUv(u, v).setColor(-1).setLight(light).setOverlay(OverlayTexture.NO_OVERLAY).setNormal(nx, ny, nz);
	}

	@Override
	public ReactorDebrisRenderState createRenderState() {
		return new ReactorDebrisRenderState();
	}

	@Override
	public void extractRenderState(ReactorDebrisBlockEntity blockEntity, ReactorDebrisRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
		BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
		state.textures = blockEntity.textures;
		state.minPos = blockEntity.minPos;
		state.maxPos = blockEntity.maxPos;
	}
}