package twilightforest.world.components.structures.trollcave;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.feature.NoiseEffect;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.StructureFeatureManager;
import twilightforest.world.registration.TFFeature;
import twilightforest.block.TFBlocks;
import twilightforest.loot.TFTreasure;
import twilightforest.world.components.structures.TFStructureComponentOld;

import java.util.Random;

public class TrollVaultComponent extends TFStructureComponentOld {

	public TrollVaultComponent(ServerLevel level, CompoundTag nbt) {
		super(TrollCavePieces.TFTCVa, nbt);
	}

	public TrollVaultComponent(TFFeature feature, int index, int x, int y, int z) {
		super(TrollCavePieces.TFTCVa, feature, index, x, y, z);
		this.setOrientation(Direction.SOUTH);

		// adjust x, y, z
		x = (x >> 2) << 2;
		y = (y / 4) * 4;
		z = (z >> 2) << 2;

		// spawn list!
		this.spawnListIndex = -1;

		this.boundingBox = feature.getComponentToAddBoundingBox(x, y, z, -8, 0, -8, 12, 12, 12, Direction.SOUTH);
	}

	@Override
	public boolean postProcess(WorldGenLevel world, StructureFeatureManager manager, ChunkGenerator generator, Random rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		// make walls
		this.generateBox(world, sbb, 0, 0, 0, 11, 11, 11, TFBlocks.GIANT_OBSIDIAN.get().defaultBlockState(), TFBlocks.GIANT_OBSIDIAN.get().defaultBlockState(), false);

		// clear inside
		this.generateAirBox(world, sbb, 4, 4, 4, 7, 7, 7);

		// cobblestone platform
		this.generateBox(world, sbb, 5, 5, 5, 6, 5, 6, Blocks.COBBLESTONE.defaultBlockState(), Blocks.COBBLESTONE.defaultBlockState(), false);

		// chests
		this.setDoubleLootChest(world, 5, 6, 5, 5, 6, 6, getOrientation().getClockWise(), TFTreasure.TROLL_VAULT, sbb, false);
		this.setDoubleLootChest(world, 6, 6, 5, 6, 6, 6, getOrientation().getClockWise(), TFTreasure.TROLL_GARDEN, sbb, false);

		return true;
	}

	@Override
	public NoiseEffect getNoiseEffect() {
		return NoiseEffect.BURY;
	}
}
