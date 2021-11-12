package twilightforest.world.components.structures.stronghold;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import twilightforest.world.registration.TFFeature;

import java.util.Random;

public class StrongholdFoundryComponent extends StructureTFStrongholdComponent {

	int entranceLevel;
	boolean deepslateVer;

	public StrongholdFoundryComponent(ServerLevel level, CompoundTag nbt) {
		super(StrongholdPieces.TFSFo, nbt);
		this.deepslateVer = nbt.getBoolean("deepslateVer");
		this.entranceLevel = nbt.getInt("entranceLevel");
	}

	public StrongholdFoundryComponent(TFFeature feature, int i, Direction facing, int x, int y, int z) {
		super(StrongholdPieces.TFSFo, feature, i, facing, x, y, z);
	}

	@Override
	protected void addAdditionalSaveData(ServerLevel level, CompoundTag tagCompound) {
		super.addAdditionalSaveData(level, tagCompound);
		tagCompound.putInt("entranceLevel", this.entranceLevel);
		tagCompound.putBoolean("deepslateVer", this.deepslateVer);
	}

	@Override
	public BoundingBox generateBoundingBox(Direction facing, int x, int y, int z) {
		this.deepslateVer = new Random().nextBoolean();
		if (y > -15) {
			this.entranceLevel = 3;
			return StructureTFStrongholdComponent.getComponentToAddBoundingBox(x, y, z, -4, -20, 0, 18, 25, 18, facing);
		} else if (y < -21) {
			this.entranceLevel = 1;
			return StructureTFStrongholdComponent.getComponentToAddBoundingBox(x, y, z, -4, -6, 0, 18, 25, 18, facing);
		} else {
			this.entranceLevel = 2;
			return StructureTFStrongholdComponent.getComponentToAddBoundingBox(x, y, z, -4, -13, 0, 18, 25, 18, facing);
		}
	}

	@Override
	public void addChildren(StructurePiece parent, StructurePieceAccessor list, Random random) {
		super.addChildren(parent, list, random);

		switch (this.entranceLevel) {
			case 1:
				this.addDoor(4, 6, 0);
				addNewComponent(parent, list, random, Rotation.CLOCKWISE_90, -1, 13, 13);
				addNewComponent(parent, list, random, Rotation.COUNTERCLOCKWISE_90, 18, 13, 4);
				addNewComponent(parent, list, random, Rotation.NONE, 13, 20, 18);
				break;
			case 2:
				this.addDoor(4, 13, 0);
				addNewComponent(parent, list, random, Rotation.CLOCKWISE_90, -1, 6, 13);
				addNewComponent(parent, list, random, Rotation.COUNTERCLOCKWISE_90, 18, 20, 4);
				addNewComponent(parent, list, random, Rotation.NONE, 13, 13, 18);
				break;
			case 3:
				this.addDoor(4, 20, 0);
				addNewComponent(parent, list, random, Rotation.NONE, 13, 6, 18);
				addNewComponent(parent, list, random, Rotation.CLOCKWISE_90, -1, 13, 13);
				addNewComponent(parent, list, random, Rotation.COUNTERCLOCKWISE_90, 18, 13, 4);
		}
	}

