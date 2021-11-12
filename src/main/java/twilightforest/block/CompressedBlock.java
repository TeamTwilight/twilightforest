package twilightforest.block;

import net.minecraft.ChatFormatting;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.Level;
import twilightforest.util.TFDamageSources;

import javax.annotation.Nullable;
import java.util.List;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class CompressedBlock extends Block {

	public CompressedBlock(Properties props) {
		super(props);
	}

	@Override
	@Deprecated
	public float getShadeBrightness(BlockState state, BlockGetter worldIn, BlockPos pos) {
		if (this == TFBlocks.FIERY_BLOCK.get()) {
			return 1.0F;
		}
		return super.getShadeBrightness(state, worldIn, pos);
	}

	@Override
	@Deprecated
	public float getDestroyProgress(BlockState state, Player player, BlockGetter worldIn, BlockPos pos) {
		// ItemShears#getDestroySpeed is really dumb and doesn't check IShearable so we have to do it this way to try to match the wool break speed with shears
		return state.getBlock() == TFBlocks.ARCTIC_FUR_BLOCK.get() && player.getMainHandItem().getItem() instanceof ShearsItem ? 0.2F : super.getDestroyProgress(state, player, worldIn, pos);
	}

	@Override
	public void stepOn(Level worldIn, BlockPos pos, BlockState state, Entity entityIn) {
		if ((!entityIn.fireImmune())
				&& entityIn instanceof LivingEntity
				&& (!EnchantmentHelper.hasFrostWalker((LivingEntity) entityIn))
				&& this == TFBlocks.FIERY_BLOCK.get()) {
			entityIn.hurt(TFDamageSources.FIERY, 1.0F);
		}

		super.stepOn(worldIn, pos, state, entityIn);
	}

	@Override
	public void fallOn(Level worldIn, BlockState state, BlockPos pos, Entity entityIn, float fallDistance) {
		if (this == TFBlocks.STEELEAF_BLOCK.get()) {
			entityIn.causeFallDamage(fallDistance, 0.75F, DamageSource.FALL);
		} else if (this == TFBlocks.ARCTIC_FUR_BLOCK.get()) {
			entityIn.causeFallDamage(fallDistance, 0.1F, DamageSource.FALL);
		}
	}

	@Override
	public boolean isFireSource(BlockState state, LevelReader world, BlockPos pos, Direction side) {
		return this == TFBlocks.FIERY_BLOCK.get();
	}

	@Override
	public boolean isStickyBlock(BlockState state) {
		return this == TFBlocks.CARMINITE_BLOCK.get();
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable BlockGetter worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		if (this == TFBlocks.STEELEAF_BLOCK.get()) {
			tooltip.add(new TranslatableComponent("block.steeleaf.tooltip").withStyle(ChatFormatting.GRAY));
		} else if (this == TFBlocks.ARCTIC_FUR_BLOCK.get()) {
			tooltip.add(new TranslatableComponent("block.arctic.tooltip").withStyle(ChatFormatting.GRAY));
		}
	}
}
