package twilightforest.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.ModList;
import twilightforest.compat.CuriosCompat;
import twilightforest.compat.TFCompat;

import javax.annotation.Nullable;

public class TrophyItem extends StandingAndWallBlockItem {

	public TrophyItem(Block floorBlockIn, Block wallBlockIn, Item.Properties builder) {
		super(floorBlockIn, wallBlockIn, builder);
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

	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
		if(ModList.get().isLoaded(TFCompat.CURIOS_ID)) {
			CuriosCompat.setupCuriosCapability(stack);
		}
		return super.initCapabilities(stack, nbt);
	}
}
