package twilightforest.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.Nullable;
import twilightforest.block.CinderFurnaceBlock;
import twilightforest.init.TFBlocks;

public class CinderFurnaceBlockEntity extends FurnaceBlockEntity {
	private static final int SMELT_LOG_FACTOR = 10;

	public CinderFurnaceBlockEntity(BlockPos pos, BlockState state) {
		super(pos, state);
	}

	// [VanillaCopy] of superclass, edits noted
	public static void tick(Level level, BlockPos pos, BlockState state, CinderFurnaceBlockEntity te) {
		boolean flag = te.isBurning();
		boolean flag1 = false;

		if (te.isBurning()) {
			--te.litTime;
		}

		if (!level.isClientSide()) {
			ItemStack itemstack = te.items.get(1);

			if (te.isBurning() || !itemstack.isEmpty() && !te.items.get(0).isEmpty()) {
				RecipeHolder<?> irecipe = level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, te, level).orElse(null);
				if (irecipe != null && !te.isBurning() && te.canBurn(irecipe.value())) {
					te.litTime = te.getBurnDuration(itemstack);
					te.litDuration = te.litTime;

					if (te.isBurning()) {
						flag1 = true;

						if (!itemstack.isEmpty()) {
							Item item = itemstack.getItem();
							itemstack.shrink(1);

							if (itemstack.isEmpty()) {
								ItemStack item1 = item.getCraftingRemainingItem(itemstack);
								te.items.set(1, item1);
							}
						}
					}
				}

				if (irecipe != null && te.isBurning() && te.canBurn(irecipe.value())) {
					// TF - cook faster
					te.cookingProgress += te.getCurrentSpeedMultiplier();

					if (te.cookingProgress >= te.cookingTotalTime) { // TF - change to geq since we can increment by >1
						te.cookingProgress = 0;
						te.cookingTotalTime = te.getRecipeBurnTime();
						te.smeltItem(irecipe.value());
						flag1 = true;
					}
				} else {
					te.cookingProgress = 0;
				}
			} else if (!te.isBurning() && te.cookingProgress > 0) {
				te.cookingProgress = Mth.clamp(te.cookingProgress - 2, 0, te.cookingTotalTime);
			}

			if (flag != te.isBurning()) {
				flag1 = true;
				level.setBlock(pos, level.getBlockState(pos).setValue(CinderFurnaceBlock.LIT, te.isBurning()), 3); // TF - use our furnace
			}

			// TF - occasionally cinderize nearby logs
			if (te.isBurning() && te.litTime % 5 == 0) {
				te.cinderizeNearbyLog();
			}
		}

