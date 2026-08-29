package twilightforest.client.renderer.block.jar;

import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.client.model.quad.MutableQuad;
import org.jspecify.annotations.Nullable;

import java.util.List;

public final class TexturedJarLidPart implements BlockStateModelPart {
	private final BlockStateModelPart base;
	private final TextureAtlasSprite endSprite;
	private final TextureAtlasSprite sideSprite;

	public TexturedJarLidPart(BlockStateModelPart base, TextureAtlasSprite endSprite, TextureAtlasSprite sideSprite) {
		this.base = base;
		this.endSprite = endSprite;
		this.sideSprite = sideSprite;
	}

	@Override
	public List<BakedQuad> getQuads(@Nullable Direction direction) {
		return base.getQuads(direction).stream()
			.map(quad -> {
				TextureAtlasSprite sprite =
					quad.direction() == Direction.UP ||
						quad.direction() == Direction.DOWN
						? endSprite
						: sideSprite;
				return replaceSprite(quad, sprite);
			}).toList();
	}

	@Override
	public Material.Baked particleMaterial() {
		return base.particleMaterial();
	}

	@Override
	public int materialFlags() {
		return base.materialFlags();
	}

	@Override
	public boolean useAmbientOcclusion() {
		return base.useAmbientOcclusion();
	}

	private static BakedQuad replaceSprite(BakedQuad quad, TextureAtlasSprite sprite) {
		MutableQuad mutableQuad = new MutableQuad();
		mutableQuad.setFrom(quad);
		BakedQuad.MaterialInfo old = quad.materialInfo();
		mutableQuad.setSpriteAndMoveUv(
			sprite,
			old.layer(),
			old.itemRenderType()
		);
		return mutableQuad.toBakedQuad();
	}
}