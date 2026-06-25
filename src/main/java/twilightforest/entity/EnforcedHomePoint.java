package twilightforest.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
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
		//properly load old home points, just assume theyre set in TF
		if (tag.childrenList("Home").isPresent()) {
			ValueInput.ValueInputList nbttaglist = tag.childrenListOrEmpty("Home");
			double hx = 0.0;
			double hy = 0.0;
			double hz = 0.0;
			int i = 0;
			for (ValueInput element : nbttaglist) {
				if (i >= 3) break;
				if (i == 0) hx = element.getDoubleOr("", 0.0);
				if (i == 1) hy = element.getDoubleOr("", 0.0);
				if (i == 2) hz = element.getDoubleOr("", 0.0);
				i++;
			}
			this.setRestrictionPoint(GlobalPos.of(TFDimension.DIMENSION_KEY, BlockPos.containing(hx, hy, hz)));
		} else {
			if (tag.child("HomePos").isPresent()) {
				this.setRestrictionPoint(tag.read("HomePos", GlobalPos.CODEC).orElse(null));
			}
		}
	}

	default boolean isMobWithinHomeArea(Entity entity) {
		if (!this.isRestrictionPointValid(entity.level().dimension())) return true;
		return this.getRestrictionPoint().pos().distSqr(entity.blockPosition()) < (double) (this.getHomeRadius() * this.getHomeRadius());
	}

	default boolean isRestrictionPointValid(ResourceKey<Level> currentMobLevel) {
		return this.getRestrictionPoint() != null && this.getRestrictionPoint().dimension().equals(currentMobLevel);
	}

	@Nullable
	GlobalPos getRestrictionPoint();

	void setRestrictionPoint(@Nullable GlobalPos pos);

	int getHomeRadius();
}
