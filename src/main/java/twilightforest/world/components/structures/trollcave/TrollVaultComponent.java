package twilightforest.world.components.structures.trollcave;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.loot.TFLootTables;
import twilightforest.util.BoundingBoxUtils;
import twilightforest.world.components.structures.TFStructureComponentOld;


public class TrollVaultComponent extends TFStructureComponentOld {

	public TrollVaultComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(TFStructurePieceTypes.TFTCVa.get(), nbt);
	}

	public TrollVaultComponent(int index, int x, int y, int z) {
		super(TFStructurePieceTypes.TFTCVa.get(), index, x, y, z);
		this.setOrientation(Direction.SOUTH);

		// adjust x, y, z
		x = (x >> 2) << 2;
		y = (y / 4) * 4;
		z = (z >> 2) << 2;

		// spawn list!
		this.spawnListIndex = -1;

		this.boundingBox = BoundingBoxUtils.getComponentToAddBoundingBox(x, y, z, -8, 0, -8, 12, 12, 12, Direction.SOUTH, false);
	}

	@Override
	public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		// make walls
		this.generateBox(world, sbb, 0, 0, 0, 11, 11, 11, TFBlocks.GIANT_OBSIDIAN.get().defaultBlockState(), TFBlocks.GIANT_OBSIDIAN.get().defaultBlockState(), false);

		// clear inside
		this.generateAirBox(world, sbb, 4, 4, 4, 7, 7, 7);

		// cobblestone platform
		this.generateBox(world, sbb, 5, 5, 5, 6, 5, 6, Blocks.COBBLESTONE.defaultBlockState(), Blocks.COBBLESTONE.defaultBlockState(), false);

		// chests
		this.setDoubleLootChest(world, 5, 6, 5, 5, 6, 6, getOrientation().getClockWise(), TFLootTables.TROLL_VAULT, TFLootTables.TROLL_VAULT_WITH_LAMP, sbb, false);
		this.setDoubleLootChest(world, 6, 6, 5, 6, 6, 6, getOrientation().getClockWise(), TFLootTables.TROLL_GARDEN, sbb, false);
	}
}
