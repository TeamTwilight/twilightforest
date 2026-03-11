package twilightforest.world.components.structures.trollcave;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.world.components.structures.TFStructureComponentOld;


public class TrollCloudComponent extends TFStructureComponentOld {

	private final int size;
	private final int height;

	public TrollCloudComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(TFStructurePieceTypes.TFTCloud.get(), nbt);
		this.size = nbt.getInt("size");
		this.height = nbt.getInt("height");
	}

	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext ctx, CompoundTag tagCompound) {
		super.addAdditionalSaveData(ctx, tagCompound);
		tagCompound.putInt("size", this.size);
		tagCompound.putInt("height", this.height);
	}

	@Override
	public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		placeCloud(world, sbb, 0, 0, 0, this.size - 1, 6, this.size - 1);
	}

	protected void placeCloud(WorldGenLevel world, BoundingBox sbb, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
		this.generateBox(world, sbb, minX, minY, minZ, maxX, maxY, maxZ, Blocks.WHITE_STAINED_GLASS.defaultBlockState(), Blocks.WHITE_STAINED_GLASS.defaultBlockState(), false);
		this.generateBox(world, sbb, minX + 2, minY + 2, minZ + 2, maxX - 2, maxY - 1, maxZ - 2, Blocks.QUARTZ_BLOCK.defaultBlockState(), Blocks.QUARTZ_BLOCK.defaultBlockState(), false);

	}
}
