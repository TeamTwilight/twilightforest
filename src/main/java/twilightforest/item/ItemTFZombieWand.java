package twilightforest.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import twilightforest.TwilightForestMod;
import twilightforest.entity.EntityTFLoyalZombie;
import net.minecraftforge.fml.common.FMLLog;


public class ItemTFZombieWand extends ItemTF {

	protected ItemTFZombieWand() {
        this.maxStackSize = 1;
        this.setMaxDamage(9);
		this.setCreativeTab(TFItems.creativeTab);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack par1ItemStack, World world, EntityPlayer player, EnumHand hand) {
		
		if (par1ItemStack.getItemDamage() < this.getMaxDamage(par1ItemStack)) {
			player.setActiveHand(hand);
			
			if (!world.isRemote) {
				// what block is the player pointing at?
				RayTraceResult mop = getPlayerPointVec(world, player, 20.0F);
				
				if (mop != null) {
					// make a zombie there
					EntityTFLoyalZombie zombie = new EntityTFLoyalZombie(world);
					zombie.setPositionAndRotation(mop.hitVec.xCoord, mop.hitVec.yCoord, mop.hitVec.zCoord, 1.0F, 1.0F);  /// NPE here needs to be fixed
					zombie.setTamed(true);
					try {
						zombie.setOwnerId(player.getUniqueID());
					} catch (NoSuchMethodError ex) {
						// ignore?
						TwilightForestMod.LOGGER.warn("Could not determine player name for loyal zombie, ignoring error.");
					}
					zombie.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 1200, 1));
					world.spawnEntity(zombie);

					par1ItemStack.damageItem(1, player);
				}
			}
		}
		else {
			player.resetActiveHand();
		}
		
		return ActionResult.newResult(EnumActionResult.SUCCESS, par1ItemStack);
	}
	
	/**
	 * What block is the player pointing the wand at?
	 * 
	 * This very similar to player.rayTrace, but that method is not available on the server.
	 * 
	 * @return
	 */
	private RayTraceResult getPlayerPointVec(World world, EntityPlayer player, float range) {
        Vec3d position = new Vec3d(player.posX, player.posY + player.getEyeHeight(), player.posZ);
        Vec3d look = player.getLook(1.0F);
        Vec3d dest = position.addVector(look.xCoord * range, look.yCoord * range, look.zCoord * range);
        return world.rayTraceBlocks(position, dest);
	}


    @Override
	public int getMaxItemUseDuration(ItemStack par1ItemStack)
    {
        return 30;
    }
    
    @Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack)
    {
        return EnumAction.BOW;
    }
    
    @Override
	public EnumRarity getRarity(ItemStack par1ItemStack) {
    	return EnumRarity.RARE;
	}
    
	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List<String> par3List, boolean par4) {
		par3List.add((par1ItemStack.getMaxDamage() -  par1ItemStack.getItemDamage()) + " charges left");
	}
}
