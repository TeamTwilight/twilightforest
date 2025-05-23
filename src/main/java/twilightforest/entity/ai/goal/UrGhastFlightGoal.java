package twilightforest.entity.ai.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import twilightforest.entity.boss.UrGhast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

public class UrGhastFlightGoal extends Goal {

	//private static final int CRUISING_ALTITUDE = 235; // absolute cruising altitude
	private static final int HOVER_ALTITUDE = 20; // how far, relatively, do we hover over ghast traps?
	private final UrGhast ghast;

	private List<BlockPos> pointsToVisit = new ArrayList<>();
	private int currentPoint = 0;

	public UrGhastFlightGoal(UrGhast ghast) {
		this.ghast = ghast;
		setFlags(EnumSet.of(Flag.MOVE));
	}

	// [VanillaCopy] Ghast.RandomFloatAroundGoal
	@Override
	public boolean canUse() {
		MoveControl entitymovehelper = this.ghast.getMoveControl();
		this.pointsToVisit = this.createPath();
		if (this.pointsToVisit.isEmpty()) return false;

		if (!entitymovehelper.hasWanted()) {
			return true;
		} else {
			double d0 = entitymovehelper.getWantedX() - this.ghast.getX();
			double d1 = entitymovehelper.getWantedY() - this.ghast.getY();
			double d2 = entitymovehelper.getWantedZ() - this.ghast.getZ();
			double d3 = d0 * d0 + d1 * d1 + d2 * d2;
			return d3 < 1.0D || d3 > 3600.0D;
		}
	}

	@Override
	public boolean canContinueToUse() {
		return false;
	}

	@Override
	public void start() {

		if (this.pointsToVisit.isEmpty()) {
			this.pointsToVisit.addAll(this.createPath());
		} else {
			if (this.currentPoint >= this.pointsToVisit.size()) {
				this.currentPoint = 0;

				// when we're in tantrum mode, this is a good time to check if we need to spawn more ghasts
				if (!this.ghast.checkGhastsAtTraps()) {
					this.ghast.spawnGhastsAtTraps((ServerLevel) this.ghast.level());
				}
			}

			// TODO reintroduce wanderFactor somehow? Would need to change move helper or add extra fields here

			double x = this.pointsToVisit.get(this.currentPoint).getX();
			double y = this.pointsToVisit.get(this.currentPoint).getY() + (this.ghast.getRestrictionPoint() != null ? HOVER_ALTITUDE : 0);
			double z = this.pointsToVisit.get(this.currentPoint).getZ();
			this.ghast.getMoveControl().setWantedPosition(x, y, z, 1.0F);
			this.currentPoint++;

			// we have reached cruising altitude, time to turn noClip off
			this.ghast.noPhysics = false;
		}
	}

	private List<BlockPos> createPath() {
		List<BlockPos> potentialPoints = new ArrayList<>();
		BlockPos pos = new BlockPos(this.ghast.getLogicalScanPoint());

		if (!this.ghast.getTrapLocations().isEmpty()) {
			// make a copy of the trap locations list
			potentialPoints.addAll(this.ghast.getTrapLocations());
		} else {
			potentialPoints.add(pos.offset(20, 0, 0));
			potentialPoints.add(pos.offset(0, 0, -20));
			potentialPoints.add(pos.offset(-20, 0, 0));
			potentialPoints.add(pos.offset(0, 0, 20));
		}

		Collections.shuffle(potentialPoints);

		if (this.ghast.getTrapLocations().isEmpty()) {
			// if in no trap mode, head back to the middle when we're done
			potentialPoints.add(pos);
		}

		return potentialPoints;
	}
}