package twilightforest.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import twilightforest.entity.ai.goal.CubeCenterOnSymbolGoal;
import twilightforest.entity.ai.goal.CubeMoveToRedstoneSymbolsGoal;
import twilightforest.init.TFParticleType;

public class RovingCube extends Monster {

	// data needed for cube AI

	// last circle visited
	public boolean hasFoundSymbol = false;
	public int symbolX = 0;
	public int symbolY = 0;
	public int symbolZ = 0;

	// direction traveling

	// blocks traveled

	public RovingCube(EntityType<? extends RovingCube> type, Level world) {
		super(type, world);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new CubeMoveToRedstoneSymbolsGoal(this, 1.0D));
		this.goalSelector.addGoal(1, new CubeCenterOnSymbolGoal(this, 1.0D));
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Monster.createMonsterAttributes()
			.add(Attributes.MAX_HEALTH, 10.0D)
			.add(Attributes.MOVEMENT_SPEED, 0.23D)
			.add(Attributes.ATTACK_DAMAGE, 5.0D);
	}

	@Override
	public void aiStep() {
		super.aiStep();

		if (this.level().isClientSide()) {
			for (int i = 0; i < 3; i++) {
				float px = (this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 2.0F;
				float py = this.getEyeHeight() - 0.25F + (this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 2.0F;
				float pz = (this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 2.0F;

				this.level().addParticle(TFParticleType.ANNIHILATE.get(), this.xOld + px, this.yOld + py, this.zOld + pz, 0, 0, 0);
			}
		}
	}
}
