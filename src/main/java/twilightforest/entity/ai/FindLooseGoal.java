package twilightforest.entity.ai;

import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import twilightforest.entity.passive.QuestRam;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;

// [VanillaCopy] TemptGoal, but attracted to item entities instead of players
public class FindLooseGoal extends Goal {
	protected final PathfinderMob creature;
	private final double speed;
	protected ItemEntity closestItem;
	private int delayTemptCounter;
	private final Ingredient temptItem;

	public FindLooseGoal(PathfinderMob creature, double speed, Ingredient temptItem) {
		this.creature = creature;
		this.speed = speed;
		this.temptItem = temptItem;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		if (this.delayTemptCounter > 0) {
			--this.delayTemptCounter;
			return false;
		} else {
			List<ItemEntity> items = this.creature.level.getEntitiesOfClass(ItemEntity.class, creature.getBoundingBox().inflate(16, 4, 16), e -> e.isAlive() && !e.getItem().isEmpty());
			items.sort(Comparator.comparingDouble(i -> i.distanceToSqr(creature.position())));
			this.closestItem = items.isEmpty() ? null : items.get(0);
			if (this.closestItem == null) {
				return false;
			} else {
				return this.isTempting(closestItem.getItem());
			}
		}
	}

	protected boolean isTempting(ItemStack stack) {
		if (creature instanceof QuestRam ram && stack.is(ItemTags.WOOL)) {
			DyeColor color = QuestRam.guessColor(stack);
			return color != null && !ram.isColorPresent(color);
		}
		return this.temptItem.test(stack);
	}

	@Override
	public void start() {
	}

	@Override
	public void stop() {
		this.closestItem = null;
		this.creature.getNavigation().stop();
		this.delayTemptCounter = 100;
	}

	@Override
	public void tick() {
		this.creature.getLookControl().setLookAt(this.closestItem, this.creature.getMaxHeadYRot() + 20, this.creature.getMaxHeadXRot());
		if (this.creature.distanceToSqr(this.closestItem) < 6.25D) {
			this.creature.getNavigation().stop();
		} else {
			this.creature.getNavigation().moveTo(this.closestItem, this.speed);
		}

	}
}
