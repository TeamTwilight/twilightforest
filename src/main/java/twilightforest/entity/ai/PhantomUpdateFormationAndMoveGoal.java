package twilightforest.entity.ai;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import twilightforest.entity.boss.KnightPhantom;
import twilightforest.item.TFItems;

import java.util.List;

public class PhantomUpdateFormationAndMoveGoal extends Goal {

	private static final float CIRCLE_SMALL_RADIUS = 2.5F;
	private static final float CIRCLE_LARGE_RADIUS = 8.5F;

	private final KnightPhantom boss;

	public PhantomUpdateFormationAndMoveGoal(KnightPhantom entity) {
		boss = entity;
	}

	@Override
	public boolean canUse() {
		return true;
	}

	@Override
	public void tick() {
		boss.noPhysics = boss.getTicksProgress() % 20 != 0;
		boss.setTicksProgress(boss.getTicksProgress() + 1);
		if (boss.getTicksProgress() >= boss.getMaxTicksForFormation())
			switchToNextFormation();
		Vec3 dest = getDestination();
		boss.getMoveControl().setWantedPosition(dest.x, dest.y, dest.z, boss.isChargingAtPlayer() ? 2 : 1);
	}

	public Vec3 getDestination() {

		if (!boss.hasHome())
			boss.restrictTo(boss.blockPosition(), 20);

		return switch (boss.getCurrentFormation()) {
			case LARGE_CLOCKWISE -> getCirclePosition(CIRCLE_LARGE_RADIUS, true);
			case SMALL_CLOCKWISE -> getCirclePosition(CIRCLE_SMALL_RADIUS, true);
			case LARGE_ANTICLOCKWISE -> getCirclePosition(CIRCLE_LARGE_RADIUS, false);
			case SMALL_ANTICLOCKWISE -> getCirclePosition(CIRCLE_SMALL_RADIUS, false);
			case CHARGE_PLUSX -> getMoveAcrossPosition(true, true);
			case CHARGE_MINUSX -> getMoveAcrossPosition(false, true);
			case CHARGE_PLUSZ -> getMoveAcrossPosition(true, false);
			case ATTACK_PLAYER_START, HOVER -> getHoverPosition(CIRCLE_LARGE_RADIUS);
			case CHARGE_MINUSZ -> getMoveAcrossPosition(false, false);
			default -> getLoiterPosition();
			case ATTACK_PLAYER_ATTACK -> getAttackPlayerPosition();
		};
	}

	/**
	 * Called each time the current formation ends.
	 * <p>
	 * If the current knight is the leader knight, it will pick and broadcast a new formation.
	 */
	private void switchToNextFormation() {
		List<KnightPhantom> nearbyKnights = boss.getNearbyKnights();

		if (boss.getCurrentFormation() == KnightPhantom.Formation.ATTACK_PLAYER_START) {
			boss.switchToFormation(KnightPhantom.Formation.ATTACK_PLAYER_ATTACK);
		} else if (boss.getCurrentFormation() == KnightPhantom.Formation.ATTACK_PLAYER_ATTACK) {
			if (nearbyKnights.size() > 1) {
				boss.switchToFormation(KnightPhantom.Formation.WAITING_FOR_LEADER);
			} else {
				// random weapon switch!
				switch (boss.getRandom().nextInt(3)) {
					case 0 -> boss.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(TFItems.KNIGHTMETAL_SWORD.get()));
					case 1 -> boss.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(TFItems.KNIGHTMETAL_AXE.get()));
					case 2 -> boss.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(TFItems.KNIGHTMETAL_PICKAXE.get()));
				}

