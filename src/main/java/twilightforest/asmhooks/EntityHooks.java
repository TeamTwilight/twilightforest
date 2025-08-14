package twilightforest.asmhooks;

import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.common.NeoForgeMod;
import org.jetbrains.annotations.Nullable;
import twilightforest.asm.transformers.entity.PathFinderUnrestrainedByLeashTransformer;
import twilightforest.init.TFDataAttachments;
import twilightforest.init.custom.TravellersModifiersManager;
import twilightforest.item.travellers_gear.TravellersArmorItem;

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

		if (!TravellersModifiersManager.isModifierActive(livingEntity.registryAccess(), livingEntity.getItemBySlot(EquipmentSlot.FEET), TravellersModifiersManager.WATER_WALK_MODIFIER))
			return null;

		double waterHeight = livingEntity.getFluidTypeHeight(NeoForgeMod.WATER_TYPE.value());
		boolean isWaterWalking = waterHeight > 0 &&
			waterHeight <= TravellersArmorItem.WATER_WALKING_MAX_SUBMERGED_HEIGHT &&
			!livingEntity.isShiftKeyDown();
		Level level = livingEntity.level();
		if (isWaterWalking && level.getGameTime() % 3 == 1)
			TravellersArmorItem.waterWalkingSplashEffect(livingEntity);
		return isWaterWalking;
	}

	/**
	 * {@link PathFinderUnrestrainedByLeashTransformer}
	 *
	 * Injection Point:<br/>
	 * {@link net.minecraft.world.entity.PathfinderMob#shouldStayCloseToLeashHolder()}<br/>
	 * Targets: IRETURN
	 */
	public static boolean overrideStayCloseToHolder(boolean prior, PathfinderMob mob) {
		return prior && !mob.hasData(TFDataAttachments.LEASH_PATHFINDER_OVERRIDE);
	}

}
