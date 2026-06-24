package twilightforest.datagen.assets.models;

import com.mojang.math.Quadrant;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.model.generators.template.ExtendedModelTemplate;
import net.neoforged.neoforge.client.model.generators.template.ExtendedModelTemplateBuilder;

/**
 * Class for using ExtenderModelTemplate builders to generate block models from scratch.
 */
public class TFExtendedModelTemplates extends TFModelTemplates {
	public static final ExtendedModelTemplate MASON_JAR = ExtendedModelTemplateBuilder.builder()
		.parent(Identifier.withDefaultNamespace("block/block"))
		.requiredTextureSlot(TextureSlot.PARTICLE)
		.requiredTextureSlot(TextureSlot.SIDE)
		.requiredTextureSlot(TextureSlot.BOTTOM)
		.requiredTextureSlot(TextureSlot.TOP)
		.element(elementBuilder ->
			elementBuilder.from(3.0F, 0.0F, 3.0F).to(13.0F, 14.0F, 13.0F)
				.face(Direction.UP, faceBuilder -> faceBuilder.texture(TextureSlot.TOP))
				.face(Direction.DOWN, faceBuilder -> faceBuilder.texture(TextureSlot.BOTTOM).cullface(Direction.DOWN))
				.face(Direction.NORTH, faceBuilder -> faceBuilder.texture(TextureSlot.SIDE))
				.face(Direction.SOUTH, faceBuilder -> faceBuilder.texture(TextureSlot.SIDE))
				.face(Direction.WEST, faceBuilder -> faceBuilder.texture(TextureSlot.SIDE))
				.face(Direction.EAST, faceBuilder -> faceBuilder.texture(TextureSlot.SIDE))
		).build();

	public static final ExtendedModelTemplate FIREFLY_PARTICLE_SPAWNER = ExtendedModelTemplateBuilder.builder()
		.parent(Identifier.withDefaultNamespace("block/block"))
		.requiredTextureSlot(TextureSlot.PARTICLE)
		.requiredTextureSlot(TextureSlot.SIDE)
		.requiredTextureSlot(TextureSlot.BOTTOM)
		.requiredTextureSlot(TextureSlot.TOP)
		.requiredTextureSlot(TFTextureSlot.SOIL)
		.requiredTextureSlot(TFTextureSlot.PLANT)
		.element(elementBuilder ->
			elementBuilder.from(3.0F, 0.0F, 3.0F).to(13.0F, 14.0F, 13.0F)
				.face(Direction.UP, faceBuilder -> faceBuilder.texture(TextureSlot.TOP).uvs(3, 3, 13, 13))
				.face(Direction.DOWN, faceBuilder -> faceBuilder.texture(TextureSlot.BOTTOM).uvs(3, 3, 13, 13).cullface(Direction.DOWN))
				.face(Direction.NORTH, faceBuilder -> faceBuilder.texture(TextureSlot.SIDE).uvs(3, 2, 13, 16))
				.face(Direction.SOUTH, faceBuilder -> faceBuilder.texture(TextureSlot.SIDE).uvs(3, 2, 13, 16))
				.face(Direction.WEST, faceBuilder -> faceBuilder.texture(TextureSlot.SIDE).uvs(3, 2, 13, 16))
				.face(Direction.EAST, faceBuilder -> faceBuilder.texture(TextureSlot.SIDE).uvs(3, 2, 13, 16))
		)
		.element(elementBuilder ->
			elementBuilder.from(4.0F, 0.01F, 4.0F).to(12.0F, 2.0F, 12.0F)
				.face(Direction.UP, faceBuilder -> faceBuilder.texture(TFTextureSlot.SOIL).uvs(4, 5, 12, 13))
				.face(Direction.DOWN, faceBuilder -> faceBuilder.texture(TFTextureSlot.SOIL).uvs(4, 5, 12, 13).cullface(Direction.DOWN))
				.face(Direction.NORTH, faceBuilder -> faceBuilder.texture(TFTextureSlot.SOIL).uvs(4, 12, 12, 14))
				.face(Direction.SOUTH, faceBuilder -> faceBuilder.texture(TFTextureSlot.SOIL).uvs(4, 3, 12, 5).rotation(Quadrant.R180))
				.face(Direction.WEST, faceBuilder -> faceBuilder.texture(TFTextureSlot.SOIL).uvs(3, 5, 5, 13).rotation(Quadrant.R270))
				.face(Direction.EAST, faceBuilder -> faceBuilder.texture(TFTextureSlot.SOIL).uvs(11, 5, 13, 13).rotation(Quadrant.R90))
		)
		.element(elementBuilder ->
			elementBuilder.from(1.0F, 2.0F, 6.0F).to(17.0F, 18.0F, 6.0F)
				.rotation(rotationBuilder -> rotationBuilder.singleAxis(Direction.Axis.Y, 45.0F).origin(8.0F, 1.0F, 8.0F))
				.face(Direction.NORTH, faceBuilder -> faceBuilder.texture(TFTextureSlot.PLANT).uvs(0, 0, 16, 16))
				.face(Direction.SOUTH, faceBuilder -> faceBuilder.texture(TFTextureSlot.PLANT).uvs(0, 0, 16, 16))
		)
		.element(elementBuilder ->
			elementBuilder.from(9.0F, 2.0F, -2.0F).to(9.0F, 18.0F, 14.0F)
				.rotation(rotationBuilder -> rotationBuilder.singleAxis(Direction.Axis.Y, 45.0F).origin(8.0F, 1.0F, 8.0F))
				.face(Direction.EAST, faceBuilder -> faceBuilder.texture(TFTextureSlot.PLANT).uvs(0, 0, 16, 16))
				.face(Direction.WEST, faceBuilder -> faceBuilder.texture(TFTextureSlot.PLANT).uvs(0, 0, 16, 16))
		)
		.element(elementBuilder ->
			elementBuilder.from(4.0F, 13.0F, 3.0F).to(12.0F, 14.0F, 4.0F)
				.face(Direction.SOUTH, faceBuilder -> faceBuilder.texture(TextureSlot.TOP).uvs(4, 3, 12, 4))
		)
		.element(elementBuilder ->
			elementBuilder.from(4.0F, 13.0F, 12.0F).to(12.0F, 14.0F, 13.0F)
				.face(Direction.NORTH, faceBuilder -> faceBuilder.texture(TextureSlot.TOP).uvs(4, 12, 12, 13).rotation(Quadrant.R180))
		)
		.element(elementBuilder ->
			elementBuilder.from(12.0F, 13.0F, 4.0F).to(13.0F, 14.0F, 12.0F)
				.face(Direction.WEST, faceBuilder -> faceBuilder.texture(TextureSlot.TOP).uvs(12, 4, 13, 12).rotation(Quadrant.R270))
		)
		.element(elementBuilder ->
			elementBuilder.from(3.0F, 13.0F, 4.0F).to(4.0F, 14.0F, 12.0F)
				.face(Direction.EAST, faceBuilder -> faceBuilder.texture(TextureSlot.TOP).uvs(3, 4, 4, 12).rotation(Quadrant.R90))
		).build();
}
