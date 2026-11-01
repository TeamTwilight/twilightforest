package twilightforest.forge;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

/**
 * Un palier d'outil, decrit par ses seules valeurs.
 *
 * <p>NeoForge fournit `SimpleTier`, Forge un `ForgeTier` dont l'ordre des parametres differe.
 * {@link Tier} n'ayant que six methodes, l'implementer directement evite toute ambiguite et se lit
 * dans le meme ordre que la declaration d'origine.</p>
 */
public record TFTier(
		TagKey<Block> incorrectBlocksForDrops,
		int uses,
		float speed,
		float attackDamageBonus,
		int enchantmentValue,
		Supplier<Ingredient> repairIngredient) implements Tier {

	@Override
	public int getUses() {
		return this.uses;
	}

	@Override
	public float getSpeed() {
		return this.speed;
	}

	@Override
	public float getAttackDamageBonus() {
		return this.attackDamageBonus;
	}

	@Override
	public TagKey<Block> getIncorrectBlocksForDrops() {
		return this.incorrectBlocksForDrops;
	}

	@Override
	public int getEnchantmentValue() {
		return this.enchantmentValue;
	}

	@Override
	public Ingredient getRepairIngredient() {
		return this.repairIngredient.get();
	}
}
