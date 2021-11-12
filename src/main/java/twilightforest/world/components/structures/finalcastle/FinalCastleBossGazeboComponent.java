package twilightforest.world.components.structures.finalcastle;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
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
import twilightforest.block.TFBlocks;
import twilightforest.world.components.structures.TFStructureComponentOld;
import twilightforest.util.RotationUtil;

import java.util.Random;

public class FinalCastleBossGazeboComponent extends TFStructureComponentOld {

	@SuppressWarnings("unused")
	public FinalCastleBossGazeboComponent(ServerLevel level, CompoundTag nbt) {
		super(FinalCastlePieces.TFFCBoGaz, nbt);
	}

	public FinalCastleBossGazeboComponent(TFFeature feature, Random rand, int i, TFStructureComponentOld keep, int x, int y, int z) {
		super(FinalCastlePieces.TFFCBoGaz, feature, i, x, y, z);
		this.spawnListIndex = -1; // no monsters

		this.setOrientation(keep.getOrientation());
		this.boundingBox = new BoundingBox(keep.getBoundingBox().minX() + 14, keep.getBoundingBox().maxY() + 2, keep.getBoundingBox().minZ() + 14, keep.getBoundingBox().maxX() - 14, keep.getBoundingBox().maxY() + 13, keep.getBoundingBox().maxZ() - 14);

	}

	@Override
	public void addChildren(StructurePiece parent, StructurePieceAccessor list, Random rand) {
		this.deco = new StructureTFDecoratorCastle();
		this.deco.blockState = TFBlocks.BLUE_CASTLE_RUNE_BRICK.get().defaultBlockState();

		this.deco.fenceState = TFBlocks.VIOLET_FORCE_FIELD.get().defaultBlockState();
	}

	@Override
	public boolean postProcess(WorldGenLevel world, StructureFeatureManager manager, ChunkGenerator generator, Random randomIn, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		// walls
		for (Rotation rotation : RotationUtil.ROTATIONS) {
			this.fillBlocksRotated(world, sbb, 0, 0, 0, 0, 10, 20, deco.fenceState, rotation);
		}

		// roof
		this.generateBox(world, sbb, 0, 11, 0, 20, 11, 20, deco.fenceState, deco.fenceState, false);

		//this.placeSignAtCurrentPosition(world, 10, 0, 10, sbb, "Final Boss Here", "You win!", "discord.gg/6v3z26B");

		setInvisibleTextEntity(world, 10, 0, 10, sbb, "Final Boss Here", true, 2.3f);
		setInvisibleTextEntity(world, 10, 0, 10, sbb, "You win!", true, 2.0f);
		setInvisibleTextEntity(world, 10, 0, 10, sbb, "You can join the Twilight Forest Discord server to follow",true, 1.0f);
		setInvisibleTextEntity(world, 10, 0, 10, sbb, "the latest updates on this castle and other content at:",true, 0.7f);
		setInvisibleTextEntity(world, 10, 0, 10, sbb, "discord.experiment115.com", true, 0.4f);

		//placeBlock(world, TFBlocks.boss_spawner_final_boss.get().defaultBlockState(), 10, 1, 10, sbb);

		return true;
	}
}
