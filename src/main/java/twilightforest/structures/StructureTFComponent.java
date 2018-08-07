package twilightforest.structures;

import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.template.TemplateManager;
import twilightforest.TFFeature;


public abstract class StructureTFComponent extends StructureComponent {

	public StructureTFDecorator deco = null;
	public int spawnListIndex = 0;
	protected TFFeature feature = TFFeature.NOTHING;

	public StructureTFComponent() {
		this.rotation = Rotation.NONE;
	}

	public StructureTFComponent(int i) {
		super(i);
		this.rotation = Rotation.NONE;
	}

	public StructureTFComponent(TFFeature feature, int i) {
		super(i);
		this.feature = feature;
		this.rotation = Rotation.NONE;
	}

	public TFFeature getFeatureType() {
		return feature;
	}

	protected static boolean shouldDebug() {
		return false;
	}

	@SuppressWarnings({"SameParameterValue", "unused"})
	protected void setDebugCorners(World world) {
		if (rotation == null) rotation = Rotation.NONE;

		if (shouldDebug() ) { // && rotation!= Rotation.NONE) {
			int i = rotation.ordinal() * 4;
			world.setBlockState(new BlockPos(this.getBoundingBox().minX, this.getBoundingBox().maxY + i    , this.getBoundingBox().minZ), Blocks.WOOL.getStateFromMeta(i));
			world.setBlockState(new BlockPos(this.getBoundingBox().maxX, this.getBoundingBox().maxY + i + 1, this.getBoundingBox().minZ), Blocks.WOOL.getStateFromMeta(1 + i));
			world.setBlockState(new BlockPos(this.getBoundingBox().minX, this.getBoundingBox().maxY + i + 2, this.getBoundingBox().maxZ), Blocks.WOOL.getStateFromMeta(2 + i));
			world.setBlockState(new BlockPos(this.getBoundingBox().maxX, this.getBoundingBox().maxY + i + 3, this.getBoundingBox().maxZ), Blocks.WOOL.getStateFromMeta(3 + i));
		}
	}

	@SuppressWarnings({"SameParameterValue", "unused"})
	protected void setDebugEntity(World world, int x, int y, int z, StructureBoundingBox sbb, String s) {
		setInvisibleTextEntity(world, x, y, z, sbb, s, shouldDebug(), 0f);
	}

	@SuppressWarnings({"SameParameterValue", "unused"})
	protected void setInvisibleTextEntity(World world, int x, int y, int z, StructureBoundingBox sbb, String s, boolean forcePlace, float additionalYOffset) {
		if (forcePlace) {
			final BlockPos pos = new BlockPos(this.getXWithOffset(x, z), this.getYWithOffset(y), this.getZWithOffset(x, z));

			if (sbb.isVecInside(pos)) {
				final EntityArmorStand armorStand = new EntityArmorStand(world);
				armorStand.setCustomNameTag(s);
				armorStand.setLocationAndAngles(pos.getX() + 0.5, pos.getY() + additionalYOffset, pos.getZ() + 0.5, 0, 0);
				armorStand.setEntityInvulnerable(true);
				armorStand.setInvisible(true);
				armorStand.setAlwaysRenderNameTag(true);
				armorStand.setSilent(true);
				armorStand.setNoGravity(true);
				// set marker flag
				armorStand.getDataManager().set(EntityArmorStand.STATUS, (byte) (armorStand.getDataManager().get(EntityArmorStand.STATUS) | 16));
				world.spawnEntity(armorStand);
			}
		}
	}

	@SuppressWarnings({"SameParameterValue", "unused"})
	protected void setDebugEntity(World world, BlockPos blockpos, String s) {
		if (shouldDebug()) {
			final EntitySheep sheep = new EntitySheep(world);
			sheep.setCustomNameTag(s);
			sheep.setNoAI(true);
			sheep.setLocationAndAngles(blockpos.getX() + 0.5, blockpos.getY() + 10, blockpos.getZ() + 0.5, 0, 0);
			sheep.setEntityInvulnerable(true);
			sheep.setInvisible(true);
			sheep.setAlwaysRenderNameTag(true);
			sheep.setSilent(true);
			sheep.setNoGravity(true);
			world.spawnEntity(sheep);
		}
	}

	@Override
	protected void writeStructureToNBT(NBTTagCompound tagCompound) {
		tagCompound.setInteger("si", this.spawnListIndex);
		tagCompound.setString("deco", StructureTFDecorator.getDecoString(this.deco));
		tagCompound.setInteger("rot", this.rotation.ordinal());
	}

	@Override
	protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager templateManager) {
		this.spawnListIndex = tagCompound.getInteger("si");
		this.deco = StructureTFDecorator.getDecoFor(tagCompound.getString("deco"));
		this.rotation = Rotation.values()[tagCompound.getInteger("rot") % Rotation.values().length];
	}

	/**
	 * Does this component fall under block protection when progression is turned on, normally true
	 */
	public boolean isComponentProtected() {
		return true;
	}
}