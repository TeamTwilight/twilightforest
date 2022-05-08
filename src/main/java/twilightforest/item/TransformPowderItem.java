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
import twilightforest.TFSounds;
import twilightforest.TwilightForestMod;
import twilightforest.item.recipe.TFRecipes;

import javax.annotation.Nonnull;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class TransformPowderItem extends Item {

	protected TransformPowderItem(Properties props) {
		super(props);
	}

	@Override
	public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
		if (!target.isAlive()) {
			return InteractionResult.PASS;
		}
		AtomicBoolean flag = new AtomicBoolean(false);

		player.level.getRecipeManager().getAllRecipesFor(TFRecipes.TRANSFORM_POWDER_RECIPE.get()).forEach((recipe) -> {
			if(flag.get()) return;
			if(recipe.getInput() == target.getType()) {
				EntityType<?> type = recipe.getResult();
				if (type == null) {
					return;
				}

				Entity newEntity = type.create(player.level);
				if (newEntity == null) {
					return;
				}

				newEntity.moveTo(target.getX(), target.getY(), target.getZ(), target.getYRot(), target.getXRot());
				if (newEntity instanceof Mob mob && target.level instanceof ServerLevelAccessor world) {
					mob.finalizeSpawn(world, target.level.getCurrentDifficultyAt(target.blockPosition()), MobSpawnType.CONVERSION, null, null);
				}

				try { // try copying what can be copied
					UUID uuid = newEntity.getUUID();
					newEntity.load(target.saveWithoutId(newEntity.saveWithoutId(new CompoundTag())));
					newEntity.setUUID(uuid);
				} catch (Exception e) {
					TwilightForestMod.LOGGER.warn("Couldn't transform entity NBT data", e);
				}

				target.level.addFreshEntity(newEntity);
				target.discard();
				stack.shrink(1);

				if (target instanceof Mob) {
					((Mob) target).spawnAnim();
					((Mob) target).spawnAnim();
				}
				target.playSound(TFSounds.POWDER_USE, 1.0F + target.level.random.nextFloat(), target.level.random.nextFloat() * 0.7F + 0.3F);
				flag.set(true);
			}
		});
		return flag.get() ? InteractionResult.SUCCESS : InteractionResult.PASS;
	}

	@Nonnull
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, @Nonnull InteractionHand hand) {
		if (world.isClientSide) {
			AABB area = getEffectAABB(player);

			// particle effect
			for (int i = 0; i < 30; i++) {
				world.addParticle(ParticleTypes.CRIT, area.minX + world.random.nextFloat() * (area.maxX - area.minX),
						area.minY + world.random.nextFloat() * (area.maxY - area.minY),
						area.minZ + world.random.nextFloat() * (area.maxZ - area.minZ),
						0, 0, 0);
			}

		}

		return new InteractionResultHolder<>(InteractionResult.SUCCESS, player.getItemInHand(hand));
	}

	private AABB getEffectAABB(Player player) {
		double range = 2.0D;
		double radius = 1.0D;
		Vec3 srcVec = new Vec3(player.getX(), player.getY() + player.getEyeHeight(), player.getZ());
		Vec3 lookVec = player.getLookAngle();
		Vec3 destVec = srcVec.add(lookVec.x * range, lookVec.y * range, lookVec.z * range);

		return new AABB(destVec.x - radius, destVec.y - radius, destVec.z - radius, destVec.x + radius, destVec.y + radius, destVec.z + radius);
	}
}
