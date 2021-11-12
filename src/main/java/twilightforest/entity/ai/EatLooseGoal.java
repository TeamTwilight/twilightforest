package twilightforest.entity.ai;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.DyeColor;
import twilightforest.entity.passive.QuestRam;

import java.util.EnumSet;
import java.util.List;

public class EatLooseGoal extends Goal {
	private final QuestRam temptedQuestRam;

	private int delayTemptCounter;
	private ItemEntity temptingItem;

	public EatLooseGoal(QuestRam entityTFQuestRam) {
		this.temptedQuestRam = entityTFQuestRam;
		setFlags(EnumSet.of(Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		if (this.delayTemptCounter > 0) {
			--this.delayTemptCounter;
			return false;
		} else {
			this.temptingItem = null;

			List<ItemEntity> nearbyItems = this.temptedQuestRam.level.getEntitiesOfClass(ItemEntity.class, this.temptedQuestRam.getBoundingBox().inflate(2.0D, 2.0D, 2.0D), e -> e.isAlive() && !e.getItem().isEmpty());

			for (ItemEntity itemNearby : nearbyItems) {
				DyeColor color = QuestRam.guessColor(itemNearby.getItem());
				if (color != null && !temptedQuestRam.isColorPresent(color)) {
					this.temptingItem = itemNearby;
					break;
				}
			}

			return temptingItem != null;
		}
	}

	@Override
	public void stop() {
		this.temptingItem = null;
		this.temptedQuestRam.getNavigation().stop();
		this.delayTemptCounter = 100;
	}

	@Override
	public void tick() {
		this.temptedQuestRam.getLookControl().setLookAt(this.temptingItem, 30.0F, this.temptedQuestRam.getMaxHeadXRot());

		if (this.temptedQuestRam.distanceToSqr(this.temptingItem) < 6.25D && temptedQuestRam.tryAccept(temptingItem.getItem())) {
			this.temptingItem.discard();
		}
	}

}
