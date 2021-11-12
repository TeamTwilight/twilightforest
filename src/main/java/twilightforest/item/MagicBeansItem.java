package twilightforest.item;

import net.minecraft.advancements.Advancement;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.ServerAdvancementManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.TwilightForestMod;
import twilightforest.block.TFBlocks;

import javax.annotation.Nonnull;

public class MagicBeansItem extends Item {

	private int blocksSkipped;

	protected MagicBeansItem(Properties props) {
		super(props);
	}

	@Nonnull
	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level world = context.getLevel();
		BlockPos pos = context.getClickedPos();
		Player player = context.getPlayer();
		Block blockAt = world.getBlockState(pos).getBlock();
		ItemStack stack = context.getItemInHand();

		int minY = pos.getY() + 1;
		int maxY = Math.max(pos.getY() + 100, 175);
		if (pos.getY() < maxY && blockAt == TFBlocks.UBEROUS_SOIL.get()) {
			if (!world.isClientSide) {
				stack.shrink(1);
				makeHugeStalk(world, pos, minY, maxY);
				if (player instanceof ServerPlayer) {
					player.awardStat(Stats.ITEM_USED.get(this));

					//fallback if the other part doesnt work since its inconsistent
					PlayerAdvancements advancements = ((ServerPlayer) player).getAdvancements();
					ServerAdvancementManager manager = ((ServerLevel)player.getCommandSenderWorld()).getServer().getAdvancements();
					Advancement advancement = manager.getAdvancement(TwilightForestMod.prefix("beanstalk"));
					if(advancement != null) {
						advancements.award(advancement, "use_beans");
					}
				}
			}

			return InteractionResult.SUCCESS;
		} else {
			return InteractionResult.PASS;
		}
	}

	private void makeHugeStalk(Level world, BlockPos pos, int minY, int maxY) {
		float x = pos.getX();
		float z = pos.getZ();

		int yOffset = world.random.nextInt(100);

		float cScale = world.random.nextFloat() * 0.25F + 0.125F; // spiral tightness scaling
		float rScale = world.random.nextFloat() * 0.25F + 0.125F; // radius change scaling

		// offset x and z to make stalk start at origin
		float radius = 4F + Mth.sin((minY + yOffset) * rScale) * 3F; // make radius a little wavy
		x -= Mth.sin((minY + yOffset) * cScale) * radius;
		z -= Mth.cos((minY + yOffset) * cScale) * radius;

		// leaves!
		int nextLeafY = minY + 10 + world.random.nextInt(20);

		// make stalk
		boolean isClear = true;
		for (int dy = minY; dy < maxY && isClear; dy++) {
			// make radius a little wavy
			radius = 5F + Mth.sin((dy + yOffset) * rScale) * 2.5F;

			// find center of stalk
			float cx = x + Mth.sin((dy + yOffset) * cScale) * radius;
			float cz = z + Mth.cos((dy + yOffset) * cScale) * radius;


			float stalkThickness = 2.5F;

			// reduce thickness near top
			if (maxY - dy < 5) {
				stalkThickness *= (maxY - dy) / 5F;
			}

			int minX = Mth.floor(x - radius - stalkThickness);
			int maxX = Mth.ceil(x + radius + stalkThickness);
			int minZ = Mth.floor(z - radius - stalkThickness);
			int maxZ = Mth.ceil(z + radius + stalkThickness);

			// generate stalk
			for (int dx = minX; dx < maxX && isClear; dx++) {
				for (int dz = minZ; dz < maxZ && isClear; dz++) {
					if ((dx - cx) * (dx - cx) + (dz - cz) * (dz - cz) < stalkThickness * stalkThickness) {
						isClear = this.tryToPlaceStalk(world, new BlockPos(dx, dy, dz));
					}
				}
			}
			//reset skipped blocks as we're moving on to a new layer
			blocksSkipped = 0;

			// leaves?
			if (dy == nextLeafY) {
				// make leaf blob

				int lx = (int) (x + Mth.sin((dy + yOffset) * cScale) * (radius + stalkThickness));
				int lz = (int) (z + Mth.cos((dy + yOffset) * cScale) * (radius + stalkThickness));

				this.placeLeaves(world, new BlockPos(lx, dy, lz));

				nextLeafY = dy + 5 + world.random.nextInt(10);
			}
		}
	}

	private void placeLeaves(Level world, BlockPos pos) {
		// stalk at center
		world.setBlockAndUpdate(pos, TFBlocks.HUGE_STALK.get().defaultBlockState());

		// small squares
		for (int dx = -1; dx <= 1; dx++) {
			for (int dz = -1; dz <= 1; dz++) {
				this.tryToPlaceLeaves(world, pos.offset(dx, -1, dz));
				this.tryToPlaceLeaves(world, pos.offset(dx, 1, dz));
			}
		}
		// larger square
		for (int dx = -2; dx <= 2; dx++) {
			for (int dz = -2; dz <= 2; dz++) {
				if (!((dx == 2 || dx == -2) && (dz == 2 || dz == -2))) {
					this.tryToPlaceLeaves(world, pos.offset(dx, 0, dz));
				}
			}
		}
	}

	/**
	 * Place the stalk block only if the destination is clear.  Return false if a layer is blocked by 15 or more blocks.
	 */
	private boolean tryToPlaceStalk(Level world, BlockPos pos) {
		BlockState state = world.getBlockState(pos);
		if (state.isAir() || state.getMaterial().isReplaceable() || (state.isAir() || state.is(BlockTags.LEAVES)) || BlockTags.LEAVES.contains(state.getBlock()) || state.getBlock().equals(TFBlocks.FLUFFY_CLOUD.get())) {
			world.setBlockAndUpdate(pos, TFBlocks.HUGE_STALK.get().defaultBlockState());
			if (pos.getY() > 150) {
				for (int i = 0; i < 7; i++) {
					if (world.getBlockState(pos.relative(Direction.UP, i)).equals(TFBlocks.WISPY_CLOUD.get().defaultBlockState()) || world.getBlockState(pos.relative(Direction.UP, i)).equals(TFBlocks.FLUFFY_CLOUD.get().defaultBlockState())) {
						world.setBlockAndUpdate(pos.relative(Direction.UP, i), Blocks.AIR.defaultBlockState());
					}
				}
			}
			return true;
		} else {
			blocksSkipped++;
			return blocksSkipped < 15;
		}
	}

	private void tryToPlaceLeaves(Level world, BlockPos pos) {
		BlockState state = world.getBlockState(pos);
		if (state.isAir() || state.is(BlockTags.LEAVES)) {
			world.setBlock(pos, TFBlocks.BEANSTALK_LEAVES.get().defaultBlockState().setValue(LeavesBlock.PERSISTENT, true), 2);
		}
	}
}
