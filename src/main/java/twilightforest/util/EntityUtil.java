package twilightforest.util;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import javax.annotation.Nullable;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.function.DoubleUnaryOperator;

public class EntityUtil {

	public static boolean canDestroyBlock(Level world, BlockPos pos, Entity entity) {
		return canDestroyBlock(world, pos, world.getBlockState(pos), entity);
	}

	public static boolean canDestroyBlock(Level world, BlockPos pos, BlockState state, Entity entity) {
		float hardness = state.getDestroySpeed(world, pos);
		return hardness >= 0f && hardness < 50f && !state.isAir()
				&& state.getBlock().canEntityDestroy(state, world, pos, entity)
				&& (/* rude type limit */!(entity instanceof LivingEntity)
				|| ForgeEventFactory.onEntityDestroyBlock((LivingEntity) entity, pos, state));
	}

	/**
	 * [VanillaCopy] Entity.pick
	 */
	public static BlockHitResult rayTrace(Entity entity, double range) {
		Vec3 position = entity.getEyePosition(1.0F);
		Vec3 look = entity.getViewVector(1.0F);
		Vec3 dest = position.add(look.x * range, look.y * range, look.z * range);
		return entity.level.clip(new ClipContext(position, dest, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity));
	}

	public static BlockHitResult rayTrace(Player player) {
		return rayTrace(player, null);
	}

	public static BlockHitResult rayTrace(Player player, @Nullable DoubleUnaryOperator modifier) {
		double range = player.getAttribute(ForgeMod.REACH_DISTANCE.get()).getValue();
		return rayTrace(player, modifier == null ? range : modifier.applyAsDouble(range));
	}

	private static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();
	private static final Method LivingEntity_getDeathSound = ObfuscationReflectionHelper.findMethod(LivingEntity.class, "m_5592_");
	private static final MethodHandle handle_LivingEntity_getDeathSound;

	static {
		MethodHandle tmp_handle_LivingEntity_getDeathSound = null;
		try {
			tmp_handle_LivingEntity_getDeathSound = LOOKUP.unreflect(LivingEntity_getDeathSound);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		handle_LivingEntity_getDeathSound = tmp_handle_LivingEntity_getDeathSound;
	}

	public static SoundEvent getDeathSound(LivingEntity living) {
		SoundEvent sound = SoundEvents.GENERIC_DEATH;
		if (handle_LivingEntity_getDeathSound != null) {
			try {
				sound = (SoundEvent) handle_LivingEntity_getDeathSound.invokeExact(living);
			} catch (Throwable e) {
				// FAIL SILENTLY
			}
		}
		return sound;
	}
}
