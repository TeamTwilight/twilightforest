package twilightforest.block;

import net.minecraft.block.BlockBreakable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import twilightforest.client.ModelRegisterCallback;
import twilightforest.item.TFItems;

import java.util.Random;

public class BlockTFWispyCloud extends BlockBreakable implements ModelRegisterCallback {

	protected BlockTFWispyCloud() {
		super(new Material(MapColor.SNOW), false);
		this.setSoundType(SoundType.CLOTH);
		this.setCreativeTab(TFItems.creativeTab);
		this.setHardness(0.3F);
	}

	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.TRANSLUCENT;
	}

	@Override
	public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		return true;
	}

	@Override
	public int quantityDropped(Random p_149745_1_) {
		return 0;
	}
}
