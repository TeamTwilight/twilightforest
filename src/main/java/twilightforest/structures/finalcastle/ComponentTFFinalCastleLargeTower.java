package twilightforest.structures.finalcastle;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;
import twilightforest.TFFeature;
import twilightforest.block.TFBlocks;
import twilightforest.structures.StructureTFComponentOld;
import twilightforest.structures.lichtower.ComponentTFTowerWing;

import java.util.List;
import java.util.Random;

public class ComponentTFFinalCastleLargeTower extends ComponentTFTowerWing {

	public ComponentTFFinalCastleLargeTower(TemplateManager manager, CompoundNBT nbt) {
		super(TFFinalCastlePieces.TFFCLaTo, nbt);
	}

	public ComponentTFFinalCastleLargeTower(TFFeature feature, Random rand, int i, int x, int y, int z, Direction rotation) {
		super(TFFinalCastlePieces.TFFCLaTo, feature, i);
		this.setCoordBaseMode(rotation);
		this.size = 13;
		this.height = 61;
		this.boundingBox = feature.getComponentToAddBoundingBox(x, y, z, -6, 0, -6, 12, 60, 12, Direction.SOUTH);

	}

	@Override
	public void buildComponent(StructurePiece parent, List<StructurePiece> list, Random rand) {
		if (parent != null && parent instanceof StructureTFComponentOld) {
			this.deco = ((StructureTFComponentOld) parent).deco;
		}
		// add crown
		ComponentTFFinalCastleRoof9Crenellated roof = new ComponentTFFinalCastleRoof9Crenellated(getFeatureType(), rand, 4, this);
		list.add(roof);
		roof.buildComponent(this, list, rand);
	}

	@Override
	public boolean func_230383_a_(ISeedReader world, StructureManager manager, ChunkGenerator generator, Random rand, MutableBoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		Random decoRNG = new Random(world.getSeed() + (this.boundingBox.minX * 321534781) ^ (this.boundingBox.minZ * 756839));

		fillWithRandomizedBlocks(world, sbb, 0, 0, 0, 12, 59, 12, false, rand, deco.randomBlocks);

		// add branching runes
		int numBranches = 6 + decoRNG.nextInt(4);
		for (int i = 0; i < numBranches; i++) {
			makeGlyphBranches(world, decoRNG, this.getGlyphMeta(), sbb);
		}

		// beard
		for (int i = 1; i < 4; i++) {
			fillWithRandomizedBlocks(world, sbb, i, 0 - (i * 2), i, 8 - i, 1 - (i * 2), 8 - i, false, rand, deco.randomBlocks);
		}
		this.setBlockState(world, deco.blockState, 4, -7, 4, sbb);

		// door, first floor
		final BlockState castleDoor = TFBlocks.castle_door_pink.get().getDefaultState(); //this.getGlyphMeta()?
		this.fillWithBlocks(world, sbb, 0, 1, 1, 0, 4, 3, castleDoor, AIR, false);

		this.placeSignAtCurrentPosition(world, 6, 1, 6, "Parkour area 1", "Unique monster?", sbb);

		return true;
	}

	public BlockState getGlyphMeta() {
		return TFBlocks.castle_rune_brick_pink.get().getDefaultState();
	}
}
