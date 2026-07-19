package twilightforest.components.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.custom.ItemDisplays;
import twilightforest.item.travellers_gear.modifiers.display.ItemDisplayType;
import twilightforest.util.TFItemStackUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.function.BiConsumer;

public class ItemDisplayContents implements TooltipComponent {
	public static final List<DeferredHolder<ItemDisplayType, ItemDisplayType>> LAYOUT = List.of(ItemDisplays.MAP, ItemDisplays.MAP, ItemDisplays.MAP, ItemDisplays.COMPASS, ItemDisplays.CLOCK, ItemDisplays.MOON_DIAL);
	private static final int FIRST_MAP_SLOT_INDEX = LAYOUT.indexOf(ItemDisplays.MAP);
	public static final ItemDisplayContents EMPTY = new ItemDisplayContents(LAYOUT.size(), FIRST_MAP_SLOT_INDEX);
	public static final Codec<ItemDisplayContents> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		DisplaySlot.CODEC.listOf().fieldOf("slots").forGetter(ItemDisplayContents::asSlots),
		Codec.INT.fieldOf("chosen_map_slot").forGetter(ItemDisplayContents::findActiveMapSlot)
	).apply(instance, ItemDisplayContents::fromSlots));
	public static final StreamCodec<RegistryFriendlyByteBuf, ItemDisplayContents> STREAM_CODEC = StreamCodec.composite(
		ItemStack.OPTIONAL_STREAM_CODEC.apply(ByteBufCodecs.list()), contents -> new ArrayList<>(contents.items),
		ByteBufCodecs.VAR_INT, contents -> contents.chosenMapSlot,
		ItemDisplayContents::new
	);
	final NonNullList<ItemStack> items;
	public final int chosenMapSlot;

	private ItemDisplayContents(int size, int chosenMapSlot) {
		this.items = NonNullList.withSize(size, ItemStack.EMPTY);
		this.chosenMapSlot = chosenMapSlot;
	}

	private ItemDisplayContents(List<ItemStack> items, int chosenMapSlot) {
		this.items = NonNullList.copyOf(items);
		this.chosenMapSlot = chosenMapSlot;
	}

	private void copyInto(NonNullList<ItemStack> list) {
		for (int i = 0; i < list.size(); i++) {
			ItemStack itemstack = i < this.items.size() ? this.items.get(i) : ItemStack.EMPTY;
			list.set(i, itemstack.copy());
		}
	}

	private static ItemDisplayContents fromSlots(List<DisplaySlot> slots, int chosenMapSlot) {
		OptionalInt optionalint = slots.stream().mapToInt(DisplaySlot::index).max();
		if (optionalint.isEmpty()) {
			return EMPTY;
		} else {
			ItemDisplayContents contents = new ItemDisplayContents(optionalint.getAsInt() + 1, chosenMapSlot);

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

	public int findActiveMapSlot() {
		return chosenMapSlot;
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
		return this == other || (other instanceof ItemDisplayContents contents
			&& this.chosenMapSlot == contents.chosenMapSlot
			&& ItemStack.listMatches(this.items, contents.items));
	}

	@Override
	public int hashCode() {
		int result = 0;

		for (ItemStack stack : this.items) {
			result = result * 31 + ItemStack.hashItemAndComponents(stack);
		}

		return 31 * result + this.chosenMapSlot;
	}

	@Override
	public String toString() {
		return "ItemDisplayContents" + this.items + "chosenMapSlot" + this.chosenMapSlot;
	}

	public static class Mutable {
		private final NonNullList<ItemStack> items;
		private int chosenMapSlot;

		public Mutable(ItemDisplayContents contents) {
			this.items = NonNullList.withSize(LAYOUT.size(), ItemStack.EMPTY);
			this.chosenMapSlot = contents.chosenMapSlot;
			contents.copyInto(this.items);
		}

		public int chosenMapSlot() {
			return this.chosenMapSlot;
		}

		private int findSwapSlot(ItemStack stack) {
			for (int i = 0; i < LAYOUT.size(); i++) {
				if (LAYOUT.get(i).get().validItems().test(stack)) {
					return i;
				}
			}
			return -1;
		}

		private int findInsertSlot(ItemStack stack) {
			for (int i = 0; i < LAYOUT.size(); i++) {
				if (LAYOUT.get(i).get().validItems().test(stack) && this.items.get(i).isEmpty()) {
					return i;
				}
			}
			return -1;
		}

		public boolean trySwap(SlotAccess source, Player player) {
			return this.trySwap(source, player, TFItemStackUtils::giveOrDrop);
		}

		public boolean trySwap(SlotAccess source, Player player, BiConsumer<ItemStack, Player> remainder) {
			ItemStack slottedStack = source.get();
			if (slottedStack.isEmpty() || !slottedStack.canFitInsideContainerItems()) {
				return false;
			}

			int slotForStack = this.findInsertSlot(slottedStack);
			if (slotForStack < 0) {
				slotForStack = this.findSwapSlot(slottedStack);
			}
			if (slotForStack < 0) {
				return false;
			}

			ItemStack targetStack = this.items.get(slotForStack);
			if (!targetStack.isEmpty() && ItemStack.isSameItemSameComponents(slottedStack, targetStack)) {
				return false;
			}

			ItemStack insert = slottedStack.split(1);
			ItemStack replaced = this.items.set(slotForStack, insert);

			if (replaced.isEmpty()) {
				tryResetChosenMapSlot(slotForStack);
				return source.set(slottedStack);
			} else {
				boolean ret = source.set(replaced);
				remainder.accept(slottedStack, player);
				return ret;
			}
		}

		@Nullable
		public ItemStack removeFirstFree(@Nullable Slot slot) {
			for (int i = 0; i < this.items.size(); i++) {
				ItemStack itemstack = this.items.get(i);
				if (!itemstack.isEmpty() && (slot == null || slot.mayPlace(itemstack))) {
					if (i == chosenMapSlot)
						cycleChosenMapSlot();
					return this.items.set(i, ItemStack.EMPTY);
				}
			}
			return null;
		}

		public ItemDisplayContents toImmutable() {
			return new ItemDisplayContents(this.items, this.chosenMapSlot);
		}

		public int cycleChosenMapSlot() {
			for (int index = this.chosenMapSlot + 1; index < this.items.size(); index++) {
				if (LAYOUT.get(index) == ItemDisplays.MAP && !this.items.get(index).isEmpty()) {
					this.chosenMapSlot = index;
					return this.chosenMapSlot;
				}
			}
			this.chosenMapSlot = -1;
			return this.chosenMapSlot;
		}

		private void tryResetChosenMapSlot(int index) {
			if (index < LAYOUT.size() && LAYOUT.get(index) == ItemDisplays.MAP && !hasOtherMaps(index)) {
				this.chosenMapSlot = index;
			}
		}

		private boolean hasOtherMaps(int mapIndex) {
			for (int i = 0; i < Math.min(items.size(), LAYOUT.size()); i++) {
				if (!items.get(i).isEmpty() && LAYOUT.get(i) == ItemDisplays.MAP && mapIndex != i)
					return true;
			}
			return false;
		}
	}

	private record DisplaySlot(int index, ItemStack item) {
		public static final Codec<DisplaySlot> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.INT.fieldOf("slot").forGetter(DisplaySlot::index),
			ItemStack.CODEC.fieldOf("item").forGetter(DisplaySlot::item)
		).apply(instance, DisplaySlot::new));
	}
}
