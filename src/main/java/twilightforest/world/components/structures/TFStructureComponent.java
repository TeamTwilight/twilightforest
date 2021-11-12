package twilightforest.world.components.structures;

import java.util.Set;

import com.google.common.collect.ImmutableSet;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.level.levelgen.feature.NoiseEffect;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.item.DyeColor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import twilightforest.world.registration.TFFeature;
import twilightforest.block.TFBlocks;
import twilightforest.util.ColorUtil;

@Deprecated
// We keep rehashing Vanillacopies and they'll keep breaking between ports, we should be adding TwilightFeature to the
//  StructurePiece classes we actually use. This class will take quite a while to dismantle
public abstract class TFStructureComponent extends StructurePiece implements TwilightFeature {

	public TFStructureDecorator deco = null;
	public int spawnListIndex = 0;
	@Deprecated // Serves no purpose
	private TFFeature feature = TFFeature.NOTHING;
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
			.add(Blocks.SPRUCE_STAIRS)
			.add(Blocks.BIRCH_STAIRS)
			.add(Blocks.COBBLESTONE_WALL)
			.add(Blocks.RED_MUSHROOM_BLOCK)
			.add(Blocks.BROWN_MUSHROOM_BLOCK)
			.add(Blocks.REDSTONE_WIRE)
			.add(Blocks.TRIPWIRE)
			.add(Blocks.TRIPWIRE_HOOK)
			.add(Blocks.CHEST)
			.add(Blocks.TRAPPED_CHEST)
			.add(Blocks.STONE_BRICK_STAIRS)
			.add(Blocks.LAVA)
			.add(Blocks.WATER)
			.add(TFBlocks.CASTLE_BRICK_STAIRS.get())
			.add(TFBlocks.BLUE_FORCE_FIELD.get())
			.add(TFBlocks.GREEN_FORCE_FIELD.get())
			.add(TFBlocks.PINK_FORCE_FIELD.get())
			.add(TFBlocks.VIOLET_FORCE_FIELD.get())
			.add(TFBlocks.ORANGE_FORCE_FIELD.get())
			.add(TFBlocks.BROWN_THORNS.get())
			.add(TFBlocks.GREEN_THORNS.get())
			.build();


	public TFStructureComponent(StructurePieceType piece, CompoundTag nbt) {
		super(piece, nbt);
		this.spawnListIndex = nbt.getInt("si");
		this.deco = TFStructureDecorator.getDecoFor(nbt.getString("deco"));
		this.rotation = Rotation.NONE;
		this.rotation = Rotation.values()[nbt.getInt("rot") % Rotation.values().length];
	}

	public TFStructureComponent(StructurePieceType type, int i, BoundingBox boundingBox) {
		super(type, i, boundingBox);
		this.rotation = Rotation.NONE;
	}

	@Deprecated // FIXME Boundingbox
	public TFStructureComponent(StructurePieceType type, TFFeature feature, int i, int x, int y, int z) {
		this(type, i, new BoundingBox(x, y, z, x, y, z));
		this.feature = feature;
	}

	@Deprecated
	@Override
	public TFFeature getFeatureType() {
		return feature;
	}

	@Override // Don't expect this to stay around
	public void setFeature(TFFeature type) {
		this.feature = type;
	}

	protected static boolean shouldDebug() {
		return false;
	}

	@SuppressWarnings({"SameParameterValue", "unused"})
	protected void setDebugCorners(Level world) {
		if (rotation == null) rotation = Rotation.NONE;

		if (shouldDebug() ) { // && rotation!= Rotation.NONE) {
			int i = rotation.ordinal() * 4;
			DyeColor[] colors = DyeColor.values();
			world.setBlockAndUpdate(new BlockPos(this.getBoundingBox().minX(), this.getBoundingBox().maxY() + i    , this.getBoundingBox().minZ()), ColorUtil.WOOL.getColor(colors[i]));
			world.setBlockAndUpdate(new BlockPos(this.getBoundingBox().maxX(), this.getBoundingBox().maxY() + i + 1, this.getBoundingBox().minZ()), ColorUtil.WOOL.getColor(colors[1 + i]));
			world.setBlockAndUpdate(new BlockPos(this.getBoundingBox().minX(), this.getBoundingBox().maxY() + i + 2, this.getBoundingBox().maxZ()), ColorUtil.WOOL.getColor(colors[2 + i]));
			world.setBlockAndUpdate(new BlockPos(this.getBoundingBox().maxX(), this.getBoundingBox().maxY() + i + 3, this.getBoundingBox().maxZ()), ColorUtil.WOOL.getColor(colors[3 + i]));
		}
	}

