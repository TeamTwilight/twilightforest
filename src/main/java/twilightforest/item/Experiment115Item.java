package twilightforest.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import twilightforest.init.TFStats;

import static twilightforest.TwilightForestMod.prefix;

public class Experiment115Item extends BlockItem {
	public static final ResourceLocation THINK = prefix("think");
	public static final ResourceLocation FULL = prefix("full");

	public Experiment115Item(Block block, Properties properties) {
		super(block, properties);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		if ( context.getLevel().getBlockState(context.getClickedPos()).is(this.getBlock())) return InteractionResult.PASS;
		Player player = context.getPlayer();
		if (player != null && !player.isShiftKeyDown()) {
			InteractionResult result = this.place(new BlockPlaceContext(context));
			return !result.consumesAction() && context.getItemInHand().get(DataComponents.FOOD) != null ? this.use(context.getLevel(), context.getPlayer(), context.getHand()) : result;
		}
		return InteractionResult.PASS;
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
		if (entity instanceof ServerPlayer player) {
			player.awardStat(TFStats.E115_SLICES_EATEN.get());
		}
		return super.finishUsingItem(stack, level, entity);
	}
}