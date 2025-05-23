package twilightforest.world.components.structures.stronghold;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import twilightforest.init.TFStructurePieceTypes;


public class StrongholdFoundryComponent extends KnightStrongholdComponent {

	int entranceLevel;

	public StrongholdFoundryComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(TFStructurePieceTypes.TFSFo.get(), nbt);
		this.entranceLevel = nbt.getInt("entranceLevel");
	}

	public StrongholdFoundryComponent(int i, Direction facing, int x, int y, int z) {
		super(TFStructurePieceTypes.TFSFo.get(), i, facing, x, y, z);
	}

	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext ctx, CompoundTag tagCompound) {
		super.addAdditionalSaveData(ctx, tagCompound);
		tagCompound.putInt("entranceLevel", this.entranceLevel);
	}

	@Override
	public BoundingBox generateBoundingBox(Direction facing, int x, int y, int z) {
		if (y > -15) {
			this.entranceLevel = 3;
			return KnightStrongholdComponent.getComponentToAddBoundingBox(x, y, z, -4, -20, 0, 18, 26, 18, facing);
		} else if (y < -21) {
			this.entranceLevel = 1;
			return KnightStrongholdComponent.getComponentToAddBoundingBox(x, y, z, -4, -6, 0, 18, 26, 18, facing);
		} else {
			this.entranceLevel = 2;
			return KnightStrongholdComponent.getComponentToAddBoundingBox(x, y, z, -4, -13, 0, 18, 26, 18, facing);
		}
	}

	@Override
	public void addChildren(StructurePiece parent, StructurePieceAccessor list, RandomSource random) {
		super.addChildren(parent, list, random);

		switch (this.entranceLevel) {
			case 1 -> {
				this.addDoor(4, 6, 0);
				addNewComponent(parent, list, random, Rotation.CLOCKWISE_90, -1, 13, 13);
				addNewComponent(parent, list, random, Rotation.COUNTERCLOCKWISE_90, 18, 13, 4);
				addNewComponent(parent, list, random, Rotation.NONE, 13, 20, 18);
			}
			case 2 -> {
				this.addDoor(4, 13, 0);
				addNewComponent(parent, list, random, Rotation.CLOCKWISE_90, -1, 6, 13);
				addNewComponent(parent, list, random, Rotation.COUNTERCLOCKWISE_90, 18, 20, 4);
				addNewComponent(parent, list, random, Rotation.NONE, 13, 13, 18);
			}
			case 3 -> {
				this.addDoor(4, 20, 0);
				addNewComponent(parent, list, random, Rotation.NONE, 13, 6, 18);
				addNewComponent(parent, list, random, Rotation.CLOCKWISE_90, -1, 13, 13);
				addNewComponent(parent, list, random, Rotation.COUNTERCLOCKWISE_90, 18, 13, 4);
			}
		}
	}

	@Override
	public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
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
		RandomSource massRandom = RandomSource.create(rand.nextLong());

		for (int x = 4; x < 14; x++) {
			for (int z = 4; z < 14; z++) {
				for (int y = 8; y < 23; y++) {
					float c = Math.abs(x - 8.5F) + Math.abs(z - 8.5F) + Math.abs(y - 18F);
					float r = 5.5F + ((massRandom.nextFloat() - massRandom.nextFloat()) * 3.5F);

					if (c < r) {
						this.placeBlock(world, Blocks.STONE.defaultBlockState(), x, y, z, sbb);
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
					this.placeBlock(world, Blocks.STONE.defaultBlockState(), dx, dy - y, dz, sbb);
				}
			}
		}

		// add some redstone ore
		for (int i = 0; i < 8; i++) {
			addOreToMass(world, sbb, massRandom, Blocks.REDSTONE_ORE.defaultBlockState());
		}

		// add some iron ore
		for (int i = 0; i < 8; i++) {
			addOreToMass(world, sbb, massRandom, Blocks.IRON_ORE.defaultBlockState());
		}

		// add some gold ore
		for (int i = 0; i < 6; i++) {
			addOreToMass(world, sbb, massRandom, Blocks.GOLD_ORE.defaultBlockState());
		}

		// add some glowstone
		for (int i = 0; i < 2; i++) {
			addOreToMass(world, sbb, massRandom, Blocks.GLOWSTONE.defaultBlockState());
		}

		// add some emerald ore
		for (int i = 0; i < 2; i++) {
			addOreToMass(world, sbb, massRandom, Blocks.EMERALD_ORE.defaultBlockState());
		}

		// add some diamond ore
		for (int i = 0; i < 4; i++) {
			addOreToMass(world, sbb, massRandom, Blocks.DIAMOND_ORE.defaultBlockState());
		}

		// add some copper ore
		for (int i = 0; i < 6; i++) {
			addOreToMass(world, sbb, massRandom, Blocks.COPPER_ORE.defaultBlockState());
		}

		// doors
		placeDoors(world, sbb);
	}

	/**
	 * Add a block of ore to the mass
	 */
	private void addOreToMass(WorldGenLevel world, BoundingBox sbb, RandomSource massRandom, BlockState state) {
		for (int i = 0; i < 10; i++) {
			int dx = massRandom.nextInt(9) + 5;
			int dz = massRandom.nextInt(9) + 5;
			int dy = massRandom.nextInt(13) + 10;

			if (!this.getBlock(world, dx, dy, dz, sbb).isAir()) {
				this.placeBlock(world, state, dx, dy, dz, sbb);
				// we have succeeded, stop looping
				break;
			}
		}
	}
}
