package twilightforest.structures.lichtower;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.template.TemplateManager;
import twilightforest.TFFeature;
import twilightforest.structures.StructureTFComponentOld;

import java.util.Random;

public class ComponentTFTowerBeard extends StructureTFComponentOld {

	int size;
	int height;

	public ComponentTFTowerBeard(TemplateManager manager, CompoundNBT nbt) {
		this(TFLichTowerPieces.TFLTBea, nbt);
	}

	public ComponentTFTowerBeard(IStructurePieceType piece, CompoundNBT nbt) {
		super(piece, nbt);
		this.size = nbt.getInt("beardSize");
		this.height = nbt.getInt("beardHeight");
	}

	public ComponentTFTowerBeard(IStructurePieceType piece, TFFeature feature, int i, ComponentTFTowerWing wing) {
		super(piece, feature, i);

		this.setCoordBaseMode(wing.getCoordBaseMode());
		this.size = wing.size - 2;
		this.height = size / 2;

		// just hang out at the very bottom of the tower
		this.boundingBox = new MutableBoundingBox(wing.getBoundingBox().minX + 1, wing.getBoundingBox().minY - this.height - 1, wing.getBoundingBox().minZ + 1, wing.getBoundingBox().maxX - 1, wing.getBoundingBox().minY - 1, wing.getBoundingBox().maxZ - 1);
	}

	@Override
	protected void readAdditional(CompoundNBT tagCompound) {
		super.readAdditional(tagCompound);
		tagCompound.putInt("beardSize", this.size);
		tagCompound.putInt("beardHeight", this.height);
	}

	/**
	 * Makes a pyramid-shaped beard
	 */
	@Override
	public boolean func_230383_a_(ISeedReader world, StructureManager manager, ChunkGenerator generator, Random rand, MutableBoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		return makePyramidBeard(world, rand, sbb);
	}

	private boolean makePyramidBeard(ISeedReader world, Random rand, MutableBoundingBox sbb) {
		for (int y = 0; y <= height; y++) {
			int min = y;
			int max = size - y - 1;

			fillWithRandomizedBlocks(world, sbb, min, height - y, min, max, height - y, max, false, rand, StructureTFComponentOld.getStrongholdStones());
		}
		return true;
	}
}
