package twilightforest.dispenser;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.phys.AABB;
import twilightforest.TFSounds;
import twilightforest.TwilightForestMod;
import twilightforest.item.recipe.TFRecipes;

import java.util.Random;
import java.util.UUID;

public class TransformationDispenseBehavior extends DefaultDispenseItemBehavior {

	boolean fired = false;

	@Override
	protected ItemStack execute(BlockSource source, ItemStack stack) {
		Level world = source.getLevel();
		RandomSource random = world.getRandom();
		BlockPos blockpos = source.getPos().relative(source.getBlockState().getValue(DispenserBlock.FACING));
		if (!world.isClientSide) {
			for (LivingEntity livingentity : world.getEntitiesOfClass(LivingEntity.class, new AABB(blockpos), EntitySelector.NO_SPECTATORS)) {
				world.getRecipeManager().getAllRecipesFor(TFRecipes.TRANSFORM_POWDER_RECIPE.get()).forEach((recipe) -> {
					if (recipe.getInput() == livingentity.getType()) {
						EntityType<?> type = recipe.getResult();
						Entity newEntity = type.create(world);
						if (type != null && newEntity != null) {
							newEntity.moveTo(livingentity.getX(), livingentity.getY(), livingentity.getZ(), livingentity.getYRot(), livingentity.getXRot());
							if (newEntity instanceof Mob mob && livingentity.level instanceof ServerLevelAccessor sworld) {
								mob.finalizeSpawn(sworld, livingentity.level.getCurrentDifficultyAt(livingentity.blockPosition()), MobSpawnType.CONVERSION, null, null);
							}

							try {
								UUID uuid = newEntity.getUUID();
								newEntity.load(livingentity.saveWithoutId(newEntity.saveWithoutId(new CompoundTag())));
								newEntity.setUUID(uuid);
							} catch (Exception e) {
								TwilightForestMod.LOGGER.warn("Couldn't transform entity NBT data", e);
							}

							livingentity.level.addFreshEntity(newEntity);
							livingentity.discard();

							if (livingentity instanceof Mob && livingentity.level.isClientSide) {
								((Mob) livingentity).spawnAnim();
								((Mob) livingentity).spawnAnim();
							}
							livingentity.playSound(TFSounds.POWDER_USE.get(), 1.0F + random.nextFloat(), random.nextFloat() * 0.7F + 0.3F);
							stack.shrink(1);
							fired = true;
						}
					}
				});
			}
		}
		return stack;
	}

	@Override
	protected void playSound(BlockSource source) {
		if (fired) {
			super.playSound(source);
		} else {
			source.getLevel().levelEvent(1001, source.getPos(), 0);
		}
	}
}
