package twilightforest.structures;

import java.util.Set;

import com.google.common.collect.ImmutableSet;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.DyeColor;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import twilightforest.TFFeature;
import twilightforest.block.TFBlocks;
import twilightforest.util.ColorUtil;

public abstract class StructureTFComponent extends StructurePiece {

	public StructureTFDecorator deco = null;
	public int spawnListIndex = 0;
	protected TFFeature feature = TFFeature.NOTHING;
	private static final Set<Block> BLOCKS_NEEDING_POSTPROCESSING = ImmutableSet.<Block>builder()
			.add(Blocks.NETHER_BRICK_FENCE)
			.add(Blocks.TORCH)
			.add(Blocks.WALL_TORCH)
			.add(Blocks.OAK_FENCE)
			.add(Blocks.SPRUCE_FENCE)
			.add(Blocks.DARK_OAK_FENCE)
			.add(Blocks.ACACIA_FENCE)
			.add(Blocks.BIRCH_FENCE)
			.add(Blocks.JUNGLE_FENCE)
			.add(Blocks.LADDER)
			.add(Blocks.IRON_BARS)
			.add(Blocks.GLASS_PANE)
			.add(Blocks.OAK_STAIRS)
			.add(Blocks.BIRCH_STAIRS)
			.add(Blocks.COBBLESTONE_WALL)
			.add(Blocks.RED_MUSHROOM_BLOCK)
			.add(Blocks.BROWN_MUSHROOM_BLOCK)
			.add(Blocks.CHEST)
			.add(Blocks.TRAPPED_CHEST)
			.add(Blocks.STONE_BRICK_STAIRS)
			.add(TFBlocks.castle_stairs_brick.get())
			.add(TFBlocks.force_field_blue.get())
			.add(TFBlocks.force_field_green.get())
			.add(TFBlocks.force_field_pink.get())
			.add(TFBlocks.force_field_purple.get())
			.add(TFBlocks.force_field_orange.get())
			.add(TFBlocks.brown_thorns.get())
			.add(TFBlocks.green_thorns.get())
			.build();


	public StructureTFComponent(IStructurePieceType piece, CompoundNBT nbt) {
		super(piece, nbt);
		this.spawnListIndex = nbt.getInt("si");
		this.deco = StructureTFDecorator.getDecoFor(nbt.getString("deco"));
		this.rotation = Rotation.values()[nbt.getInt("rot") % Rotation.values().length];
		this.rotation = Rotation.NONE;
	}

	public StructureTFComponent(IStructurePieceType type, int i) {
		super(type, i);
		this.rotation = Rotation.NONE;
	}

	public StructureTFComponent(IStructurePieceType type, TFFeature feature, int i) {
		this(type, i);
		this.feature = feature;
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
	protected void setDebugEntity(ISeedReader world, int x, int y, int z, MutableBoundingBox sbb, String s) {
		setInvisibleTextEntity(world, x, y, z, sbb, s, shouldDebug(), 0f);
	}

	@SuppressWarnings({"SameParameterValue", "unused"})
	protected void setInvisibleTextEntity(ISeedReader world, int x, int y, int z, MutableBoundingBox sbb, String s, boolean forcePlace, float additionalYOffset) {
		if (forcePlace) {
			final BlockPos pos = new BlockPos(this.getXWithOffset(x, z), this.getYWithOffset(y), this.getZWithOffset(x, z));

			if (sbb.isVecInside(pos)) {
				// FIXME
				/*final ArmorStandEntity armorStand = new ArmorStandEntity(EntityType.ARMOR_STAND, world);
				armorStand.setCustomName(new StringTextComponent(s));
				armorStand.setLocationAndAngles(pos.getX() + 0.5, pos.getY() + additionalYOffset, pos.getZ() + 0.5, 0, 0);
				armorStand.setInvulnerable(true);
				armorStand.setInvisible(true);
				armorStand.getAlwaysRenderNameTagForRender();
				armorStand.setSilent(true);
				armorStand.setNoGravity(true);
				// set marker flag
				armorStand.getDataManager().set(ArmorStandEntity.STATUS, (byte) (armorStand.getDataManager().get(ArmorStandEntity.STATUS) | 16));
				world.addEntity(armorStand);*/
			}
		}
	}
	
	@Override
	protected void setBlockState(ISeedReader worldIn, BlockState blockstateIn, int x, int y, int z, MutableBoundingBox boundingboxIn) {
	      BlockPos blockpos = new BlockPos(this.getXWithOffset(x, z), this.getYWithOffset(y), this.getZWithOffset(x, z));

	      if (boundingboxIn.isVecInside(blockpos)) {
	          if (this.mirror != Mirror.NONE) {
	             blockstateIn = blockstateIn.mirror(this.mirror);
	          }

	          if (this.rotation != Rotation.NONE) {
	             blockstateIn = blockstateIn.rotate(this.rotation);
	          }

	          worldIn.setBlockState(blockpos, blockstateIn, 2);
	          FluidState fluidstate = worldIn.getFluidState(blockpos);
	          if (!fluidstate.isEmpty()) {
	             worldIn.getPendingFluidTicks().scheduleTick(blockpos, fluidstate.getFluid(), 0);
	          }

	          if (BLOCKS_NEEDING_POSTPROCESSING.contains(blockstateIn.getBlock())) {
	             worldIn.getChunk(blockpos).markBlockForPostprocessing(blockpos);
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

	@Override
	protected void readAdditional(CompoundNBT tagCompound) {
		tagCompound.putInt("si", this.spawnListIndex);
		tagCompound.putString("deco", StructureTFDecorator.getDecoString(this.deco));
		tagCompound.putInt("rot", this.rotation.ordinal());
	}

	/**
	 * Does this component fall under block protection when progression is turned on, normally true
	 */
	public boolean isComponentProtected() {
		return true;
	}
}
