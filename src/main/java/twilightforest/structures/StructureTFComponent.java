package twilightforest.structures;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import twilightforest.TFFeature;
import twilightforest.util.ColorUtil;

public abstract class StructureTFComponent extends StructurePiece {

	public StructureTFDecorator deco = null;
	public int spawnListIndex = 0;
	protected TFFeature feature = TFFeature.NOTHING;

	public StructureTFComponent(IStructurePieceType piece, CompoundNBT nbt) {
		super(piece, nbt);
		this.rotation = Rotation.NONE;
	}

	public StructureTFComponent(IStructurePieceType type, int i) {
		super(type, i);
		this.rotation = Rotation.NONE;
	}

	public StructureTFComponent(IStructurePieceType type, TFFeature feature, int i) {
		super(type, i);
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
			DyeColor[] colors = DyeColor.values();
			world.setBlockState(new BlockPos(this.getBoundingBox().minX, this.getBoundingBox().maxY + i    , this.getBoundingBox().minZ), ColorUtil.WOOL.getColor(colors[i]));
			world.setBlockState(new BlockPos(this.getBoundingBox().maxX, this.getBoundingBox().maxY + i + 1, this.getBoundingBox().minZ), ColorUtil.WOOL.getColor(colors[1 + i]));
			world.setBlockState(new BlockPos(this.getBoundingBox().minX, this.getBoundingBox().maxY + i + 2, this.getBoundingBox().maxZ), ColorUtil.WOOL.getColor(colors[2 + i]));
			world.setBlockState(new BlockPos(this.getBoundingBox().maxX, this.getBoundingBox().maxY + i + 3, this.getBoundingBox().maxZ), ColorUtil.WOOL.getColor(colors[3 + i]));
		}
	}

	@SuppressWarnings({"SameParameterValue", "unused"})
	protected void setDebugEntity(World world, int x, int y, int z, MutableBoundingBox sbb, String s) {
		setInvisibleTextEntity(world, x, y, z, sbb, s, shouldDebug(), 0f);
	}

	@SuppressWarnings({"SameParameterValue", "unused"})
	protected void setInvisibleTextEntity(World world, int x, int y, int z, MutableBoundingBox sbb, String s, boolean forcePlace, float additionalYOffset) {
		if (forcePlace) {
			final BlockPos pos = new BlockPos(this.getXWithOffset(x, z), this.getYWithOffset(y), this.getZWithOffset(x, z));

			if (sbb.isVecInside(pos)) {
				final ArmorStandEntity armorStand = new ArmorStandEntity(EntityType.ARMOR_STAND, world);
				armorStand.setCustomName(new StringTextComponent(s));
				armorStand.setLocationAndAngles(pos.getX() + 0.5, pos.getY() + additionalYOffset, pos.getZ() + 0.5, 0, 0);
				armorStand.setInvulnerable(true);
				armorStand.setInvisible(true);
				armorStand.getAlwaysRenderNameTagForRender();
				armorStand.setSilent(true);
				armorStand.setNoGravity(true);
				// set marker flag
				armorStand.getDataManager().set(ArmorStandEntity.STATUS, (byte) (armorStand.getDataManager().get(ArmorStandEntity.STATUS) | 16));
				world.addEntity(armorStand);
			}
		}
	}

	@SuppressWarnings({"SameParameterValue", "unused"})
	protected void setDebugEntity(World world, BlockPos blockpos, String s) {
		if (shouldDebug()) {
			final SheepEntity sheep = new SheepEntity(EntityType.SHEEP, world);
			sheep.setCustomName(new StringTextComponent(s));
			sheep.setNoAI(true);
			sheep.setLocationAndAngles(blockpos.getX() + 0.5, blockpos.getY() + 10, blockpos.getZ() + 0.5, 0, 0);
			sheep.setInvulnerable(true);
			sheep.setInvisible(true);
			sheep.getAlwaysRenderNameTagForRender();
			sheep.setSilent(true);
			sheep.setNoGravity(true);
			world.addEntity(sheep);
		}
	}

	//TODO: I do not think we need to write. As far as I can see, readAdditional does this itself
	//TODO: However, the NBT does need to be set in the constructor. Just in the ctors with (TemplateManager, CompoundNBT)
//	@Override
//	protected void writeStructureToNBT(CompoundNBT tagCompound) {
//		tagCompound.putInt("si", this.spawnListIndex);
//		tagCompound.putString("deco", StructureTFDecorator.getDecoString(this.deco));
//		tagCompound.putInt("rot", this.rotation.ordinal());
//	}

	@Override
	protected void readAdditional(CompoundNBT tagCompound) {
		this.spawnListIndex = tagCompound.getInt("si");
		this.deco = StructureTFDecorator.getDecoFor(tagCompound.getString("deco"));
		this.rotation = Rotation.values()[tagCompound.getInt("rot") % Rotation.values().length];
	}

	/**
	 * Does this component fall under block protection when progression is turned on, normally true
	 */
	public boolean isComponentProtected() {
		return true;
	}
}
