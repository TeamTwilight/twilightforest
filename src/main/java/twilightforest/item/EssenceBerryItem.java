package twilightforest.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
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
	public InteractionResult use(Level level, Player player, InteractionHand usedHand) {
		ItemStack stack = player.getItemInHand(usedHand);
		stack.consume(1, player);
		if (!level.isClientSide()) {
			ExperienceOrb orb = new ExperienceOrb(level, player.getX(), player.getY() + 1, player.getZ(), 6 + level.getRandom().nextInt(14));
			level.addFreshEntity(orb);
		}

		return InteractionResult.SUCCESS;
	}
}
