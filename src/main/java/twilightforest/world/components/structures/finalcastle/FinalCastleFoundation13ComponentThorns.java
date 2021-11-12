package twilightforest.world.components.structures.finalcastle;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.StructureFeatureManager;
import twilightforest.world.registration.TFFeature;
import twilightforest.block.TFBlocks;
import twilightforest.world.components.structures.TFStructureComponentOld;
import twilightforest.util.RotationUtil;

import java.util.Random;

/**
 * Foundation that makes thorns go all through the tower
 *
 * @author benma_000
 */
public class FinalCastleFoundation13ComponentThorns extends FinalCastleFoundation13Component {

	public FinalCastleFoundation13ComponentThorns(ServerLevel level, CompoundTag nbt) {
		super(FinalCastlePieces.TFFCFTh21, nbt);
	}

	public FinalCastleFoundation13ComponentThorns(TFFeature feature, Random rand, int i, TFStructureComponentOld sideTower, int x, int y, int z) {
		super(FinalCastlePieces.TFFCFTh21, feature, rand, i, sideTower, x, y, z);

		this.boundingBox = new BoundingBox(sideTower.getBoundingBox().minX() - 5, sideTower.getBoundingBox().maxY() - 1, sideTower.getBoundingBox().minZ() - 5, sideTower.getBoundingBox().maxX() + 5, sideTower.getBoundingBox().maxY(), sideTower.getBoundingBox().maxZ() + 5);
	}

	@Override
	public boolean postProcess(WorldGenLevel world, StructureFeatureManager manager, ChunkGenerator generator, Random rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		// thorns
		Random decoRNG = new Random(world.getSeed() + (this.boundingBox.minX() * 321534781L) ^ (this.boundingBox.minZ() * 756839L));

		for (Rotation i : RotationUtil.ROTATIONS) {
			this.makeThornVine(world, decoRNG, i, sbb);
		}

		return true;
	}

	private void makeThornVine(WorldGenLevel world, Random decoRNG, Rotation rotation, BoundingBox sbb) {

		int x = 3 + decoRNG.nextInt(13);
		int z = 3 + decoRNG.nextInt(13);

		int y = this.boundingBox.getYSpan() + 5;

		int twist = decoRNG.nextInt(4);
		int twistMod = 3 + decoRNG.nextInt(3);

		final BlockState thorns = TFBlocks.BROWN_THORNS.get().defaultBlockState();

		while (this.getBlockStateFromPosRotated(world, x, y, z, sbb, rotation).getBlock() != TFBlocks.DEADROCK.get() && this.getWorldY(y) > 60) {
			this.setBlockStateRotated(world, thorns, x, y, z, rotation, sbb);
			// twist vines around the center block
			switch (twist) {
				case 0:
					this.setBlockStateRotated(world, thorns, x + 1, y, z, rotation, sbb);
					this.setBlockStateRotated(world, thorns, x, y, z + 1, rotation, sbb);
					this.setBlockStateRotated(world, thorns, x + 1, y, z + 1, rotation, sbb);
					break;
				case 1:
					this.setBlockStateRotated(world, thorns, x + 1, y, z, rotation, sbb);
					this.setBlockStateRotated(world, thorns, x, y, z - 1, rotation, sbb);
					this.setBlockStateRotated(world, thorns, x + 1, y, z - 1, rotation, sbb);
					break;
				case 2:
					this.setBlockStateRotated(world, thorns, x - 1, y, z, rotation, sbb);
					this.setBlockStateRotated(world, thorns, x, y, z - 1, rotation, sbb);
					this.setBlockStateRotated(world, thorns, x - 1, y, z - 1, rotation, sbb);
					break;
				case 3:
					this.setBlockStateRotated(world, thorns, x - 1, y, z, rotation, sbb);
					this.setBlockStateRotated(world, thorns, x, y, z + 1, rotation, sbb);
					this.setBlockStateRotated(world, thorns, x - 1, y, z + 1, rotation, sbb);
					break;
			}

			if (Math.abs(y % twistMod) == 1) {
				// make branch
				this.makeThornBranch(world, x, y, z, rotation, sbb);
			}

			// twist randomly
			if (y % twistMod == 0) {
				twist++;
				twist = twist % 4;
			}
			y--;
		}
	}

	private void makeThornBranch(WorldGenLevel world, int x, int y, int z, Rotation rotation, BoundingBox sbb) {
		Random rand = new Random(world.getSeed() + (x * 321534781) ^ (y * 756839) + z);

		// pick a direction
		Rotation dir = RotationUtil.getRandomRotation(rand);

		// initialize direction variables
		int dx = 0;
		int dz = 0;

		switch (dir) {
			case NONE:
				dx = +1;
				break;
			case CLOCKWISE_90:
				dz = +1;
				break;
			case CLOCKWISE_180:
				dx = -1;
				break;
			case COUNTERCLOCKWISE_90:
				dz = -1;
				break;
		}

		// how far do we branch?
		int dist = 2 + rand.nextInt(3);

		// check to make sure there's room
		int destX = x + (dist * dx);
		int destZ = z + (dist * dz);

		if (destX > 0 && destX < this.boundingBox.getXSpan() && destZ > 0 && destZ < this.boundingBox.getZSpan()) {
			for (int i = 0; i < dist; i++) {
				// go out that far
				final Rotation add = dir.getRotated(rotation).getRotated(this.rotation);
				BlockState thorns = TFBlocks.GREEN_THORNS.get().defaultBlockState()
						.setValue(
								RotatedPillarBlock.AXIS,
								add == Rotation.NONE || add == Rotation.CLOCKWISE_180 ? Direction.Axis.X : Direction.Axis.Z
						);
				if (i > 0) {
					this.setBlockStateRotated(world, thorns, x + (dx * i), y, z + (dz * i), rotation, sbb);
				}
				// go up that far
				this.setBlockStateRotated(world, thorns.setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y), destX, y + i, destZ, rotation, sbb);
				// go back half that far
				if (i > (dist / 2)) {
					this.setBlockStateRotated(world, thorns, x + (dx * i), y + dist - 1, z + (dz * i), rotation, sbb);
				}
			}
		}
	}
}
