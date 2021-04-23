package twilightforest.structures.trollcave;

import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.template.TemplateManager;
import twilightforest.TFFeature;
import twilightforest.structures.StructureTFComponentOld;

import java.util.Random;

public class ComponentTFTrollCloud extends StructureTFComponentOld {

	private int size;
	private int height;

	public ComponentTFTrollCloud(TemplateManager manager, CompoundNBT nbt) {
		super(TFTrollCavePieces.TFTCloud, nbt);
		this.size = nbt.getInt("size");
		this.height = nbt.getInt("height");
	}

	public ComponentTFTrollCloud(TFFeature feature, int index, int x, int y, int z) {
		super(TFTrollCavePieces.TFTCloud, feature, index);
		this.setCoordBaseMode(Direction.SOUTH);

		this.size = 40;
		this.height = 20;

		int radius = this.size / 2;
		this.boundingBox = feature.getComponentToAddBoundingBox(x, y, z, -radius, -this.height, -radius, this.size, this.height, this.size, Direction.SOUTH);
	}

	@Override
	protected void readAdditional(CompoundNBT tagCompound) {
		super.readAdditional(tagCompound);
		tagCompound.putInt("size", this.size);
		tagCompound.putInt("height", this.height);
	}

	@Override
	public boolean func_230383_a_(ISeedReader world, StructureManager manager, ChunkGenerator generator, Random rand, MutableBoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		placeCloud(world, sbb, 0, 0, 0, this.size - 1, 6, this.size - 1);

		return true;
	}

	protected void placeCloud(ISeedReader world, MutableBoundingBox sbb, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
		this.fillWithBlocks(world, sbb, minX, minY, minZ, maxX, maxY, maxZ, Blocks.WHITE_STAINED_GLASS.getDefaultState(), Blocks.WHITE_STAINED_GLASS.getDefaultState(), false);
		this.fillWithBlocks(world, sbb, minX + 2, minY + 2, minZ + 2, maxX - 2, maxY - 1, maxZ - 2, Blocks.QUARTZ_BLOCK.getDefaultState(), Blocks.QUARTZ_BLOCK.getDefaultState(), false);

	}
}
