package twilightforest.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class EssenceBerryItem extends Item {
	public EssenceBerryItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
		ItemStack stack = player.getItemInHand(usedHand);
		stack.consume(1, player);
		if (!level.isClientSide) {
			ExperienceOrb orb = new ExperienceOrb(level, player.getX(), player.getY() + 1, player.getZ(), 6 + level.random.nextInt(14));
			level.addFreshEntity(orb);
		}

		return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
	}
}
