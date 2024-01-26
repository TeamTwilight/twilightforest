package twilightforest.block;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ArcticFurBlock extends Block {
	private static final MutableComponent TOOLTIP = Component.translatable("block.twilightforest.arctic_fur_block.desc").withStyle(ChatFormatting.GRAY);

	public ArcticFurBlock(Properties properties) {
		super(properties);
	}

	@Override
	public float getDestroyProgress(BlockState state, Player player, BlockGetter getter, BlockPos pos) {
		// ItemShears#getDestroySpeed is really dumb and doesn't check IShearable so we have to do it this way to try to match the wool break speed with shears
		return player.getMainHandItem().getItem() instanceof ShearsItem ? 0.2F : super.getDestroyProgress(state, player, getter, pos);
	}

	@Override
	public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
		entity.causeFallDamage(fallDistance, 0.1F, level.damageSources().fall());
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable BlockGetter getter, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(TOOLTIP);
	}
}
