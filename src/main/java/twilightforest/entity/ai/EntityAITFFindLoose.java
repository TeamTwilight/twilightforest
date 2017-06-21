package twilightforest.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import twilightforest.entity.passive.EntityTFQuestRam;

import java.util.List;


public class EntityAITFFindLoose extends EntityAIBase {

	/**
	 * The entity using this AI that is tempted by the player.
	 */
	private EntityCreature temptedEntity;

	private Item temptID;
	private float pursueSpeed;

	private int delayTemptCounter;

	private EntityItem temptingItem;

	public EntityAITFFindLoose(EntityTFQuestRam entityTFQuestRam, float speed, Item blockID) {
		this.temptedEntity = entityTFQuestRam;
		this.pursueSpeed = speed;
		this.temptID = blockID;
		this.setMutexBits(3);
	}

	@Override
	public boolean shouldExecute() {
		if (this.delayTemptCounter > 0) {
			--this.delayTemptCounter;
			return false;
		} else {
			this.temptingItem = null;

			List<EntityItem> nearbyItems = this.temptedEntity.world.getEntitiesWithinAABB(EntityItem.class, this.temptedEntity.getEntityBoundingBox().expand(16.0D, 4.0D, 16.0D));

			for (EntityItem itemNearby : nearbyItems) {
				if (itemNearby.getEntityItem().getItem() == temptID && itemNearby.isEntityAlive()) {
					this.temptingItem = itemNearby;
					break;
				}
			}

			if (this.temptingItem == null) {
				return false;
			} else {
				return true;
			}
		}
	}

	@Override
	public boolean shouldContinueExecuting() {
		return this.shouldExecute();
	}

	@Override
	public void startExecuting() {
		;
	}

	@Override
	public void resetTask() {
		this.temptingItem = null;
		this.temptedEntity.getNavigator().clearPathEntity();
		this.delayTemptCounter = 100;
	}

	@Override
	public void updateTask() {
		this.temptedEntity.getLookHelper().setLookPositionWithEntity(this.temptingItem, 30.0F, this.temptedEntity.getVerticalFaceSpeed());

		if (this.temptedEntity.getDistanceSqToEntity(this.temptingItem) < 6.25D) {
			this.temptedEntity.getNavigator().clearPathEntity();
		} else {
			this.temptedEntity.getNavigator().tryMoveToXYZ(temptingItem.posX, temptingItem.posY, temptingItem.posZ, this.pursueSpeed);
		}
	}
}
