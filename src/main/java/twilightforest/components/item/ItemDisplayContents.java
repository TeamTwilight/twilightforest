package twilightforest.components.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import twilightforest.TFRegistries;
import twilightforest.item.travellers_gear.modifiers.display.ItemDisplayType;
import twilightforest.util.TFItemStackUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.function.Consumer;

public class ItemDisplayContents implements TooltipComponent {

	public static final ItemDisplayContents EMPTY = new ItemDisplayContents(TFRegistries.ITEM_DISPLAY_TYPE.size());
	public static final Codec<ItemDisplayContents> CODEC = DisplaySlot.CODEC.listOf().xmap(ItemDisplayContents::fromSlots, ItemDisplayContents::asSlots);
	public static final StreamCodec<RegistryFriendlyByteBuf, ItemDisplayContents> STREAM_CODEC = ItemStack.OPTIONAL_STREAM_CODEC.apply(ByteBufCodecs.list()).map(ItemDisplayContents::new, contents -> contents.items);
	final NonNullList<ItemStack> items;

	private ItemDisplayContents(int size) {
		this.items = NonNullList.withSize(size, ItemStack.EMPTY);
	}

	private ItemDisplayContents(List<ItemStack> items) {
		this.items = NonNullList.copyOf(items);
	}

	private void copyInto(NonNullList<ItemStack> list) {
		for (int i = 0; i < list.size(); i++) {
			ItemStack itemstack = i < this.items.size() ? this.items.get(i) : ItemStack.EMPTY;
			list.set(i, itemstack.copy());
		}
	}

	private static ItemDisplayContents fromSlots(List<DisplaySlot> slots) {
		OptionalInt optionalint = slots.stream().mapToInt(DisplaySlot::index).max();
		if (optionalint.isEmpty()) {
			return EMPTY;
		} else {
			ItemDisplayContents contents = new ItemDisplayContents(optionalint.getAsInt() + 1);

			for (DisplaySlot slot : slots) {
				contents.items.set(slot.index(), slot.item());
			}

			return contents;
		}
	}

	private List<DisplaySlot> asSlots() {
		List<DisplaySlot> list = new ArrayList<>();

		for (int i = 0; i < this.items.size(); i++) {
			ItemStack itemstack = this.items.get(i);
			if (!itemstack.isEmpty()) {
				list.add(new DisplaySlot(i, itemstack));
			}
		}

		return list;
	}

	public NonNullList<ItemStack> items() {
		return this.items;
	}

	public int size() {
		return this.items.size();
	}

	public boolean isEmpty() {
		return this.items.isEmpty();
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		} else {
			return other instanceof ItemDisplayContents contents && ItemStack.listMatches(this.items, contents.items);
		}
	}

	@Override
	public int hashCode() {
		return ItemStack.hashStackList(this.items);
	}

	@Override
	public String toString() {
		return "ItemDisplayContents" + this.items;
	}

	public static class Mutable {
		private final NonNullList<ItemStack> items;

		public Mutable(ItemDisplayContents contents) {
			this.items = NonNullList.withSize(TFRegistries.ITEM_DISPLAY_TYPE.size(), ItemStack.EMPTY);
			contents.copyInto(this.items);
		}

		private int findFreeSlot(ItemStack stack) {
			for (ItemDisplayType type : TFRegistries.ITEM_DISPLAY_TYPE) {
				if (type.validItems().test(stack)) {
					return TFRegistries.ITEM_DISPLAY_TYPE.getId(type);
				}
			}
			return -1;
		}

		public boolean trySwap(SlotAccess source, Player player) {
			return this.trySwap(source, remainder -> TFItemStackUtils.giveOrDrop(remainder, player));
		}

		public boolean trySwap(SlotAccess source, Consumer<ItemStack> remainder) {
			ItemStack slottedStack = source.get();
			if (slottedStack.isEmpty() || !slottedStack.getItem().canFitInsideContainerItems())
				return false;

			int slotForStack = this.findFreeSlot(slottedStack);
			if (slotForStack < 0 || ItemStack.isSameItemSameComponents(slottedStack, this.items.get(slotForStack)))
				return false;

			ItemStack insert = slottedStack.split(1);
			ItemStack replaced = this.items.set(slotForStack, insert);

			if (replaced.isEmpty()) {
				return source.set(slottedStack);
			} else {
				boolean ret = source.set(replaced);
				remainder.accept(slottedStack);
				return ret;
			}
		}

		public boolean tryInsert(ItemStack stack) {
			if (!stack.isEmpty() && stack.getItem().canFitInsideContainerItems()) {
				int j = this.findFreeSlot(stack);
				if (j != -1 && this.items.get(j).isEmpty()) {
					this.items.set(j, stack.split(1));
					return true;
				}
			}
			return false;
		}

		@Nullable
		public ItemStack removeFirstFree() {
			for (int i = 0; i < this.items.size(); i++) {
				if (!this.items.get(i).isEmpty()) {
					return this.items.set(i, ItemStack.EMPTY);
				}
			}
			return null;
		}

		public ItemDisplayContents toImmutable() {
			return new ItemDisplayContents(this.items);
		}
	}

	private record DisplaySlot(int index, ItemStack item) {
		public static final Codec<DisplaySlot> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.INT.fieldOf("slot").forGetter(DisplaySlot::index),
			ItemStack.CODEC.fieldOf("item").forGetter(DisplaySlot::item)
		).apply(instance, DisplaySlot::new));
	}
}
