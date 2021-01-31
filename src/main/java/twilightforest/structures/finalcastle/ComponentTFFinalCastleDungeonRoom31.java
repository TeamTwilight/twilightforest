package twilightforest.structures.finalcastle;

import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;
import twilightforest.TFFeature;
import twilightforest.block.TFBlocks;
import twilightforest.structures.StructureTFComponentOld;
import twilightforest.structures.lichtower.ComponentTFTowerWing;
import twilightforest.util.RotationUtil;

import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public class ComponentTFFinalCastleDungeonRoom31 extends ComponentTFTowerWing {

	public int level; // this is not serialized, since it's only used during build, which should be all one step

	public ComponentTFFinalCastleDungeonRoom31(TemplateManager manager, CompoundNBT nbt) {
		super(TFFinalCastlePieces.TFFCDunR31, nbt);
	}

	public ComponentTFFinalCastleDungeonRoom31(IStructurePieceType piece, CompoundNBT nbt) {
		super(piece, nbt);
	}

	//TODO: Parameter "rand" is unused. Remove?
	public ComponentTFFinalCastleDungeonRoom31(IStructurePieceType piece, TFFeature feature, Random rand, int i, int x, int y, int z, Direction direction, int level) {
		super(piece, feature, i);
		this.setCoordBaseMode(direction);
		this.spawnListIndex = 2; // dungeon monsters
		this.size = 31;
		this.height = 7;
		this.level = level;
		this.boundingBox = StructureTFComponentOld.getComponentToAddBoundingBox(x, y, z, -15, 0, -15, this.size - 1, this.height - 1, this.size - 1, Direction.SOUTH);
	}

	@Override
	public void buildComponent(StructurePiece parent, List<StructurePiece> list, Random rand) {
		if (parent instanceof StructureTFComponentOld) {
			this.deco = ((StructureTFComponentOld) parent).deco;
		}

		int mySpread = this.getComponentType() - parent.getComponentType();
		int maxSpread = (this.level == 1) ? 2 : 3;

		// add exit if we're far enough away and don't have one
		if (mySpread == maxSpread && !isExitBuildForLevel(parent)) {
			Rotation direction = RotationUtil.getRandomRotation(rand);
			for (int i = 0; i < 8 && !isExitBuildForLevel(parent); i++) {
				direction = direction.add(RotationUtil.ROTATIONS[i & 3]);
				if (this.addDungeonExit(parent, list, rand, direction)) {
					this.setExitBuiltForLevel(parent, true);
				}
			}
		}

		// add other rooms
		if (mySpread < maxSpread) {
			Rotation direction = RotationUtil.getRandomRotation(rand);
			for (int i = 0; i < 12; i++) {
				direction = direction.add(RotationUtil.ROTATIONS[i & 3]);
				this.addDungeonRoom(parent, list, rand, direction, this.level);
			}
		}
	}

	private boolean isExitBuildForLevel(StructurePiece parent) {
		if (parent instanceof ComponentTFFinalCastleDungeonEntrance) {
			return ((ComponentTFFinalCastleDungeonEntrance) parent).hasExit;
		}
		return false;
	}

	private void setExitBuiltForLevel(StructurePiece parent, boolean exit) {
		if (parent instanceof ComponentTFFinalCastleDungeonEntrance) {
			((ComponentTFFinalCastleDungeonEntrance) parent).hasExit = exit;
		}
	}

	protected boolean addDungeonRoom(StructurePiece parent, List<StructurePiece> list, Random rand, Rotation rotation, int level) {
		rotation = rotation.add(this.rotation);

		BlockPos rc = this.getNewRoomCoords(rand, rotation);

		ComponentTFFinalCastleDungeonRoom31 dRoom = new ComponentTFFinalCastleDungeonRoom31(TFFinalCastlePieces.TFFCDunR31, getFeatureType(), rand, this.componentType + 1, rc.getX(), rc.getY(), rc.getZ(), rotation.rotate(Direction.SOUTH), level);

		MutableBoundingBox largerBB = new MutableBoundingBox(dRoom.getBoundingBox());

		int expand = 0;
		largerBB.minX -= expand;
		largerBB.minZ -= expand;
		largerBB.maxX += expand;
		largerBB.maxZ += expand;

		StructurePiece intersect = StructureTFComponentOld.findIntersectingExcluding(list, largerBB, this);
		if (intersect == null) {
			list.add(dRoom);
			dRoom.buildComponent(parent, list, rand);
			return true;
		}
		return false;
	}

	//TODO: Parameter "parent" is unused. Remove?
	protected boolean addDungeonExit(StructurePiece parent, List<StructurePiece> list, Random rand, Rotation rotation) {

		//TODO: check if we are sufficiently near the castle center

		rotation = rotation.add(this.rotation);
		BlockPos rc = this.getNewRoomCoords(rand, rotation);
		ComponentTFFinalCastleDungeonExit dRoom = new ComponentTFFinalCastleDungeonExit(getFeatureType(), rand, this.componentType + 1, rc.getX(), rc.getY(), rc.getZ(), rotation.rotate(Direction.SOUTH), this.level);
		StructurePiece intersect = StructureTFComponentOld.findIntersectingExcluding(list, dRoom.getBoundingBox(), this);
		if (intersect == null) {
			list.add(dRoom);
			dRoom.buildComponent(this, list, rand);
			return true;
		}
		return false;
	}

	private BlockPos getNewRoomCoords(Random rand, Rotation rotation) {
		// make the rooms connect around the corners, not the centers
		int offset = rand.nextInt(15) - 9;
		if (rand.nextBoolean()) {
			offset += this.size;
		}

		switch (rotation) {
			default:
			case NONE:
				return new BlockPos(this.boundingBox.maxX + 9, this.boundingBox.minY, this.boundingBox.minZ + offset);
			case CLOCKWISE_90:
				return new BlockPos(this.boundingBox.minX + offset, this.boundingBox.minY, this.boundingBox.maxZ + 9);
			case CLOCKWISE_180:
				return new BlockPos(this.boundingBox.minX - 9, this.boundingBox.minY, this.boundingBox.minZ + offset);
			case COUNTERCLOCKWISE_90:
				return new BlockPos(this.boundingBox.minX + offset, this.boundingBox.minY, this.boundingBox.minZ - 9);
		}
	}

	@Override
	public boolean func_230383_a_(ISeedReader world, StructureManager manager, ChunkGenerator generator, Random rand, MutableBoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		if (this.isBoundingBoxOutsideBiomes(world, sbb, plateauBiomes)) {
			return false;
		}

		Random decoRNG = new Random(world.getSeed() + (this.boundingBox.minX * 321534781) ^ (this.boundingBox.minZ * 756839));

		this.fillWithAir(world, sbb, 0, 0, 0, this.size - 1, this.height - 1, this.size - 1, state -> state.getMaterial() == Material.ROCK);

		BlockState floor = TFBlocks.castle_brick.get().getDefaultState();
		BlockState border = TFBlocks.castle_brick_frame.get().getDefaultState();

		Predicate<BlockState> replacing = state -> {
			Material material = state.getMaterial();
			return material == Material.ROCK || material == Material.AIR;
		};

		final int cs = 7;

		this.fillWithBlocks(world, sbb, cs , -1, cs, this.size - 1 - cs, -1, this.size - 1 - cs, border, floor, replacing);
		this.fillWithBlocks(world, sbb, cs , this.height, cs, this.size - 1 - cs, this.height, this.size - 1 - cs, border, floor, replacing);

		BlockState forceField = getForceFieldColor(decoRNG);
		BlockState castleMagic = getRuneColor(forceField);

		for (Rotation rotation : RotationUtil.ROTATIONS) {

			this.fillBlocksRotated(world, sbb, cs, 0, cs + 1, cs, this.height - 1, this.size - 2 - cs, forceField, rotation);
			// verticals
			for (int z = cs; z < ((this.size - 1) - cs); z += 4) {

				this.fillBlocksRotated(world, sbb, cs, 0, z, cs, this.height - 1, z, castleMagic, rotation);
				// horizontals
				int y = ((z - cs) % 8 == 0) ? decoRNG.nextInt(3) + 0 : decoRNG.nextInt(3) + 4;
				this.fillBlocksRotated(world, sbb, cs, y, z + 1, cs, y, z + 3, castleMagic, rotation);
			}
		}

		return true;
	}

	protected static final Predicate<Biome> plateauBiomes = biome -> false; /* FIXME or remove
			biome == TFBiomes.highlandsCenter.get() || biome == TFBiomes.thornlands.get()*/;

	protected BlockState getRuneColor(BlockState forceFieldColor) {
		return forceFieldColor == TFBlocks.force_field_blue.get().getDefaultState() ? TFBlocks.castle_rune_brick_blue.get().getDefaultState() : TFBlocks.castle_rune_brick_yellow.get().getDefaultState();
	}

	protected BlockState getForceFieldColor(Random decoRNG) {
		int i = decoRNG.nextInt(2) + 3;

		if (i == 3)
			return TFBlocks.force_field_green.get().getDefaultState();
		else
			return TFBlocks.force_field_blue.get().getDefaultState();
	}
}
