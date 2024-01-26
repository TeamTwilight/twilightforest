package twilightforest.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.entity.ai.goal.AttemptToGoHomeGoal;
import twilightforest.world.registration.TFGenerationSettings;

public interface EnforcedHomePoint {

	default <T extends PathfinderMob & EnforcedHomePoint> void addRestrictionGoals(T entity, GoalSelector selector) {
		selector.addGoal(5, new AttemptToGoHomeGoal<>(entity, 1.25D));
	}

	default void saveHomePointToNbt(CompoundTag tag) {
		if (this.getRestrictionPoint() != null) {
			GlobalPos.CODEC.encodeStart(NbtOps.INSTANCE, this.getRestrictionPoint()).resultOrPartial(TwilightForestMod.LOGGER::error).ifPresent(tag1 -> tag.put("HomePos", tag1));
		}
	}

	default void loadHomePointFromNbt(CompoundTag tag) {
		//properly load old home points, just assume theyre set in TF
		if (tag.contains("Home", 9)) {
			ListTag nbttaglist = tag.getList("Home", 6);
			double hx = nbttaglist.getDouble(0);
			double hy = nbttaglist.getDouble(1);
			double hz = nbttaglist.getDouble(2);
			this.setRestrictionPoint(GlobalPos.of(TFGenerationSettings.DIMENSION_KEY, BlockPos.containing(hx, hy, hz)));
		} else {
			if (tag.contains("HomePos")) {
				this.setRestrictionPoint(GlobalPos.CODEC.parse(NbtOps.INSTANCE, tag.get("HomePos")).resultOrPartial(TwilightForestMod.LOGGER::error).orElse(null));
			}
		}
	}

	default boolean isMobWithinHomeArea(Entity entity) {
		if (!this.isRestrictionPointValid(entity.level().dimension())) return true;
		return this.getRestrictionPoint().pos().distSqr(entity.blockPosition()) < (double)(this.getHomeRadius() * this.getHomeRadius());
	}

	default boolean isRestrictionPointValid(ResourceKey<Level> currentMobLevel) {
		return this.getRestrictionPoint() != null && this.getRestrictionPoint().dimension().equals(currentMobLevel);

	}

	@Nullable GlobalPos getRestrictionPoint();

	void setRestrictionPoint(@Nullable GlobalPos pos);

	int getHomeRadius();
}
