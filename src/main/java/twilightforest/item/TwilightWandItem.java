package twilightforest.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import twilightforest.entity.projectile.TwilightWandBolt;
import twilightforest.init.TFItems;
import twilightforest.init.TFSounds;

public class TwilightWandItem extends ScepterItem {

	public TwilightWandItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult performScepterAction(Level level, ItemStack stack, Player player, InteractionHand hand) {
		player.playSound(TFSounds.TWILIGHT_SCEPTER_USE.get(), 1.0F, (level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 0.2F + 1.0F);

		if (!level.isClientSide()) {
			level.addFreshEntity(new TwilightWandBolt(level, player));
			if (!player.isCreative() && (!player.getItemBySlot(EquipmentSlot.HEAD).is(TFItems.MYSTIC_CROWN) || level.getRandom().nextFloat() > 0.05f)) {
				stack.hurtWithoutBreaking(1, player);
			}
		}
		return InteractionResult.SUCCESS;
	}
}