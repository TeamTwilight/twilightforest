package twilightforest.item;

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
import twilightforest.util.TFStats;

import static twilightforest.TwilightForestMod.prefix;

public class Experiment115Item extends BlockItem {
	public static final ResourceLocation THINK = prefix("think");
	public static final ResourceLocation FULL = prefix("full");

	public Experiment115Item(Block block, Properties props) {
		super(block, props);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Player player = context.getPlayer();
		if(!player.isShiftKeyDown()) {
			InteractionResult actionresulttype = this.place(new BlockPlaceContext(context));
			return !actionresulttype.consumesAction() && this.isEdible() ? this.use(context.getLevel(), context.getPlayer(), context.getHand()).getResult() : actionresulttype;
		}
		return InteractionResult.PASS;
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
		if(entity instanceof ServerPlayer player) {
			player.awardStat(TFStats.E115_SLICES_EATEN);
		}
		return super.finishUsingItem(stack, level, entity);
	}
}
