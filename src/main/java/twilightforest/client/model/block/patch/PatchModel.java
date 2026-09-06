package twilightforest.client.model.block.patch;

import com.mojang.math.Quadrant;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.dispatch.BlockModelRotation;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.SimpleModelWrapper;
import net.minecraft.client.resources.model.cuboid.CuboidFace;
import net.minecraft.client.resources.model.cuboid.FaceBakery;
import net.minecraft.client.resources.model.cuboid.ItemTransforms;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.geometry.QuadCollection;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.neoforged.neoforge.client.model.DynamicBlockStateModel;
import net.neoforged.neoforge.client.model.ExtraFaceData;
import org.joml.Vector3f;
import twilightforest.block.PatchBlock;

import java.util.List;

public class PatchModel implements DynamicBlockStateModel {

	private final BakedQuad.MaterialInfo materialInfo;
	private final boolean shaggify;
	private final Material.Baked particleTexture;
	private final int materialFlags;

	public PatchModel(BakedQuad.MaterialInfo materialInfo, boolean shaggify, Material.Baked particleTexture) {
		this.materialInfo = materialInfo;
		this.shaggify = shaggify;
		this.particleTexture = particleTexture;
		this.materialFlags = materialInfo.flags();
	}

	@Override
	public void collectParts(BlockAndTintGetter level, BlockPos pos, BlockState state, RandomSource random, List<BlockStateModelPart> parts) {
		QuadCollection.Builder quadCollection = new QuadCollection.Builder();

        this.collectParts(
				state.getValueOrElse(PatchBlock.NORTH, false),
				state.getValueOrElse(PatchBlock.EAST, false),
				state.getValueOrElse(PatchBlock.SOUTH, false),
				state.getValueOrElse(PatchBlock.WEST, false),
				random,
				quadCollection
		);

		parts.add(new SimpleModelWrapper(quadCollection.build(), this.materialInfo.ambientOcclusion(), this.particleMaterial()));
	}

	@Override
	public Material.Baked particleMaterial() {
		return this.particleTexture;
	}

	@Override
	public @BakedQuad.MaterialFlags int materialFlags() {
		return this.materialFlags;
	}

	private void collectParts(boolean north, boolean east, boolean south, boolean west, RandomSource posRandom, QuadCollection.Builder parts) {
		ModelBakery.InternerImpl interner = new ModelBakery.InternerImpl();

		BoundingBox bb = PatchBlock.AABBFromRandom(posRandom);

		this.addFromAABB(parts, west ? 0 : bb.minX(), bb.minY(), north ? 0 : bb.minZ(), east ? 16 : bb.maxX(), bb.maxY(), south ? 16 : bb.maxZ(), interner);

		if (!this.shaggify) return;

		// Poll these seeds before entering branching code, otherwise placing neighbors will cause odd changes
		long westSeed = posRandom.nextLong();
		long eastSeed = posRandom.nextLong();
		long northSeed = posRandom.nextLong();
		long southSeed = posRandom.nextLong();

		int minY = bb.minY();
		int maxY = bb.maxY();

		// add on shaggy edges
		if (!west) {
			long seed = westSeed;
			seed = seed * seed * 42317861L + seed * 7L;

			int num0 = (int) (seed >> 12 & 3L) + 1;
			int num1 = (int) (seed >> 15 & 3L) + 1;
			int num2 = (int) (seed >> 18 & 3L) + 1;
			int num3 = (int) (seed >> 21 & 3L) + 1;

			int minZ = bb.minZ() + num0;
			int maxZ = bb.maxZ();

			if (maxZ - ((num1 + num2 + num3)) > minZ) {
				// draw two blobs
				int innerZ = bb.maxZ() - num2;
				this.addFromAABB(parts, bb.minX() - 1, minY, minZ, bb.minX(), maxY, minZ + num1, interner);
				this.addFromAABB(parts, bb.minX() - 1, minY, innerZ - num3, bb.minX(), maxY, innerZ, interner);
			} else {
				//draw one blob
				this.addFromAABB(parts, bb.minX() - 1, minY, minZ, bb.minX(), maxY, maxZ - num2, interner);
			}
		}

		if (!east) {
			long seed = eastSeed;
			seed = seed * seed * 42317861L + seed * 17L;

			int num0 = (int) (seed >> 12 & 3L) + 1;
			int num1 = (int) (seed >> 15 & 3L) + 1;
			int num2 = (int) (seed >> 18 & 3L) + 1;
			int num3 = (int) (seed >> 21 & 3L) + 1;

			int minZ = bb.minZ() + num0;
			int maxZ = bb.maxZ();

			if (maxZ - ((num1 + num2 + num3)) > minZ) {
				// draw two blobs
				int innerZ = maxZ - num2;
				this.addFromAABB(parts, bb.maxX(), minY, minZ, bb.maxX() + 1, maxY, minZ + num1, interner);
				this.addFromAABB(parts, bb.maxX(), minY, innerZ - num3, bb.maxX() + 1, maxY, innerZ, interner);
			} else {
				//draw one blob
				this.addFromAABB(parts, bb.maxX(), minY, minZ, bb.maxX() + 1, maxY, maxZ - num2, interner);
			}
		}

		if (!north) {
			long seed = northSeed;
			seed = seed * seed * 42317861L + seed * 23L;

			int num0 = (int) (seed >> 12 & 3L) + 1;
			int num1 = (int) (seed >> 15 & 3L) + 1;
			int num2 = (int) (seed >> 18 & 3L) + 1;
			int num3 = (int) (seed >> 21 & 3L) + 1;

			int minX = bb.minX() + num0;
			int innerX = minX + num1;
			int maxX = bb.maxX() - num2;

			this.addFromAABB(parts, minX, minY, bb.minZ() - 1, innerX, maxY, bb.minZ(), interner);
			this.addFromAABB(parts, maxX - num3, minY, bb.minZ() - 1, maxX, maxY, bb.minZ(), interner);
		}

		if (!south) {
			long seed = southSeed;
			seed = seed * seed * 42317861L + seed * 11L;

			int num0 = (int) (seed >> 12 & 3L) + 1;
			int num1 = (int) (seed >> 15 & 3L) + 1;
			int num2 = (int) (seed >> 18 & 3L) + 1;
			int num3 = (int) (seed >> 21 & 3L) + 1;

			int minX = bb.minX() + num0;
			int maxX = bb.maxX() - num2;

			this.addFromAABB(parts, minX, minY, bb.maxZ(), minX + num1, maxY, bb.maxZ() + 1, interner);
			this.addFromAABB(parts, maxX - num3, minY, bb.maxZ(), maxX, maxY, bb.maxZ() + 1, interner);
		}
	}

