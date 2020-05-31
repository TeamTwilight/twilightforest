package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import twilightforest.tileentity.TFTileEntities;

import javax.annotation.Nullable;

public class BlockTFCicada extends BlockTFCritter {

	protected BlockTFCicada(Block.Properties props) {
		super(props);
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return TFTileEntities.CICADA.get().create();
	}

	@Override
	public ItemStack getSquishResult() {
		return new ItemStack(Items.GRAY_DYE, 1);
	}

	//Atomic: Forge would like to get rid of registerTESRItemStack, but there's no alternative yet (as at 1.11)
	//TODO 1.14: Something may have changed, look into this when we compile
//	@SuppressWarnings("deprecation")
//	@OnlyIn(Dist.CLIENT)
//	@Override
//	public void registerModel() {
//		ModelLoader.setCustomStateMapper(this, new StateMap.Builder().ignore(BlockDirectional.FACING).build());
//		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
//		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(this), 0, TileEntityTFCicadaTicking.class);
//	}

	//TODO: Immersive Engineering is unavailable
	/*@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
		super.addInformation(stack, world, tooltip, flag);

		if (TFCompat.IMMERSIVEENGINEERING.isActivated()) {
			tooltip.add(TextFormatting.ITALIC.toString() + TwilightForestMod.getRarity().color.toString() + I18n.translateToLocalFormatted("tile.twilightforest.Cicada.desc"));
		}
	}*/
}
