package twilightforest.client.model.block.giantblock;

import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.RenderTypeGroup;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import net.neoforged.neoforge.client.model.geometry.IUnbakedGeometry;

import java.util.ArrayList;
import java.util.Locale;
import java.util.function.Function;

public record UnbakedGiantBlockModel(ResourceLocation parent) implements IUnbakedGeometry<UnbakedGiantBlockModel> {

	@Override
	public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides, ResourceLocation modelLocation) {
		TextureAtlasSprite[] sprites;
		if (context.hasMaterial("all")) {
			sprites = new TextureAtlasSprite[]{spriteGetter.apply(context.getMaterial("all"))};
		} else {
			ArrayList<TextureAtlasSprite> materials = new ArrayList<>();
			for (Direction dir : Direction.values()) {
				materials.add(spriteGetter.apply(context.getMaterial(dir.getName().toLowerCase(Locale.ROOT))));
			}
			sprites = materials.toArray(new TextureAtlasSprite[]{});
		}

		ResourceLocation renderTypeHint = context.getRenderTypeHint();
		RenderTypeGroup renderTypes = renderTypeHint != null ? context.getRenderType(renderTypeHint) : RenderTypeGroup.EMPTY;
		return new GiantBlockModel(sprites, spriteGetter.apply(context.getMaterial("particle")), overrides, context.getTransforms(), renderTypes);
	}
}
