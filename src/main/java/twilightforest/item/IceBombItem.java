package twilightforest.item;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import twilightforest.TFSounds;
import twilightforest.entity.TFEntities;
import twilightforest.entity.projectile.IceBomb;

public class IceBombItem extends Item {

	public IceBombItem(Properties props) {
		super(props);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		player.playSound(TFSounds.ICEBOMB_FIRED, 0.5F, 0.4F / (world.random.nextFloat() * 0.4F + 0.8F));

		if (!world.isClientSide) {
			if (!player.getAbilities().instabuild) {
				player.getItemInHand(hand).shrink(1);
			}
			IceBomb ice = new IceBomb(TFEntities.THROWN_ICE, world, player);
			ice.shootFromRotation(player, player.getXRot(), player.getYRot(), -20.0F, 0.75F, 1.0F);
			world.addFreshEntity(ice);
		}

		return new InteractionResultHolder<>(InteractionResult.SUCCESS, player.getItemInHand(hand));
	}
}
