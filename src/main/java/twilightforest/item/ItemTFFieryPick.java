package twilightforest.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.*;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.TwilightForestMod;
import twilightforest.util.ParticleHelper;
import twilightforest.util.TFItemStackUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = TwilightForestMod.ID)
public class ItemTFFieryPick extends PickaxeItem {

	protected ItemTFFieryPick(IItemTier toolMaterial, Properties props) {
		super(toolMaterial, 1, -2.8F, props);
	}

	@SubscribeEvent
	public static void onDrops(BlockEvent.HarvestDropsEvent event) {
		if (event.getHarvester() != null && event.getHarvester().getHeldItemMainhand().getItem() == TFItems.fiery_pickaxe.get()
				&& event.getState().getBlock().canHarvestBlock(event.getState(), event.getWorld(), event.getPos(), event.getHarvester())) {

			List<ItemStack> removeThese = new ArrayList<>();
			List<ItemStack> addThese = new ArrayList<>();

			//TODO 1.14: Furnace recipes are now handled differently. Verify
			for (ItemStack input : event.getDrops()) {
				IRecipe<?> irecipe = event.getWorld().getWorld().getRecipeManager().getRecipe(IRecipeType.SMELTING, new Inventory(input), event.getWorld().getWorld()).orElse(null);
				ItemStack result = irecipe.getRecipeOutput();
				if (!result.isEmpty()) {

					int combinedCount = input.getCount() * result.getCount();

					addThese.addAll(TFItemStackUtils.splitToSize(new ItemStack(result.getItem(), combinedCount/*, result.getItemDamage()*/)));
					removeThese.add(input);

					// [VanillaCopy] SlotFurnaceOutput.onCrafting
					int i = combinedCount;
					float f = ((AbstractCookingRecipe) irecipe).getExperience();

					if (f == 0.0F) {
						i = 0;
					} else if (f < 1.0F) {
						int j = MathHelper.floor((float) i * f);

						if (j < MathHelper.ceil((float) i * f) && Math.random() < (double) ((float) i * f - (float) j)) {
							++j;
						}

						i = j;
					}

					while (i > 0) {
						int k = ExperienceOrbEntity.getXPSplit(i);
						i -= k;
						event.getHarvester().world.addEntity(new ExperienceOrbEntity(event.getWorld().getWorld(), event.getHarvester().getX(), event.getHarvester().getY() + 0.5D, event.getHarvester().getZ(), k));
					}

					ParticleHelper.spawnParticles(event.getWorld().getWorld(), event.getPos(), ParticleTypes.FLAME);
				}
			}

			event.getDrops().removeAll(removeThese);
			event.getDrops().addAll(addThese);
		}
	}

	@Override
	public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		boolean result = super.hitEntity(stack, target, attacker);

		if (result && !target.world.isRemote && !target.isImmuneToFire()) {
			ParticleHelper.spawnParticles(target, ParticleTypes.FLAME);
			target.setFire(15);
		}

		return result;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flags) {
		super.addInformation(stack, world, tooltip, flags);
		tooltip.add(new TranslationTextComponent(getTranslationKey() + ".tooltip"));
	}
}
