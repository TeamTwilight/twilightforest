package twilightforest.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.Tags;
import twilightforest.init.TFBlockEntities;
import twilightforest.init.TFBlocks;

public class CinderFurnaceBlockEntity extends FurnaceBlockEntity {
	private static final int SMELT_LOG_FACTOR = 10;

	public CinderFurnaceBlockEntity(BlockPos pos, BlockState state) {
		super(pos, state);
	}

	@Override
	public BlockEntityType<?> getType() {
		return TFBlockEntities.CINDER_FURNACE.get();
	}

	// [VanillaCopy] AbstactFurnaceBlockEntity.serverTick, edits noted
	public static void serverTick(Level level, BlockPos pos, BlockState state, CinderFurnaceBlockEntity entity) {
		boolean changed = false;
		boolean isLit;
		boolean wasLit;
		if (entity.litTimeRemaining > 0) {
			wasLit = true;
			entity.litTimeRemaining--;
			isLit = entity.litTimeRemaining > 0;
		} else {
			wasLit = false;
			isLit = false;
		}

		ItemStack fuel = entity.items.get(1);
		ItemStack ingredient = entity.items.get(0);
		boolean hasIngredient = !ingredient.isEmpty();
		boolean hasFuel = !fuel.isEmpty();
		if (isLit || hasFuel && hasIngredient) {
			if (hasIngredient) {
				SingleRecipeInput input = new SingleRecipeInput(ingredient);
				RecipeHolder<? extends AbstractCookingRecipe> recipe = entity.quickCheck.getRecipeFor(input, (ServerLevel) level).orElse(null);
				if (recipe != null) {
					int maxStackSize = entity.getMaxStackSize();
					ItemStack burnResult = recipe.value().assemble(input);
					if (!burnResult.isEmpty() && entity.canBurn(level, maxStackSize, burnResult)) {
						if (!isLit) {
							int newLitTime = entity.getBurnDuration(level.fuelValues(), fuel);
							entity.litTimeRemaining = newLitTime;
							entity.litTotalTime = newLitTime;
							if (newLitTime > 0) {
								consumeFuel(entity.items, fuel);
								isLit = true;
								changed = true;
							}
						}

						if (isLit) {
							// TF - cook faster
							entity.cookingTimer += entity.getCurrentSpeedMultiplier(level);
							if (entity.cookingTimer >= entity.cookingTotalTime) { // TF - change to geq since we can increment by >1
								entity.cookingTimer = 0;
								entity.cookingTotalTime = recipe.value().cookingTime();
								entity.burn(ingredient, burnResult);
								entity.setRecipeUsed(recipe);
								changed = true;
							}
						} else {
							entity.cookingTimer = 0;
						}
					} else {
						entity.cookingTimer = 0;
					}
				}
			} else {
				entity.cookingTimer = 0;
			}
		} else if (entity.cookingTimer > 0) {
			entity.cookingTimer = Mth.clamp(entity.cookingTimer - 2, 0, entity.cookingTotalTime);
		}

		if (wasLit != isLit) {
			changed = true;
			state = state.setValue(AbstractFurnaceBlock.LIT, isLit);
			level.setBlock(pos, state, 3);
		}

		// TF - occasionally cinderize nearby logs
		if (isLit && entity.litTimeRemaining % 5 == 0) {
			entity.cinderizeNearbyLog(level, pos);
		}

		if (changed) {
			setChanged(level, pos, state);
		}

	}

	@SuppressWarnings("deprecation")
	private void cinderizeNearbyLog(Level level, BlockPos origin) {
		RandomSource rand = level.getRandom();

		int dx = rand.nextInt(2) - rand.nextInt(2);
		int dy = rand.nextInt(2) - rand.nextInt(2);
		int dz = rand.nextInt(2) - rand.nextInt(2);
		BlockPos pos = origin.offset(dx, dy, dz);

		if (level.hasChunkAt(pos)) {
			BlockState nearbyBlock = level.getBlockState(pos);

			if (!nearbyBlock.is(TFBlocks.CINDER_LOG) && nearbyBlock.is(BlockTags.LOGS)) {
				level.setBlock(pos, TFBlocks.CINDER_LOG.get().withPropertiesOf(nearbyBlock), Block.UPDATE_CLIENTS);
				level.levelEvent(LevelEvent.PARTICLES_MOBBLOCK_SPAWN, pos, 0);
				level.levelEvent(LevelEvent.PARTICLES_MOBBLOCK_SPAWN, pos, 0);
				level.playSound(null, pos, SoundEvents.FIRE_AMBIENT, SoundSource.BLOCKS, 1.0F, 1.0F);
			}
		}
	}

