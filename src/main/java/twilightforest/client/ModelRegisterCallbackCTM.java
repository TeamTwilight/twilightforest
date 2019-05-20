package twilightforest.client;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;

public interface ModelRegisterCallbackCTM extends ModelRegisterCallback {
	/**
	 * Should ONLY be implemented on Block or its children.
	 */
	@Override
	@SideOnly(Side.CLIENT)
	default void registerModel() {
		ModelLoader.setCustomStateMapper((Block) this, new CTMOptionalStateMapper(this.getIgnoredProperties()));

		this.registerItemModel();
	}

	@SideOnly(Side.CLIENT)
	default void registerItemModel() {
		Block block = (Block) this;
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(block.getRegistryName(), "inventory"));
	}

	@SideOnly(Side.CLIENT)
	default IProperty<?>[] getIgnoredProperties() {
		return new IProperty<?>[0];
	}

	@SideOnly(Side.CLIENT)
	class CTMOptionalStateMapper extends StateMapperBase {
		private final IProperty<?>[] IGNORED_PROPERTIES;

		public CTMOptionalStateMapper(IProperty<?>... ignoredProperties) {
			this.IGNORED_PROPERTIES = ignoredProperties;
		}

		@Override
		protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
			HashMap<IProperty<?>, Comparable<?>> properties = new HashMap<>(state.getProperties());

			for (IProperty<?> ignored : IGNORED_PROPERTIES)
				properties.remove(ignored);

			boolean ctm = Loader.isModLoaded("ctm");

			// If there's no additional properties, then let's cheese it and just fold it into a single blockstate json
			// Otherwise, split to two blockstate jsons
			if (properties.isEmpty())
				return new ModelResourceLocation(Block.REGISTRY.getNameForObject(state.getBlock()), ctm ? "ctm" : "normal");
			else
				return new ModelResourceLocation(ctm ? Block.REGISTRY.getNameForObject(state.getBlock()) + "_ctm" : Block.REGISTRY.getNameForObject(state.getBlock()).toString(), this.getPropertyString(properties));
		}
	}
}
