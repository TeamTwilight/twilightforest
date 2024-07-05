package twilightforest.util;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.PaintingVariantTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ProtoChunk;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.fml.util.ObfuscationReflectionHelper;
import net.neoforged.neoforge.event.EventHooks;
import org.jetbrains.annotations.Nullable;
import twilightforest.entity.EnforcedHomePoint;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.DoubleUnaryOperator;

public class EntityUtil {

	public static <T extends Mob & EnforcedHomePoint> BlockPos bossChestLocation(T boss) {
		return !boss.isRestrictionPointValid(boss.level().dimension()) ? boss.blockPosition() : boss.getRestrictionPoint().pos().below();
	}

	public static boolean canDestroyBlock(Level world, BlockPos pos, Entity entity) {
		return canDestroyBlock(world, pos, world.getBlockState(pos), entity);
	}

	public static boolean canDestroyBlock(Level world, BlockPos pos, BlockState state, Entity entity) {
		float hardness = state.getDestroySpeed(world, pos);
		return hardness >= 0f && hardness < 50f && !state.isAir()
			&& !(world.getBlockEntity(pos) instanceof Container)
			&& state.getBlock().canEntityDestroy(state, world, pos, entity)
			&& (/* rude type limit */!(entity instanceof LivingEntity)
			|| EventHooks.onEntityDestroyBlock((LivingEntity) entity, pos, state));
	}

	/**
	 * [VanillaCopy] Entity.pick
	 */
	public static BlockHitResult rayTrace(Entity entity, double range) {
		Vec3 position = entity.getEyePosition(1.0F);
		Vec3 look = entity.getViewVector(1.0F);
		Vec3 dest = position.add(look.x * range, look.y * range, look.z * range);
		return entity.level().clip(new ClipContext(position, dest, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity));
	}

	public static BlockHitResult rayTrace(Player player) {
		return rayTrace(player, null);
	}

	public static BlockHitResult rayTrace(Player player, @Nullable DoubleUnaryOperator modifier) {
		double range = player.getAttribute(Attributes.BLOCK_INTERACTION_RANGE).getValue();
		return rayTrace(player, modifier == null ? range : modifier.applyAsDouble(range));
	}

	private static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();
	private static final Method LivingEntity_getDeathSound = ObfuscationReflectionHelper.findMethod(LivingEntity.class, "getDeathSound");
	private static final MethodHandle handle_LivingEntity_getDeathSound;
	private static final Method HangingEntity_setDirection = ObfuscationReflectionHelper.findMethod(HangingEntity.class, "setDirection", Direction.class);
	private static final MethodHandle handle_HangingEntity_setDirection;

