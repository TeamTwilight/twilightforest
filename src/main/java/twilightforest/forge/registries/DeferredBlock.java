package twilightforest.forge.registries;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

/** Un {@link DeferredHolder} de bloc, utilisable partout ou le jeu attend un {@link ItemLike}. */
public class DeferredBlock<T extends Block> extends DeferredHolder<Block, T> implements ItemLike {
	protected DeferredBlock(RegistryObject<T> object) {
		super(sourceOf(object));
	}

	@Override
	public Item asItem() {
		return this.get().asItem();
	}

	/** Une pile de l'objet correspondant au bloc — pratique pour garnir les onglets creatifs. */
	public ItemStack toStack() {
		return this.toStack(1);
	}

	public ItemStack toStack(int count) {
		return new ItemStack(this.asItem(), count);
	}
}
