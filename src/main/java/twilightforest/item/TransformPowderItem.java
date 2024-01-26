package twilightforest.item;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFRecipes;
import twilightforest.init.TFSounds;

import javax.annotation.Nonnull;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class TransformPowderItem extends Item {

	public TransformPowderItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
		if (!target.isAlive()) {
			return InteractionResult.PASS;
		}

		return transformEntityIfPossible(player.level(), target, player.getItemInHand(hand)) ? InteractionResult.SUCCESS : InteractionResult.PASS;
	}

	@Nonnull
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, @Nonnull InteractionHand hand) {
		if (level.isClientSide()) {
			AABB area = this.getEffectAABB(player);

			// particle effect
			for (int i = 0; i < 30; i++) {
				level.addParticle(ParticleTypes.CRIT, area.minX + level.getRandom().nextFloat() * (area.maxX - area.minX),
						area.minY + level.getRandom().nextFloat() * (area.maxY - area.minY),
						area.minZ + level.getRandom().nextFloat() * (area.maxZ - area.minZ),
						0, 0, 0);
			}

		}

		return new InteractionResultHolder<>(InteractionResult.SUCCESS, player.getItemInHand(hand));
	}

	public static boolean transformEntityIfPossible(Level level, LivingEntity target, ItemStack powder) {
		AtomicBoolean flag = new AtomicBoolean(false);

		//dont transform tamed animals that have owners
		if (target instanceof OwnableEntity ownable && ownable.getOwner() != null) return false;

		level.getRecipeManager().getAllRecipesFor(TFRecipes.TRANSFORM_POWDER_RECIPE.get()).forEach(recipeHolder -> {
			if (flag.get()) return;
			if (recipeHolder.value().input() == target.getType() || (recipeHolder.value().isReversible() && recipeHolder.value().result() == target.getType())) {
				EntityType<?> type = recipeHolder.value().isReversible() && recipeHolder.value().result() == target.getType() ? recipeHolder.value().input() : recipeHolder.value().result();
				if (type == null) {
					return;
				}

				Entity newEntity = type.create(level);
				if (newEntity == null) {
					return;
				}

				newEntity.moveTo(target.getX(), target.getY(), target.getZ(), target.getYRot(), target.getXRot());
				if (newEntity instanceof Mob mob && target.level() instanceof ServerLevelAccessor world) {
					EventHooks.onFinalizeSpawn(mob, world, target.level().getCurrentDifficultyAt(target.blockPosition()), MobSpawnType.CONVERSION, null, null);
				}

				try { // try copying what can be copied
					UUID uuid = newEntity.getUUID();
					newEntity.load(target.saveWithoutId(newEntity.saveWithoutId(new CompoundTag())));
					newEntity.setUUID(uuid);
				} catch (Exception e) {
					TwilightForestMod.LOGGER.warn("Couldn't transform entity NBT data", e);
				}

				target.level().addFreshEntity(newEntity);
				target.discard();
				powder.shrink(1);

				if (target instanceof Mob mob) {
					mob.spawnAnim();
					mob.spawnAnim();
				}
				target.playSound(TFSounds.POWDER_USE.get(), 1.0F + target.level().getRandom().nextFloat(), target.level().getRandom().nextFloat() * 0.7F + 0.3F);
				flag.set(true);
			}
		});
		return flag.get();
	}

	private AABB getEffectAABB(Player player) {
		double range = 2.0D;
		double radius = 1.0D;
		Vec3 srcVec = new Vec3(player.getX(), player.getY() + player.getEyeHeight(), player.getZ());
		Vec3 lookVec = player.getLookAngle();
		Vec3 destVec = srcVec.add(lookVec.x() * range, lookVec.y() * range, lookVec.z() * range);

		return new AABB(destVec.x() - radius, destVec.y() - radius, destVec.z() - radius, destVec.x() + radius, destVec.y() + radius, destVec.z() + radius);
	}
}