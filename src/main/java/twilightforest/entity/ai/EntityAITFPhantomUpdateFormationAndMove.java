package twilightforest.entity.ai;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import twilightforest.entity.boss.EntityTFKnightPhantom;
import twilightforest.item.TFItems;

import java.util.List;

public class EntityAITFPhantomUpdateFormationAndMove extends EntityAIBase {

	private static final float CIRCLE_SMALL_RADIUS = 2.5F;
	private static final float CIRCLE_LARGE_RADIUS = 8.5F;

	private final EntityTFKnightPhantom boss;

	public EntityAITFPhantomUpdateFormationAndMove(EntityTFKnightPhantom entity) {
		boss = entity;
	}

	@Override
	public boolean shouldExecute() {
		return true;
	}

	@Override
	public void updateTask() {
		boss.noClip = boss.getTicksProgress() % 20 != 0;
		boss.setTicksProgress(boss.getTicksProgress() + 1);
		if (boss.getTicksProgress() >= boss.getMaxTicksForFormation())
			switchToNextFormation();
		Vec3d dest = getDestination();
		boss.getMoveHelper().setMoveTo(dest.x, dest.y, dest.z, boss.isChargingAtPlayer() ? 2 : 1);
	}

	public Vec3d getDestination() {

		if (!boss.hasHome())
			boss.setHomePosAndDistance(boss.getPosition(), 20);

		switch (boss.getCurrentFormation()) {
			case LARGE_CLOCKWISE:
				return getCirclePosition(CIRCLE_LARGE_RADIUS, true);
			case SMALL_CLOCKWISE:
				return getCirclePosition(CIRCLE_SMALL_RADIUS, true);
			case LARGE_ANTICLOCKWISE:
				return getCirclePosition(CIRCLE_LARGE_RADIUS, false);
			case SMALL_ANTICLOCKWISE:
				return getCirclePosition(CIRCLE_SMALL_RADIUS, false);
			case CHARGE_PLUSX:
				return getMoveAcrossPosition(true, true);
			case CHARGE_MINUSX:
				return getMoveAcrossPosition(false, true);
			case CHARGE_PLUSZ:
				return getMoveAcrossPosition(true, false);
			case ATTACK_PLAYER_START:
			case HOVER:
				return getHoverPosition(CIRCLE_LARGE_RADIUS);
			case CHARGE_MINUSZ:
				return getMoveAcrossPosition(false, false);
			case WAITING_FOR_LEADER:
				return getLoiterPosition();
			case ATTACK_PLAYER_ATTACK:
				return getAttackPlayerPosition();
			default:
				return getLoiterPosition();
		}
	}

	/**
	 * Called each time the current formation ends.
	 * <p>
	 * If the current knight is the leader knight, it will pick and broadcast a new formation.
	 */
	private void switchToNextFormation() {
		List<EntityTFKnightPhantom> nearbyKnights = boss.getNearbyKnights();

		if (boss.getCurrentFormation() == EntityTFKnightPhantom.Formation.ATTACK_PLAYER_START) {
			boss.switchToFormation(EntityTFKnightPhantom.Formation.ATTACK_PLAYER_ATTACK);
		} else if (boss.getCurrentFormation() == EntityTFKnightPhantom.Formation.ATTACK_PLAYER_ATTACK) {
			if (nearbyKnights.size() > 1) {
				boss.switchToFormation(EntityTFKnightPhantom.Formation.WAITING_FOR_LEADER);
			} else {
				// random weapon switch!
				switch (boss.getRNG().nextInt(3)) {
					case 0:
						boss.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(TFItems.knightmetal_sword));
						break;
					case 1:
						boss.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(TFItems.knightmetal_axe));
						break;
					case 2:
						boss.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(TFItems.knightmetal_pickaxe));
						break;
				}