	static {
		MethodHandle tmp_handle_LivingEntity_getDeathSound = null;
		MethodHandle tmp_handle_HangingEntity_setDirection = null;

		try {
			tmp_handle_LivingEntity_getDeathSound = LOOKUP.unreflect(LivingEntity_getDeathSound);
			tmp_handle_HangingEntity_setDirection = LOOKUP.unreflect(HangingEntity_setDirection);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		handle_LivingEntity_getDeathSound = tmp_handle_LivingEntity_getDeathSound;
		handle_HangingEntity_setDirection = tmp_handle_HangingEntity_setDirection;
	}

	@Nullable
	public static SoundEvent getDeathSound(LivingEntity living) {
		SoundEvent sound = null;
		if (handle_LivingEntity_getDeathSound != null) {
			try {
				sound = (SoundEvent) handle_LivingEntity_getDeathSound.invokeExact(living);
			} catch (Throwable e) {
				// FAIL SILENTLY
			}
		}
		return sound;
	}

	public static void killLavaAround(Entity entity) {
		AABB bounds = entity.getBoundingBox().inflate(9D);
		for (double x = bounds.minX; x < bounds.maxX; x++) {
			for (double z = bounds.minZ; z < bounds.maxZ; z++) {
				for (double y = bounds.minY; y < bounds.maxY; y++) {
					BlockPos pos = BlockPos.containing(x, y, z);
					BlockState state = entity.level().getBlockState(pos);
					if (state.is(Blocks.LAVA)) {
						entity.level().setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
					}
				}
			}
		}
	}

	//copy of Mob.doHurtTarget, allows for using a custom DamageSource instead of the generic Mob Attack one
	public static boolean properlyApplyCustomDamageSource(Mob entity, Entity victim, DamageSource source, @Nullable SoundEvent flingSound) {
		float f = (float)entity.getAttributeValue(Attributes.ATTACK_DAMAGE);
		if (entity.level() instanceof ServerLevel serverlevel) {
			f = EnchantmentHelper.modifyDamage(serverlevel, entity.getWeaponItem(), entity, source, f);
		}

		boolean flag = victim.hurt(source, f);
		if (flag) {
			float f1 = getKnockback(entity, victim, source);
			if (f1 > 0.0F && victim instanceof LivingEntity livingentity) {
				if (flingSound != null) {
					entity.playSound(flingSound, 1.0F, 1.0F);
				}
				livingentity.knockback(f1 * 0.5F, Mth.sin(entity.getYRot() * Mth.DEG_TO_RAD), -Mth.cos(entity.getYRot() * Mth.DEG_TO_RAD));
				entity.setDeltaMovement(entity.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
			}

			if (entity.level() instanceof ServerLevel level) {
				EnchantmentHelper.doPostAttackEffects(level, victim, source);
			}

			entity.setLastHurtMob(entity);
		}

		return flag;
	}

	protected static float getKnockback(Mob entity, Entity victim, DamageSource source) {
		float f = (float)entity.getAttributeValue(Attributes.ATTACK_KNOCKBACK);
		return entity.level() instanceof ServerLevel serverlevel ? EnchantmentHelper.modifyKnockback(serverlevel, entity.getWeaponItem(), victim, source, f) : f;
	}

	// [VanillaCopy] with modifications: StructureTemplate.createEntityIgnoreException
	@Nullable
	private static <T extends Entity> T createEntityIgnoreException(EntityType<T> type, ServerLevelAccessor levelAccessor) {
		try {
			return type.create(levelAccessor.getLevel());
		} catch (Exception exception) {
			return null;
		}
	}

	public static void tryHangPainting(WorldGenLevel world, BlockPos pos, Direction direction, @Nullable Holder<PaintingVariant> chosenPainting) {
		if (chosenPainting == null) return;

		Painting painting = createEntityIgnoreException(EntityType.PAINTING, world);

		painting.setPos(pos.getX(), pos.getY(), pos.getZ());
		try {
			handle_HangingEntity_setDirection.invoke(painting, direction);
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}
		painting.setVariant(chosenPainting);

		if (checkValidPaintingPosition(world, painting))
			world.addFreshEntity(painting);
	}

	@Nullable
	public static Holder<PaintingVariant> getPaintingOfSize(WorldGenLevel level, RandomSource rand, int minSize) {
		return getPaintingOfSize(level, rand, minSize, minSize, false);
	}

	@Nullable
	public static Holder<PaintingVariant> getPaintingOfSize(WorldGenLevel level, RandomSource rand, int width, int height, boolean exactMeasurements) {
		List<Holder<PaintingVariant>> valid = new ArrayList<>();

		for (Holder<PaintingVariant> art : level.registryAccess().registryOrThrow(Registries.PAINTING_VARIANT).holders().toList()) {
			if (exactMeasurements) {
				if (art.value().width() == width && art.value().height() == height) {
					valid.add(art);
				}
			} else {
				if (art.value().width() >= width || art.value().height() >= height) {
					valid.add(art);
				}
			}
		}

		if (!valid.isEmpty()) {
			return valid.get(rand.nextInt(valid.size()));
		} else {
			return null;
		}
	}

	public static boolean checkValidPaintingPosition(WorldGenLevel world, @Nullable Painting painting) {
		if (painting == null) {
			return false;
		}

		final AABB largerBox = painting.getBoundingBox();

		if (!world.noCollision(painting, largerBox)) {
			return false;
		} else {
			List<Entity> collidingEntities = getEntitiesInAABB(world, largerBox);

			for (Entity entityOnList : collidingEntities) {
				if (entityOnList instanceof HangingEntity) {
					return false;
				}
			}

			return true;
		}
	}

	public static List<Entity> getEntitiesInAABB(WorldGenLevel world, AABB boundingBox) {
		List<Entity> list = Lists.newArrayList();
		int i = Mth.floor((boundingBox.minX - 2) / 16.0D);
		int j = Mth.floor((boundingBox.maxX + 2) / 16.0D);
		int k = Mth.floor((boundingBox.minZ - 2) / 16.0D);
		int l = Mth.floor((boundingBox.maxZ + 2) / 16.0D);

		for (int i1 = i; i1 <= j; ++i1) {
			for (int j1 = k; j1 <= l; ++j1) {
				ChunkAccess chunk = world.getChunk(i1, j1, ChunkStatus.STRUCTURE_STARTS);
				if (chunk instanceof ProtoChunk proto) {
					proto.getEntities().forEach(nbt -> {
						Entity entity = EntityType.loadEntityRecursive(nbt, world.getLevel(), e -> e);
						if (entity != null && boundingBox.intersects(entity.getBoundingBox())) {
							list.add(entity);
						}
					});
				}
			}
		}

		return list;
	}
}
