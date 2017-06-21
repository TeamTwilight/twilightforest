//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package twilightforest.client.texture.render;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import team.chisel.ctm.client.util.CTMLogic;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class CTMLogicReversed extends CTMLogic {
	public static CTMLogicReversed getInstance() {
		return new CTMLogicReversed();
	}

	@Override
	public boolean isConnected(IBlockAccess world, BlockPos current, BlockPos connection, EnumFacing dir, IBlockState state) {
		return super.isConnected(world, current, current.add(current.subtract(connection)), dir, state);
	}
}
