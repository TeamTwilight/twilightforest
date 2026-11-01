package twilightforest.forge.registries;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.RegistryObject;

/** Un {@link DeferredHolder} d'objet, qui sait aussi se rendre en pile. */
public class DeferredItem<T extends Item> extends DeferredHolder<Item, T> implements ItemLike {
	protected DeferredItem(RegistryObject<T> object) {
		super(sourceOf(object));
	}

	@Override
	public Item asItem() {
		return this.get();
	}

	public ItemStack toStack() {
		return this.toStack(1);
	}

	public ItemStack toStack(int count) {
		return new ItemStack(this.get(), count);
	}
}
