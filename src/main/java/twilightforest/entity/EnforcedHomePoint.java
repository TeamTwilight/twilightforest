package twilightforest.entity;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import twilightforest.entity.ai.goal.AttemptToGoHomeGoal;
import twilightforest.init.TFDimension;

public interface EnforcedHomePoint {
	default <T extends PathfinderMob & EnforcedHomePoint> void addRestrictionGoals(T entity, GoalSelector selector) {
		selector.addGoal(5, new AttemptToGoHomeGoal<>(entity, 1.25D));
	}

	default void saveHomePointToNbt(ValueOutput tag) {
		if (this.getRestrictionPoint() != null) {
			tag.store("HomePos", GlobalPos.CODEC, this.getRestrictionPoint());
		}
	}

	default void loadHomePointFromNbt(ValueInput tag) {
		var modernPos = tag.read("HomePos", GlobalPos.CODEC);

		if (modernPos.isPresent()) {
			this.setRestrictionPoint(modernPos.get());
		} else {
			tag.read("Home", Codec.DOUBLE.listOf()).ifPresent(doubleList -> {
				if (doubleList.size() >= 3) {
					double hx = doubleList.get(0);
					double hy = doubleList.get(1);
					double hz = doubleList.get(2);

					this.setRestrictionPoint(GlobalPos.of(
						TFDimension.DIMENSION_KEY,
						BlockPos.containing(hx, hy, hz)
					));
				}
			});
		}
	}


	default boolean isMobWithinHomeArea(Entity entity) {
		if (!this.isRestrictionPointValid(entity.level().dimension())) return true;
		return this.getRestrictionPoint().pos().distSqr(entity.blockPosition()) < (double) (this.getHomeRadius() * this.getHomeRadius());
	}

	default boolean isRestrictionPointValid(ResourceKey<@NotNull Level> currentMobLevel) {
		return this.getRestrictionPoint() != null && this.getRestrictionPoint().dimension().equals(currentMobLevel);
	}

	@Nullable
	GlobalPos getRestrictionPoint();

	void setRestrictionPoint(@Nullable GlobalPos pos);

	int getHomeRadius();
}
