package twilightforest.entity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;

public interface IBreathAttacker {

	boolean isBreathing();

	void setBreathing(boolean flag);

	/**
	 * Deal damage for our breath attack
	 *
	 * @param target
	 */
	void doBreathAttack(ServerLevel level, Entity target);
}