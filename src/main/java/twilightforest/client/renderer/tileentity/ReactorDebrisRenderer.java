package twilightforest.client.renderer.tileentity;

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
import org.joml.Matrix4f;
import twilightforest.block.entity.ReactorDebrisBlockEntity;

public class ReactorDebrisRenderer implements BlockEntityRenderer<ReactorDebrisBlockEntity> {
	public ReactorDebrisRenderer(BlockEntityRendererProvider.Context context) {}

	private record QuadRenderInfo(VertexConsumer builder, Matrix4f matrix4f, int packedLight, int packedOverlay) {}  // Reduce copy-paste, thanks to TFC for idea

	private static void vertex(QuadRenderInfo info, float x, float y, float z, float u, float v, float nx, float ny, float nz)
	{
		info.builder.addVertex(info.matrix4f, x, y, z)
			.setUv(u, v).setLight(info.packedLight)
			.setOverlay(info.packedOverlay)
			.setNormal(nx, ny, nz)
			.setColor(1f, 1f, 1f, 1f);
	}

	@Override
	public void render(ReactorDebrisBlockEntity reactorDebrisBlockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay)
	{
		if (reactorDebrisBlockEntity.getLevel() == null) return;

		poseStack.pushPose();

		VertexConsumer builder = buffer.getBuffer(RenderType.entityTranslucentCull(TextureAtlas.LOCATION_BLOCKS));
		Matrix4f matrix4f = poseStack.last().pose();

		final QuadRenderInfo info = new QuadRenderInfo(builder, matrix4f, packedLight, packedOverlay);

		renderBlock(info, reactorDebrisBlockEntity);


		poseStack.popPose();
	}

	private void renderBlock(QuadRenderInfo info, ReactorDebrisBlockEntity reactorDebrisBlockEntity) {
		float minX = (float) reactorDebrisBlockEntity.minPos.x;
		float minY = (float) reactorDebrisBlockEntity.minPos.y;
		float minZ = (float) reactorDebrisBlockEntity.minPos.z;
		float maxX = (float) reactorDebrisBlockEntity.maxPos.x;
		float maxY = (float) reactorDebrisBlockEntity.maxPos.y;
		float maxZ = (float) reactorDebrisBlockEntity.maxPos.z;

		renderXSides(info, getSprite(reactorDebrisBlockEntity.textures[0]), minX, minY, minZ, maxY, maxZ, -1);
		renderXSides(info, getSprite(reactorDebrisBlockEntity.textures[1]), maxX, minY, maxZ, maxY, minZ, 1);
		renderYSides(info, getSprite(reactorDebrisBlockEntity.textures[2]), minY, minX, maxZ, maxX, minZ, -1);
		renderYSides(info, getSprite(reactorDebrisBlockEntity.textures[3]), maxY, minX, minZ, maxX, maxZ, 1);
		renderZSides(info, getSprite(reactorDebrisBlockEntity.textures[4]), minZ, minX, minY, maxX, maxY, -1);
		renderZSides(info, getSprite(reactorDebrisBlockEntity.textures[5]), maxZ, maxX, minY, minX, maxY, 1);

		// Duplication for inner side because portal is transparent
		renderXSides(info, getSprite(reactorDebrisBlockEntity.textures[0]), minX, minY, maxZ, maxY, minZ, 1);
		renderXSides(info, getSprite(reactorDebrisBlockEntity.textures[1]), maxX, minY, minZ, maxY, maxZ, -1);
		renderYSides(info, getSprite(reactorDebrisBlockEntity.textures[2]), minY, minX, minZ, maxX, maxZ, 1);
		renderYSides(info, getSprite(reactorDebrisBlockEntity.textures[3]), maxY, minX, maxZ, maxX, minZ, -1);
		renderZSides(info, getSprite(reactorDebrisBlockEntity.textures[4]), minZ, maxX, minY, minX, maxY, 1);
		renderZSides(info, getSprite(reactorDebrisBlockEntity.textures[5]), maxZ, minX, minY, maxX, maxY, -1);
	}

	private void renderXSides(QuadRenderInfo info, TextureAtlasSprite sprite, float x, float minY, float minZ, float maxY, float maxZ, float n) {
		float u0 = Mth.lerp(minY, sprite.getU0(), sprite.getU1());
		float v0 = Mth.lerp(minZ, sprite.getV0(), sprite.getV1());
		float u1 = Mth.lerp(maxY, sprite.getU0(), sprite.getU1());
		float v1 = Mth.lerp(maxZ, sprite.getV0(), sprite.getV1());

		vertex(info, x, minY, minZ, u0, v0, n, 0, 0);
		vertex(info, x, minY, maxZ, u0, v1, n, 0, 0);
		vertex(info, x, maxY, maxZ, u1, v1, n, 0, 0);
		vertex(info, x, maxY, minZ, u1, v0, n, 0, 0);
	}

	private void renderYSides(QuadRenderInfo info, TextureAtlasSprite sprite, float y, float minX, float minZ, float maxX, float maxZ, float n) {
		float u0 = Mth.lerp(minX, sprite.getU0(), sprite.getU1());
		float v0 = Mth.lerp(minZ, sprite.getV0(), sprite.getV1());
		float u1 = Mth.lerp(maxX, sprite.getU0(), sprite.getU1());
		float v1 = Mth.lerp(maxZ, sprite.getV0(), sprite.getV1());

		vertex(info, minX, y, minZ, u0, v0, 0, n, 0);
		vertex(info, minX, y, maxZ, u0, v1, 0, n, 0);
		vertex(info, maxX, y, maxZ, u1, v1, 0, n, 0);
		vertex(info, maxX, y, minZ, u1, v0, 0, n, 0);
	}

	private void renderZSides(QuadRenderInfo info, TextureAtlasSprite sprite, float z, float minX, float minY, float maxX, float maxY, float n) {
		float u0 = Mth.lerp(minX, sprite.getU0(), sprite.getU1());
		float v0 = Mth.lerp(minY, sprite.getV0(), sprite.getV1());
		float u1 = Mth.lerp(maxX, sprite.getU0(), sprite.getU1());
		float v1 = Mth.lerp(maxY, sprite.getV0(), sprite.getV1());

		vertex(info, minX, minY, z, u0, v0, 0, 0, n);
		vertex(info, minX, maxY, z, u0, v1, 0, 0, n);
		vertex(info, maxX, maxY, z, u1, v1, 0, 0, n);
		vertex(info, maxX, minY, z, u1, v0, 0, 0, n);
	}

	private TextureAtlasSprite getSprite(ResourceLocation location) {
		return Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(location);
	}
}