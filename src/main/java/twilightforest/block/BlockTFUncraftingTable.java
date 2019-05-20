package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.TwilightForestMod;
import twilightforest.client.ModelRegisterCallback;
import twilightforest.item.TFItems;

public class BlockTFUncraftingTable extends Block implements ModelRegisterCallback {

	protected BlockTFUncraftingTable() {
		super(Material.WOOD);
		this.setHardness(2.5F);
		this.setSoundType(SoundType.WOOD);
		this.setCreativeTab(TFItems.creativeTab);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (world.isRemote)
			return true;

		player.openGui(TwilightForestMod.instance, 1, world, pos.getX(), pos.getY(), pos.getZ());
		player.addStat(StatList.CRAFTING_TABLE_INTERACTION);
		return true;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModel() {
		final ModelResourceLocation mrl = Loader.isModLoaded("ctm") ? new ModelResourceLocation(this.getRegistryName(), "ctm") : new ModelResourceLocation(this.getRegistryName(), "normal");

		ModelLoader.setCustomStateMapper(this, new StateMapperBase() {
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
				return mrl;
			}
		});

		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, mrl);
	}
}
