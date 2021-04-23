package twilightforest.item;

import net.minecraft.block.BlockState;
import net.minecraft.block.FlowerBlock;
import net.minecraft.block.TallGrassBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import twilightforest.TFSounds;
import twilightforest.block.BlockTFCritter;
import twilightforest.util.WorldUtil;

import javax.annotation.Nonnull;

public class ItemTFPeacockFan extends Item {

	boolean launched = false;

	ItemTFPeacockFan(Properties props) {
		super(props);
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, @Nonnull Hand hand) {

		if (!world.isRemote && !player.getCooldownTracker().hasCooldown(this)) {
			if (!player.isOnGround() && !player.isSwimming() && !player.isCreative() && !player.isElytraFlying()) {
				player.addPotionEffect(new EffectInstance(Effects.JUMP_BOOST, 200, 0, false, false));
				player.getHeldItem(hand).damageItem(1, player, (user) -> user.sendBreakAnimation(hand));
			} else {
				int fanned = doFan(world, player);
				if (fanned > 0) {
					player.getHeldItem(hand).damageItem(fanned, player, (user) -> user.sendBreakAnimation(hand));
				}
			}
		} else {
			if(player.isElytraFlying()) {
				player.setMotion(new Vector3d(
						player.getMotion().getX() * 3F,
						1.5F,
						player.getMotion().getZ() * 3F
				));
				player.getCooldownTracker().setCooldown(this, 60);
			}
			// jump if the player is in the air
			if (!player.isOnGround() && !player.isPotionActive(Effects.JUMP_BOOST) && !player.isSwimming() && !launched) {
				player.setMotion(new Vector3d(
						player.getMotion().getX() * 1.05F,
						1.5F,
						player.getMotion().getZ() * 1.05F
				));
				launched = true;
			} else {
				AxisAlignedBB fanBox = getEffectAABB(player);
				Vector3d lookVec = player.getLookVec();

				// particle effect
				for (int i = 0; i < 30; i++) {
					world.addParticle(ParticleTypes.CLOUD, fanBox.minX + world.rand.nextFloat() * (fanBox.maxX - fanBox.minX),
							fanBox.minY + world.rand.nextFloat() * (fanBox.maxY - fanBox.minY),
							fanBox.minZ + world.rand.nextFloat() * (fanBox.maxZ - fanBox.minZ),
							lookVec.x, lookVec.y, lookVec.z);
				}
			}
			player.playSound(TFSounds.FAN_WOOSH, 1.0F + random.nextFloat(), random.nextFloat() * 0.7F + 0.3F);
			return new ActionResult<>(ActionResultType.SUCCESS, player.getHeldItem(hand));
		}

		player.setActiveHand(hand);

		return new ActionResult<>(ActionResultType.PASS, player.getHeldItem(hand));
	}

	@Override
	public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if(entityIn instanceof PlayerEntity && ((PlayerEntity)entityIn).isElytraFlying() && isSelected) {
			entityIn.fallDistance = 0.0F;
		}
		if(entityIn instanceof PlayerEntity && ((PlayerEntity)entityIn).isPotionActive(Effects.JUMP_BOOST)) {
			entityIn.fallDistance = 0.0F;
		}
		if (entityIn instanceof PlayerEntity && entityIn.isOnGround() && launched) {
			((PlayerEntity)entityIn).removePotionEffect(Effects.JUMP_BOOST);
			launched = false;
		}
		super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
	}

	@Nonnull
	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.BLOCK;
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 20;
	}

	private int doFan(World world, PlayerEntity player) {
		AxisAlignedBB fanBox = getEffectAABB(player);

		fanBlocksInAABB(world, fanBox);

		fanEntitiesInAABB(world, player, fanBox);

		return 1;
	}

	private void fanEntitiesInAABB(World world, PlayerEntity player, AxisAlignedBB fanBox) {
		Vector3d moveVec = player.getLookVec().scale(2);
		Item fan = player.getActiveItemStack().getItem();

		for (Entity entity : world.getEntitiesWithinAABB(Entity.class, fanBox)) {
			if (entity.canBePushed() || entity instanceof ItemEntity || entity instanceof ProjectileEntity) {
				entity.setMotion(moveVec.x, moveVec.y, moveVec.z);
			}

			if(entity instanceof PlayerEntity && !entity.isSneaking()) {
				entity.setMotion(moveVec.x, moveVec.y, moveVec.z);
				player.getCooldownTracker().setCooldown(fan, 40);
			}
		}
	}

	private AxisAlignedBB getEffectAABB(PlayerEntity player) {
		double range = 3.0D;
		double radius = 2.0D;
		Vector3d srcVec = new Vector3d(player.getPosX(), player.getPosY() + player.getEyeHeight(), player.getPosZ());
		Vector3d lookVec = player.getLookVec().scale(range);
		Vector3d destVec = srcVec.add(lookVec.x, lookVec.y, lookVec.z);

		return new AxisAlignedBB(destVec.x - radius, destVec.y - radius, destVec.z - radius, destVec.x + radius, destVec.y + radius, destVec.z + radius);
	}

	private int fanBlocksInAABB(World world, AxisAlignedBB box) {
		int fan = 0;
		for (BlockPos pos : WorldUtil.getAllInBB(box)) {
			fan += fanBlock(world, pos);
		}
		return fan;
	}

	private int fanBlock(World world, BlockPos pos) {
		int cost = 0;

		BlockState state = world.getBlockState(pos);

		if (state.getBlock() instanceof FlowerBlock || state.getBlock() instanceof TallGrassBlock || state.getBlock() instanceof BlockTFCritter) {
			if (random.nextInt(3) == 0) {
				world.destroyBlock(pos, true);
			}
		}

		return cost;
	}
}
