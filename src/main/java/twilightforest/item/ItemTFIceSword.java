package twilightforest.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.PotionEffect;
import twilightforest.client.ModelRegisterCallback;
import twilightforest.client.particle.TFParticleType;
import twilightforest.potions.TFPotions;
import twilightforest.util.ParticleHelper;

public class ItemTFIceSword extends ItemSword implements ModelRegisterCallback {

	public ItemTFIceSword(Item.ToolMaterial toolMaterial) {
		super(toolMaterial);
		this.setCreativeTab(TFItems.creativeTab);
	}

	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
		boolean result = super.hitEntity(stack, target, attacker);

		if (result && !target.world.isRemote) {
			target.addPotionEffect(new PotionEffect(TFPotions.frosty, 20 * 10, 2));
			ParticleHelper.spawnParticles(target, TFParticleType.SNOW, 20);
		}

		return result;
	}
}
