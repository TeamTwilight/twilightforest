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
import twilightforest.block.TFBlocks;
import twilightforest.loot.TFTreasure;
import twilightforest.structures.StructureTFComponentOld;

import java.util.Random;

public class ComponentTFTrollVault extends StructureTFComponentOld {

	public ComponentTFTrollVault(TemplateManager manager, CompoundNBT nbt) {
		super(TFTrollCavePieces.TFTCVa, nbt);
	}

	public ComponentTFTrollVault(TFFeature feature, int index, int x, int y, int z) {
		super(TFTrollCavePieces.TFTCVa, feature, index);
		this.setCoordBaseMode(Direction.SOUTH);

		// adjust x, y, z
		x = (x >> 2) << 2;
		y = (y / 4) * 4;
		z = (z >> 2) << 2;

		// spawn list!
		this.spawnListIndex = -1;

		this.boundingBox = feature.getComponentToAddBoundingBox(x, y, z, -8, 0, -8, 12, 12, 12, Direction.SOUTH);
	}

	@Override
	public boolean func_230383_a_(ISeedReader world, StructureManager manager, ChunkGenerator generator, Random rand, MutableBoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		// make walls
		this.fillWithBlocks(world, sbb, 0, 0, 0, 11, 11, 11, TFBlocks.giant_obsidian.get().getDefaultState(), TFBlocks.giant_obsidian.get().getDefaultState(), false);

		// clear inside
		this.fillWithAir(world, sbb, 4, 4, 4, 7, 7, 7);

		// cobblestone platform
		this.fillWithBlocks(world, sbb, 5, 5, 5, 6, 5, 6, Blocks.COBBLESTONE.getDefaultState(), Blocks.COBBLESTONE.getDefaultState(), false);

		// chests
		this.setDoubleLootChest(world, 5, 6, 5, 5, 6, 6, getCoordBaseMode().rotateY(), TFTreasure.troll_vault, sbb, false);
		this.setDoubleLootChest(world, 6, 6, 5, 6, 6, 6, getCoordBaseMode().rotateY(), TFTreasure.troll_garden, sbb, false);

		return true;
	}
}
