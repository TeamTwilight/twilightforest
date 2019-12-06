package twilightforest.entity;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import twilightforest.TwilightForestMod;
import twilightforest.client.particle.TFParticleType;
import twilightforest.entity.ai.EntityAICubeCenterOnSymbol;
import twilightforest.entity.ai.EntityAICubeMoveToRedstoneSymbols;

public class EntityTFRovingCube extends EntityMob {

	public static final ResourceLocation LOOT_TABLE = TwilightForestMod.prefix("entities/roving_cube");
	// data needed for cube AI

	// last circle visited
	public boolean hasFoundSymbol = false;
	public int symbolX = 0;
	public int symbolY = 0;
	public int symbolZ = 0;

	// direction traveling

	// blocks traveled

	public EntityTFRovingCube(World world) {
		super(world);
		setSize(1.2F, 2.1F);
	}

	@Override
	protected void registerGoals() {
		this.tasks.addTask(0, new EntityAICubeMoveToRedstoneSymbols(this, 1.0D));
		this.tasks.addTask(1, new EntityAICubeCenterOnSymbol(this, 1.0D));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23000000417232513D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5.0D);
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();

		if (this.world.isRemote) {
			for (int i = 0; i < 3; i++) {
				float px = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.75F;
				float py = this.getEyeHeight() - 0.25F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.75F;
				float pz = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.75F;

				TwilightForestMod.proxy.spawnParticle(TFParticleType.ANNIHILATE, this.lastTickPosX + px, this.lastTickPosY + py, this.lastTickPosZ + pz, 0, 0, 0);
			}
		}
	}

	@Override
	protected ResourceLocation getLootTable() {
		return LOOT_TABLE;
	}
}
