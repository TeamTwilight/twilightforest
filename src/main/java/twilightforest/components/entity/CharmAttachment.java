package twilightforest.components.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.ItemStackWithSlot;
import net.minecraft.world.entity.EntityEquipment;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public record CharmAttachment(List<ItemStackWithSlot> items) {
	public static final MapCodec<CharmAttachment> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
		Codec.list(ItemStackWithSlot.CODEC)
			.fieldOf("items")
			.forGetter(CharmAttachment::items)
	).apply(i, CharmAttachment::new));

	public CharmAttachment {
		items = List.copyOf(items);
	}

	public CharmAttachment() {
		this(List.of());
	}

	public static CharmAttachment fromInventory(Inventory inventory) {
		List<ItemStackWithSlot> items = new ArrayList<>();

		for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
			ItemStack stack = inventory.getItem(slot);

			if (!stack.isEmpty()) {
				items.add(new ItemStackWithSlot(slot, stack.copy()));
			}
		}

		return new CharmAttachment(items);
	}

	public Inventory toInventory(Player player) {
		Inventory inventory = new Inventory(player, new EntityEquipment());

		for (ItemStackWithSlot item : items) {
			inventory.setItem(item.slot(), item.stack().copy());
		}

		return inventory;
	}


	public void applyTo(Inventory inventory) {
		for (ItemStackWithSlot item : items) {
			inventory.setItem(item.slot(), item.stack().copy());
		}
	}

	public boolean isEmpty() {
		return items.isEmpty();
	}
}