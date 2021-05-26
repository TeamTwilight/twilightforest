package twilightforest.entity.ai;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.GroundPathNavigator;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;

// [VanillaCopy] TemptGoal, but attracted to item entities instead of players
public class EntityAITFFindLoose extends Goal {
	protected final CreatureEntity creature;
	private final double speed;
	protected ItemEntity closestItem;
	private int delayTemptCounter;
	private final Ingredient temptItem;

	public EntityAITFFindLoose(CreatureEntity creature, double speed, Ingredient temptItem) {
		this.creature = creature;
		this.speed = speed;
		this.temptItem = temptItem;
		this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
		if (!(creature.getNavigator() instanceof GroundPathNavigator) && !(creature.getNavigator() instanceof FlyingPathNavigator)) {
			throw new IllegalArgumentException("Unsupported mob type for TemptGoal");
		}
	}

	@Override
	public boolean shouldExecute() {
		if (this.delayTemptCounter > 0) {
			--this.delayTemptCounter;
			return false;
		} else {
			List<ItemEntity> items = this.creature.world.getEntitiesWithinAABB(ItemEntity.class, creature.getBoundingBox().grow(16, 4, 16), e -> e.isAlive() && !e.getItem().isEmpty());
			items.sort(Comparator.comparingDouble(i -> i.getDistanceSq(creature.getPositionVec())));
			this.closestItem = items.isEmpty() ? null : items.get(0);
			if (this.closestItem == null) {
				return false;
			} else {
				return this.isTempting(closestItem.getItem());
			}
		}
	}

	protected boolean isTempting(ItemStack stack) {
		return this.temptItem.test(stack);
	}

	@Override
	public void startExecuting() {
	}

	@Override
	public void resetTask() {
		this.closestItem = null;
		this.creature.getNavigator().clearPath();
		this.delayTemptCounter = 100;
	}

	@Override
	public void tick() {
		this.creature.getLookController().setLookPositionWithEntity(this.closestItem, this.creature.getHorizontalFaceSpeed() + 20, this.creature.getVerticalFaceSpeed());
		if (this.creature.getDistanceSq(this.closestItem) < 6.25D) {
			this.creature.getNavigator().clearPath();
		} else {
			this.creature.getNavigator().tryMoveToEntityLiving(this.closestItem, this.speed);
		}

	}
}
