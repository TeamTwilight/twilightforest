package twilightforest.world.components.structures.finalcastle;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import twilightforest.init.TFBiomes;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.util.BoundingBoxUtils;
import twilightforest.util.RotationUtil;
import twilightforest.world.components.structures.TFStructureComponentOld;
import twilightforest.world.components.structures.lichtower.TowerWingComponent;

import java.util.function.Predicate;

public class FinalCastleDungeonRoom31Component extends TowerWingComponent {

	public int level; // this is not serialized, since it's only used during build, which should be all one step

	public FinalCastleDungeonRoom31Component(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		this(TFStructurePieceTypes.TFFCDunR31.get(), nbt);
	}

	public FinalCastleDungeonRoom31Component(StructurePieceType piece, CompoundTag nbt) {
		super(piece, nbt);
	}

	public FinalCastleDungeonRoom31Component(StructurePieceType piece, int i, int x, int y, int z, Direction direction, int level) {
		super(piece, i, x, y, z);
		this.setOrientation(direction);
		this.spawnListIndex = 2; // dungeon monsters
		this.size = 31;
		this.height = 7;
		this.level = level;
		this.boundingBox = BoundingBoxUtils.getComponentToAddBoundingBox(x, y, z, -15, 0, -15, this.size - 1, this.height - 1, this.size - 1, Direction.SOUTH, false);
	}

	@Override
	public void addChildren(StructurePiece parent, StructurePieceAccessor list, RandomSource rand) {
		if (parent instanceof TFStructureComponentOld) {
			this.deco = ((TFStructureComponentOld) parent).deco;
		}

		int mySpread = this.getGenDepth() - parent.getGenDepth();
		int maxSpread = (this.level == 1) ? 2 : 3;

		// add exit if we're far enough away and don't have one
		if (mySpread == maxSpread && !isExitBuildForLevel(parent)) {
			Rotation direction = RotationUtil.getRandomRotation(rand);
			for (int i = 0; i < 8 && !isExitBuildForLevel(parent); i++) {
				direction = direction.getRotated(RotationUtil.ROTATIONS[i & 3]);
				if (this.addDungeonExit(list, rand, direction)) {
					this.setExitBuiltForLevel(parent, true);
				}
			}
		}

		// add other rooms
		if (mySpread < maxSpread) {
			Rotation direction = RotationUtil.getRandomRotation(rand);
			for (int i = 0; i < 12; i++) {
				direction = direction.getRotated(RotationUtil.ROTATIONS[i & 3]);
				this.addDungeonRoom(parent, list, rand, direction, this.level);
			}
		}
	}

	private boolean isExitBuildForLevel(StructurePiece parent) {
		if (parent instanceof FinalCastleDungeonEntranceComponent) {
			return ((FinalCastleDungeonEntranceComponent) parent).hasExit;
		}
		return false;
	}

	private void setExitBuiltForLevel(StructurePiece parent, boolean exit) {
		if (parent instanceof FinalCastleDungeonEntranceComponent) {
			((FinalCastleDungeonEntranceComponent) parent).hasExit = exit;
		}
	}

	protected boolean addDungeonRoom(StructurePiece parent, StructurePieceAccessor list, RandomSource rand, Rotation rotation, int level) {
		rotation = rotation.getRotated(this.rotation);

		BlockPos rc = this.getNewRoomCoords(rand, rotation);

		FinalCastleDungeonRoom31Component dRoom = new FinalCastleDungeonRoom31Component(TFStructurePieceTypes.TFFCDunR31.get(), this.genDepth + 1, rc.getX(), rc.getY(), rc.getZ(), rotation.rotate(Direction.SOUTH), level);

		BoundingBox largerBB = BoundingBoxUtils.clone(dRoom.getBoundingBox());

		if (list instanceof StructurePiecesBuilder start) {
			StructurePiece intersect = TFStructureComponentOld.findIntersectingExcluding(start.pieces, largerBB, this);
			if (intersect == null) {
				list.addPiece(dRoom);
				dRoom.addChildren(parent, list, rand);
				return true;
			}
		}
		return false;
	}

	protected boolean addDungeonExit(StructurePieceAccessor list, RandomSource rand, Rotation rotation) {

		//TODO: check if we are sufficiently near the castle center

		rotation = rotation.getRotated(this.rotation);
		BlockPos rc = this.getNewRoomCoords(rand, rotation);
		FinalCastleDungeonExitComponent dRoom = new FinalCastleDungeonExitComponent(this.genDepth + 1, rc.getX(), rc.getY(), rc.getZ(), rotation.rotate(Direction.SOUTH), this.level);
		if (list instanceof StructurePiecesBuilder start) {
			StructurePiece intersect = TFStructureComponentOld.findIntersectingExcluding(start.pieces, dRoom.getBoundingBox(), this);
			if (intersect == null) {
				list.addPiece(dRoom);
				dRoom.addChildren(this, list, rand);
				return true;
			}
		}
		return false;
	}

