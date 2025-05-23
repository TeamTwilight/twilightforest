package twilightforest.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import org.apache.commons.lang3.text.WordUtils;
import twilightforest.block.AbstractSkullCandleBlock;

import java.util.List;

public class SkullCandleItem extends StandingAndWallBlockItem {

	public SkullCandleItem(AbstractSkullCandleBlock floor, AbstractSkullCandleBlock wall, Properties properties) {
		super(floor, wall, Direction.DOWN, properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		CustomData data = stack.get(DataComponents.BLOCK_ENTITY_DATA);
		if (data != null && !data.isEmpty()) {
			CompoundTag tag = data.copyTag();
			if (tag.contains("CandleColor") && tag.contains("CandleAmount")) {
				tooltip.add(
					Component.translatable(tag.getInt("CandleAmount") > 1 ?
								"item.twilightforest.skull_candle.desc.multiple" :
								"item.twilightforest.skull_candle.desc",
							String.valueOf(tag.getInt("CandleAmount")),
							WordUtils.capitalize(AbstractSkullCandleBlock.CandleColors.colorFromInt(tag.getInt("CandleColor")).getSerializedName()
								.replace("\"", "").replace("_", " ")))
						.withStyle(ChatFormatting.GRAY));
			}
		}
	}

	@Override
	public Component getName(ItemStack stack) {
		ResolvableProfile resolvableprofile = stack.get(DataComponents.PROFILE);
		return resolvableprofile != null && resolvableprofile.name().isPresent()
			? Component.translatable(this.getDescriptionId() + ".named", resolvableprofile.name().get())
			: super.getName(stack);
	}

	@Override
	public void verifyComponentsAfterLoad(ItemStack stack) {
		ResolvableProfile resolvableprofile = stack.get(DataComponents.PROFILE);
		if (resolvableprofile != null && !resolvableprofile.isResolved()) {
			resolvableprofile.resolve().thenAcceptAsync(profile -> stack.set(DataComponents.PROFILE, profile), SkullBlockEntity.CHECKED_MAIN_THREAD_EXECUTOR);
		}
	}
}