package twilightforest.item;

import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.item.component.ResolvableProfile;
import twilightforest.block.AbstractSkullCandleBlock;

public class SkullCandleItem extends StandingAndWallBlockItem {

	public SkullCandleItem(AbstractSkullCandleBlock floor, AbstractSkullCandleBlock wall, Properties properties) {
		super(floor, wall, Direction.DOWN, properties);
	}

	@Override
	public Component getName(ItemStack stack) {
		ResolvableProfile resolvableprofile = stack.get(DataComponents.PROFILE);
		return resolvableprofile != null && resolvableprofile.name().isPresent()
			? Component.translatable(this.getDescriptionId() + ".named", resolvableprofile.name().get())
			: super.getName(stack);
	}
}