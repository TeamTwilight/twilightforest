package twilightforest.entity.passive;

import net.minecraft.entity.effect.EntityWeatherEffect;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;


public class EntityTFTinyFirefly extends EntityWeatherEffect {

	private int lifeTime;
	private int halfLife;

	public float glowSize;

	public EntityTFTinyFirefly(World world, double d, double d1, double d2) {
		super(world);
		setSize(0, 0);
		setLocationAndAngles(d, d1, d2, 0.0F, 0.0F);

		lifeTime = 10 + rand.nextInt(21);
		halfLife = lifeTime / 2;

		glowSize = 0.2f + (rand.nextFloat() * 0.6f);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		if (lifeTime <= 1) {
			setDead();
		} else {
			lifeTime--;
		}

	}

	public float getGlowBrightness() {
		if (lifeTime <= halfLife) {
			return (float) lifeTime / (float) halfLife;
		} else {
			return 1.0f - (((float) lifeTime - halfLife) / halfLife);
		}
	}

	@Override
	protected void entityInit() {
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {
	}

}
