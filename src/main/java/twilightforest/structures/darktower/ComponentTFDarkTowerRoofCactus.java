package twilightforest.structures.darktower;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.template.TemplateManager;
import twilightforest.TFFeature;
import twilightforest.structures.lichtower.ComponentTFTowerWing;

import java.util.Random;

public class ComponentTFDarkTowerRoofCactus extends ComponentTFDarkTowerRoof {

	public ComponentTFDarkTowerRoofCactus(TemplateManager manager, CompoundNBT nbt) {
		super(TFDarkTowerPieces.TFDTRC, nbt);
	}

	public ComponentTFDarkTowerRoofCactus(TFFeature feature, int i, ComponentTFTowerWing wing) {
		super(TFDarkTowerPieces.TFDTRC, feature, i, wing);
	}

	@Override
	public boolean func_230383_a_(ISeedReader world, StructureManager manager, ChunkGenerator generator, Random rand, MutableBoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		super.func_230383_a_(world, manager, generator, rand, sbb, chunkPosIn, blockPos);

		// antenna
		for (int y = 1; y < 10; y++) {
			setBlockState(world, deco.blockState, size / 2, y, size / 2, sbb);
		}
		setBlockState(world, deco.accentState, size / 2, 10, size / 2, sbb);

		setBlockState(world, deco.accentState, size / 2 - 1, 1, size / 2, sbb);
		setBlockState(world, deco.accentState, size / 2 + 1, 1, size / 2, sbb);
		setBlockState(world, deco.accentState, size / 2, 1, size / 2 - 1, sbb);
		setBlockState(world, deco.accentState, size / 2, 1, size / 2 + 1, sbb);

		// cactus things
		setBlockState(world, deco.accentState, size / 2 + 1, 7, size / 2, sbb);
		setBlockState(world, deco.accentState, size / 2 + 2, 7, size / 2, sbb);
		setBlockState(world, deco.accentState, size / 2 + 2, 8, size / 2, sbb);
		setBlockState(world, deco.accentState, size / 2 + 2, 9, size / 2, sbb);
		setBlockState(world, deco.accentState, size / 2 + 3, 9, size / 2, sbb);

		setBlockState(world, deco.accentState, size / 2, 6, size / 2 + 1, sbb);
		setBlockState(world, deco.accentState, size / 2, 6, size / 2 + 2, sbb);
		setBlockState(world, deco.accentState, size / 2, 7, size / 2 + 2, sbb);
		setBlockState(world, deco.accentState, size / 2, 8, size / 2 + 2, sbb);
		setBlockState(world, deco.accentState, size / 2, 8, size / 2 + 3, sbb);

		setBlockState(world, deco.accentState, size / 2 - 1, 5, size / 2, sbb);
		setBlockState(world, deco.accentState, size / 2 - 2, 5, size / 2, sbb);
		setBlockState(world, deco.accentState, size / 2 - 2, 6, size / 2, sbb);
		setBlockState(world, deco.accentState, size / 2 - 2, 7, size / 2, sbb);
		setBlockState(world, deco.accentState, size / 2 - 3, 7, size / 2, sbb);

		setBlockState(world, deco.accentState, size / 2, 4, size / 2 - 1, sbb);
		setBlockState(world, deco.accentState, size / 2, 4, size / 2 - 2, sbb);
		setBlockState(world, deco.accentState, size / 2, 5, size / 2 - 2, sbb);
		setBlockState(world, deco.accentState, size / 2, 6, size / 2 - 2, sbb);
		setBlockState(world, deco.accentState, size / 2, 6, size / 2 - 3, sbb);

		return true;
	}
}
