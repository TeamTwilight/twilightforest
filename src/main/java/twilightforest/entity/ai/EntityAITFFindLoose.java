package twilightforest.entity.ai;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;

import java.util.EnumSet;
import java.util.List;

public class EntityAITFFindLoose extends Goal {

	/**
	 * The entity using this AI that is tempted by the player.
	 */
	private final CreatureEntity temptedEntity;

	private final Item item;
	private final float pursueSpeed;

	private int delayTemptCounter;

	private ItemEntity temptingItem;

	public EntityAITFFindLoose(CreatureEntity entityCreature, float speed, Item item) {
		this.temptedEntity = entityCreature;
		this.pursueSpeed = speed;
		this.item = item;
		this.setMutexFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
	}

	@Override
	public boolean shouldExecute() {
		if (this.delayTemptCounter > 0) {
			--this.delayTemptCounter;
			return false;
		} else {
			this.temptingItem = null;

			List<ItemEntity> nearbyItems = this.temptedEntity.world.getEntitiesWithinAABB(ItemEntity.class, this.temptedEntity.getBoundingBox().grow(16.0D, 4.0D, 16.0D));

			for (ItemEntity itemNearby : nearbyItems) {
				if (itemNearby.getItem().getItem() == item && itemNearby.isAlive()) {
					this.temptingItem = itemNearby;
					break;
				}
			}

			return this.temptingItem != null;
		}
	}

	@Override
	public boolean shouldContinueExecuting() {
		return this.shouldExecute();
	}

	@Override
	public void startExecuting() {}

	@Override
	public void resetTask() {
		this.temptingItem = null;
		this.temptedEntity.getNavigator().clearPath();
		this.delayTemptCounter = 100;
	}

	@Override
	public void tick() {
		this.temptedEntity.getLookController().setLookPositionWithEntity(this.temptingItem, 30.0F, this.temptedEntity.getVerticalFaceSpeed());

		if (this.temptedEntity.getDistanceSq(this.temptingItem) < 6.25D) {
			this.temptedEntity.getNavigator().clearPath();
		} else {
			this.temptedEntity.getNavigator().tryMoveToXYZ(temptingItem.getX(), temptingItem.getY(), temptingItem.getZ(), this.pursueSpeed);
		}
	}
}
