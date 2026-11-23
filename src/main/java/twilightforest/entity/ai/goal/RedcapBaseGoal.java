package twilightforest.entity.ai.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import twilightforest.entity.monster.Redcap;

public abstract class RedcapBaseGoal extends Goal {

	protected final Redcap redcap;

	protected RedcapBaseGoal(Redcap entity) {
		this.redcap = entity;
	}

	/**
	 * Fairly straightforward.  Returns true in a 120 degree arc in front of the target's view.
	 */
	public boolean isTargetLookingAtMe(LivingEntity attackTarget) {
		// find angle of approach
		double dx = this.redcap.getX() - attackTarget.getX();
		double dz = this.redcap.getZ() - attackTarget.getZ();
		float angle = (float) ((Math.atan2(dz, dx) * 180D) / Math.PI) - 90F;

		float difference = Mth.abs((attackTarget.getYRot() - angle) % 360);

		return difference < 60 || difference > 300;
	}

	/**
	 * Finds a TNT block within {@code range} of the redcap. Runs from goal checks every couple of ticks, so instead of
	 * reading every block in the (2r+1)^3 box it skips whole chunk sections whose palette can't contain TNT, which
	 * is almost all of them. Unloaded chunks are skipped rather than loaded.
	 */
	@Nullable
	public BlockPos findBlockTNTNearby(int range) {
		Level level = this.redcap.level();
		BlockPos center = this.redcap.blockPosition();
		int minX = center.getX() - range, maxX = center.getX() + range;
		int minY = Math.max(center.getY() - range, level.getMinBuildHeight()), maxY = Math.min(center.getY() + range, level.getMaxBuildHeight() - 1);
		int minZ = center.getZ() - range, maxZ = center.getZ() + range;
		if (minY > maxY)
			return null;

		for (int cx = SectionPos.blockToSectionCoord(minX); cx <= SectionPos.blockToSectionCoord(maxX); cx++) {
			for (int cz = SectionPos.blockToSectionCoord(minZ); cz <= SectionPos.blockToSectionCoord(maxZ); cz++) {
				if (!(level.getChunk(cx, cz, ChunkStatus.FULL, false) instanceof LevelChunk chunk))
					continue;
				for (int sy = SectionPos.blockToSectionCoord(minY); sy <= SectionPos.blockToSectionCoord(maxY); sy++) {
					LevelChunkSection section = chunk.getSection(chunk.getSectionIndexFromSectionY(sy));
					if (section.hasOnlyAir() || !section.maybeHas(state -> state.is(Blocks.TNT)))
						continue;
					int x0 = Math.max(minX, SectionPos.sectionToBlockCoord(cx)), x1 = Math.min(maxX, SectionPos.sectionToBlockCoord(cx, 15));
					int y0 = Math.max(minY, SectionPos.sectionToBlockCoord(sy)), y1 = Math.min(maxY, SectionPos.sectionToBlockCoord(sy, 15));
					int z0 = Math.max(minZ, SectionPos.sectionToBlockCoord(cz)), z1 = Math.min(maxZ, SectionPos.sectionToBlockCoord(cz, 15));
					for (int x = x0; x <= x1; x++) {
						for (int y = y0; y <= y1; y++) {
							for (int z = z0; z <= z1; z++) {
								if (section.getBlockState(x & 15, y & 15, z & 15).is(Blocks.TNT))
									return new BlockPos(x, y, z);
							}
						}
					}
				}
			}
		}

		return null;
	}

	public boolean isLitTNTNearby(int range) {
		AABB expandedBox = this.redcap.getBoundingBox().inflate(range, range, range);
		return !this.redcap.level().getEntitiesOfClass(PrimedTnt.class, expandedBox).isEmpty();
	}
}
