package twilightforest.item;

import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import tamaized.beanification.Autowired;
import tamaized.beanification.Configurable;
import twilightforest.action.PeacockFeatherFanAction;
import twilightforest.init.TFDataAttachments;

@Configurable
public class PeacockFanItem extends Item {

	@Autowired
	private PeacockFeatherFanAction peacockFeatherFanAction;

	public PeacockFanItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);

		boolean flag = !player.onGround() && !player.isSwimming() && !player.getData(TFDataAttachments.FEATHER_FAN);

		if (!level.isClientSide()) {
			int fanned = peacockFeatherFanAction.fan(level, player.getEyePosition(), player.getLookAngle(), player, stack);
			stack.hurtAndBreak(fanned + 1, player, hand);
			if (flag) {
				player.setData(TFDataAttachments.FEATHER_FAN, true);
			} else {
				peacockFeatherFanAction.spawnParticles(level, player.getEyePosition(), player.getLookAngle());
			}
			peacockFeatherFanAction.playWhoosh(level, player.blockPosition(), SoundSource.PLAYERS);
		} else {
			if (player.isFallFlying()) {
				Vec3 look = player.getLookAngle();
				Vec3 movement = player.getDeltaMovement();
				//add a directional boost similar to the rocket, but slightly faster and always add a little more upwards
				player.setDeltaMovement(movement.add(
					look.x() * 0.1D + (look.x() * 2.0D - movement.x()) * 0.5D,
					(look.y() * 0.1D + (look.y() * 2.0D - movement.y()) * 0.5D) + 1.25D,
					look.z() * 0.1D + (look.z() * 2.0D - movement.z()) * 0.5D
				));
			}
			// jump if the player is in the air
			if (flag) {
				player.setDeltaMovement(new Vec3(
					player.getDeltaMovement().x() * 1.05F,
					1.5F,
					player.getDeltaMovement().z() * 1.05F
				));
			}
			return InteractionResult.SUCCESS;
		}

		player.startUsingItem(hand);

		return InteractionResult.PASS;
	}

	@Override
	public ItemUseAnimation getUseAnimation(ItemStack stack) {
		return ItemUseAnimation.NONE;
	}

	@Override
	public int getUseDuration(ItemStack stack, LivingEntity user) {
		return 20;
	}
}
