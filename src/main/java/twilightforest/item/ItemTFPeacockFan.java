package twilightforest.item;

import net.minecraft.block.BlockState;
import net.minecraft.block.FlowerBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.potion.Effects;
import net.minecraft.item.UseAction;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import twilightforest.TFSounds;
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
			if (!player.isOnGround()) {
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
			if (!player.isOnGround() && !player.isPotionActive(Effects.JUMP_BOOST)) {
				player.setMotion(new Vector3d(
						player.getMotion().getX() * 3F,
						1.5F,
						player.getMotion().getZ() * 3F
				));
				player.fallDistance = 0.0F;
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

		fanBlocksInAABB(world, fanBox);

		fanEntitiesInAABB(world, player, fanBox);

		return 1;
	}

	private void fanEntitiesInAABB(World world, PlayerEntity player, AxisAlignedBB fanBox) {
		Vector3d moveVec = player.getLookVec().scale(2);

		for (Entity entity : world.getEntitiesWithinAABB(Entity.class, fanBox)) {
			if (entity.canBePushed() || entity instanceof ItemEntity) {
				entity.setMotion(moveVec.x, moveVec.y, moveVec.z);
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

		if (state.getBlock() instanceof FlowerBlock) {
			if (random.nextInt(3) == 0) {
				world.destroyBlock(pos, true);
			}
		}

		return cost;
	}
}
