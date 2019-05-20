package twilightforest.entity;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityTFProtectionBox extends Entity {

	public int lifeTime = 60;

	public final int sizeX;
	public final int sizeY;
	public final int sizeZ;

	private final StructureBoundingBox sbb;

	public EntityTFProtectionBox(World world, StructureBoundingBox sbb) {
		super(world);

		this.sbb = new StructureBoundingBox(sbb);

		this.setLocationAndAngles(sbb.minX, sbb.minY, sbb.minZ, 0.0F, 0.0F);

		sizeX = sbb.getXSize();
		sizeY = sbb.getYSize();
		sizeZ = sbb.getZSize();

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

	public boolean matches(StructureBoundingBox sbb) {
		return this.sbb.minX == sbb.minX && this.sbb.minY == sbb.minY && this.sbb.minZ == sbb.minZ
				&& this.sbb.maxX == sbb.maxX && this.sbb.maxY == sbb.maxY && this.sbb.maxZ == sbb.maxZ;
	}

	public void resetLifetime() {
		lifeTime = 60;
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
	protected void entityInit() {}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean canRenderOnFire() {
		return false;
	}
}
