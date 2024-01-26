package twilightforest.dispenser;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.block.AbstractSkullCandleBlock;
import twilightforest.block.LightableBlock;
import twilightforest.block.SkullCandleBlock;
import twilightforest.block.WallSkullCandleBlock;
import twilightforest.block.entity.SkullCandleBlockEntity;
import twilightforest.init.TFBlocks;

public class SkullCandleDispenseBehavior extends OptionalDispenseItemBehavior {

	public SkullCandleDispenseBehavior() {
	}

	@Override
	protected ItemStack execute(BlockSource source, ItemStack stack) {
		ServerLevel level = source.level();
		if (!level.isClientSide()) {
			BlockPos blockpos = source.pos().relative(source.state().getValue(DispenserBlock.FACING));
			this.setSuccess(tryAddCandle(level, blockpos, stack.getItem()) || tryCreateSkullCandle(level, blockpos, stack.getItem()));
			if (this.isSuccess()) {
				stack.shrink(1);
			}
		}

		return stack;
	}

	private static boolean tryAddCandle(ServerLevel level, BlockPos pos, Item candle) {
		if (level.getBlockEntity(pos) instanceof SkullCandleBlockEntity sc) {
			if (candle == AbstractSkullCandleBlock.candleColorToCandle(AbstractSkullCandleBlock.CandleColors.colorFromInt(sc.getCandleColor())).asItem()) {
				if (sc.getCandleAmount() < 4) {
					sc.incrementCandleAmount();
					level.playSound(null, pos, SoundEvents.CANDLE_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
					level.getLightEngine().checkBlock(pos);
					level.sendBlockUpdated(pos, level.getBlockState(pos), level.getBlockState(pos), 1);
					return true;
				}
			}
		}
		return false;
	}

	private static boolean tryCreateSkullCandle(ServerLevel level, BlockPos pos, Item candle) {
		BlockState blockstate = level.getBlockState(pos);
		if (blockstate.getBlock() instanceof AbstractSkullBlock skull) {
			SkullBlock.Types type = (SkullBlock.Types) skull.getType();
			boolean wall = blockstate.getBlock() instanceof WallSkullBlock;
			switch (type) {

				case SKELETON -> {
					if (wall) makeWallSkull(level, pos, TFBlocks.SKELETON_WALL_SKULL_CANDLE.get(), candle);
					else makeFloorSkull(level, pos, TFBlocks.SKELETON_SKULL_CANDLE.get(), candle);
				}
				case WITHER_SKELETON -> {
					if (wall) makeWallSkull(level, pos, TFBlocks.WITHER_SKELE_WALL_SKULL_CANDLE.get(), candle);
					else makeFloorSkull(level, pos, TFBlocks.WITHER_SKELE_SKULL_CANDLE.get(), candle);
				}
				case PLAYER -> {
					if (wall) makeWallSkull(level, pos, TFBlocks.PLAYER_WALL_SKULL_CANDLE.get(), candle);
					else makeFloorSkull(level, pos, TFBlocks.PLAYER_SKULL_CANDLE.get(), candle);
				}
				case ZOMBIE -> {
					if (wall) makeWallSkull(level, pos, TFBlocks.ZOMBIE_WALL_SKULL_CANDLE.get(), candle);
					else makeFloorSkull(level, pos, TFBlocks.ZOMBIE_SKULL_CANDLE.get(), candle);
				}
				case CREEPER -> {
					if (wall) makeWallSkull(level, pos, TFBlocks.CREEPER_WALL_SKULL_CANDLE.get(), candle);
					else makeFloorSkull(level, pos, TFBlocks.CREEPER_SKULL_CANDLE.get(), candle);
				}
				default -> {
					return false;
				}
			}
			return true;
		}

		return false;
	}

	private static void makeFloorSkull(Level level, BlockPos pos, Block newBlock, Item candle) {
		GameProfile profile = null;
		if (level.getBlockEntity(pos) instanceof SkullBlockEntity skull) profile = skull.getOwnerProfile();
		level.setBlockAndUpdate(pos, newBlock.defaultBlockState()
				.setValue(AbstractSkullCandleBlock.LIGHTING, LightableBlock.Lighting.NONE)
				.setValue(SkullCandleBlock.ROTATION, level.getBlockState(pos).getValue(SkullBlock.ROTATION)));
		level.setBlockEntity(new SkullCandleBlockEntity(pos,
				newBlock.defaultBlockState()
						.setValue(AbstractSkullCandleBlock.LIGHTING, LightableBlock.Lighting.NONE)
						.setValue(SkullCandleBlock.ROTATION, level.getBlockState(pos).getValue(SkullBlock.ROTATION)),
				AbstractSkullCandleBlock.candleToCandleColor(candle).getValue(), 1));
		if (level.getBlockEntity(pos) instanceof SkullCandleBlockEntity sc) sc.setOwner(profile);
	}

	private static void makeWallSkull(Level level, BlockPos pos, Block newBlock, Item candle) {
		GameProfile profile = null;
		if (level.getBlockEntity(pos) instanceof SkullBlockEntity skull) profile = skull.getOwnerProfile();
		level.setBlockAndUpdate(pos, newBlock.defaultBlockState()
				.setValue(AbstractSkullCandleBlock.LIGHTING, LightableBlock.Lighting.NONE)
				.setValue(WallSkullCandleBlock.FACING, level.getBlockState(pos).getValue(WallSkullBlock.FACING)));
		level.setBlockEntity(new SkullCandleBlockEntity(pos,
				newBlock.defaultBlockState()
						.setValue(AbstractSkullCandleBlock.LIGHTING, LightableBlock.Lighting.NONE)
						.setValue(WallSkullCandleBlock.FACING, level.getBlockState(pos).getValue(WallSkullBlock.FACING)),
				AbstractSkullCandleBlock.candleToCandleColor(candle).getValue(), 1));
		if (level.getBlockEntity(pos) instanceof SkullCandleBlockEntity sc) sc.setOwner(profile);
	}
}
