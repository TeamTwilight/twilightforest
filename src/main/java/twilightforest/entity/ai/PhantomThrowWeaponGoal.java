package twilightforest.entity.ai;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.Mth;
import twilightforest.TFSounds;
import twilightforest.entity.TFEntities;
import twilightforest.entity.boss.KnightPhantom;
import twilightforest.entity.projectile.ThrownWep;
import twilightforest.item.TFItems;

import java.util.EnumSet;

public class PhantomThrowWeaponGoal extends Goal {

	private final KnightPhantom boss;

	public PhantomThrowWeaponGoal(KnightPhantom entity) {
		boss = entity;
		setFlags(EnumSet.of(Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		return boss.getTarget() != null && boss.getCurrentFormation() == KnightPhantom.Formation.ATTACK_PLAYER_ATTACK;
	}

	@Override
	public void tick() {
		if (boss.getTarget() != null && boss.getTicksProgress() % 4 == 0) {
			if (boss.isAxeKnight())
				launchAxeAt(boss.getTarget());
			else if (boss.isPickKnight())
				launchPicks();
		}
	}

	private void launchAxeAt(Entity targetedEntity) {
		float bodyFacingAngle = ((boss.yBodyRot * 3.141593F) / 180F);
		double sx = boss.getX() + (Mth.cos(bodyFacingAngle) * 1);
		double sy = boss.getY() + (boss.getBbHeight() * 0.82);
		double sz = boss.getZ() + (Mth.sin(bodyFacingAngle) * 1);

		double tx = targetedEntity.getX() - sx;
		double ty = (targetedEntity.getBoundingBox().minY + targetedEntity.getBbHeight() / 2.0F) - (boss.getY() + boss.getBbHeight() / 2.0F);
		double tz = targetedEntity.getZ() - sz;

		boss.playSound(TFSounds.PHANTOM_THROW_AXE, 1.0F, (boss.getRandom().nextFloat() - boss.getRandom().nextFloat()) * 0.2F + 0.4F);
		ThrownWep projectile = new ThrownWep(TFEntities.THROWN_WEP, boss.level, boss).setItem(new ItemStack(TFItems.KNIGHTMETAL_AXE.get()));

		float speed = 0.75F;

		projectile.shoot(tx, ty, tz, speed, 1.0F);

		projectile.moveTo(sx, sy, sz, boss.getYRot(), boss.getXRot());

		boss.level.addFreshEntity(projectile);
	}

	private void launchPicks() {
		boss.playSound(TFSounds.PHANTOM_THROW_PICK, 1.0F, (boss.getRandom().nextFloat() - boss.getRandom().nextFloat()) * 0.2F + 0.4F);

		for (int i = 0; i < 8; i++) {
			float throwAngle = i * 3.14159165F / 4F;

			double sx = boss.getX() + (Mth.cos(throwAngle) * 1);
			double sy = boss.getY() + (boss.getBbHeight() * 0.82);
			double sz = boss.getZ() + (Mth.sin(throwAngle) * 1);

			double vx = Mth.cos(throwAngle);
			double vy = 0;
			double vz = Mth.sin(throwAngle);


			ThrownWep projectile = new ThrownWep(TFEntities.THROWN_WEP, boss.level, boss).setDamage(3).setVelocity(0.015F).setItem(new ItemStack(TFItems.KNIGHTMETAL_PICKAXE.get()));


			projectile.moveTo(sx, sy, sz, i * 45F, boss.getXRot());

			float speed = 0.5F;

			projectile.shoot(vx, vy, vz, speed, 1.0F);

			boss.level.addFreshEntity(projectile);
		}
	}
}
