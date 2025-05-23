package twilightforest.entity.ai.goal;

import net.minecraft.world.entity.ai.goal.Goal;
import twilightforest.entity.boss.AlphaYeti;
import twilightforest.init.TFSounds;

import java.util.EnumSet;

public class YetiTiredGoal extends Goal {

	private final AlphaYeti yeti;
	private final int tiredDuration;
	private int tiredTimer;

	public YetiTiredGoal(AlphaYeti alpha, int i) {
		this.yeti = alpha;
		this.tiredDuration = i;
		this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP));
	}

	@Override
	public boolean canUse() {
		return this.yeti.isTired();
	}

	@Override
	public boolean canContinueToUse() {
		return this.tiredTimer < this.tiredDuration;
	}

	@Override
	public boolean isInterruptable() {
		return false;
	}

	@Override
	public void start() {
		this.tiredTimer = 0;
	}

	@Override
	public void stop() {
		this.tiredTimer = 0;
		this.yeti.setTired(false);
	}

	@Override
	public void tick() {
		if (++this.tiredTimer % 10 == 0)
			this.yeti.playSound(TFSounds.ALPHA_YETI_PANT.get(), 4F, 0.5F + this.yeti.getRandom().nextFloat() * 0.5F);
	}
}
