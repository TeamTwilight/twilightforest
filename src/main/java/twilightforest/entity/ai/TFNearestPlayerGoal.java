package twilightforest.entity.ai;

import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.TargetGoal;
import net.minecraft.entity.player.PlayerEntity;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

/**
 * Customized AI that checks more that 4 blocks up/down and also ignores sight
 */
//TODO: High chance the superclass is wrong. Verify
public class TFNearestPlayerGoal extends NearestAttackableTargetGoal<PlayerEntity> {
	/**
	 * VanillaCopy super, but change predicate to not check sight, or bother reducing range for sneaking/invisibility
	 */
	public TFNearestPlayerGoal(MobEntity entityLivingIn) {
		super(entityLivingIn, PlayerEntity.class, false);
	}

	// TODO: Currently the predicate is based off the entity's follow range

	/**
	 * VanillaCopy super, but change bounding box y expansion from 4 to full range
	 */
	// TODO: No longer necessary as setting 3rd parameter to false = ignore line of sight
	// TODO: While the player entity predicate checks the entire player list
	// TODO: Potentially could be too far now?
}
