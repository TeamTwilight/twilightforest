package twilightforest.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.apache.commons.lang3.text.WordUtils;
import org.jetbrains.annotations.Nullable;
import twilightforest.block.AbstractSkullCandleBlock;
import twilightforest.client.ISTER;
import twilightforest.init.TFBlocks;

import java.util.List;
import java.util.function.Consumer;

public class SkullCandleItem extends StandingAndWallBlockItem {

	public SkullCandleItem(AbstractSkullCandleBlock floor, AbstractSkullCandleBlock wall, Properties properties) {
		super(floor, wall, properties, Direction.DOWN);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		if (stack.hasTag()) {
			CompoundTag tag = stack.getTagElement("BlockEntityTag");
			if (tag != null) {
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
	}

	@Override
	public Component getName(ItemStack stack) {
		if (stack.is(TFBlocks.PLAYER_SKULL_CANDLE.asItem()) && stack.hasTag()) {
			String s = null;
			CompoundTag compoundtag = stack.getTag();
			if (compoundtag != null && compoundtag.contains("SkullOwner", 8)) {
				s = compoundtag.getString("SkullOwner");
			} else if (compoundtag != null && compoundtag.contains("SkullOwner", 10)) {
				CompoundTag compoundtag1 = compoundtag.getCompound("SkullOwner");
				if (compoundtag1.contains("Name", 8)) {
					s = compoundtag1.getString("Name");
				}
			}

			if (s != null) {
				return Component.translatable(this.getDescriptionId() + ".named", s);
			}
		}

		return super.getName(stack);
	}

	@Override
	public void verifyTagAfterLoad(CompoundTag tag) {
		super.verifyTagAfterLoad(tag);
		SkullBlockEntity.resolveGameProfile(tag);
	}

	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(ISTER.CLIENT_ITEM_EXTENSION);
	}
}