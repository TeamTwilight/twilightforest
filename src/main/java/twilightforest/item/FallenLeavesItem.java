package twilightforest.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;

// [VanillaCopy] of PlaceOnWaterBlockItem, which delegates useOn to BlockItem when the block at the clicked pos is a FallenLeavesBlock instead of always returning InteractionResult.PASS.
public class FallenLeavesItem extends BlockItem {
	public FallenLeavesItem(Block block, Properties properties) {
		super(block, properties);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		return context.getLevel().getBlockState(context.getClickedPos()).is(this.getBlock()) ? super.useOn(context) : InteractionResult.PASS;
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		BlockHitResult hitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
		BlockHitResult blockAboveResult = hitResult.withPosition(hitResult.getBlockPos().above());
		return super.useOn(new UseOnContext(player, hand, blockAboveResult));
	}
}
