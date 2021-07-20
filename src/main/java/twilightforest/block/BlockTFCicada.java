package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.ModList;
import twilightforest.TwilightForestMod;
import twilightforest.tileentity.TFTileEntities;

import javax.annotation.Nullable;
import java.util.List;

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

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, IBlockReader world, List<ITextComponent> tooltip, ITooltipFlag flag) {
		super.addInformation(stack, world, tooltip, flag);

		if (ModList.get().isLoaded("immersiveengineering")) {
			tooltip.add(new TranslationTextComponent("block.twilightforest.cicada.desc")
					.mergeStyle(TwilightForestMod.getRarity().color)
					.mergeStyle(TextFormatting.ITALIC));
		}
	}
}
