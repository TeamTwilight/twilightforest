package twilightforest.entity;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityTFProtectionBox extends Entity {

	public int lifeTime = 60;
	public int sizeX;
	public int sizeY;
	public int sizeZ;

	public EntityTFProtectionBox(World world, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
		super(world);

		this.setLocationAndAngles(minX, minY, minZ, 0.0F, 0.0F);

		sizeX = Math.abs(maxX - minX) + 1;
		sizeY = Math.abs(maxY - minY) + 1;
		sizeZ = Math.abs(maxZ - minZ) + 1;

		this.setSize(Math.max(sizeX, sizeZ), sizeY);
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

	@Override
	public float getBrightness() {
		return 1.0F;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public int getBrightnessForRender() {
		return 15728880;
	}

	@Override
	protected void entityInit() {
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean canRenderOnFire() {
		return false;
	}

}