	private void addFromAABB(QuadCollection.Builder parts, float minX, float minY, float minZ, float maxX, float maxY, float maxZ, ModelBakery.InternerImpl interner) {
		parts.addUnculledFace(this.quadFromVectors(Direction.UP, minX, minY, minZ, maxX, maxY, maxZ, interner));
		parts.addUnculledFace(this.quadFromVectors(Direction.NORTH, minX, minY, minZ, maxX, maxY, maxZ, interner));
		parts.addUnculledFace(this.quadFromVectors(Direction.EAST, minX, minY, minZ, maxX, maxY, maxZ, interner));
		parts.addUnculledFace(this.quadFromVectors(Direction.SOUTH, minX, minY, minZ, maxX, maxY, maxZ, interner));
		parts.addUnculledFace(this.quadFromVectors(Direction.WEST, minX, minY, minZ, maxX, maxY, maxZ, interner));
		parts.addUnculledFace(this.quadFromVectors(Direction.DOWN, minX, minY, minZ, maxX, maxY, maxZ, interner));
	}

	private BakedQuad quadFromVectors(Direction direction, float minX, float minY, float minZ, float maxX, float maxY, float maxZ, ModelBakery.InternerImpl interner) {
		CuboidFace.UVs uvs = switch (direction) {
			case NORTH -> new CuboidFace.UVs(maxX, minZ + 1f, minX, minZ);
			case EAST -> new CuboidFace.UVs(maxX, minZ, maxX - 1f, maxZ);
			case SOUTH -> new CuboidFace.UVs(minX, maxZ, maxX, maxZ - 1f);
			case WEST -> new CuboidFace.UVs(minX, maxZ, minX + 1f, minZ);
			default -> new CuboidFace.UVs(minX, minZ, maxX, maxZ);
		};

		return FaceBakery.bakeQuad(interner, new Vector3f(minX, minY, minZ), new Vector3f(maxX, maxY, maxZ), uvs, direction.getAxis() == Direction.Axis.X ? Quadrant.R90 : Quadrant.R0, this.materialInfo, direction, BlockModelRotation.IDENTITY, null, ExtraFaceData.DEFAULT);
	}

}
