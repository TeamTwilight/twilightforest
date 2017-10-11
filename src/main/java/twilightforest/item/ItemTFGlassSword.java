package twilightforest.item;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.EnumParticleTypes;
import twilightforest.client.ModelRegisterCallback;

import javax.annotation.Nullable;

public class ItemTFGlassSword extends ItemSword implements ModelRegisterCallback {

	public ItemTFGlassSword(Item.ToolMaterial par2EnumToolMaterial) {
		super(par2EnumToolMaterial);
		this.setCreativeTab(TFItems.creativeTab);
	}

	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, @Nullable EntityLivingBase attacker) {
		stack.damageItem(stack.getMaxDamage() + 1, attacker);
		return true;
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
		if (player.world.isRemote) {
			for (int var1 = 0; var1 < 20; ++var1) {
				double px = entity.posX + itemRand.nextFloat() * entity.width * 2.0F - entity.width;
				double py = entity.posY + itemRand.nextFloat() * entity.height;
				double pz = entity.posZ + itemRand.nextFloat() * entity.width * 2.0F - entity.width;
				entity.world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, px, py, pz, 0, 0, 0, Block.getStateId(Blocks.STAINED_GLASS.getDefaultState()));
			}

			player.playSound(Blocks.GLASS.getSoundType(Blocks.GLASS.getDefaultState(), entity.world, entity.getPosition(), player).getBreakSound(), 1F, 0.5F);
		}
		return false;
	}
}