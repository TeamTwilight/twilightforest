package twilightforest.client.model.block.giantblock;

import com.mojang.math.Quadrant;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.renderer.block.dispatch.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.SimpleModelWrapper;
import net.minecraft.client.resources.model.cuboid.CuboidFace;
import net.minecraft.client.resources.model.cuboid.CuboidRotation;
import net.minecraft.client.resources.model.cuboid.FaceBakery;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.geometry.QuadCollection;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.neoforged.neoforge.client.model.block.CustomUnbakedBlockStateModel;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import twilightforest.block.GiantBlock;
import twilightforest.util.Vec2i;

import java.util.ArrayList;

public record UnbakedGiantBlockStateModel(BlockStateModel.Unbaked sourceModel) implements CustomUnbakedBlockStateModel {

	public static final MapCodec<UnbakedGiantBlockStateModel> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		BlockStateModel.Unbaked.CODEC.fieldOf("source_model").forGetter(UnbakedGiantBlockStateModel::sourceModel)
	).apply(instance, UnbakedGiantBlockStateModel::new));

	@Override
	public BlockStateModel bake(ModelBaker modelBaker) {
		return new GiantBlockModel(this.subdivideBakedSource(modelBaker, this.sourceModel.bake(modelBaker)));
	}

	private BlockStateModel @NotNull [] subdivideBakedSource(ModelBaker modelBaker, BlockStateModel bakedSourceModel) {
		BlockStateModel[] giantSubBlocks = new BlockStateModel[4 * 4 * 4];

		ArrayList<BlockStateModelPart> list = new ArrayList<>();
		// noinspection deprecation
		bakedSourceModel.collectParts(RandomSource.create(0L), list);
		BlockStateModelPart firstModelPart = list.getFirst(); // TODO support actual slicing of BakedQuad collection into 4x4x4, given customized source block models. But testing is not possible at this moment as this was written mid-port

		// FIXME Do loop levels need re-ordering?
		for (int index = 0; index < giantSubBlocks.length; index++) {
			QuadCollection.Builder quadBuilder = new QuadCollection.Builder();

			BlockPos indexedPos = GiantBlock.unpackCoords(index);

			for (Direction side : Direction.values()) {
				Vec2i coords = this.calculateOffset(side, indexedPos.offset(this.magicOffsetFromDir(side)));

				for (BakedQuad quad : firstModelPart.getQuads(side)) {
					TextureAtlasSprite sprite = quad.materialInfo().sprite();

					CuboidFace cuboidFace = new CuboidFace(side, quad.materialInfo().tintIndex(), sprite.contents().name().toString(), new CuboidFace.UVs(0.0F + coords.x, 0.0F + coords.z, 4.0F + coords.x, 4.0F + coords.z), Quadrant.R0);

					BakedQuad bakedQuad = FaceBakery.bakeQuad(
						modelBaker,
						new Vector3f(0.0F, 0.0F, 0.0F),
						new Vector3f(16.0F, 16.0F, 16.0F),
						cuboidFace,
						new Material.Baked(sprite, false),
						side,
						BlockModelRotation.IDENTITY,
						new CuboidRotation(new Vector3f(), Matrix4f::new, false),
						quad.materialInfo().shade(),
						quad.materialInfo().lightEmission()
					);

					quadBuilder.addCulledFace(side, bakedQuad);
				}
			}

			giantSubBlocks[index] = new SingleVariant(new SimpleModelWrapper(quadBuilder.build(), firstModelPart.useAmbientOcclusion(), firstModelPart.particleMaterial()));
		}


		return giantSubBlocks;
	}

	//based on the offsets written in the original giant block json
	private BlockPos magicOffsetFromDir(Direction side) {
		return switch (side) {
			case DOWN -> new BlockPos(0, 0, 2);
			case NORTH, SOUTH -> new BlockPos(0, 1, 0);
			case WEST, EAST -> new BlockPos(0, 1, -1);
			default -> new BlockPos(0, 0, -1);
		};
	}

	//an offset is calculated between 0 and 15 based on which side its on.
	//it then grabs the remainder after dividing by 4 and then multiply by 4 to scale correctly
	private Vec2i calculateOffset(Direction side, BlockPos pos) {
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();

		int offsetX, offsetY;

		if (side.getAxis().isVertical()) {
			offsetX = x % 4;
			offsetY = (side.getStepY() * z + 1) % 4;
		} else if (side.getAxis() == Direction.Axis.Z) {
			offsetX = x % 4;
			offsetY = -y % 4;
		} else {
			offsetX = (z + 1) % 4;
			offsetY = -y % 4;
		}

		if (side == Direction.NORTH || side == Direction.EAST) {
			offsetX = (4 - offsetX - 1) % 4;
		}

		if (offsetX < 0) {
			offsetX += 16;
		}
		if (offsetY < 0) {
			offsetY += 16;
		}

		return new Vec2i((offsetX % 4) * 4, (offsetY % 4) * 4);
	}

	@Override
	public void resolveDependencies(Resolver resolver) {
		this.sourceModel.resolveDependencies(resolver);
	}

	@Override
	public MapCodec<? extends CustomUnbakedBlockStateModel> codec() {
		return MAP_CODEC;
	}
}
