package twilightforest.asmhooks;

import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFDataAttachments;
import twilightforest.init.custom.TravellersModifiersManager;
import twilightforest.item.travellers_gear.TravellersGearLogic;

import java.util.function.BiPredicate;

@SuppressWarnings({"JavadocReference", "unused"})
public class EntityHooks {

	/**
	 * {@link twilightforest.asm.transformers.entity.WaterWalkTransformer}<p/>
	 *
	 * Injection Point:<br/>
	 * {@link net.minecraft.world.entity.LivingEntity#canStandOnFluid(FluidState)}
	 */
	@Nullable
	public static boolean processWaterWalking(boolean o, LivingEntity livingEntity, FluidState fluidState) {
		if (!fluidState.is(FluidTags.WATER))
			return o;

		if (!TravellersModifiersManager.isModifierActive(livingEntity, TravellersModifiersManager.WATER_WALK_MODIFIER))
			return o;

		boolean isWaterWalking = TravellersGearLogic.isBelowMaxWaterWalkingSubmergedHeight(livingEntity) && !livingEntity.isShiftKeyDown();
		if (livingEntity.getFluidHeight(FluidTags.WATER) > 0 && isWaterWalking && livingEntity.level().getGameTime() % 3 == 1)
			TravellersGearLogic.waterWalkingSplashEffect(livingEntity);
		return isWaterWalking;
	}

	/**
	 * {@link twilightforest.asm.transformers.entity.WaterSprintTransformer}<p/>
	 * <p>
	 * Injection Point:<br/>
	 * {@link net.minecraft.client.player.LocalPlayer#aiStep()}
	 * Targets: {@link Entity#isInWater()}
	 */
	public static boolean unrestrainedSprintingInWater(boolean isInWater, LivingEntity livingEntity) {
		if (!TravellersModifiersManager.isModifierActive(livingEntity, TravellersModifiersManager.UNRESTRAINED_MODIFIER))
			return isInWater;
		return !livingEntity.canStandOnFluid(livingEntity.level().getFluidState(livingEntity.blockPosition())) && isInWater;
	}

	/**
	 * {@link twilightforest.asm.transformers.entity.WaterSprintTransformer}<p/>
	 * <p>
	 * Injection Point:<br/>
	 * {@link net.minecraft.client.player.LocalPlayer#aiStep()}
	 * Targets: {@link net.neoforged.neoforge.common.extensions.IEntityExtension#isInFluidType(java.util.function.BiPredicate<net.neoforged.neoforge.fluids.FluidType, Double>)}
	 */
	public static BiPredicate<FluidType, Double> unrestrainedSwimPredicate(BiPredicate<FluidType, Double> o, LivingEntity livingEntity) {
		return (fluidType, height) -> {
			FluidState fs = livingEntity.level().getFluidState(livingEntity.blockPosition());
			boolean oResult = o.test(fluidType, height);
			if (fluidType != NeoForgeMod.WATER_TYPE.value())
				return oResult;
			return unrestrainedSprintingInWater(oResult, livingEntity);
		};
	}

	/**
	 * {@link twilightforest.asm.transformers.entity.PathFinderUnrestrainedByLeashTransformer} <p/>
	 *
	 * Injection Point:<br/>
	 * {@link net.minecraft.world.entity.PathfinderMob#shouldStayCloseToLeashHolder()}<br/>
	 * Targets: IRETURN
	 */
	public static boolean overrideStayCloseToHolder(boolean prior, PathfinderMob mob) {
		return prior && !mob.hasData(TFDataAttachments.LEASH_PATHFINDER_OVERRIDE);
	}

	/**
	 * {@link twilightforest.asm.transformers.entity.UnrestrainedBlockSpeedAndJumpFactorTransformer} <p/>
	 *
	 * Injection Points:<br/>
	 * {@link net.minecraft.world.entity.Entity#getBlockJumpFactor()}<br/>
	 * {@link net.minecraft.world.entity.Entity#getBlockSpeedFactor()}<br/>
	 * Targets: FRETURN
	 */
	public static float resetFactorWithUnrestrained(float o, Entity entity) {
		return TravellersModifiersManager.isModifierActive(entity, TravellersModifiersManager.UNRESTRAINED_MODIFIER) ? 1.0F : o;
	}

	/**
	 * {@link twilightforest.asm.transformers.entity.ResetStuckUnrestrainedTransformer} <p/>
	 * <p>
	 * Injection Points:<br/>
	 * {@link net.minecraft.world.entity.Entity#move(MoverType, Vec3)}<br/>
	 */
	public static Entity resetStuckUnrestrained(Entity entity) {
		if (!(entity instanceof LivingEntity) || !TravellersModifiersManager.isModifierActive(entity, TravellersModifiersManager.UNRESTRAINED_MODIFIER))
			return entity;
		entity.makeStuckInBlock(entity.getBlockStateOn(), Vec3.ZERO);

		return entity;
	}
}
