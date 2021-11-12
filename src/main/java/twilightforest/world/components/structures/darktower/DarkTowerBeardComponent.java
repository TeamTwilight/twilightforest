package twilightforest.world.components.structures.darktower;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.StructureFeatureManager;
import twilightforest.world.registration.TFFeature;
import twilightforest.block.TFBlocks;
import twilightforest.world.components.structures.TFStructureComponentOld;
import twilightforest.world.components.structures.lichtower.TowerWingComponent;

import java.util.Random;

public class DarkTowerBeardComponent extends TFStructureComponentOld {

	protected int size;
	protected int height;

	public DarkTowerBeardComponent(ServerLevel level, CompoundTag nbt) {
		super(DarkTowerPieces.TFDTBea, nbt);
		this.size = nbt.getInt("beardSize");
		this.height = nbt.getInt("beardHeight");
	}

	public DarkTowerBeardComponent(TFFeature feature, int i, TowerWingComponent wing, int x, int y, int z) {
		super(DarkTowerPieces.TFDTBea, feature, i, x, y, z);

		this.setOrientation(wing.getOrientation());
		this.size = wing.size;
		this.height = size / 2;

		// just hang out at the very bottom of the tower
		this.boundingBox = new BoundingBox(wing.getBoundingBox().minX(), wing.getBoundingBox().minY() - this.height, wing.getBoundingBox().minZ(), wing.getBoundingBox().maxX(), wing.getBoundingBox().maxY(), wing.getBoundingBox().maxZ());
	}

	@Override
	protected void addAdditionalSaveData(ServerLevel level, CompoundTag tagCompound) {
		super.addAdditionalSaveData(level, tagCompound);

		tagCompound.putInt("beardSize", this.size);
		tagCompound.putInt("beardHeight", this.height);
	}

	/**
	 * Makes a dark tower type beard
	 */
	@Override
	public boolean postProcess(WorldGenLevel world, StructureFeatureManager manager, ChunkGenerator generator, Random rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		makeDarkBeard(world, sbb, 0, 0, size - 1, height - 1, size - 1);

		return true;
	}

	protected void makeDarkBeard(WorldGenLevel world, BoundingBox sbb, int minX, int minZ, int maxX, int maxY, int maxZ) {
		BlockState frameState = TFBlocks.ENCASED_TOWERWOOD.get().defaultBlockState();

		for (int x = minX; x <= maxX; x++) {
			for (int z = minZ; z <= maxZ; z++) {
				if (x == minX || x == maxX || z == minZ || z == maxZ) {
					int length = Math.min(Math.abs(x - height) - 1, Math.abs(z - height) - 1);

					if (length == height - 1) {
						length++;
					}

					if (length == -1) {
						length = 1;
					}

					for (int y = maxY; y >= height - length; y--) {
						// wall
						this.placeBlock(world, frameState, x, y, z, sbb);
					}
				}
			}
		}
	}
}
