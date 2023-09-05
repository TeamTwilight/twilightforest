package twilightforest.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import twilightforest.entity.projectile.IceBomb;
import twilightforest.init.TFEntities;
import twilightforest.init.TFSounds;

public class IceBombItem extends Item {

	public IceBombItem(Properties props) {
		super(props);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		player.playSound(TFSounds.ICE_BOMB_FIRED.get(), 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));

		if (!level.isClientSide()) {
			if (!player.getAbilities().instabuild) {
				player.getItemInHand(hand).shrink(1);
			}
			IceBomb ice = new IceBomb(TFEntities.THROWN_ICE.get(), level, player);
			ice.shootFromRotation(player, player.getXRot(), player.getYRot(), -20.0F, 0.75F, 1.0F);
			level.addFreshEntity(ice);
		}

		return new InteractionResultHolder<>(InteractionResult.SUCCESS, player.getItemInHand(hand));
	}
}