package twilightforest.util;

import net.minecraft.client.renderer.block.dispatch.ModelState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.cuboid.CuboidFace;
import net.minecraft.client.resources.model.cuboid.CuboidModelElement;
import net.minecraft.client.resources.model.cuboid.FaceBakery;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Direction;
import twilightforest.client.model.block.connected.ConnectionLogic;

// this class returns back removed methods from the sources
public class UnbakedGeometryUtil {
	public static CuboidFace.UVs uvsByFace(Direction face, CuboidModelElement element) {
		return switch (face) {
			case DOWN -> new CuboidFace.UVs(element.from().x(), 16.0F - element.to().z(), element.to().x(), 16.0F - element.from().z());
			case UP -> new CuboidFace.UVs(element.from().x(), element.from().z(), element.to().x(), element.to().z());
			case SOUTH -> new CuboidFace.UVs(element.from().x(), 16.0F - element.to().y(), element.to().x(), 16.0F - element.from().y());
			case WEST -> new CuboidFace.UVs(element.from().z(), 16.0F - element.to().y(), element.to().z(), 16.0F - element.from().y());
			case EAST -> new CuboidFace.UVs(16.0F - element.to().z(), 16.0F - element.to().y(), 16.0F - element.from().z(), 16.0F - element.from().y());
			default -> new CuboidFace.UVs(16.0F - element.to().x(), 16.0F - element.to().y(), 16.0F - element.from().x(), 16.0F - element.from().y());
		};
	}

	public static BakedQuad bakeElementFace(ModelBaker baker, CuboidModelElement element, CuboidFace face, Material.Baked sprite, Direction direction, ModelState state) {
		return FaceBakery.bakeQuad(baker, element.from(), element.to(), face, sprite, direction, state, null, element.shade(), 0);
	}

	public static Material.Baked chooseAndBake(ConnectionLogic target, TextureAtlasSprite[] spriteOptions, Material[] materials) {
		TextureAtlasSprite unbakedChoice = target.chooseTexture(spriteOptions);
		// spriteOptions.length should be equal to materials.length
		for (int i = 0; i < spriteOptions.length; i++) {
			if (unbakedChoice == spriteOptions[i]) {
				return new Material.Baked(spriteOptions[i], materials[i].forceTranslucent());
			}
		}
		return new Material.Baked(spriteOptions[0], false);
	}
}
