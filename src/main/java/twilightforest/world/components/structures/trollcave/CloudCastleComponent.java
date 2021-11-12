package twilightforest.world.components.structures.trollcave;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.MobSpawnType;
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
import twilightforest.entity.monster.ArmoredGiant;
import twilightforest.entity.monster.GiantMiner;
import twilightforest.entity.TFEntities;
import twilightforest.world.components.structures.TFStructureComponentOld;

import java.util.Random;

public class CloudCastleComponent extends TFStructureComponentOld {

	private boolean minerPlaced = false;
	private boolean warriorPlaced = false;

	public CloudCastleComponent(ServerLevel level, CompoundTag nbt) {
		super(TrollCavePieces.TFClCa, nbt);
		this.minerPlaced = nbt.getBoolean("minerPlaced");
		this.warriorPlaced = nbt.getBoolean("warriorPlaced");
	}

	public CloudCastleComponent(TFFeature feature, int index, int x, int y, int z) {
		super(TrollCavePieces.TFClCa, feature, index, x, y, z);
		this.setOrientation(Direction.SOUTH);

		// round to nearest mult of 4
		x &= ~0b11;
		y &= ~0b11;
		z &= ~0b11;

		// spawn list!
		this.spawnListIndex = 1;

		this.boundingBox = feature.getComponentToAddBoundingBox(x, y, z, -8, -4, -8, 64, 16, 64, Direction.SOUTH);
	}

	@Override
	protected void addAdditionalSaveData(ServerLevel level, CompoundTag tagCompound) {
		super.addAdditionalSaveData(level, tagCompound);
		tagCompound.putBoolean("minerPlaced", this.minerPlaced);
		tagCompound.putBoolean("warriorPlaced", this.warriorPlaced);
	}

	@Override
	public void addChildren(StructurePiece parent, StructurePieceAccessor list, Random rand) {
		// up to two trees
		// tree in x direction
		boolean plus = rand.nextBoolean();
		int offset = rand.nextInt(5) - rand.nextInt(5);
		CloudTreeComponent treeX = new CloudTreeComponent(getFeatureType(), this.getGenDepth() + 1, boundingBox.minX() + 8 + (plus ? 32 : -16), 168, boundingBox.minZ() + (offset * 4));
		list.addPiece(treeX);
		treeX.addChildren(this, list, rand);

		// tree in z direction
		plus = rand.nextBoolean();
		offset = rand.nextInt(5) - rand.nextInt(5);
		CloudTreeComponent treeZ = new CloudTreeComponent(getFeatureType(), this.getGenDepth() + 1, boundingBox.minX() + (offset * 4), 168, boundingBox.minZ() + 8 + (plus ? 32 : -16));
		list.addPiece(treeZ);
		treeZ.addChildren(this, list, rand);

	}

	@Override
	public boolean postProcess(WorldGenLevel world, StructureFeatureManager manager, ChunkGenerator generator, Random rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {

		// make haus
		this.generateBox(world, sbb, 8, 0, 8, 23, 3, 23, TFBlocks.FLUFFY_CLOUD.get().defaultBlockState(), TFBlocks.FLUFFY_CLOUD.get().defaultBlockState(), false);
		this.generateBox(world, sbb, 8, 4, 8, 23, 15, 23, TFBlocks.GIANT_COBBLESTONE.get().defaultBlockState(), TFBlocks.GIANT_COBBLESTONE.get().defaultBlockState(), false);
		this.generateBox(world, sbb, 8, 16, 8, 23, 19, 23, TFBlocks.GIANT_LOG.get().defaultBlockState(), TFBlocks.GIANT_LOG.get().defaultBlockState(), false);

		// clear inside
		this.generateAirBox(world, sbb, 12, 4, 12, 19, 15, 19);

		// clear door
		this.generateAirBox(world, sbb, 8, 4, 12, 12, 11, 15);

		// add giants
		if (!this.minerPlaced) {
			int bx = this.getWorldX(14, 14);
			int by = this.getWorldY(4);
			int bz = this.getWorldZ(14, 14);
			BlockPos pos = new BlockPos(bx, by, bz);

			if (sbb.isInside(pos)) {
				this.minerPlaced = true;

				GiantMiner miner = new GiantMiner(TFEntities.GIANT_MINER, world.getLevel());
				miner.setPos(bx, by, bz);
				miner.setPersistenceRequired();
				miner.finalizeSpawn(world, world.getCurrentDifficultyAt(pos), MobSpawnType.STRUCTURE, null, null);

				world.addFreshEntity(miner);
			}
		}
		if (!this.warriorPlaced) {
			int bx = this.getWorldX(17, 17);
			int by = this.getWorldY(4);
			int bz = this.getWorldZ(17, 17);
			BlockPos pos = new BlockPos(bx, by, bz);

			if (sbb.isInside(pos)) {
				this.warriorPlaced = true;

				ArmoredGiant warrior = new ArmoredGiant(TFEntities.ARMORED_GIANT, world.getLevel());
				warrior.setPos(bx, by, bz);
				warrior.setPersistenceRequired();
				warrior.finalizeSpawn(world, world.getCurrentDifficultyAt(pos), MobSpawnType.STRUCTURE, null, null);

				world.addFreshEntity(warrior);
			}
		}

		return true;
	}
}
