package twilightforest.world.components.structures.finalcastle;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import twilightforest.world.registration.TFFeature;
import twilightforest.block.TFBlocks;
import twilightforest.world.components.structures.TFStructureComponentOld;
import twilightforest.world.components.structures.lichtower.TowerWingComponent;

import java.util.Random;

public class FinalCastleLargeTowerComponent extends TowerWingComponent {

	public FinalCastleLargeTowerComponent(ServerLevel level, CompoundTag nbt) {
		super(FinalCastlePieces.TFFCLaTo, nbt);
	}

	public FinalCastleLargeTowerComponent(TFFeature feature, Random rand, int i, int x, int y, int z, Direction rotation) {
		super(FinalCastlePieces.TFFCLaTo, feature, i, x, y, z);
		this.setOrientation(rotation);
		this.size = 13;
		this.height = 61;
		this.boundingBox = feature.getComponentToAddBoundingBox(x, y, z, -6, 0, -6, 12, 60, 12, Direction.SOUTH);

	}

	@Override
	public void addChildren(StructurePiece parent, StructurePieceAccessor list, Random rand) {
		if (parent != null && parent instanceof TFStructureComponentOld) {
			this.deco = ((TFStructureComponentOld) parent).deco;
		}
		// add crown
		FinalCastleRoof9CrenellatedComponent roof = new FinalCastleRoof9CrenellatedComponent(getFeatureType(), rand, 4, this, getLocatorPosition().getX(), getLocatorPosition().getY(), getLocatorPosition().getZ());
		list.addPiece(roof);
		roof.addChildren(this, list, rand);
	}

	@Override
	public boolean postProcess(WorldGenLevel world, StructureFeatureManager manager, ChunkGenerator generator, Random rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		Random decoRNG = new Random(world.getSeed() + (this.boundingBox.minX() * 321534781L) ^ (this.boundingBox.minZ() * 756839L));

		generateBox(world, sbb, 0, 0, 0, 12, 59, 12, false, rand, deco.randomBlocks);

		// add branching runes
		int numBranches = 6 + decoRNG.nextInt(4);
		for (int i = 0; i < numBranches; i++) {
			makeGlyphBranches(world, decoRNG, this.getGlyphMeta(), sbb);
		}

		// beard
		for (int i = 1; i < 4; i++) {
			generateBox(world, sbb, i, -(i * 2), i, 8 - i, 1 - (i * 2), 8 - i, false, rand, deco.randomBlocks);
		}
		this.placeBlock(world, deco.blockState, 4, -7, 4, sbb);

		// door, first floor
		final BlockState castleDoor = TFBlocks.PINK_CASTLE_DOOR.get().defaultBlockState(); //this.getGlyphMeta()?
		this.generateBox(world, sbb, 0, 1, 1, 0, 4, 3, castleDoor, AIR, false);

		this.placeSignAtCurrentPosition(world, 6, 1, 6, "Parkour area 1", "Unique monster?", sbb);

		return true;
	}

	public BlockState getGlyphMeta() {
		return TFBlocks.PINK_CASTLE_RUNE_BRICK.get().defaultBlockState();
	}
}