	private BlockPos getNewRoomCoords(RandomSource rand, Rotation rotation) {
		// make the rooms connect around the corners, not the centers
		int offset = rand.nextInt(15) - 9;
		if (rand.nextBoolean()) {
			offset += this.size;
		}

		return switch (rotation) {
			case CLOCKWISE_90 -> new BlockPos(this.boundingBox.minX() + offset, this.boundingBox.minY(), this.boundingBox.maxZ() + 9);
			case CLOCKWISE_180 -> new BlockPos(this.boundingBox.minX() - 9, this.boundingBox.minY(), this.boundingBox.minZ() + offset);
			case COUNTERCLOCKWISE_90 -> new BlockPos(this.boundingBox.minX() + offset, this.boundingBox.minY(), this.boundingBox.minZ() - 9);
			default -> new BlockPos(this.boundingBox.maxX() + 9, this.boundingBox.minY(), this.boundingBox.minZ() + offset);
		};
	}

	@Override
	public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		Predicate<Biome> plateauBiomes = biome ->
			biome == world.registryAccess().lookupOrThrow(Registries.BIOME).getValue(TFBiomes.THORNLANDS) ||
				biome == world.registryAccess().lookupOrThrow(Registries.BIOME).getValue(TFBiomes.FINAL_PLATEAU);

		if (this.isBoundingBoxOutsideBiomes(world, plateauBiomes, blockPos)) {
			return;
		}

		RandomSource decoRNG = RandomSource.create(world.getSeed() + (this.boundingBox.minX() * 321534781L) ^ (this.boundingBox.minZ() * 756839L));

		//TODO add a deadrock tag, or a tag for castle replaceables
		Predicate<BlockState> replacing = state -> state.isAir() || state.is(TFBlocks.DEADROCK) || state.is(TFBlocks.CRACKED_DEADROCK) || state.is(TFBlocks.WEATHERED_DEADROCK);


		this.fillWithAir(world, sbb, 0, 0, 0, this.size - 1, this.height - 1, this.size - 1, replacing);

		BlockState floor = TFBlocks.CASTLE_BRICK.get().defaultBlockState();
		BlockState border = TFBlocks.THICK_CASTLE_BRICK.get().defaultBlockState();

		final int cs = 7;

		this.fillWithBlocks(world, sbb, cs, -1, cs, this.size - 1 - cs, -1, this.size - 1 - cs, border, floor, replacing);
		this.fillWithBlocks(world, sbb, cs, this.height, cs, this.size - 1 - cs, this.height, this.size - 1 - cs, border, floor, replacing);

		BlockState forceField = getForceFieldColor(decoRNG);
		BlockState castleMagic = getRuneColor(forceField);

		for (Rotation rotation : RotationUtil.ROTATIONS) {

			this.fillBlocksRotated(world, sbb, cs, 0, cs + 1, cs, this.height - 1, this.size - 2 - cs, forceField, rotation);
			// verticals
			for (int z = cs; z < ((this.size - 1) - cs); z += 4) {

				this.fillBlocksRotated(world, sbb, cs, 0, z, cs, this.height - 1, z, castleMagic, rotation);
				// horizontals
				int y = ((z - cs) % 8 == 0) ? decoRNG.nextInt(3) : decoRNG.nextInt(3) + 4;
				this.fillBlocksRotated(world, sbb, cs, y, z + 1, cs, y, z + 3, castleMagic, rotation);
			}
		}
	}

	protected BlockState getRuneColor(BlockState forceFieldColor) {
		return forceFieldColor.is(TFBlocks.GREEN_FORCE_FIELD) ? TFBlocks.YELLOW_CASTLE_RUNE_BRICK.get().defaultBlockState() : TFBlocks.BLUE_CASTLE_RUNE_BRICK.get().defaultBlockState();
	}

	protected BlockState getForceFieldColor(RandomSource decoRNG) {
		int i = decoRNG.nextInt(2) + 3;

		if (i == 3)
			return TFBlocks.GREEN_FORCE_FIELD.get().defaultBlockState();
		else
			return TFBlocks.BLUE_FORCE_FIELD.get().defaultBlockState();
	}

	@Override
	public TerrainAdjustment getTerrainAdjustment() {
		return TerrainAdjustment.BURY;
	}

	@Override
	public int getGroundLevelDelta() {
		return 0;
	}
}
