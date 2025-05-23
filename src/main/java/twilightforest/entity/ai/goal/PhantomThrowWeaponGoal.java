package twilightforest.entity.ai.goal;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.gameevent.GameEvent;
import twilightforest.entity.boss.KnightPhantom;
import twilightforest.entity.projectile.ThrownWep;
import twilightforest.init.TFEntities;
import twilightforest.init.TFItems;
import twilightforest.init.TFSounds;

import java.util.EnumSet;

public class PhantomThrowWeaponGoal extends Goal {

	private final KnightPhantom boss;

	public PhantomThrowWeaponGoal(KnightPhantom entity) {
		this.boss = entity;
		setFlags(EnumSet.of(Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		return this.boss.getTarget() != null && this.boss.getCurrentFormation() == KnightPhantom.Formation.ATTACK_PLAYER_ATTACK;
	}

	@Override
	public void tick() {
		if (this.boss.getTarget() != null && this.boss.getTicksProgress() % 4 == 0) {
			if (this.boss.isAxeKnight())
				this.launchAxeAt(this.boss.getTarget());
			else if (this.boss.isPickKnight())
				this.launchPicks();
		}
	}

	private void launchAxeAt(Entity targetedEntity) {
		float bodyFacingAngle = ((this.boss.yBodyRot * Mth.PI) / 180F);
		double sx = this.boss.getX() + (Mth.cos(bodyFacingAngle) * 1);
		double sy = this.boss.getY() + (this.boss.getBbHeight() * 0.82D);
		double sz = this.boss.getZ() + (Mth.sin(bodyFacingAngle) * 1);

		double tx = targetedEntity.getX() - sx;
		double ty = (targetedEntity.getBoundingBox().minY + targetedEntity.getBbHeight() / 2.0F) - (this.boss.getY() + this.boss.getBbHeight() / 2.0F);
		double tz = targetedEntity.getZ() - sz;

		this.boss.playSound(TFSounds.KNIGHT_PHANTOM_THROW_AXE.get(), 1.0F, (this.boss.getRandom().nextFloat() - this.boss.getRandom().nextFloat()) * 0.2F + 0.4F);
		this.boss.gameEvent(GameEvent.PROJECTILE_SHOOT);
		ThrownWep projectile = new ThrownWep(TFEntities.THROWN_WEP.get(), this.boss.level(), this.boss).setItem(new ItemStack(TFItems.KNIGHTMETAL_AXE.get()));

		float speed = 0.75F;

		projectile.shoot(tx, ty, tz, speed, 1.0F);

		projectile.moveTo(sx, sy, sz, this.boss.getYRot(), this.boss.getXRot());

		this.boss.level().addFreshEntity(projectile);
	}

	private void launchPicks() {
		this.boss.playSound(TFSounds.KNIGHT_PHANTOM_THROW_PICK.get(), 1.0F, (boss.getRandom().nextFloat() - boss.getRandom().nextFloat()) * 0.2F + 0.4F);
		this.boss.gameEvent(GameEvent.PROJECTILE_SHOOT);

		for (int i = 0; i < 8; i++) {
			float throwAngle = i * Mth.PI / 4F;

			double sx = this.boss.getX() + (Mth.cos(throwAngle) * 1);
			double sy = this.boss.getY() + (this.boss.getBbHeight() * 0.82D);
			double sz = this.boss.getZ() + (Mth.sin(throwAngle) * 1);

			double vx = Mth.cos(throwAngle);
			double vy = 0;
			double vz = Mth.sin(throwAngle);


			ThrownWep projectile = new ThrownWep(TFEntities.THROWN_WEP.get(), this.boss.level(), this.boss).setDamage(3).setVelocity(0.015F).setItem(new ItemStack(TFItems.KNIGHTMETAL_PICKAXE.get()));


			projectile.moveTo(sx, sy, sz, i * 45F, this.boss.getXRot());

			float speed = 0.5F;

			projectile.shoot(vx, vy, vz, speed, 1.0F);

			this.boss.level().addFreshEntity(projectile);
		}
	}
}
