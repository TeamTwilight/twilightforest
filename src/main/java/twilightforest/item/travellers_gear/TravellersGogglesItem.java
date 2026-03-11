package twilightforest.item.travellers_gear;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import twilightforest.components.item.ItemDisplayContents;
import twilightforest.init.TFDataComponents;
import twilightforest.init.custom.TravellersModifiersManager;

import java.util.Optional;

public class TravellersGogglesItem extends TravellersArmorItem {

	public TravellersGogglesItem(Properties properties) {
		super(Type.HELMET, properties, 3, 12);
	}

	@Override
	public @NotNull Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
		return !stack.has(DataComponents.HIDE_TOOLTIP) && !stack.has(DataComponents.HIDE_ADDITIONAL_TOOLTIP)
			? Optional.ofNullable(stack.get(TFDataComponents.ITEM_DISPLAY)).map(Tooltip::new)
			: Optional.empty();
	}

	@Override
	public boolean overrideStackedOnOther(ItemStack stack, @NotNull Slot slot, @NotNull ClickAction action, @NotNull Player player) {
		if (stack.getCount() != 1 || action != ClickAction.SECONDARY)
			return false;

		ItemDisplayContents contents = stack.get(TFDataComponents.ITEM_DISPLAY);
		if (contents == null)
			return false;

		ItemDisplayContents.Mutable mutableContents = new ItemDisplayContents.Mutable(contents);
		ItemStack itemstack = slot.getItem();
		if (itemstack.isEmpty()) {
			ItemStack removedStack = mutableContents.removeFirstFree(slot);
			if (removedStack != null) {
				slot.safeInsert(removedStack);
				this.playRemoveOneSound(player);
			}
		} else if (itemstack.canFitInsideContainerItems()) {
			if (mutableContents.trySwap(SlotAccess.of(slot::getItem, slot::set), player))
				this.playInsertSound(player);
		}

		stack.set(TFDataComponents.ITEM_DISPLAY, mutableContents.toImmutable());
		return true;
	}

	@Override
	public boolean overrideOtherStackedOnMe(ItemStack stack, @NotNull ItemStack other, @NotNull Slot slot, @NotNull ClickAction action, @NotNull Player player, @NotNull SlotAccess access) {
		if (stack.getCount() != 1 || action != ClickAction.SECONDARY || !slot.allowModification(player))
			return false;

		ItemDisplayContents contents = stack.get(TFDataComponents.ITEM_DISPLAY);
		if (contents == null)
			return false;

		ItemDisplayContents.Mutable mutableContents = new ItemDisplayContents.Mutable(contents);
		if (other.isEmpty()) {
			ItemStack itemstack = mutableContents.removeFirstFree(null);
			if (itemstack != null) {
				this.playRemoveOneSound(player);
				access.set(itemstack);
			}
		} else {
			if (mutableContents.trySwap(access, player))
				this.playInsertSound(player);
		}

		stack.set(TFDataComponents.ITEM_DISPLAY, mutableContents.toImmutable());
		return true;
	}

	@Override
	public void inventoryTick(@NotNull ItemStack stack, @NotNull Level level, @NotNull Entity entity, int slotId, boolean isSelected) {
		//only tick while on the player's head
		if (slotId != Inventory.INVENTORY_SIZE + EquipmentSlot.HEAD.getIndex())
			return;
		if (level.isClientSide() || !TravellersModifiersManager.isModifierActive(entity, TravellersModifiersManager.ITEM_DISPLAY_MODIFIER))
			return;

		ItemDisplayContents contents = stack.get(TFDataComponents.ITEM_DISPLAY);
		if (contents == null || contents.isEmpty())
			return;

		int mapSlot = contents.findActiveMapSlot();
		if (mapSlot == -1)
			return;

		ItemStack map = contents.items().get(mapSlot);
		if (map.isEmpty() || !(map.getItem() instanceof MapItem mapItem))
			return;

		//mark as selected so map properly updates
		mapItem.inventoryTick(map, level, entity, slotId, true);
		//send update packets here instead as the goggles aren't considered a complex item
		if (entity instanceof ServerPlayer player) {
			Packet<?> packet = mapItem.getUpdatePacket(map, level, player);
			if (packet != null)
				player.connection.send(packet);
		}
	}

	@Override
	public boolean isEnderMask(@NotNull ItemStack stack, @NotNull Player player, @NotNull EnderMan enderman) {
		return TravellersModifiersManager.isModifierActive(player, TravellersModifiersManager.ALL_NIGHT_GOGGLES_MODIFIER);
	}

	private void playRemoveOneSound(Entity entity) {
		entity.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
	}

	private void playInsertSound(Entity entity) {
		entity.playSound(SoundEvents.BUNDLE_INSERT, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
	}

	public record Tooltip(ItemDisplayContents contents) implements TooltipComponent {}
}
