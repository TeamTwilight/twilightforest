package twilightforest.enchantment;

import com.mojang.serialization.MapCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;
import twilightforest.init.TFItems;
import twilightforest.inventory.InventoryUtil;
import twilightforest.item.recipe.ScepterRepairRecipe;

import java.util.ArrayList;
import java.util.List;

public record RechargeScepterEffect() implements EnchantmentEntityEffect {

	public static final MapCodec<RechargeScepterEffect> CODEC = MapCodec.unit(RechargeScepterEffect::new);

	@Override
	public void apply(ServerLevel level, int enchantmentLevel, EnchantedItemInUse item, Entity entity, Vec3 vec3) {
		applyRecharge(level, item.itemStack(), entity);
	}

	public static void applyRecharge(ServerLevel level, ItemStack item, Entity entity) {
		if (entity instanceof Player player && item.getDamageValue() == item.getMaxDamage()) {
			List<ScepterRepairRecipe> recipes = level.recipeAccess().getRecipes().stream().filter(holder -> holder.value() instanceof ScepterRepairRecipe).map(RecipeHolder::value).map(ScepterRepairRecipe.class::cast).toList();
			List<Integer> slotsToConsume = new ArrayList<>();
			for (var recipe : recipes) {
				if (item.is(recipe.getScepter())) {
					var ingredientCopy = new ArrayList<>(recipe.getRepairItems());
					scepterItemsCheck:
					for (int i = 0; i < player.getInventory().getNonEquipmentItems().size(); i++) {
						var stack = player.getInventory().getNonEquipmentItems().get(i);
						if (stack.isEmpty()) continue;
						if (stack.is(TFItems.EXANIMATE_ESSENCE)) {
							stack.shrink(1);
							item.setDamageValue(0);
							return;
						}
						for (var ingredient : recipe.getRepairItems()) {
							if (ingredientCopy.contains(ingredient) && ingredient.test(stack)) {
								ingredientCopy.remove(ingredient);
								slotsToConsume.add(i);
								if (ingredientCopy.isEmpty()) break scepterItemsCheck;
							}
						}
					}

					if (slotsToConsume.size() == recipe.getRepairItems().size()) {
						for (int slot : slotsToConsume) {
							ItemStack stack = player.getInventory().getNonEquipmentItems().get(slot);
							stack.shrink(1);
							ItemStackTemplate remainder = stack.getCraftingRemainder();
							if (remainder != null) {
								InventoryUtil.giveItemToPlayer(player, remainder.create());
							}
						}
						item.setDamageValue(item.getDamageValue() - recipe.getRepairDurability());
					}
				}
			}
		}
	}

	@Override
	public MapCodec<? extends EnchantmentEntityEffect> codec() {
		return CODEC;
	}
}
