package twilightforest.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.*;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.TwilightForestMod;
import javax.annotation.Nullable;
import java.util.List;

@Mod.EventBusSubscriber(modid = TwilightForestMod.ID)
public class ItemTFFieryPick extends PickaxeItem {

	protected ItemTFFieryPick(IItemTier toolMaterial, Properties props) {
		super(toolMaterial, 1, -2.8F, props);
	}

	/*@SubscribeEvent FIXME Re-implement with IGlobalLootModifier
	public static void onDrops(BlockEvent.HarvestDropsEvent event) {
		if (event.getHarvester() != null && event.getHarvester().getHeldItemMainhand().getItem() == TFItems.fiery_pickaxe.get()
				&& event.getState().getBlock().canHarvestBlock(event.getState(), event.getWorld(), event.getPos(), event.getHarvester())
				&& event.getWorld() instanceof World) {

			List<ItemStack> removeThese = new ArrayList<>();
			List<ItemStack> addThese = new ArrayList<>();

			World world = ((World) event.getWorld());

			//TODO 1.14: Furnace recipes are now handled differently. Verify
			//TODO figure out how we want to handle alt furnace recipes (Furnace vs Blasting/Smoking/Campfire)
			for (ItemStack input : event.getDrops()) {
				IRecipe<?> irecipe = world.getRecipeManager().getRecipe(IRecipeType.SMELTING, new Inventory(input), world).orElse(null);
				ItemStack result = irecipe.getRecipeOutput();
				if (!result.isEmpty()) {

					int combinedCount = input.getCount() * result.getCount();

					addThese.addAll(TFItemStackUtils.splitToSize(new ItemStack(result.getItem(), combinedCount*//*, result.getItemDamage()*//*)));
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
						event.getHarvester().world.addEntity(new ExperienceOrbEntity(world, event.getHarvester().getPosX(), event.getHarvester().getPosY() + 0.5D, event.getHarvester().getPosZ(), k));
					}

					BlockPos pos = event.getPos();
					world.addParticle(ParticleTypes.FLAME, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0.5, 0.5, 0.5);
				}
			}

			event.getDrops().removeAll(removeThese);
			event.getDrops().addAll(addThese);
		}
	}*/

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
}
