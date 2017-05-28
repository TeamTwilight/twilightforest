package twilightforest.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemTFFieryPick extends ItemPickaxe {

	protected ItemTFFieryPick(Item.ToolMaterial par2EnumToolMaterial) {
		super(par2EnumToolMaterial);
		this.setCreativeTab(TFItems.creativeTab);
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void onDrops(BlockEvent.HarvestDropsEvent event) {
		if (event.getHarvester() != null && event.getHarvester().inventory.getCurrentItem() != null && event.getHarvester().inventory.getCurrentItem().getItem().canHarvestBlock(event.getState())
				&& event.getHarvester().inventory.getCurrentItem().getItem() == this) {
			List<ItemStack> removeThese = new ArrayList<ItemStack>();
			List<ItemStack> addThese = new ArrayList<ItemStack>();

			for (ItemStack input : event.getDrops())
			{
				// does it smelt?
				ItemStack result = FurnaceRecipes.instance().getSmeltingResult(input);
				if (result != null)
				{
					addThese.add(new ItemStack(result.getItem(), input.stackSize, result.getItemDamage()));
					removeThese.add(input);

					// [VanillaCopy] SlotFurnaceOutput.onCrafting
					int i = result.stackSize;
					float f = FurnaceRecipes.instance().getSmeltingExperience(result);

					if (f == 0.0F)
					{
						i = 0;
					}
					else if (f < 1.0F)
					{
						int j = MathHelper.floor((float)i * f);

						if (j < MathHelper.ceil((float)i * f) && Math.random() < (double)((float)i * f - (float)j))
						{
							++j;
						}

						i = j;
					}

					while (i > 0)
					{
						int k = EntityXPOrb.getXPSplit(i);
						i -= k;
						event.getHarvester().world.spawnEntity(new EntityXPOrb(event.getWorld(), event.getHarvester().posX, event.getHarvester().posY + 0.5D, event.getHarvester().posZ, k));
					}
				}
			}

			event.getDrops().removeAll(removeThese);
			event.getDrops().addAll(addThese);
		}
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World world, IBlockState state, BlockPos pos, EntityLivingBase living) {
		if (super.onBlockDestroyed(stack, world, state, pos, living) && this.canHarvestBlock(state))
		{
			// we are just displaying the fire animation here, so check if we're on the client
			if (world.isRemote)
			{
				List<ItemStack> items = state.getBlock().getDrops(world, pos, state, 0);

				for (ItemStack input : items)
				{
					// does it smelt?
					ItemStack result = FurnaceRecipes.instance().getSmeltingResult(input);
					if (result != null)
					{

						// display fire animation
						for (int var1 = 0; var1 < 5; ++var1)
						{
							double rx = itemRand.nextGaussian() * 0.02D;
							double ry = itemRand.nextGaussian() * 0.02D;
							double rz = itemRand.nextGaussian() * 0.02D;
							double magnitude = 20.0;
							world.spawnParticle(EnumParticleTypes.FLAME, pos.getX() + 0.5 + (rx * magnitude), pos.getY() + 0.5 + (ry * magnitude), pos.getZ() + 0.5 + (rz * magnitude), -rx, -ry, -rz);
						}

					}

				}
			}


			return true;
		}
		else
		{
			return false;
		}
	}
	
    @Override
	public boolean hitEntity(ItemStack par1ItemStack, EntityLivingBase par2EntityLiving, EntityLivingBase par3EntityLiving) {
		boolean result = super.hitEntity(par1ItemStack, par2EntityLiving, par3EntityLiving);
		
		if (result && !par2EntityLiving.isImmuneToFire())
		{
			if (par2EntityLiving.world.isRemote)
			{
				// fire animation!
		        for (int var1 = 0; var1 < 20; ++var1)
		        {
		            double var2 = itemRand.nextGaussian() * 0.02D;
		            double var4 = itemRand.nextGaussian() * 0.02D;
		            double var6 = itemRand.nextGaussian() * 0.02D;
		            double var8 = 10.0D;
		            par2EntityLiving.world.spawnParticle(EnumParticleTypes.FLAME, par2EntityLiving.posX + itemRand.nextFloat() * par2EntityLiving.width * 2.0F - par2EntityLiving.width - var2 * var8, par2EntityLiving.posY + itemRand.nextFloat() * par2EntityLiving.height - var4 * var8, par2EntityLiving.posZ + itemRand.nextFloat() * par2EntityLiving.width * 2.0F - par2EntityLiving.width - var6 * var8, var2, var4, var6);
		        }
			}
			else
			{
				par2EntityLiving.setFire(15);
			}
		}
		
		return result;
	}

    @Override
	public EnumRarity getRarity(ItemStack par1ItemStack) {
    	return EnumRarity.RARE;
	}
    
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List<String> par3List, boolean par4) {
		super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);
		par3List.add(I18n.format(getUnlocalizedName() + ".tooltip"));
	}
}
