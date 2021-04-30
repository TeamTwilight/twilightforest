package twilightforest.item;

import com.google.gson.JsonObject;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.ItemHandlerHelper;
import twilightforest.TwilightForestMod;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ItemTFFieryPick extends PickaxeItem {

	protected ItemTFFieryPick(IItemTier toolMaterial, Properties props) {
		super(toolMaterial, 1, -2.8F, props);
	}

	@Override
	public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		boolean result = super.hitEntity(stack, target, attacker);

		if (result && !target.isImmuneToFire()) {
			if (!target.world.isRemote) {
				target.setFire(15);
			} else {
				target.world.addParticle(ParticleTypes.FLAME, target.getPosX(), target.getPosY() + target.getHeight() * 0.5, target.getPosZ(), target.getWidth() * 0.5, target.getHeight() * 0.5, target.getWidth() * 0.5);
			}
		}

		return result;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flags) {
		super.addInformation(stack, world, tooltip, flags);
		tooltip.add(new TranslationTextComponent(getTranslationKey() + ".tooltip"));
	}

	private static class SmeltModifier extends LootModifier {

		protected final ILootCondition[] conditions;

		public SmeltModifier(ILootCondition[] conditionsIn) {
			super(conditionsIn);
			this.conditions = conditionsIn;
		}

		@Override
		public List<ItemStack> doApply(List<ItemStack> originalLoot, LootContext context) {
			List<ItemStack> newLoot = new ArrayList<>();
			originalLoot.forEach((stack) -> newLoot.add(
					context.getWorld().getRecipeManager()
							.getRecipe(IRecipeType.SMELTING, new Inventory(stack), context.getWorld())
							.map(FurnaceRecipe::getRecipeOutput)
							.filter(itemStack -> !itemStack.isEmpty())
							.map(itemStack -> ItemHandlerHelper.copyStackWithSize(itemStack, stack.getCount() * itemStack.getCount()))
							.orElse(stack)));
			return newLoot;
		}
	}

	public static class Serializer extends GlobalLootModifierSerializer<SmeltModifier> {

		@Override
		public SmeltModifier read(ResourceLocation name, JsonObject json, ILootCondition[] conditionsIn) {
			return new SmeltModifier(conditionsIn);
		}

		@Override
		public JsonObject write(SmeltModifier instance) {
			return null;
		}
	}
}