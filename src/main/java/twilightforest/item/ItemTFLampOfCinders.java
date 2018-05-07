package twilightforest.item;

import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import twilightforest.block.TFBlocks;

import javax.annotation.Nonnull;

public class ItemTFLampOfCinders extends ItemTF {

	private static final int FIRING_TIME = 12;

	public ItemTFLampOfCinders() {
		this.setCreativeTab(TFItems.creativeTab);
		this.maxStackSize = 1;
		this.setMaxDamage(1024);
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
		player.setActiveHand(hand);
		return ActionResult.newResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}

	@Nonnull
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (burnBlock(world, pos)) {
			player.playSound(SoundEvents.ENTITY_GHAST_SHOOT, 0.5F, 1.5F);

			// spawn flame particles
			for (int i = 0; i < 10; i++) {
				float dx = pos.getX() + 0.5F + (itemRand.nextFloat() - itemRand.nextFloat()) * 0.75F;
				float dy = pos.getY() + 0.5F + (itemRand.nextFloat() - itemRand.nextFloat()) * 0.75F;
				float dz = pos.getZ() + 0.5F + (itemRand.nextFloat() - itemRand.nextFloat()) * 0.75F;

				world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, dx, dy, dz, 0.0D, 0.0D, 0.0D);
				world.spawnParticle(EnumParticleTypes.FLAME, dx, dy, dz, 0.0D, 0.0D, 0.0D);
			}

			return EnumActionResult.SUCCESS;
		} else {
			return EnumActionResult.PASS;
		}
	}

	private boolean burnBlock(World world, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
		if (state.getBlock() == TFBlocks.thorns) {
			world.setBlockState(pos, TFBlocks.burnt_thorns.getDefaultState().withProperty(BlockRotatedPillar.AXIS, state.getValue(BlockRotatedPillar.AXIS)));
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack par1ItemStack, World world, EntityLivingBase living, int useRemaining) {
		int useTime = this.getMaxItemUseDuration(par1ItemStack) - useRemaining;

		if (useTime > FIRING_TIME && (par1ItemStack.getItemDamage() + 1) < this.getMaxDamage(par1ItemStack)) {
			doBurnEffect(world, living);
		}
	}

	private void doBurnEffect(World world, EntityLivingBase living) {
		BlockPos pos = new BlockPos(
				MathHelper.floor(living.lastTickPosX),
				MathHelper.floor(living.lastTickPosY + living.getEyeHeight()),
				MathHelper.floor(living.lastTickPosZ)
		);

		int range = 4;

		if (!world.isRemote) {
			world.playSound(null, living.posX, living.posY, living.posZ, SoundEvents.ENTITY_GHAST_SHOOT, living.getSoundCategory(), 1.5F, 0.8F);

			// set nearby thorns to burnt
			for (int dx = -range; dx <= range; dx++) {
				for (int dy = -range; dy <= range; dy++) {
					for (int dz = -range; dz <= range; dz++) {
						this.burnBlock(world, pos.add(dx, dy, dz));
					}
				}
			}
		}

		if (living instanceof EntityPlayer) {
			for (int i = 0; i < 6; i++) {
				BlockPos rPos = pos.add(
						itemRand.nextInt(range) - itemRand.nextInt(range),
						itemRand.nextInt(2),
						itemRand.nextInt(range) - itemRand.nextInt(range)
				);

				world.playEvent((EntityPlayer) living, 2004, rPos, 0);
			}
		}
	}

	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack) {
		return EnumAction.BOW;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack par1ItemStack) {
		return 72000;
	}
}
