package twilightforest.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.block.Blocks;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;

public class ItemTFGlassSword extends SwordItem {

	public ItemTFGlassSword(IItemTier toolMaterial, Properties props) {
		super(toolMaterial, 3, -2.4F, props);
	}

	@Override
	public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		attacker.world.playSound(null, attacker.getPosX(), attacker.getPosY(), attacker.getPosZ(), Blocks.GLASS.getDefaultState().getSoundType().getBreakSound(), attacker.getSoundCategory(), 1F, 0.5F);
		target.world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, Blocks.WHITE_STAINED_GLASS.getDefaultState()), target.getPosX(), target.getPosY(), target.getPosZ(), 1, 1, 1);
		stack.damageItem(stack.getMaxDamage() + 1, attacker, (user) -> user.sendBreakAnimation(Hand.MAIN_HAND));
		return true;
	}

	@Override
	public void fillItemGroup(ItemGroup tab, NonNullList<ItemStack> items) {
		super.fillItemGroup(tab, items);

		if (isInGroup(tab)) {
			ItemStack stack = new ItemStack(this);
			CompoundNBT tags = new CompoundNBT();
			tags.putBoolean("Unbreakable", true);
			stack.setTag(tags);
			items.add(stack);
		}
	}
}
