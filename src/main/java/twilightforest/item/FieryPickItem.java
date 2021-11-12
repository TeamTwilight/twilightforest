package twilightforest.item;

import com.google.gson.JsonObject;
import net.minecraft.ChatFormatting;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class FieryPickItem extends PickaxeItem {

	protected FieryPickItem(Tier toolMaterial, Properties props) {
		super(toolMaterial, 1, -2.8F, props);
	}

	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		boolean result = super.hurtEnemy(stack, target, attacker);

		if (result && !target.fireImmune()) {
			if (!target.level.isClientSide) {
				target.setSecondsOnFire(15);
			} else {
				target.level.addParticle(ParticleTypes.FLAME, target.getX(), target.getY() + target.getBbHeight() * 0.5, target.getZ(), target.getBbWidth() * 0.5, target.getBbHeight() * 0.5, target.getBbWidth() * 0.5);
			}
		}

		return result;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flags) {
		super.appendHoverText(stack, world, tooltip, flags);
		tooltip.add(new TranslatableComponent(getDescriptionId() + ".tooltip").withStyle(ChatFormatting.GRAY));
	}

	private static class SmeltModifier extends LootModifier {

		protected final LootItemCondition[] conditions;

		public SmeltModifier(LootItemCondition[] conditionsIn) {
			super(conditionsIn);
			this.conditions = conditionsIn;
		}

		@Override
		public List<ItemStack> doApply(List<ItemStack> originalLoot, LootContext context) {
			List<ItemStack> newLoot = new ArrayList<>();
			originalLoot.forEach((stack) -> newLoot.add(
					context.getLevel().getRecipeManager()
							.getRecipeFor(RecipeType.SMELTING, new SimpleContainer(stack), context.getLevel())
							.map(SmeltingRecipe::getResultItem)
							.filter(itemStack -> !itemStack.isEmpty())
							.map(itemStack -> ItemHandlerHelper.copyStackWithSize(itemStack, stack.getCount() * itemStack.getCount()))
							.orElse(stack)));
			return newLoot;
		}
	}

	public static class Serializer extends GlobalLootModifierSerializer<SmeltModifier> {

		@Override
		public SmeltModifier read(ResourceLocation name, JsonObject json, LootItemCondition[] conditionsIn) {
			return new SmeltModifier(conditionsIn);
		}

		@Override
		public JsonObject write(SmeltModifier instance) {
			return null;
		}
	}
}