	@Override
	public boolean postProcess(WorldGenLevel world, StructureFeatureManager manager, ChunkGenerator generator, Random rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		placeStrongholdWalls(world, sbb, 0, 0, 0, 17, 25, 17, rand, deco.randomBlocks);

		// lava bottom
		this.generateBox(world, sbb, 1, 0, 1, 16, 4, 16, Blocks.LAVA.defaultBlockState(), Blocks.LAVA.defaultBlockState(), false);

		// top ledge
		this.generateBox(world, sbb, 1, 19, 1, 16, 19, 16, false, rand, deco.randomBlocks);
		this.generateAirBox(world, sbb, 2, 19, 2, 15, 19, 15);

		// middle ledge
		this.generateBox(world, sbb, 1, 12, 1, 16, 12, 16, false, rand, deco.randomBlocks);
		this.generateAirBox(world, sbb, 2, 12, 2, 15, 12, 15);

		// bottom ledge
		this.generateBox(world, sbb, 1, 5, 1, 16, 5, 16, false, rand, deco.randomBlocks);
		this.generateAirBox(world, sbb, 2, 5, 2, 15, 5, 15);

		// corner pillars
		this.generateBox(world, sbb, 1, 1, 1, 1, 24, 2, false, rand, deco.randomBlocks);
		this.generateBox(world, sbb, 2, 1, 1, 2, 24, 1, false, rand, deco.randomBlocks);

		this.generateBox(world, sbb, 16, 1, 1, 16, 24, 2, false, rand, deco.randomBlocks);
		this.generateBox(world, sbb, 15, 1, 1, 15, 24, 1, false, rand, deco.randomBlocks);

		this.generateBox(world, sbb, 1, 1, 15, 1, 24, 16, false, rand, deco.randomBlocks);
		this.generateBox(world, sbb, 2, 1, 16, 2, 24, 16, false, rand, deco.randomBlocks);

		this.generateBox(world, sbb, 16, 1, 15, 16, 24, 16, false, rand, deco.randomBlocks);
		this.generateBox(world, sbb, 15, 1, 16, 15, 24, 16, false, rand, deco.randomBlocks);

		// suspended mass
		Random massRandom = new Random(rand.nextLong());

		for (int x = 4; x < 14; x++) {
			for (int z = 4; z < 14; z++) {
				for (int y = 8; y < 23; y++) {
					float c = Math.abs(x - 8.5F) + Math.abs(z - 8.5F) + Math.abs(y - 18F);
					float r = 5.5F + ((massRandom.nextFloat() - massRandom.nextFloat()) * 3.5F);

					if (c < r) {
						this.placeBlock(world, deepslateVer ? Blocks.DEEPSLATE.defaultBlockState() : Blocks.STONE.defaultBlockState(), x, y, z, sbb);
					}
				}
			}
		}

		// drips
		for (int i = 0; i < 400; i++) {
			int dx = massRandom.nextInt(9) + 5;
			int dz = massRandom.nextInt(9) + 5;
			int dy = massRandom.nextInt(13) + 10;

			if (this.getBlock(world, dx, dy, dz, sbb).getBlock() != Blocks.AIR) {
				for (int y = 0; y < 3; y++) {
					this.placeBlock(world, deepslateVer ? Blocks.DEEPSLATE.defaultBlockState() : Blocks.STONE.defaultBlockState(), dx, dy - y, dz, sbb);
				}
			}
		}

		// add some redstone ore
		for (int i = 0; i < 8; i++) {
			addOreToMass(world, sbb, massRandom, deepslateVer ? Blocks.DEEPSLATE_REDSTONE_ORE.defaultBlockState() : Blocks.REDSTONE_ORE.defaultBlockState());
		}

		// add some iron ore
		for (int i = 0; i < 8; i++) {
			addOreToMass(world, sbb, massRandom, deepslateVer ? Blocks.DEEPSLATE_IRON_ORE.defaultBlockState() : Blocks.IRON_ORE.defaultBlockState());
		}

		// add some gold ore
		for (int i = 0; i < 6; i++) {
			addOreToMass(world, sbb, massRandom, deepslateVer ? Blocks.DEEPSLATE_GOLD_ORE.defaultBlockState() : Blocks.GOLD_ORE.defaultBlockState());
		}

		// add some glowstone
		for (int i = 0; i < 2; i++) {
			addOreToMass(world, sbb, massRandom, deepslateVer ? Blocks.SHROOMLIGHT.defaultBlockState() : Blocks.GLOWSTONE.defaultBlockState());
		}

		// add some emerald ore
		for (int i = 0; i < 2; i++) {
			addOreToMass(world, sbb, massRandom, deepslateVer ? Blocks.DEEPSLATE_EMERALD_ORE.defaultBlockState() : Blocks.EMERALD_ORE.defaultBlockState());
		}

		// add some diamond ore
		for (int i = 0; i < 4; i++) {
			addOreToMass(world, sbb, massRandom, deepslateVer ? Blocks.DEEPSLATE_DIAMOND_ORE.defaultBlockState() : Blocks.DIAMOND_ORE.defaultBlockState());
		}

		// add some copper ore
		for (int i = 0; i < 6; i++) {
			addOreToMass(world, sbb, massRandom, deepslateVer ? Blocks.DEEPSLATE_COPPER_ORE.defaultBlockState() : Blocks.COPPER_ORE.defaultBlockState());
		}

		// doors
		placeDoors(world, sbb);

		return true;
	}

	/**
	 * Add a block of ore to the mass
	 */
	private void addOreToMass(WorldGenLevel world, BoundingBox sbb, Random massRandom, BlockState state) {
		for (int i = 0; i < 10; i++) {
			int dx = massRandom.nextInt(9) + 5;
			int dz = massRandom.nextInt(9) + 5;
			int dy = massRandom.nextInt(13) + 10;

			if (this.getBlock(world, dx, dy, dz, sbb).getBlock() != Blocks.AIR) {
				this.placeBlock(world, state, dx, dy, dz, sbb);
				// we have succeeded, stop looping
				break;
			}
		}
	}
}
