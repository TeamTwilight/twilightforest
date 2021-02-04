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

public class ComponentTFDarkTowerRoofRings extends ComponentTFDarkTowerRoof {

	public ComponentTFDarkTowerRoofRings(TemplateManager manager, CompoundNBT nbt) {
		super(TFDarkTowerPieces.TFDTRR, nbt);
	}

	public ComponentTFDarkTowerRoofRings(TFFeature feature, int i, ComponentTFTowerWing wing) {
		super(TFDarkTowerPieces.TFDTRR, feature, i, wing);
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

		makeARing(world, 6, sbb);
		makeARing(world, 8, sbb);

		return true;
	}

	protected void makeARing(ISeedReader world, int y, MutableBoundingBox sbb) {
		setBlockState(world, deco.accentState, size / 2 - 2, y, size / 2 + 1, sbb);
		setBlockState(world, deco.accentState, size / 2 - 2, y, size / 2 + 0, sbb);
		setBlockState(world, deco.accentState, size / 2 - 2, y, size / 2 - 1, sbb);
		setBlockState(world, deco.accentState, size / 2 + 2, y, size / 2 + 1, sbb);
		setBlockState(world, deco.accentState, size / 2 + 2, y, size / 2 + 0, sbb);
		setBlockState(world, deco.accentState, size / 2 + 2, y, size / 2 - 1, sbb);
		setBlockState(world, deco.accentState, size / 2 + 1, y, size / 2 - 2, sbb);
		setBlockState(world, deco.accentState, size / 2 + 0, y, size / 2 - 2, sbb);
		setBlockState(world, deco.accentState, size / 2 - 1, y, size / 2 - 2, sbb);
		setBlockState(world, deco.accentState, size / 2 + 1, y, size / 2 + 2, sbb);
		setBlockState(world, deco.accentState, size / 2 + 0, y, size / 2 + 2, sbb);
		setBlockState(world, deco.accentState, size / 2 - 1, y, size / 2 + 2, sbb);
	}
}
