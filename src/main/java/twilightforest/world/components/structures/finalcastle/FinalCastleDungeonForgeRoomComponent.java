package twilightforest.world.components.structures.finalcastle;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import twilightforest.world.components.structures.TFStructureComponentOld;
import twilightforest.world.registration.TFFeature;



public class FinalCastleDungeonForgeRoomComponent extends TFStructureComponentOld {

	public FinalCastleDungeonForgeRoomComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(FinalCastlePieces.TFFCDunBoR, nbt);
	}

	public FinalCastleDungeonForgeRoomComponent(TFFeature feature, RandomSource rand, int i, int x, int y, int z, Direction direction) {
		super(FinalCastlePieces.TFFCDunBoR, feature, i, x, y, z);
		this.spawnListIndex = 3; // forge monsters
		this.setOrientation(direction);
		this.boundingBox = TFStructureComponentOld.getComponentToAddBoundingBox2(x, y, z, -15, 0, -15, 50, 30, 50, direction);
	}

	@Override
	public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		this.generateAirBox(world, sbb, 0, 0, 0, 50, 30, 50);

		// sign
		this.placeSignAtCurrentPosition(world, 25, 0, 25, "Mini-boss 2", "Gives talisman", sbb);
	}
}
