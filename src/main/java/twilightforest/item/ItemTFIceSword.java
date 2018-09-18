package twilightforest.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.PotionEffect;
import twilightforest.TwilightForestMod;
import twilightforest.client.ModelRegisterCallback;
import twilightforest.client.particle.TFParticleType;
import twilightforest.potions.TFPotions;

public class ItemTFIceSword extends ItemSword implements ModelRegisterCallback {

	public ItemTFIceSword(Item.ToolMaterial toolMaterial) {
		super(toolMaterial);
		this.setCreativeTab(TFItems.creativeTab);
	}

	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
		boolean result = super.hitEntity(stack, target, attacker);

		if (result) {
			int chillLevel = 2;
			target.addPotionEffect(new PotionEffect(TFPotions.frosty, 20 * 10, chillLevel));
		}

		return result;
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
		if (player.world.isRemote) {
			// snow animation!
			for (int i = 0; i < 20; ++i) {
				double px = entity.posX + itemRand.nextFloat() * entity.width * 2.0F - entity.width;
				double py = entity.posY + itemRand.nextFloat() * entity.height;
				double pz = entity.posZ + itemRand.nextFloat() * entity.width * 2.0F - entity.width;
				TwilightForestMod.proxy.spawnParticle(entity.world, TFParticleType.SNOW, px, py, pz, 0, 0, 0);
			}
		}
		return false;
	}
}
