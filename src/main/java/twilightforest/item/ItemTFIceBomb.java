package twilightforest.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import twilightforest.TFSounds;
import twilightforest.entity.TFEntities;
import twilightforest.entity.boss.EntityTFIceBomb;

public class ItemTFIceBomb extends Item {

	public ItemTFIceBomb(Properties props) {
		super(props);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
		player.playSound(TFSounds.ICEBOMB_FIRED, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));

		if (!world.isRemote) {
			if (!player.abilities.isCreativeMode) {
				player.getHeldItem(hand).shrink(1);
			}
			EntityTFIceBomb ice = new EntityTFIceBomb(TFEntities.thrown_ice, world, player);
			ice.setDirectionAndMovement(player, player.rotationPitch, player.rotationYaw, -20.0F, 0.75F, 1.0F);
			world.addEntity(ice);
		}

		return new ActionResult<>(ActionResultType.SUCCESS, player.getHeldItem(hand));
	}
}
