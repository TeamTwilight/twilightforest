package twilightforest.structures.finalcastle;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
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

public class ComponentTFFinalCastleStairTower extends ComponentTFTowerWing {

	public ComponentTFFinalCastleStairTower(TemplateManager manager, CompoundNBT nbt) {
		super(TFFinalCastlePieces.TFFCStTo, nbt);
	}

	//TODO: Parameter "rand" is unused. Remove?
	public ComponentTFFinalCastleStairTower(TFFeature feature, Random rand, int i, int x, int y, int z, Direction rotation) {
		super(TFFinalCastlePieces.TFFCStTo, feature, i);
		this.setCoordBaseMode(rotation);
		this.size = 9;
		this.height = 51;
		this.boundingBox = feature.getComponentToAddBoundingBox(x, y, z, -4, 0, -4, 8, 50, 8, Direction.SOUTH);
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

		fillWithRandomizedBlocks(world, sbb, 0, 0, 0, 8, 49, 8, false, rand, deco.randomBlocks);

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
		final BlockState castleDoor = TFBlocks.castle_door_blue.get().getDefaultState();
		this.fillWithBlocks(world, sbb, 0, 1, 1, 0, 3, 2, castleDoor, AIR, false);

		// stairs
		Rotation rotation = Rotation.CLOCKWISE_90;
		for (int f = 0; f < 5; f++) {
			//int rotation = (f + 2) % 4;
			rotation = rotation.add(Rotation.CLOCKWISE_90);
			int y = f * 3 + 1;
			for (int i = 0; i < 3; i++) {
				int sx = 3 + i;
				int sy = y + i;
				int sz = 1;

				this.setBlockStateRotated(world, getStairState(deco.stairState, Direction.WEST, rotation, false), sx, sy, sz, rotation, sbb);
				this.setBlockStateRotated(world, deco.blockState, sx, sy - 1, sz, rotation, sbb);
				this.setBlockStateRotated(world, getStairState(deco.stairState, Direction.WEST, rotation, false), sx, sy, sz + 1, rotation, sbb);
				this.setBlockStateRotated(world, deco.blockState, sx, sy - 1, sz + 1, rotation, sbb);
			}
			// landing
			this.fillBlocksRotated(world, sbb, 6, y + 2, 1, 7, y + 2, 2, deco.blockState, rotation);
		}

		// door, second floor
		this.fillWithBlocks(world, sbb, 1, 18, 0, 2, 20, 0, castleDoor, AIR, false);

		BlockState stairState = getStairState(deco.stairState, Direction.SOUTH, rotation, false);

		// second floor landing
		this.fillWithBlocks(world, sbb, 1, 17, 1, 3, 17, 3, deco.blockState, deco.blockState, false);
		this.fillWithBlocks(world, sbb, 1, 17, 4, 2, 17, 4, stairState, stairState, false);
		this.fillWithBlocks(world, sbb, 1, 16, 4, 2, 16, 4, deco.blockState, deco.blockState, false);
		this.fillWithBlocks(world, sbb, 1, 16, 5, 2, 16, 5, stairState, stairState, false);
		this.fillWithBlocks(world, sbb, 1, 15, 5, 2, 15, 5, deco.blockState, deco.blockState, false);

		// door, roof
		this.fillWithBlocks(world, sbb, 1, 39, 0, 2, 41, 0, castleDoor, AIR, false);

		// stairs
		rotation = Rotation.COUNTERCLOCKWISE_90;
		for (int f = 0; f < 7; f++) {
			//int rotation = (f + 0) % 4;
			rotation = rotation.add(Rotation.CLOCKWISE_90);
			int y = f * 3 + 18;
			for (int i = 0; i < 3; i++) {
				int sx = 3 + i;
				int sy = y + i;
				int sz = 1;

				this.setBlockStateRotated(world, getStairState(deco.stairState, Direction.WEST, rotation, false), sx, sy, sz, rotation, sbb);
				this.setBlockStateRotated(world, deco.blockState, sx, sy - 1, sz, rotation, sbb);
				this.setBlockStateRotated(world, getStairState(deco.stairState, Direction.WEST, rotation, false), sx, sy, sz + 1, rotation, sbb);
				this.setBlockStateRotated(world, deco.blockState, sx, sy - 1, sz + 1, rotation, sbb);
			}
			// landing
			this.fillBlocksRotated(world, sbb, 6, y + 2, 1, 7, y + 2, 2, deco.blockState, rotation);
		}

		// roof access landing
		this.fillWithBlocks(world, sbb, 1, 38, 1, 3, 38, 5, deco.blockState, deco.blockState, false);
		this.fillWithBlocks(world, sbb, 3, 39, 1, 3, 39, 5, deco.fenceState, deco.fenceState, false);

		return true;
	}

	public BlockState getGlyphMeta() {
		return TFBlocks.castle_rune_brick_blue.get().getDefaultState();
	}
}
