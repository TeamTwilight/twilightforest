package twilightforest.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.world.World;
import twilightforest.client.particle.TFParticleType;
import twilightforest.entity.ai.EntityAICubeCenterOnSymbol;
import twilightforest.entity.ai.EntityAICubeMoveToRedstoneSymbols;

public class EntityTFRovingCube extends MonsterEntity {

	// data needed for cube AI

	// last circle visited
	public boolean hasFoundSymbol = false;
	public int symbolX = 0;
	public int symbolY = 0;
	public int symbolZ = 0;

	// direction traveling

	// blocks traveled

	public EntityTFRovingCube(EntityType<? extends EntityTFRovingCube> type, World world) {
		super(type, world);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new EntityAICubeMoveToRedstoneSymbols(this, 1.0D));
		this.goalSelector.addGoal(1, new EntityAICubeCenterOnSymbol(this, 1.0D));
	}

	@Override
	protected void registerAttributes() {
		super.registerAttributes();
		this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0D);
		this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23000000417232513D);
		this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5.0D);
	}

	@Override
	public void livingTick() {
		super.livingTick();

		if (this.world.isRemote) {
			for (int i = 0; i < 3; i++) {
				float px = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.75F;
				float py = this.getEyeHeight() - 0.25F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.75F;
				float pz = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.75F;

				world.addParticle(TFParticleType.ANNIHILATE.get(), this.lastTickPosX + px, this.lastTickPosY + py, this.lastTickPosZ + pz, 0, 0, 0);
			}
		}
	}
}
