package twilightforest.structures;

import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;
import twilightforest.TFFeature;
import twilightforest.block.BlockTFBossSpawner;
import twilightforest.block.TFBlocks;
import twilightforest.enums.BossVariant;

import java.util.List;
import java.util.Random;

public class ComponentTFHydraLair extends ComponentTFHollowHill {

	public ComponentTFHydraLair(TemplateManager manager, CompoundNBT nbt) {
		super(TFFeature.TFHydra, nbt);
	}

	public ComponentTFHydraLair(TFFeature feature, Random rand, int i, int x, int y, int z) {
		super(TFFeature.TFHydra, feature, i, 2, x, y + 2, z);
	}

	@Override
	public void buildComponent(StructurePiece structurecomponent, List<StructurePiece> list, Random random) {
		// NO-OP
	}

	@Override
	public boolean func_230383_a_(ISeedReader world, StructureManager manager, ChunkGenerator generator, Random rand, MutableBoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		int stalacts = 64;
		int stalags = 8;

		// fill in features
		// ore or glowing stalactites! (smaller, less plentiful)
		for (int i = 0; i < stalacts; i++) {
			int[] dest = getCoordsInHill2D(rand);
			generateOreStalactite(world, generator, manager, dest[0], 1, dest[1], sbb);
		}
		// stone stalactites!
		for (int i = 0; i < stalacts; i++) {
			int[] dest = getCoordsInHill2D(rand);
			generateBlockStalactite(world, generator, manager, Blocks.STONE, 1.0F, true, dest[0], 1, dest[1], sbb);
		}
		// stone stalagmites!
		for (int i = 0; i < stalags; i++) {
			int[] dest = getCoordsInHill2D(rand);
			generateBlockStalactite(world, generator, manager, Blocks.STONE, 0.9F, false, dest[0], 1, dest[1], sbb);
		}

		// boss spawner seems important
		setBlockState(world, TFBlocks.boss_spawner_hydra.get().getDefaultState(), 27, 3, 27, sbb);

		return true;
	}
}