				boss.switchToFormation(KnightPhantom.Formation.ATTACK_PLAYER_START);
			}
		} else if (boss.getCurrentFormation() == KnightPhantom.Formation.WAITING_FOR_LEADER) {
			// try to find a nearby knight and do what they're doing
			if (nearbyKnights.size() > 1) {
				boss.switchToFormation(nearbyKnights.get(1).getCurrentFormation());
				boss.setTicksProgress(nearbyKnights.get(1).getTicksProgress());
			} else {
				boss.switchToFormation(KnightPhantom.Formation.ATTACK_PLAYER_START);
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
	private boolean isThisTheLeader(List<KnightPhantom> nearbyKnights) {
		boolean iAmTheLowest = true;

		// find more knights
		for (KnightPhantom knight : nearbyKnights) {
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
		switch (boss.getRandom().nextInt(8)) {
			case 0:
			case 7:
				boss.switchToFormation(KnightPhantom.Formation.SMALL_CLOCKWISE);
				break;
			case 1:
			case 2:
				//boss.switchToFormation(EntityTFKnightPhantom.Formation.SMALL_ANTICLOCKWISE);
				break;
			case 3:
				boss.switchToFormation(KnightPhantom.Formation.CHARGE_PLUSX);
				break;
			case 4:
				boss.switchToFormation(KnightPhantom.Formation.CHARGE_MINUSX);
				break;
			case 5:
				boss.switchToFormation(KnightPhantom.Formation.CHARGE_PLUSZ);
				break;
			case 6:
				boss.switchToFormation(KnightPhantom.Formation.CHARGE_MINUSZ);
				break;
		}
	}

	/**
	 * Tell a random knight from the list to charge
	 */
	private void makeARandomKnightCharge(List<KnightPhantom> nearbyKnights) {
		int randomNum = boss.getRandom().nextInt(nearbyKnights.size());
		nearbyKnights.get(randomNum).switchToFormation(KnightPhantom.Formation.ATTACK_PLAYER_START);
	}

	private void broadcastMyFormation(List<KnightPhantom> nearbyKnights) {
		// find more knights
		for (KnightPhantom knight : nearbyKnights) {
			if (!knight.isChargingAtPlayer()) {
				knight.switchToFormation(boss.getCurrentFormation());
			}
		}
	}

	private boolean isNobodyCharging(List<KnightPhantom> nearbyKnights) {
		boolean noCharge = true;
		for (KnightPhantom knight : nearbyKnights) {
			if (knight.isChargingAtPlayer()) {
				noCharge = false;
				break; // don't bother checking more
			}
		}

		return noCharge;
	}

	private Vec3 getMoveAcrossPosition(boolean plus, boolean alongX) {
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

		double dx = boss.getRestrictCenter().getX() + (alongX ? offset0 : offset1);
		double dy = boss.getRestrictCenter().getY() + Math.cos(boss.getTicksProgress() / 7F + boss.getNumber());
		double dz = boss.getRestrictCenter().getZ() + (alongX ? offset1 : offset0);
		return new Vec3(dx, dy, dz);
	}

	private Vec3 getCirclePosition(float distance, boolean clockwise) {
		float angle = (boss.getTicksProgress() * 2.0F);

		if (!clockwise) {
			angle *= -1;
		}

		angle += (60F * boss.getNumber());

		double dx = boss.getRestrictCenter().getX() + Math.cos((angle) * Math.PI / 180.0D) * distance;
		double dy = boss.getRestrictCenter().getY() + Math.cos(boss.getTicksProgress() / 7F + boss.getNumber());
		double dz = boss.getRestrictCenter().getZ() + Math.sin((angle) * Math.PI / 180.0D) * distance;
		return new Vec3(dx, dy, dz);
	}

	private Vec3 getHoverPosition(float distance) {
		// bound this by distance so we don't hover in walls if we get knocked into them

		double dx = boss.xOld;
		double dy = boss.getRestrictCenter().getY() + Math.cos(boss.getTicksProgress() / 7F + boss.getNumber());
		double dz = boss.zOld;

		// let's just bound this by 2D distance
		double ox = (boss.getRestrictCenter().getX() - dx);
		double oz = (boss.getRestrictCenter().getZ() - dz);
		double dDist = Math.sqrt(ox * ox + oz * oz);

		if (dDist > distance) {
			// normalize back to boundaries

			dx = boss.getRestrictCenter().getX() + (ox / dDist * distance);
			dz = boss.getRestrictCenter().getZ() + (oz / dDist * distance);
		}

		return new Vec3(dx, dy, dz);
	}

	private Vec3 getLoiterPosition() {
		double dx = boss.getRestrictCenter().getX();
		double dy = boss.getRestrictCenter().getY() + Math.cos(boss.getTicksProgress() / 7F + boss.getNumber());
		double dz = boss.getRestrictCenter().getZ();
		return new Vec3(dx, dy, dz);
	}

	private Vec3 getAttackPlayerPosition() {
		if (boss.isSwordKnight()) {
			return Vec3.atLowerCornerOf(boss.getChargePos());
		} else {
			return getHoverPosition(CIRCLE_LARGE_RADIUS);
		}

	}
}