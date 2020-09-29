package twilightforest.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.world.World;
import twilightforest.block.TFBlocks;

import static twilightforest.TwilightForestMod.prefix;

public class ItemTFExperiment115 extends BlockItem {
	public static final ResourceLocation THINK = prefix("think");
	public static final ResourceLocation FULL = prefix("full");

	public ItemTFExperiment115(Block block, Properties props) {
		super(block, props);
	}

	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		World world = context.getWorld();
		BlockPos pos = context.getPos();
		BlockState state = world.getBlockState(pos);
		PlayerEntity player = context.getPlayer();
		Direction facing = context.getFace();
		BlockItemUseContext blockitemusecontext = new BlockItemUseContext(context);

		// Let eating take priority over block placement
		if (player.canEat(false))
			return ActionResultType.PASS;

		BlockState e115 = TFBlocks.experiment_115.get().getDefaultState();

		if (!state.isReplaceable(blockitemusecontext)) pos = pos.offset(facing);
		ItemStack itemstack = player.getHeldItem(context.getHand());

		if (!itemstack.isEmpty() && player.canPlayerEdit(pos, facing, itemstack) && world.placedBlockCollides(e115, pos, ISelectionContext.dummy())) {
			if (world.setBlockState(pos, e115, 11)) {
				TFBlocks.experiment_115.get().onBlockPlacedBy(world, pos, e115, player, itemstack);
				SoundType soundtype = world.getBlockState(pos).getBlock().getSoundType(world.getBlockState(pos), world, pos, player);
				world.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
				itemstack.shrink(1);
			}

			if (player instanceof ServerPlayerEntity) CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayerEntity) player, pos, itemstack);

			return ActionResultType.SUCCESS;
		} else return super.onItemUse(context);
	}
}