	@SuppressWarnings({"SameParameterValue", "unused"})
	protected void setDebugEntity(WorldGenLevel world, int x, int y, int z, BoundingBox sbb, String s) {
		setInvisibleTextEntity(world, x, y, z, sbb, s, shouldDebug(), 0f);
	}

	@SuppressWarnings({"SameParameterValue", "unused"})
	protected void setInvisibleTextEntity(WorldGenLevel world, int x, int y, int z, BoundingBox sbb, String s, boolean forcePlace, float additionalYOffset) {
		if (forcePlace) {
			final BlockPos pos = new BlockPos(this.getWorldX(x, z), this.getWorldY(y), this.getWorldZ(x, z));

			if (sbb.isInside(pos)) {
				final ArmorStand armorStand = new ArmorStand(EntityType.ARMOR_STAND, world.getLevel());
				armorStand.setCustomName(new TextComponent(s));
				armorStand.moveTo(pos.getX() + 0.5, pos.getY() + additionalYOffset, pos.getZ() + 0.5, 0, 0);
				armorStand.setInvulnerable(true);
				armorStand.setInvisible(true);
				armorStand.setCustomNameVisible(true);
				armorStand.setSilent(true);
				armorStand.setNoGravity(true);
				// set marker flag
				armorStand.getEntityData().set(ArmorStand.DATA_CLIENT_FLAGS, (byte) (armorStand.getEntityData().get(ArmorStand.DATA_CLIENT_FLAGS) | 16));
				world.addFreshEntity(armorStand);
			}
		}
	}

	@Override
	protected void placeBlock(WorldGenLevel worldIn, BlockState blockstateIn, int x, int y, int z, BoundingBox boundingboxIn) {
	      BlockPos blockpos = new BlockPos(this.getWorldX(x, z), this.getWorldY(y), this.getWorldZ(x, z));

	      if (boundingboxIn.isInside(blockpos)) {
	          if (this.mirror != Mirror.NONE) {
	             blockstateIn = blockstateIn.mirror(this.mirror);
	          }

	          if (this.rotation != Rotation.NONE) {
	             blockstateIn = blockstateIn.rotate(this.rotation);
	          }

	          worldIn.setBlock(blockpos, blockstateIn, 2);
	          FluidState fluidstate = worldIn.getFluidState(blockpos);
	          if (!fluidstate.isEmpty()) {
	             worldIn.getLiquidTicks().scheduleTick(blockpos, fluidstate.getType(), 0);
	          }

	          if (BLOCKS_NEEDING_POSTPROCESSING.contains(blockstateIn.getBlock())) {
	             worldIn.getChunk(blockpos).markPosForPostprocessing(blockpos);
	          }

	       }
	   }

	@SuppressWarnings({"SameParameterValue", "unused"})
	protected void setDebugEntity(Level world, BlockPos blockpos, String s) {
		if (shouldDebug()) {
			final Sheep sheep = new Sheep(EntityType.SHEEP, world);
			sheep.setCustomName(new TextComponent(s));
			sheep.setNoAi(true);
			sheep.moveTo(blockpos.getX() + 0.5, blockpos.getY() + 10, blockpos.getZ() + 0.5, 0, 0);
			sheep.setInvulnerable(true);
			sheep.setInvisible(true);
			sheep.setCustomNameVisible(true);
			sheep.setSilent(true);
			sheep.setNoGravity(true);
			world.addFreshEntity(sheep);
		}
	}

	@Override
	protected void addAdditionalSaveData(ServerLevel level, CompoundTag tagCompound) {
		tagCompound.putInt("si", this.spawnListIndex);
		tagCompound.putString("deco", TFStructureDecorator.getDecoString(this.deco));
		tagCompound.putInt("rot", this.rotation.ordinal());
	}

	/**
	 * Does this component fall under block protection when progression is turned on, normally true
	 */
	public boolean isComponentProtected() {
		return true;
	}

	@Override
	public NoiseEffect getNoiseEffect() {
		return NoiseEffect.NONE;
	}
}
