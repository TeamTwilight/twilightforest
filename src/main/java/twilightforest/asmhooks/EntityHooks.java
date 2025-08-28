package twilightforest.asmhooks;

import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.common.NeoForgeMod;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFDataAttachments;
import twilightforest.init.custom.TravellersModifiersManager;
import twilightforest.item.travellers_gear.TravellersGearLogic;

@SuppressWarnings({"JavadocReference", "unused"})
public class EntityHooks {

	/**
	 * {@link twilightforest.asm.transformers.entity.WaterWalkTransformer}<p/>
	 *
	 * Injection Point:<br/>
	 * {@link net.minecraft.world.entity.LivingEntity#canStandOnFluid(FluidState)}
	 */
	@Nullable
	public static Boolean processWaterWalking(LivingEntity livingEntity, FluidState fluidState) {
		if (!fluidState.is(FluidTags.WATER))
			return null;

		if (!TravellersModifiersManager.isModifierActive(livingEntity, livingEntity.getItemBySlot(EquipmentSlot.FEET), TravellersModifiersManager.WATER_WALK_MODIFIER))
			return null;

		double waterHeight = livingEntity.getFluidTypeHeight(NeoForgeMod.WATER_TYPE.value());
		boolean isWaterWalking = waterHeight > 0 &&
			waterHeight <= TravellersGearLogic.WATER_WALKING_MAX_SUBMERGED_HEIGHT &&
			!livingEntity.isShiftKeyDown();
		Level level = livingEntity.level();
		if (isWaterWalking && level.getGameTime() % 3 == 1)
			TravellersGearLogic.waterWalkingSplashEffect(livingEntity);
		return isWaterWalking;
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
		return entity instanceof LivingEntity living && TravellersModifiersManager.isModifierActive(entity, living.getItemBySlot(EquipmentSlot.FEET), TravellersModifiersManager.UNRESTRAINED_MODIFIER) ? 1.0F : o;
	}
}
