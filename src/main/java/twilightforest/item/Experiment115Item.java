package twilightforest.item;

import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import twilightforest.init.TFStats;

import static twilightforest.TwilightForestMod.prefix;

public class Experiment115Item extends BlockItem {
	public static final Identifier THINK = prefix("think");
	public static final Identifier FULL = prefix("full");

	public Experiment115Item(Block block, Properties properties) {
		super(block, properties);
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
		if (entity instanceof ServerPlayer player) {
			player.awardStat(TFStats.E115_SLICES_EATEN.get());
		}
		return super.finishUsingItem(stack, level, entity);
	}
}