				boss.switchToFormation(EntityTFKnightPhantom.Formation.ATTACK_PLAYER_START);
			}
		} else if (boss.getCurrentFormation() == EntityTFKnightPhantom.Formation.WAITING_FOR_LEADER) {
			// try to find a nearby knight and do what they're doing
			if (nearbyKnights.size() > 1) {
				boss.switchToFormation(nearbyKnights.get(1).getCurrentFormation());
				boss.setTicksProgress(nearbyKnights.get(1).getTicksProgress());
			} else {
				boss.switchToFormation(EntityTFKnightPhantom.Formation.ATTACK_PLAYER_START);
			}
		} else {

			if (isThisTheLeader(nearbyKnights)) {
				// pick a random formation
				pickRandomFormation();

				// broadcast it
				broadcastMyFormation(nearbyKnights);

				// if no one is charging
				if (isNobodyCharging(nearbyKnights)) {
					makeARandomKnightCharge(nearbyKnights);
				}
			}
		}
	}

	/**
	 * Check within 20ish squares.  If this phantom is the lowest numbered one, return true
	 */
	private boolean isThisTheLeader(List<EntityTFKnightPhantom> nearbyKnights) {
		boolean iAmTheLowest = true;

		// find more knights
		for (EntityTFKnightPhantom knight : nearbyKnights) {
			if (knight.getNumber() < boss.getNumber()) {
				iAmTheLowest = false;
				break; // don't bother checking more
			}
		}

		return iAmTheLowest;
	}

	/**
	 * Pick a random formation.  Called by the leader when his current formation duration ends
	 */
	private void pickRandomFormation() {
		switch (boss.getRNG().nextInt(8)) {
			case 0:
				boss.switchToFormation(EntityTFKnightPhantom.Formation.SMALL_CLOCKWISE);
				break;
			case 1:
				boss.switchToFormation(EntityTFKnightPhantom.Formation.SMALL_ANTICLOCKWISE);
				//boss.switchToFormation(EntityTFKnightPhantom.Formation.LARGE_ANTICLOCKWISE);
				break;
			case 2:
				boss.switchToFormation(EntityTFKnightPhantom.Formation.SMALL_ANTICLOCKWISE);
				break;
			case 3:
				boss.switchToFormation(EntityTFKnightPhantom.Formation.CHARGE_PLUSX);
				break;
			case 4:
				boss.switchToFormation(EntityTFKnightPhantom.Formation.CHARGE_MINUSX);
				break;
			case 5:
				boss.switchToFormation(EntityTFKnightPhantom.Formation.CHARGE_PLUSZ);
				break;
			case 6:
				boss.switchToFormation(EntityTFKnightPhantom.Formation.CHARGE_MINUSZ);
				break;
			case 7:
				boss.switchToFormation(EntityTFKnightPhantom.Formation.SMALL_CLOCKWISE);
				//boss.switchToFormation(EntityTFKnightPhantom.Formation.LARGE_CLOCKWISE);
				break;
		}
	}

	/**
	 * Tell a random knight from the list to charge
	 */
	private void makeARandomKnightCharge(List<EntityTFKnightPhantom> nearbyKnights) {
		int randomNum = boss.getRNG().nextInt(nearbyKnights.size());
		nearbyKnights.get(randomNum).switchToFormation(EntityTFKnightPhantom.Formation.ATTACK_PLAYER_START);
	}

	private void broadcastMyFormation(List<EntityTFKnightPhantom> nearbyKnights) {
		// find more knights
		for (EntityTFKnightPhantom knight : nearbyKnights) {
			if (!knight.isChargingAtPlayer()) {
				knight.switchToFormation(boss.getCurrentFormation());
			}
		}
	}

	private boolean isNobodyCharging(List<EntityTFKnightPhantom> nearbyKnights) {
		boolean noCharge = true;
		for (EntityTFKnightPhantom knight : nearbyKnights) {
			if (knight.isChargingAtPlayer()) {
				noCharge = false;
				break; // don't bother checking more
			}
		}

		return noCharge;
	}

	private Vec3d getMoveAcrossPosition(boolean plus, boolean alongX) {
		float offset0 = (boss.getNumber() * 3F) - 7.5F;
		float offset1;

		if (boss.getTicksProgress() < 60) {
			offset1 = -7F;
		} else {
			offset1 = -7F + (((boss.getTicksProgress() - 60) / 120F) * 14F);
		}

		if (!plus) {
			offset1 *= -1;
		}

		double dx = boss.getHomePosition().getX() + (alongX ? offset0 : offset1);
		double dy = boss.getHomePosition().getY() + Math.cos(boss.getTicksProgress() / 7F + boss.getNumber());
		double dz = boss.getHomePosition().getZ() + (alongX ? offset1 : offset0);
		return new Vec3d(dx, dy, dz);
	}

	private Vec3d getCirclePosition(float distance, boolean clockwise) {
		float angle = (boss.getTicksProgress() * 2.0F);

		if (!clockwise) {
			angle *= -1;
		}

		angle += (60F * boss.getNumber());

		double dx = boss.getHomePosition().getX() + Math.cos((angle) * Math.PI / 180.0D) * distance;
		double dy = boss.getHomePosition().getY() + Math.cos(boss.getTicksProgress() / 7F + boss.getNumber());
		double dz = boss.getHomePosition().getZ() + Math.sin((angle) * Math.PI / 180.0D) * distance;
		return new Vec3d(dx, dy, dz);
	}

	private Vec3d getHoverPosition(float distance) {
		// bound this by distance so we don't hover in walls if we get knocked into them

		double dx = boss.lastTickPosX;
		double dy = boss.getHomePosition().getY() + Math.cos(boss.getTicksProgress() / 7F + boss.getNumber());
		double dz = boss.lastTickPosZ;

		// let's just bound this by 2D distance
		double ox = (boss.getHomePosition().getX() - dx);
		double oz = (boss.getHomePosition().getZ() - dz);
		double dDist = Math.sqrt(ox * ox + oz * oz);

		if (dDist > distance) {
			// normalize back to boundaries

			dx = boss.getHomePosition().getX() + (ox / dDist * distance);
			dz = boss.getHomePosition().getZ() + (oz / dDist * distance);
		}

		return new Vec3d(dx, dy, dz);
	}

	private Vec3d getLoiterPosition() {
		double dx = boss.getHomePosition().getX();
		double dy = boss.getHomePosition().getY() + Math.cos(boss.getTicksProgress() / 7F + boss.getNumber());
		double dz = boss.getHomePosition().getZ();
		return new Vec3d(dx, dy, dz);
	}

	private Vec3d getAttackPlayerPosition() {
		if (boss.isSwordKnight()) {
			return new Vec3d(boss.getChargePos());
		} else {
			return getHoverPosition(CIRCLE_LARGE_RADIUS);
		}

	}
}