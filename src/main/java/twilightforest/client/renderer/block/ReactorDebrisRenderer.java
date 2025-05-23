package twilightforest.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import org.joml.Matrix4f;
import twilightforest.block.entity.ReactorDebrisBlockEntity;

import java.util.HashMap;
import java.util.Map;

public class ReactorDebrisRenderer implements BlockEntityRenderer<ReactorDebrisBlockEntity> {

	public ReactorDebrisRenderer(BlockEntityRendererProvider.Context context) {}

	private static final Map<ResourceLocation, TextureAtlasSprite> spriteCache = new HashMap<>();

	private enum Axis {
		X, Y, Z
	}

	private record QuadRenderInfo(VertexConsumer builder, Matrix4f matrix, int light, int overlay) {  // Reduce copy-paste, thanks to TFC for idea
		private void vertex(float x, float y, float z, float u, float v, float nx, float ny, float nz)
		{
			this.builder.addVertex(this.matrix, x, y, z)
				.setUv(u, v).setLight(this.light)
				.setOverlay(this.overlay)
				.setNormal(nx, ny, nz)
				.setColor(1f, 1f, 1f, 1f);
		}
	}

	@Override
	public void render(ReactorDebrisBlockEntity entity, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
		if (entity.getLevel() == null) return;

		poseStack.pushPose();
		VertexConsumer builder = buffer.getBuffer(RenderType.itemEntityTranslucentCull(TextureAtlas.LOCATION_BLOCKS));
		Matrix4f matrix = poseStack.last().pose();
		QuadRenderInfo info = new QuadRenderInfo(builder, matrix, light, overlay);

		renderBlock(info, entity);
		poseStack.popPose();
	}

	private void renderBlock(QuadRenderInfo info, ReactorDebrisBlockEntity entity) {
		float minX = entity.minPos.x;
		float minY = entity.minPos.y;
		float minZ = entity.minPos.z;
		float maxX = entity.maxPos.x;
		float maxY = entity.maxPos.y;
		float maxZ = entity.maxPos.z;

		renderSide(info, getSprite(entity.textures[0]), Axis.X, minX, minY, minZ, maxY, maxZ, -1);
		renderSide(info, getSprite(entity.textures[1]), Axis.X, maxX, minY, maxZ, maxY, minZ, 1);
		renderSide(info, getSprite(entity.textures[2]), Axis.Y, minY, minX, maxZ, maxX, minZ, -1);
		renderSide(info, getSprite(entity.textures[3]), Axis.Y, maxY, minX, minZ, maxX, maxZ, 1);
		renderSide(info, getSprite(entity.textures[4]), Axis.Z, minZ, minX, minY, maxX, maxY, -1);
		renderSide(info, getSprite(entity.textures[5]), Axis.Z, maxZ, maxX, minY, minX, maxY, 1);

		// Duplication for inner side because portal is transparent
		renderSide(info, getSprite(entity.textures[0]), Axis.X, minX, minY, maxZ, maxY, minZ, 1);
		renderSide(info, getSprite(entity.textures[1]), Axis.X, maxX, minY, minZ, maxY, maxZ, -1);
		renderSide(info, getSprite(entity.textures[2]), Axis.Y, minY, minX, minZ, maxX, maxZ, 1);
		renderSide(info, getSprite(entity.textures[3]), Axis.Y, maxY, minX, maxZ, maxX, minZ, -1);
		renderSide(info, getSprite(entity.textures[4]), Axis.Z, minZ, maxX, minY, minX, maxY, 1);
		renderSide(info, getSprite(entity.textures[5]), Axis.Z, maxZ, minX, minY, maxX, maxY, -1);
	}

	private void renderSide(QuadRenderInfo info, TextureAtlasSprite sprite, Axis axis, float c1, float min1, float min2, float max1, float max2, float n) {
		float u0 = Mth.lerp(min1, sprite.getU0(), sprite.getU1());
		float v0 = Mth.lerp(min2, sprite.getV0(), sprite.getV1());
		float u1 = Mth.lerp(max1, sprite.getU0(), sprite.getU1());
		float v1 = Mth.lerp(max2, sprite.getV0(), sprite.getV1());

		switch (axis) {
			case X -> {
				info.vertex(c1, min1, min2, u0, v0, n, 0, 0);
				info.vertex(c1, min1, max2, u0, v1, n, 0, 0);
				info.vertex(c1, max1, max2, u1, v1, n, 0, 0);
				info.vertex(c1, max1, min2, u1, v0, n, 0, 0);
			}
			case Y -> {
				info.vertex(min1, c1, min2, u0, v0, 0, n, 0);
				info.vertex(min1, c1, max2, u0, v1, 0, n, 0);
				info.vertex(max1, c1, max2, u1, v1, 0, n, 0);
				info.vertex(max1, c1, min2, u1, v0, 0, n, 0);
			}
			case Z -> {
				info.vertex(min1, min2, c1, u0, v0, 0, 0, n);
				info.vertex(min1, max2, c1, u0, v1, 0, 0, n);
				info.vertex(max1, max2, c1, u1, v1, 0, 0, n);
				info.vertex(max1, min2, c1, u1, v0, 0, 0, n);
			}
		}
	}

	public static TextureAtlasSprite getSprite(ResourceLocation location) {
		if (location == null)  // Handles cases with too many debris placed at once
			return getSprite(ReactorDebrisBlockEntity.DEFAULT_TEXTURE);
		return spriteCache.computeIfAbsent(location, loc ->
			Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(loc)
		);
	}
}