package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import twilightforest.item.ItemTFMazebreakerPick;

import javax.annotation.Nullable;

/**
 * Castle block makes a castle
 *
 * @author Ben
 */
public class BlockTFCastleBlock extends Block {

	public BlockTFCastleBlock(MaterialColor color) {
		super(Properties.create(Material.ROCK, color).setRequiresTool().hardnessAndResistance(100.0F, 35.0F).sound(SoundType.STONE));
	}

	@Override
	public void harvestBlock(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity te, ItemStack stack) {
		ItemStack cei = player.getHeldItemMainhand();
		if (cei.getItem() instanceof ToolItem && !(cei.getItem() instanceof ItemTFMazebreakerPick)) {
			cei.damageItem(16, player, (user) -> user.sendBreakAnimation(Hand.MAIN_HAND));
		}

		super.harvestBlock(world, player, pos, state, te, stack);
	}
}
