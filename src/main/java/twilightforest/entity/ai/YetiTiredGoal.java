package twilightforest.entity.ai;

import net.minecraft.world.entity.ai.goal.Goal;
import twilightforest.TFSounds;
import twilightforest.entity.boss.AlphaYeti;

import java.util.EnumSet;

public class YetiTiredGoal extends Goal {

	private AlphaYeti yeti;
	private int tiredDuration;
	private int tiredTimer;

	public YetiTiredGoal(AlphaYeti entityTFYetiAlpha, int i) {
		this.yeti = entityTFYetiAlpha;
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
		if(++this.tiredTimer % 10 == 0)
			this.yeti.playSound(TFSounds.ALPHAYETI_PANT, 4F, 0.5F + yeti.getRandom().nextFloat() * 0.5F);
	}
}
