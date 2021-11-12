package twilightforest.entity.ai;

import net.minecraft.world.entity.ai.goal.Goal;
import twilightforest.entity.passive.TinyBird;

import java.util.EnumSet;

public class TinyBirdFlyGoal extends Goal {

	private TinyBird entity;

	public TinyBirdFlyGoal(TinyBird bird) {
		this.entity = bird;
		this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP));
	}

	@Override
	public boolean canUse() {
		return !entity.isBirdLanded();
	}
}
