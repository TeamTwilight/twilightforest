package twilightforest.item;

import net.minecraft.block.BlockFlower;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import twilightforest.util.WorldUtil;

import javax.annotation.Nonnull;

public class ItemTFPeacockFan extends ItemTF {
	protected ItemTFPeacockFan() {
		this.setCreativeTab(TFItems.creativeTab);
		this.maxStackSize = 1;
		this.setMaxDamage(1024);
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {

		if (!world.isRemote) {
			if (!player.onGround) {
				player.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, 45, 0));
			} else {
				int fanned = doFan(world, player);

				if (fanned > 0) {
					player.getHeldItem(hand).damageItem(fanned, player);
				}
			}
		} else {
			// jump if the player is in the air
			//TODO: only one extra jump per jump
			if (!player.onGround && !player.isPotionActive(MobEffects.JUMP_BOOST)) {
				player.motionX *= 3F;
				player.motionY = 1.5F;
				player.motionZ *= 3F;
				player.fallDistance = 0.0F;
			} else {
				AxisAlignedBB fanBox = getEffectAABB(player);
				Vec3d lookVec = player.getLookVec();

				// particle effect
				for (int i = 0; i < 30; i++) {
					world.spawnParticle(EnumParticleTypes.CLOUD, fanBox.minX + world.rand.nextFloat() * (fanBox.maxX - fanBox.minX),
							fanBox.minY + world.rand.nextFloat() * (fanBox.maxY - fanBox.minY),
							fanBox.minZ + world.rand.nextFloat() * (fanBox.maxZ - fanBox.minZ),
							lookVec.x, lookVec.y, lookVec.z);
				}

			}

			player.playSound(SoundEvents.ENTITY_PLAYER_BREATH, 1.0F + itemRand.nextFloat(), itemRand.nextFloat() * 0.7F + 0.3F);
		}

		player.setActiveHand(hand);

		return ActionResult.newResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}

	@Nonnull
	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.BLOCK;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 20;
	}

	private int doFan(World world, EntityPlayer player) {
		AxisAlignedBB fanBox = getEffectAABB(player);

		fanBlocksInAABB(world, player, fanBox);

		fanEntitiesInAABB(world, player, fanBox);

		return 1;
	}

	private void fanEntitiesInAABB(World world, EntityPlayer player, AxisAlignedBB fanBox) {
		Vec3d moveVec = player.getLookVec().scale(2);

		for (Entity entity : world.getEntitiesWithinAABB(Entity.class, fanBox)) {
			if (entity.canBePushed() || entity instanceof EntityItem) {
				entity.motionX = moveVec.x;
				entity.motionY = moveVec.y;
				entity.motionZ = moveVec.z;
			}
		}

	}

	private AxisAlignedBB getEffectAABB(EntityPlayer player) {
		double range = 3.0D;
		double radius = 2.0D;
		Vec3d srcVec = new Vec3d(player.posX, player.posY + player.getEyeHeight(), player.posZ);
		Vec3d lookVec = player.getLookVec().scale(range);
		Vec3d destVec = srcVec.add(lookVec.x, lookVec.y, lookVec.z);

		return new AxisAlignedBB(destVec.x - radius, destVec.y - radius, destVec.z - radius, destVec.x + radius, destVec.y + radius, destVec.z + radius);
	}

	private int fanBlocksInAABB(World world, EntityPlayer player, AxisAlignedBB box) {
		int fan = 0;
		for (BlockPos pos : WorldUtil.getAllInBB(box)) {
			fan += fanBlock(world, player, pos);
		}
		return fan;
	}

	private int fanBlock(World world, EntityPlayer player, BlockPos pos) {
		int cost = 0;

		IBlockState state = world.getBlockState(pos);

		if (state.getBlock() != Blocks.AIR) {
			if (state.getBlock() instanceof BlockFlower) {
				if (state.getBlock().canHarvestBlock(world, pos, player) && itemRand.nextInt(3) == 0) {
					state.getBlock().harvestBlock(world, player, pos, state, world.getTileEntity(pos), ItemStack.EMPTY);
					world.destroyBlock(pos, false);
				}
			}
		}

		return cost;
	}
}
