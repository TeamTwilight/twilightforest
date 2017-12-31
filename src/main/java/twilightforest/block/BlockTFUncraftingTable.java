package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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
}
