package twilightforest.item;

import com.mojang.authlib.GameProfile;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import twilightforest.block.AbstractSkullCandleBlock;
import twilightforest.block.TFBlocks;
import twilightforest.block.entity.SkullCandleBlockEntity;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class SkullCandleItem extends StandingAndWallBlockItem {

	public SkullCandleItem(AbstractSkullCandleBlock floor, AbstractSkullCandleBlock wall, Properties properties) {
		super(floor, wall, properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag) {
		if(stack.hasTag()) {
			CompoundTag tag = stack.getTagElement("BlockEntityTag");
			if (tag != null) {
				if (tag.contains("CandleColor") && tag.contains("CandleAmount")) {
					tooltip.add(
							new TranslatableComponent(tag.getInt("CandleAmount") > 1 ?
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
	public Component getName(ItemStack pStack) {
		if (pStack.is(TFBlocks.PLAYER_SKULL_CANDLE.get().asItem()) && pStack.hasTag()) {
			String s = null;
			CompoundTag compoundtag = pStack.getTag();
			if (compoundtag.contains("SkullOwner", 8)) {
				s = compoundtag.getString("SkullOwner");
			} else if (compoundtag.contains("SkullOwner", 10)) {
				CompoundTag compoundtag1 = compoundtag.getCompound("SkullOwner");
				if (compoundtag1.contains("Name", 8)) {
					s = compoundtag1.getString("Name");
				}
			}

			if (s != null) {
				return new TranslatableComponent(this.getDescriptionId() + ".named", s);
			}
		}

		return super.getName(pStack);
	}

	@Override
	public void verifyTagAfterLoad(CompoundTag pCompoundTag) {
		super.verifyTagAfterLoad(pCompoundTag);
		if (pCompoundTag.contains("SkullOwner", 8) && !StringUtils.isBlank(pCompoundTag.getString("SkullOwner"))) {
			GameProfile gameprofile = new GameProfile(null, pCompoundTag.getString("SkullOwner"));
			SkullCandleBlockEntity.updateGameprofile(gameprofile, (p_151177_) -> {
				pCompoundTag.put("SkullOwner", NbtUtils.writeGameProfile(new CompoundTag(), p_151177_));
			});
		}

	}

	@Override
	public boolean canEquip(ItemStack stack, EquipmentSlot armorType, Entity entity) {
		return armorType == EquipmentSlot.HEAD;
	}

	@Override
	@Nullable
	public EquipmentSlot getEquipmentSlot(ItemStack stack) {
		return EquipmentSlot.HEAD;
	}

	@Override
	public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> list) {
		if (allowdedIn(tab)) {
			ItemStack istack = new ItemStack(this);
			CompoundTag tag = new CompoundTag();
			tag.putInt("CandleAmount", 1);
			tag.putInt("CandleColor", 0);
			istack.addTagElement("BlockEntityTag", tag);
			list.add(istack);
		}
	}
}
