package twilightforest.structures.trollcave;

import net.minecraft.entity.SpawnReason;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
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
import twilightforest.entity.EntityTFArmoredGiant;
import twilightforest.entity.EntityTFGiantMiner;
import twilightforest.entity.TFEntities;
import twilightforest.structures.StructureTFComponentOld;

import java.util.List;
import java.util.Random;

public class ComponentTFCloudCastle extends StructureTFComponentOld {

	private boolean minerPlaced = false;
	private boolean warriorPlaced = false;

	public ComponentTFCloudCastle(TemplateManager manager, CompoundNBT nbt) {
		super(TFTrollCavePieces.TFClCa, nbt);
		this.minerPlaced = nbt.getBoolean("minerPlaced");
		this.warriorPlaced = nbt.getBoolean("warriorPlaced");
	}

	public ComponentTFCloudCastle(TFFeature feature, int index, int x, int y, int z) {
		super(TFTrollCavePieces.TFClCa, feature, index);
		this.setCoordBaseMode(Direction.SOUTH);

		// round to nearest mult of 4
		x &= ~0b11;
		y &= ~0b11;
		z &= ~0b11;

		// spawn list!
		this.spawnListIndex = 1;

		this.boundingBox = feature.getComponentToAddBoundingBox(x, y, z, -8, -4, -8, 64, 16, 64, Direction.SOUTH);
	}

	@Override
	protected void readAdditional(CompoundNBT tagCompound) {
		super.readAdditional(tagCompound);
		tagCompound.putBoolean("minerPlaced", this.minerPlaced);
		tagCompound.putBoolean("warriorPlaced", this.warriorPlaced);
	}

	@Override
	public void buildComponent(StructurePiece parent, List<StructurePiece> list, Random rand) {
		// up to two trees
		// tree in x direction
		boolean plus = rand.nextBoolean();
		int offset = rand.nextInt(5) - rand.nextInt(5);
		ComponentTFCloudTree treeX = new ComponentTFCloudTree(getFeatureType(), this.getComponentType() + 1, boundingBox.minX + 8 + (plus ? 32 : -16), 168, boundingBox.minZ + (offset * 4));
		list.add(treeX);
		treeX.buildComponent(this, list, rand);

		// tree in z direction
		plus = rand.nextBoolean();
		offset = rand.nextInt(5) - rand.nextInt(5);
		ComponentTFCloudTree treeZ = new ComponentTFCloudTree(getFeatureType(), this.getComponentType() + 1, boundingBox.minX + (offset * 4), 168, boundingBox.minZ + 8 + (plus ? 32 : -16));
		list.add(treeZ);
		treeZ.buildComponent(this, list, rand);

	}

	@Override
	public boolean func_230383_a_(ISeedReader world, StructureManager manager, ChunkGenerator generator, Random rand, MutableBoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {

		// make haus
		this.fillWithBlocks(world, sbb, 8, 0, 8, 23, 3, 23, TFBlocks.fluffy_cloud.get().getDefaultState(), TFBlocks.fluffy_cloud.get().getDefaultState(), false);
		this.fillWithBlocks(world, sbb, 8, 4, 8, 23, 15, 23, TFBlocks.giant_cobblestone.get().getDefaultState(), TFBlocks.giant_cobblestone.get().getDefaultState(), false);
		this.fillWithBlocks(world, sbb, 8, 16, 8, 23, 19, 23, TFBlocks.giant_log.get().getDefaultState(), TFBlocks.giant_log.get().getDefaultState(), false);

		// clear inside
		this.fillWithAir(world, sbb, 12, 4, 12, 19, 15, 19);

		// clear door
		this.fillWithAir(world, sbb, 8, 4, 12, 12, 11, 15);

		// add giants
		if (!this.minerPlaced) {
			int bx = this.getXWithOffset(14, 14);
			int by = this.getYWithOffset(4);
			int bz = this.getZWithOffset(14, 14);
			BlockPos pos = new BlockPos(bx, by, bz);

			if (sbb.isVecInside(pos)) {
				this.minerPlaced = true;

				EntityTFGiantMiner miner = new EntityTFGiantMiner(TFEntities.giant_miner, world.getWorld());
				miner.setPosition(bx, by, bz);
				miner.enablePersistence();
				miner.onInitialSpawn(world, world.getDifficultyForLocation(pos), SpawnReason.STRUCTURE, null, null);

				world.addEntity(miner);
			}
		}
		if (!this.warriorPlaced) {
			int bx = this.getXWithOffset(17, 17);
			int by = this.getYWithOffset(4);
			int bz = this.getZWithOffset(17, 17);
			BlockPos pos = new BlockPos(bx, by, bz);

			if (sbb.isVecInside(pos)) {
				this.warriorPlaced = true;

				EntityTFArmoredGiant warrior = new EntityTFArmoredGiant(TFEntities.armored_giant, world.getWorld());
				warrior.setPosition(bx, by, bz);
				warrior.enablePersistence();
				warrior.onInitialSpawn(world, world.getDifficultyForLocation(pos), SpawnReason.STRUCTURE, null, null);

				world.addEntity(warrior);
			}
		}

		return true;
	}
}