		if (flag1) {
			te.setChanged();
		}
	}

	// [VanillaCopy] of super
	private boolean isBurning() {
		return this.litTime > 0;
	}

	// [VanillaCopy] of super, only using SMELTING IRecipeType
	protected int getRecipeBurnTime() {
		return this.getLevel().getRecipeManager().getRecipeFor(RecipeType.SMELTING, this, this.getLevel()).map(recipeHolder -> recipeHolder.value().getCookingTime()).orElse(200);
	}

	private void cinderizeNearbyLog() {
		RandomSource rand = this.getLevel().getRandom();

		int dx = rand.nextInt(2) - rand.nextInt(2);
		int dy = rand.nextInt(2) - rand.nextInt(2);
		int dz = rand.nextInt(2) - rand.nextInt(2);
		BlockPos pos = this.getBlockPos().offset(dx, dy, dz);

		if (this.getLevel().hasChunkAt(pos)) {
			BlockState nearbyBlock = this.getLevel().getBlockState(pos);

			if (!nearbyBlock.is(TFBlocks.CINDER_LOG) && nearbyBlock.is(BlockTags.LOGS)) {
				this.getLevel().setBlock(pos, this.getCinderLog(dx, dy, dz), 2);
				this.getLevel().levelEvent(2004, pos, 0);
				this.getLevel().levelEvent(2004, pos, 0);
				this.getLevel().playSound(null, pos, SoundEvents.FIRE_AMBIENT, SoundSource.BLOCKS, 1.0F, 1.0F);
			}
		}
	}

	/**
	 * What meta should we set the log block with the specified offset to?
	 */
	private BlockState getCinderLog(int dx, int dy, int dz) {
		@Nullable Direction.Axis direction;
		if (dz == 0 && dx != 0) {
			direction = dy == 0 ? Direction.Axis.X : Direction.Axis.Z;
		} else if (dx == 0 && dz != 0) {
			direction = dy == 0 ? Direction.Axis.Z : Direction.Axis.X;
		} else if (dx == 0) {
			direction = Direction.Axis.Y;
		} else {
			direction = dy == 0 ? Direction.Axis.Y : null; //We return null so we can get Cinder Wood.
		}

		return direction != null ? TFBlocks.CINDER_LOG.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, direction)
				: TFBlocks.CINDER_WOOD.get().defaultBlockState();
	}

	/**
	 * What is the current speed multiplier, as an int.
	 */
	private int getCurrentSpeedMultiplier() {
		return this.getCurrentMultiplier(2);
	}

	/**
	 * Returns a number that is based on the number of nearby logs divided by the factor given.
	 */
	private int getCurrentMultiplier(int factor) {
		int logs = this.countNearbyLogs();

		if (logs < factor) {
			return 1;
		} else {
			return (logs / factor) + (this.getLevel().getRandom().nextInt(factor) >= (logs % factor) ? 0 : 1);
		}
	}

	private int countNearbyLogs() {
		int count = 0;

		for (int dx = -1; dx <= 1; dx++) {
			for (int dy = -1; dy <= 1; dy++) {
				for (int dz = -1; dz <= 1; dz++) {
					BlockPos pos = getBlockPos().offset(dx, dy, dz);
					if (this.getLevel().hasChunkAt(pos) && this.getLevel().getBlockState(pos).getBlock() == TFBlocks.CINDER_LOG.get()) {
						count++;
					}
				}
			}
		}

		return count;
	}

	// [VanillaCopy] of superclass ver, changes noted
	//@Override
	protected boolean canBurn(Recipe<?> recipe) {
		if (this.items.get(0).isEmpty()) {
			return false;
		} else {
			ItemStack itemstack = recipe.getResultItem(this.getLevel().registryAccess());

			if (itemstack.isEmpty()) {
				return false;
			} else {
				ItemStack itemstack1 = this.items.get(2);
				if (itemstack1.isEmpty()) return true;
				if (!itemstack1.is(itemstack.getItem())) return false;
				int result = itemstack1.getCount() + getMaxOutputStacks(items.get(0), itemstack); // TF - account for multiplying
				return result <= this.getMaxStackSize() && result <= itemstack1.getMaxStackSize(); // Forge fix: make furnace respect stack sizes in furnace recipes
			}
		}
	}

	/**
	 * Return the max number of items in the output stack, given our current multiplier
	 */
	public int getMaxOutputStacks(ItemStack input, ItemStack output) {
		if (this.canMultiply(input, output)) {
			return output.getCount() * this.getCurrentMaxSmeltMultiplier();
		} else {
			return output.getCount();
		}
	}

	// [VanillaCopy] superclass, using our own canSmelt and multiplying output
	public void smeltItem(Recipe<?> recipe) {
		if (this.canBurn(recipe)) {
			ItemStack itemstack = this.items.get(0);
			ItemStack itemstack1 = recipe.getResultItem(this.getLevel().registryAccess());
			itemstack1.setCount(itemstack1.getCount() * this.getCurrentSmeltMultiplier());
			ItemStack itemstack2 = this.items.get(2);

			if (itemstack2.isEmpty()) {
				this.items.set(2, itemstack1.copy());
			} else if (itemstack2.getItem() == itemstack1.getItem()) {
				itemstack2.grow(itemstack1.getCount());
			}

			if (itemstack.getItem() == Blocks.WET_SPONGE.asItem() && !this.items.get(1).isEmpty() && this.items.get(1).getItem() == Items.BUCKET) {
				this.items.set(1, new ItemStack(Items.WATER_BUCKET));
			}

			itemstack.shrink(1);
		}
	}

	private boolean canMultiply(ItemStack input, ItemStack output) {
		return input.is(ItemTags.LOGS) || input.is(Tags.Items.ORES);
	}

	/**
	 * What is the current speed multiplier, as an int.
	 */
	private int getCurrentSmeltMultiplier() {
		return this.getCurrentMultiplier(SMELT_LOG_FACTOR);
	}

	/**
	 * What is the current speed multiplier, as an int.
	 */
	private int getCurrentMaxSmeltMultiplier() {
		return (int) Math.ceil((float) this.countNearbyLogs() / (float) SMELT_LOG_FACTOR);
	}
}
