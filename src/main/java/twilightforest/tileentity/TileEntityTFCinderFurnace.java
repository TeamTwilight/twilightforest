package twilightforest.tileentity;

import net.minecraft.block.Block;
import net.minecraft.block.LogBlock;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tileentity.FurnaceTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.Tags;
import twilightforest.block.BlockTFCinderFurnace;
import twilightforest.block.BlockTFCinderLog;
import twilightforest.block.TFBlocks;

import java.util.Random;

public class TileEntityTFCinderFurnace extends FurnaceTileEntity {
	private static final int SMELT_LOG_FACTOR = 10;

	// [VanillaCopy] of superclass, edits noted
	@Override
	public void tick() {
		boolean flag = this.isBurning();
		boolean flag1 = false;

		if (this.isBurning()) {
			--this.burnTime;
		}

		if (!this.world.isRemote) {
			ItemStack itemstack = this.items.get(1);

			if (this.isBurning() || !itemstack.isEmpty() && !this.items.get(0).isEmpty()) {
				IRecipe<?> irecipe = this.world.getRecipeManager().getRecipe(IRecipeType.SMELTING, this, this.world).orElse(null);
				if (!this.isBurning() && this.canSmelt(irecipe)) {
					this.burnTime = getBurnTime(itemstack);
					this.recipesUsed = this.burnTime;

					if (this.isBurning()) {
						flag1 = true;

						if (!itemstack.isEmpty()) {
							Item item = itemstack.getItem();
							itemstack.shrink(1);

							if (itemstack.isEmpty()) {
								ItemStack item1 = item.getContainerItem(itemstack);
								this.items.set(1, item1);
							}
						}
					}
				}

				if (this.isBurning() && this.canSmelt(irecipe)) {
					// TF - cook faster
					this.cookTime += this.getCurrentSpeedMultiplier();

					if (this.cookTime >= this.cookTimeTotal) { // TF - change to geq since we can increment by >1
						this.cookTime = 0;
						this.cookTimeTotal = this.getRecipeBurnTime();
						this.smeltItem(irecipe);
						flag1 = true;
					}
				} else {
					this.cookTime = 0;
				}
			} else if (!this.isBurning() && this.cookTime > 0) {
				this.cookTime = MathHelper.clamp(this.cookTime - 2, 0, this.cookTimeTotal);
			}

			if (flag != this.isBurning()) {
				flag1 = true;
				this.world.setBlockState(this.pos, this.world.getBlockState(pos).with(BlockTFCinderFurnace.LIT, isBurning()), 3); // TF - use our furnace
			}

			// TF - occasionally cinderize nearby logs
			if (this.isBurning() && this.burnTime % 5 == 0) {
				this.cinderizeNearbyLog();
			}
		}

		if (flag1) {
			this.markDirty();
		}
	}

	// [VanillaCopy] of super
	private boolean isBurning() {
		return this.burnTime > 0;
	}

	// [VanillaCopy] of super, only using SMELTING IRecipeType
	protected int getRecipeBurnTime() {
		return this.world.getRecipeManager().getRecipe(IRecipeType.SMELTING, this, this.world).map(AbstractCookingRecipe::getCookTime).orElse(200);
	}

	private void cinderizeNearbyLog() {
		Random rand = this.getWorld().rand;

		int dx = rand.nextInt(2) - rand.nextInt(2);
		int dy = rand.nextInt(2) - rand.nextInt(2);
		int dz = rand.nextInt(2) - rand.nextInt(2);
		BlockPos pos = getPos().add(dx, dy, dz);

		if (this.world.isBlockLoaded(pos)) {
			Block nearbyBlock = this.getWorld().getBlockState(pos).getBlock();

			if (nearbyBlock != TFBlocks.cinder_log.get() && BlockTags.LOGS.contains(nearbyBlock)) {
				this.getWorld().setBlockState(pos, TFBlocks.cinder_log.get().getDefaultState().with(BlockTFCinderLog.AXIS, getCinderFacing(dx, dy, dz)), 2);
				this.getWorld().playEvent(2004, pos, 0);
				this.getWorld().playEvent(2004, pos, 0);
				this.getWorld().playSound(null, pos, SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 1.0F, 1.0F);
			}
		}
	}

	/**
	 * What meta should we set the log block with the specified offset to?
	 */
	private Direction.Axis getCinderFacing(int dx, int dy, int dz) {
		if (dz == 0 && dx != 0) {
			return dy == 0 ? Direction.Axis.X : Direction.Axis.Z;
		} else if (dx == 0 && dz != 0) {
			return dy == 0 ? Direction.Axis.Z : Direction.Axis.X;
		} else if (dx == 0 && dz == 0) {
			return Direction.Axis.Y;
		} else {
			return dy == 0 ? Direction.Axis.Y : LogBlock.EnumAxis.NONE; //TODO: Needs Cinder Wood
		}

	}

	/**
	 * What is the current speed multiplier, as an int.
	 */
	private int getCurrentSpeedMultiplier() {
		return getCurrentMultiplier(2);
	}

	/**
	 * Returns a number that is based on the number of nearby logs divided by the factor given.
	 */
	private int getCurrentMultiplier(int factor) {
		int logs = this.countNearbyLogs();

		if (logs < factor) {
			return 1;
		} else {
			return (logs / factor) + (this.world.rand.nextInt(factor) >= (logs % factor) ? 0 : 1);
		}
	}

	private int countNearbyLogs() {
		int count = 0;

		for (int dx = -1; dx <= 1; dx++) {
			for (int dy = -1; dy <= 1; dy++) {
				for (int dz = -1; dz <= 1; dz++) {
					BlockPos pos = getPos().add(dx, dy, dz);
					if (this.world.isBlockLoaded(pos) && this.getWorld().getBlockState(pos).getBlock() == TFBlocks.cinder_log.get()) {
						count++;
					}
				}
			}
		}

		return count;
	}

	// [VanillaCopy] of superclass ver, changes noted
	@Override
	protected boolean canSmelt(IRecipe<?> recipe) {
		if (this.items.get(0).isEmpty()) {
			return false;
		} else {
			ItemStack itemstack = recipe.getRecipeOutput();

			if (itemstack.isEmpty()) {
				return false;
			} else {
				ItemStack itemstack1 = this.items.get(2);
				if (itemstack1.isEmpty()) return true;
				if (!itemstack1.isItemEqual(itemstack)) return false;
				int result = itemstack1.getCount() + getMaxOutputStacks(items.get(0), itemstack); // TF - account for multiplying
				return result <= getInventoryStackLimit() && result <= itemstack1.getMaxStackSize(); // Forge fix: make furnace respect stack sizes in furnace recipes
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
	public void smeltItem(IRecipe<?> recipe) {
		if (this.canSmelt(recipe)) {
			ItemStack itemstack = this.items.get(0);
			ItemStack itemstack1 = recipe.getRecipeOutput();
			itemstack1.setCount(itemstack1.getCount() * getCurrentSmeltMultiplier());
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
		return ItemTags.LOGS.contains(input.getItem()) || Tags.Items.ORES.contains(input.getItem());
	}

	/**
	 * What is the current speed multiplier, as an int.
	 */
	private int getCurrentSmeltMultiplier() {
		return getCurrentMultiplier(SMELT_LOG_FACTOR);
	}

	/**
	 * What is the current speed multiplier, as an int.
	 */
	private int getCurrentMaxSmeltMultiplier() {
		return (int) Math.ceil((float) this.countNearbyLogs() / (float) SMELT_LOG_FACTOR);
	}
}
