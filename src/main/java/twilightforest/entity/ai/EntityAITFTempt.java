package twilightforest.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.TemptGoal;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import java.util.Collections;

/**
 * Same as superclass, but uses an {@code Ingredient} instead of a {@code Set<Item>}
 */
public class EntityAITFTempt extends TemptGoal {

	protected final Ingredient ingredient;

	public EntityAITFTempt(EntityCreature temptedEntity, double speed, boolean scaredByPlayerMovement, Ingredient ingredient) {
		super(temptedEntity, speed, scaredByPlayerMovement, Collections.emptySet());
		this.ingredient = ingredient;
	}

	@Override
	protected boolean isTempting(ItemStack stack) {
		return ingredient.apply(stack);
	}
}
