package twilightforest.world.components.structures.mushroomtower;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.util.BoundingBoxUtils;
import twilightforest.world.components.structures.TFStructureComponentOld;


public class MushroomTowerBridgeComponent extends MushroomTowerWingComponent {

	final int dSize;
	final int dHeight;

	public MushroomTowerBridgeComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		this(TFStructurePieceTypes.TFMTBri.get(), nbt);
	}

	public MushroomTowerBridgeComponent(StructurePieceType piece, CompoundTag nbt) {
		super(piece, nbt);
		this.dSize = nbt.getInt("destSize");
		this.dHeight = nbt.getInt("destHeight");
	}

	protected MushroomTowerBridgeComponent(StructurePieceType piece, int i, int x, int y, int z, int pSize, int pHeight, Direction direction) {
		super(piece, i, x, y, z, pSize, pHeight, direction);

		this.boundingBox = BoundingBoxUtils.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, size - 1, height - 1, 3, direction, false);

		this.dSize = pSize;
		this.dHeight = pHeight;
	}

	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext ctx, CompoundTag tagCompound) {
		super.addAdditionalSaveData(ctx, tagCompound);
		tagCompound.putInt("destSize", this.dSize);
		tagCompound.putInt("destHeight", this.dHeight);
	}

	@Override
	public void addChildren(StructurePiece parent, StructurePieceAccessor list, RandomSource rand) {
		if (parent != null && parent instanceof TFStructureComponentOld) {
			this.deco = ((TFStructureComponentOld) parent).deco;
		}

		int[] dest = new int[]{dSize - 1, 1, 1};
		boolean madeWing = makeTowerWing(list, rand, this.getGenDepth(), dest[0], dest[1], dest[2], dSize, dHeight, Rotation.NONE);

		if (!madeWing) {
			int[] dx = offsetTowerCoords(dest[0], dest[1], dest[2], dSize, Direction.SOUTH);
			TwilightForestMod.LOGGER.info("Making tower wing failed when bridge was already made.  Size = {}, x = {}, z = {}", dSize, dx[0], dx[2]);
		}
	}

	public BoundingBox getWingBB() {
		int[] dest = offsetTowerCoords(dSize - 1, 1, 1, dSize, this.getOrientation());
		return BoundingBoxUtils.getComponentToAddBoundingBox(dest[0], dest[1], dest[2], 0, 0, 0, dSize - 1, dHeight - 1, dSize - 1, this.getOrientation(), false);
	}

	@Override
	public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {

		// make walls
		for (int x = 0; x < dSize; x++) {
			placeBlock(world, deco.fenceState, x, 1, 0, sbb);
			placeBlock(world, deco.fenceState, x, 1, 2, sbb);

			placeBlock(world, this.isAscender ? Blocks.JUNGLE_PLANKS.defaultBlockState() : deco.floorState, x, 0, 1, sbb);
		}

		// clear bridge walkway
		this.generateAirBox(world, sbb, 0, 1, 1, 2, 2, 1);
	}
}
