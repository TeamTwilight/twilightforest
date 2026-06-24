package twilightforest.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import twilightforest.init.TFDataAttachments;
import twilightforest.init.TFSounds;

public class FortificationWandItem extends ScepterItem {

	public FortificationWandItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult performScepterAction(Level level, ItemStack stack, Player player, InteractionHand hand) {
		if (!level.isClientSide()) {
			player.getData(TFDataAttachments.FORTIFICATION_SHIELDS).setShields(player, 5, true);
			if (!player.isCreative()) {
				stack.hurtWithoutBreaking(1, player);
			}
		}
		player.playSound(TFSounds.SHIELD_ADD.get(), 1.0F, (player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.2F + 1.0F);

		if (!player.isCreative()) {
			player.getCooldowns().addCooldown(stack, 1200);
		}
		return InteractionResult.SUCCESS;
	}
}