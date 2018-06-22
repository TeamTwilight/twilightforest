package twilightforest.item;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.TwilightForestMod;
import twilightforest.client.ModelRegisterCallback;
import twilightforest.util.TFItemStackUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = TwilightForestMod.ID)
public class ItemTFFieryPick extends ItemPickaxe implements ModelRegisterCallback {

	protected ItemTFFieryPick(Item.ToolMaterial toolMaterial) {
		super(toolMaterial);
		this.setCreativeTab(TFItems.creativeTab);
	}

	@SubscribeEvent
	public static void onDrops(BlockEvent.HarvestDropsEvent event) {
		if (event.getHarvester() != null && !event.getHarvester().getHeldItemMainhand().isEmpty()
				&& event.getHarvester().inventory.getCurrentItem().getItem() == TFItems.fiery_pickaxe
				&& ForgeHooks.canHarvestBlock(event.getState().getBlock(), event.getHarvester(), event.getWorld(), event.getPos())) {

			List<ItemStack> removeThese = new ArrayList<ItemStack>();
			List<ItemStack> addThese = new ArrayList<ItemStack>();

			for (ItemStack input : event.getDrops()) {
				ItemStack result = FurnaceRecipes.instance().getSmeltingResult(input);
				if (!result.isEmpty()) {

					int combinedCount = input.getCount() * result.getCount();

					addThese.addAll(TFItemStackUtils.splitToSize(new ItemStack(result.getItem(), combinedCount, result.getItemDamage())));
					removeThese.add(input);

					// [VanillaCopy] SlotFurnaceOutput.onCrafting
					int i = combinedCount;
					float f = FurnaceRecipes.instance().getSmeltingExperience(result);

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
						int k = EntityXPOrb.getXPSplit(i);
						i -= k;
						event.getHarvester().world.spawnEntity(new EntityXPOrb(event.getWorld(), event.getHarvester().posX, event.getHarvester().posY + 0.5D, event.getHarvester().posZ, k));
					}

					spawnParticles((WorldServer) event.getWorld(), event.getPos().getX(), event.getPos().getY(), event.getPos().getZ());
				}
			}

			event.getDrops().removeAll(removeThese);
			event.getDrops().addAll(addThese);
		}
	}

	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, @Nullable EntityLivingBase attacker) {
		boolean result = super.hitEntity(stack, target, attacker);

		if (result && !target.isImmuneToFire()) {
			spawnParticles((WorldServer) target.world, target.posX, target.posY, target.posZ);
			target.setFire(15);
		}

		return result;
	}

	private static void spawnParticles(WorldServer world, double x, double y, double z) {
		for (int i = 0; i < 5; ++i) {
			double rx = itemRand.nextGaussian() * 0.02D;
			double ry = itemRand.nextGaussian() * 0.02D;
			double rz = itemRand.nextGaussian() * 0.02D;
			final double magnitude = 20.0;
			world.spawnParticle(EnumParticleTypes.FLAME, x + 0.5 + (rx * magnitude), y + 0.5 + (ry * magnitude), z + 0.5 + (rz * magnitude),
					5, 0, 0, 0, 0.02);
		}
	}

	@Override
	public EnumRarity getRarity(ItemStack par1ItemStack) {
		return EnumRarity.RARE;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flags) {
		super.addInformation(stack, world, tooltip, flags);
		tooltip.add(I18n.format(getUnlocalizedName() + ".tooltip"));
	}
}
