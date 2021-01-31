package twilightforest.entity.ai;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.crafting.Ingredient;
import twilightforest.entity.passive.EntityTFQuestRam;

import java.util.EnumSet;
import java.util.List;

public class EntityAITFEatLoose extends Goal {
	private final EntityTFQuestRam temptedQuestRam;
	private final Ingredient target;

	private int delayTemptCounter;
	private ItemEntity temptingItem;

	public EntityAITFEatLoose(EntityTFQuestRam entityTFQuestRam, Ingredient target) {
		this.temptedQuestRam = entityTFQuestRam;
		this.target = target;
		setMutexFlags(EnumSet.of(Flag.LOOK));
	}

	@Override
	public boolean shouldExecute() {
		if (this.delayTemptCounter > 0) {
			--this.delayTemptCounter;
			return false;
		} else {
			this.temptingItem = null;

			List<ItemEntity> nearbyItems = this.temptedQuestRam.world.getEntitiesWithinAABB(ItemEntity.class, this.temptedQuestRam.getBoundingBox().grow(2.0D, 2.0D, 2.0D), e -> e.isAlive() && !e.getItem().isEmpty());

			for (ItemEntity itemNearby : nearbyItems) {
				DyeColor color = EntityTFQuestRam.guessColor(itemNearby.getItem());
				if (color != null && !temptedQuestRam.isColorPresent(color)) {
					this.temptingItem = itemNearby;
					break;
				}
			}

			return temptingItem != null;
		}
	}

	@Override
	public void resetTask() {
		this.temptingItem = null;
		this.temptedQuestRam.getNavigator().clearPath();
		this.delayTemptCounter = 100;
	}

	@Override
	public void tick() {
		this.temptedQuestRam.getLookController().setLookPositionWithEntity(this.temptingItem, 30.0F, this.temptedQuestRam.getVerticalFaceSpeed());

		if (this.temptedQuestRam.getDistanceSq(this.temptingItem) < 6.25D && temptedQuestRam.tryAccept(temptingItem.getItem())) {
			this.temptingItem.remove();
		}
	}

}
