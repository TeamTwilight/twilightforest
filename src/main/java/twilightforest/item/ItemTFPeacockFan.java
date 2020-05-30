package twilightforest.item;

import net.minecraft.block.BlockState;
import net.minecraft.block.FlowerBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundEvents;
import net.minecraft.item.UseAction;
import net.minecraft.item.Rarity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import twilightforest.util.WorldUtil;

import javax.annotation.Nonnull;

public class ItemTFPeacockFan extends Item {
	ItemTFPeacockFan(Properties props) {
		super(props);
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, @Nonnull Hand hand) {

		if (!world.isRemote) {
			if (!player.onGround) {
				player.addPotionEffect(new EffectInstance(Effects.JUMP_BOOST, 45, 0));
			} else {
				int fanned = doFan(world, player);

				if (fanned > 0) {
					player.getHeldItem(hand).damageItem(fanned, player, (user) -> user.sendBreakAnimation(hand));
				}
			}
		} else {
			// jump if the player is in the air
			//TODO: only one extra jump per jump
			if (!player.onGround && !player.isPotionActive(Effects.JUMP_BOOST)) {
//				player.motionX *= 3F;
//				player.motionY = 1.5F;
//				player.motionZ *= 3F;
				player.setMotion(new Vec3d(
						player.getMotion().getX() * 3F,
						1.5F,
						player.getMotion().getZ() * 3F
				));
				player.fallDistance = 0.0F;
			} else {
				AxisAlignedBB fanBox = getEffectAABB(player);
				Vec3d lookVec = player.getLookVec();

				// particle effect
				for (int i = 0; i < 30; i++) {
					world.addParticle(ParticleTypes.CLOUD, fanBox.minX + world.rand.nextFloat() * (fanBox.maxX - fanBox.minX),
							fanBox.minY + world.rand.nextFloat() * (fanBox.maxY - fanBox.minY),
							fanBox.minZ + world.rand.nextFloat() * (fanBox.maxZ - fanBox.minZ),
							lookVec.x, lookVec.y, lookVec.z);
				}

			}

			player.playSound(SoundEvents.ENTITY_PLAYER_BREATH, 1.0F + random.nextFloat(), random.nextFloat() * 0.7F + 0.3F);
		}

		player.setActiveHand(hand);

		return new ActionResult<>(ActionResultType.SUCCESS, player.getHeldItem(hand));
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

		fanBlocksInAABB(world, player, fanBox);

		fanEntitiesInAABB(world, player, fanBox);

		return 1;
	}

	private void fanEntitiesInAABB(World world, PlayerEntity player, AxisAlignedBB fanBox) {
		Vec3d moveVec = player.getLookVec().scale(2);

		for (Entity entity : world.getEntitiesWithinAABB(Entity.class, fanBox)) {
			if (entity.canBePushed() || entity instanceof ItemEntity) {
//				entity.motionX = moveVec.x;
//				entity.motionY = moveVec.y;
//				entity.motionZ = moveVec.z;
				entity.setMotion(moveVec.x, moveVec.y, moveVec.z);
			}
		}

	}

	private AxisAlignedBB getEffectAABB(PlayerEntity player) {
		double range = 3.0D;
		double radius = 2.0D;
		Vec3d srcVec = new Vec3d(player.getX(), player.getY() + player.getEyeHeight(), player.getZ());
		Vec3d lookVec = player.getLookVec().scale(range);
		Vec3d destVec = srcVec.add(lookVec.x, lookVec.y, lookVec.z);

		return new AxisAlignedBB(destVec.x - radius, destVec.y - radius, destVec.z - radius, destVec.x + radius, destVec.y + radius, destVec.z + radius);
	}

	private int fanBlocksInAABB(World world, PlayerEntity player, AxisAlignedBB box) {
		int fan = 0;
		for (BlockPos pos : WorldUtil.getAllInBB(box)) {
			fan += fanBlock(world, player, pos);
		}
		return fan;
	}

	private int fanBlock(World world, PlayerEntity player, BlockPos pos) {
		int cost = 0;

		BlockState state = world.getBlockState(pos);

		if (state.getBlock() != Blocks.AIR) {
			if (state.getBlock() instanceof FlowerBlock) {
				if (state.getBlock().canHarvestBlock(state, world, pos, player) && random.nextInt(3) == 0) {
					state.getBlock().harvestBlock(world, player, pos, state, world.getTileEntity(pos), ItemStack.EMPTY);
					world.destroyBlock(pos, false);
				}
			}
		}

		return cost;
	}
}