	/**
	 * What is the current speed multiplier, as an int.
	 */
	private int getCurrentSpeedMultiplier(Level level) {
		return this.getCurrentMultiplier(level, 2);
	}

	/**
	 * Returns a number that is based on the number of nearby logs divided by the factor given.
	 */
	private int getCurrentMultiplier(Level level, int factor) {
		int logs = this.countNearbyLogs(level);

		if (logs < factor) {
			return 1;
		} else {
			return (logs / factor) + (level.getRandom().nextInt(factor) >= (logs % factor) ? 0 : 1);
		}
	}

	@SuppressWarnings("deprecation")
	private int countNearbyLogs(Level level) {
		int count = 0;

		for (int dx = -1; dx <= 1; dx++) {
			for (int dy = -1; dy <= 1; dy++) {
				for (int dz = -1; dz <= 1; dz++) {
					BlockPos pos = getBlockPos().offset(dx, dy, dz);
					if (level.hasChunkAt(pos) && level.getBlockState(pos).is(TFBlocks.CINDER_LOG.get())) {
						count++;
					}
				}
			}
		}

		return count;
	}

	// [VanillaCopy] AbstractFuranceBlockEntity.canBurn, edits noted
	protected boolean canBurn(Level level, int maxStackSize, ItemStack burnResult) {
		ItemStack resultItemStack = this.items.get(2);
		if (resultItemStack.isEmpty()) {
			return true;
		} else if (!ItemStack.isSameItemSameComponents(resultItemStack, burnResult)) {
			return false;
		} else {
			// TF - account for multiplying, clamp to max stack size so we don't overstack things
			int resultCount = Math.min(burnResult.getMaxStackSize(), resultItemStack.getCount() + this.getMaxOutputStacks(level, this.items.getFirst(), burnResult));
			int maxResultCount = Math.min(maxStackSize, burnResult.getMaxStackSize());
			return resultCount <= maxResultCount;
		}
	}

	/**
	 * Return the max number of items in the output stack, given our current multiplier
	 */
	public int getMaxOutputStacks(Level level, ItemStack input, ItemStack output) {
		if (this.canMultiply(input)) {
			return output.getCount() * this.getCurrentMaxSmeltMultiplier(level);
		} else {
			return output.getCount();
		}
	}

	// [VanillaCopy] AbstractFuranceBlockEntity.consumeFuel
	private static void consumeFuel(NonNullList<ItemStack> items, ItemStack fuel) {
		fuel.shrink(1);
		if (fuel.isEmpty()) {
			ItemStackTemplate remainder = fuel.getCraftingRemainder();
			items.set(1, remainder != null ? remainder.create() : ItemStack.EMPTY);
		}
	}

	// [VanillaCopy] AbstractFuranceBlockEntity.burn
	private void burn(ItemStack inputItemStack, ItemStack result) {
		ItemStack resultItemStack = this.items.get(2);
		if (resultItemStack.isEmpty()) {
			this.items.set(2, result.copy());
		} else {
			resultItemStack.grow(result.getCount());
		}

		if (inputItemStack.is(Items.WET_SPONGE) && !this.items.get(1).isEmpty() && this.items.get(1).is(Items.BUCKET)) {
			this.items.set(1, new ItemStack(Items.WATER_BUCKET));
		}

		inputItemStack.shrink(1);
	}

	private boolean canMultiply(ItemStack input) {
		return input.is(ItemTags.LOGS) || input.is(Tags.Items.ORES);
	}

	/**
	 * What is the current speed multiplier, as an int.
	 */
	private int getCurrentSmeltMultiplier(Level level) {
		return this.getCurrentMultiplier(level, SMELT_LOG_FACTOR);
	}

	/**
	 * What is the current speed multiplier, as an int.
	 */
	private int getCurrentMaxSmeltMultiplier(Level level) {
		return (int) Math.ceil((float) this.countNearbyLogs(level) / (float) SMELT_LOG_FACTOR);
	}
}
