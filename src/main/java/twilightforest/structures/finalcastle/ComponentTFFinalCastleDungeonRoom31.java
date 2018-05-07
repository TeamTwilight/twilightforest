package twilightforest.structures.finalcastle;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import twilightforest.TFFeature;
import twilightforest.biomes.TFBiomes;
import twilightforest.block.BlockTFCastleMagic;
import twilightforest.block.BlockTFForceField;
import twilightforest.block.TFBlocks;
import twilightforest.structures.StructureTFComponentOld;
import twilightforest.structures.lichtower.ComponentTFTowerWing;
import twilightforest.util.RotationUtil;

import java.util.List;
import java.util.Random;

public class ComponentTFFinalCastleDungeonRoom31 extends ComponentTFTowerWing {
	public int level; // this is not serialized, since it's only used during build, which should be all one step

	public ComponentTFFinalCastleDungeonRoom31() {
	}

	public ComponentTFFinalCastleDungeonRoom31(TFFeature feature, Random rand, int i, int x, int y, int z, EnumFacing direction, int level) {
		super(feature, i);
		this.setCoordBaseMode(direction);
		this.spawnListIndex = 2; // dungeon monsters
		this.size = 31;
		this.height = 7;
		this.level = level;
		this.boundingBox = StructureTFComponentOld.getComponentToAddBoundingBox(x, y, z, -15, 0, -15, this.size - 1, this.height - 1, this.size - 1, EnumFacing.SOUTH);
	}

	@Override
	public void buildComponent(StructureComponent parent, List<StructureComponent> list, Random rand) {
		if (parent != null && parent instanceof StructureTFComponentOld) {
			this.deco = ((StructureTFComponentOld) parent).deco;
		}

		int mySpread = this.getComponentType() - parent.getComponentType();
		int maxSpread = (this.level == 1) ? 2 : 3;

		// add exit if we're far enough away and don't have one
		if (mySpread == maxSpread && !isExitBuildForLevel(parent)) {
			Rotation direction = RotationUtil.getRandomRotation(rand);
			for (int i = 0; i < 8 && !isExitBuildForLevel(parent); i++) {
				direction = direction.add(RotationUtil.ROTATIONS[i & 3]);
				if (this.addDungeonExit(parent, list, rand, direction)) {
					this.setExitBuiltForLevel(parent, true);
				}
			}
		}

		// add other rooms
		if (mySpread < maxSpread) {
			Rotation direction = RotationUtil.getRandomRotation(rand);
			for (int i = 0; i < 12; i++) {
				direction = direction.add(RotationUtil.ROTATIONS[i & 3]);
				this.addDungeonRoom(parent, list, rand, direction, this.level);
			}
		}
	}

	private boolean isExitBuildForLevel(StructureComponent parent) {
		if (parent instanceof ComponentTFFinalCastleDungeonEntrance) {
			return ((ComponentTFFinalCastleDungeonEntrance) parent).hasExit;
		} else {
			return false;
		}
	}

	private void setExitBuiltForLevel(StructureComponent parent, boolean exit) {
		if (parent instanceof ComponentTFFinalCastleDungeonEntrance) {
			((ComponentTFFinalCastleDungeonEntrance) parent).hasExit = exit;
		} else {
		}
	}

	protected boolean addDungeonRoom(StructureComponent parent, List<StructureComponent> list, Random rand, Rotation rotation, int level) {
		rotation = rotation.add(this.rotation);

		BlockPos rc = this.getNewRoomCoords(rand, rotation);

		ComponentTFFinalCastleDungeonRoom31 dRoom = new ComponentTFFinalCastleDungeonRoom31(getFeatureType(), rand, this.componentType + 1, rc.getX(), rc.getY(), rc.getZ(), rotation.rotate(EnumFacing.SOUTH), level);

		StructureBoundingBox largerBB = new StructureBoundingBox(dRoom.getBoundingBox());

		int expand = 0;
		largerBB.minX -= expand;
		largerBB.minZ -= expand;
		largerBB.maxX += expand;
		largerBB.maxZ += expand;

		StructureComponent intersect = StructureTFComponentOld.findIntersectingExcluding(list, largerBB, this);
		if (intersect == null) {
			list.add(dRoom);
			dRoom.buildComponent(parent, list, rand);
			return true;
		} else {
			return false;
		}
	}

	protected boolean addDungeonExit(StructureComponent parent, List<StructureComponent> list, Random rand, Rotation rotation) {

		//TODO: check if we are sufficiently near the castle center

		rotation = rotation.add(this.rotation);
		BlockPos rc = this.getNewRoomCoords(rand, rotation);
		ComponentTFFinalCastleDungeonExit dRoom = new ComponentTFFinalCastleDungeonExit(getFeatureType(), rand, this.componentType + 1, rc.getX(), rc.getY(), rc.getZ(), rotation.rotate(EnumFacing.SOUTH), this.level);
		StructureComponent intersect = StructureTFComponentOld.findIntersectingExcluding(list, dRoom.getBoundingBox(), this);
		if (intersect == null) {
			list.add(dRoom);
			dRoom.buildComponent(this, list, rand);
			return true;
		} else {
			return false;
		}
	}

	private BlockPos getNewRoomCoords(Random rand, Rotation rotation) {
		// make the rooms connect around the corners, not the centers
		int offset = rand.nextInt(15) - 9;
		if (rand.nextBoolean()) {
			offset += this.size;
		}

		switch (rotation) {
			default:
			case NONE:
				return new BlockPos(this.boundingBox.maxX + 9, this.boundingBox.minY, this.boundingBox.minZ + offset);
			case CLOCKWISE_90:
				return new BlockPos(this.boundingBox.minX + offset, this.boundingBox.minY, this.boundingBox.maxZ + 9);
			case CLOCKWISE_180:
				return new BlockPos(this.boundingBox.minX - 9, this.boundingBox.minY, this.boundingBox.minZ + offset);
			case COUNTERCLOCKWISE_90:
				return new BlockPos(this.boundingBox.minX + offset, this.boundingBox.minY, this.boundingBox.minZ - 9);
		}
	}

	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {
		if (this.isBoundingBoxOutOfPlateau(world, sbb)) {
			return false;
		}

		Random decoRNG = new Random(world.getSeed() + (this.boundingBox.minX * 321534781) ^ (this.boundingBox.minZ * 756839));

		this.fillWithAir(world, sbb, 0, 0, 0, this.size - 1, this.height - 1, this.size - 1);
		EnumDyeColor forceFieldMeta = this.getForceFieldMeta(decoRNG);
		EnumDyeColor runeMeta = getRuneMeta(forceFieldMeta);

		final IBlockState forceField = TFBlocks.force_field.getDefaultState()
				.withProperty(BlockTFForceField.COLOR, forceFieldMeta);
		final IBlockState castleMagic = TFBlocks.castle_rune_brick.getDefaultState()
				.withProperty(BlockTFCastleMagic.COLOR, runeMeta);

		for (Rotation rotation : RotationUtil.ROTATIONS) {
			int cs = 7;

			this.fillBlocksRotated(world, sbb, cs, 0, cs + 1, cs, this.height - 1, this.size - 2 - cs, forceField, rotation);
			// verticals
			for (int z = cs; z < ((this.size - 1) - cs); z += 4) {

				this.fillBlocksRotated(world, sbb, cs, 0, z, cs, this.height - 1, z, castleMagic, rotation);
				// horizontals
				int y = ((z - cs) % 8 == 0) ? decoRNG.nextInt(3) + 0 : decoRNG.nextInt(3) + 4;
				this.fillBlocksRotated(world, sbb, cs, y, z + 1, cs, y, z + 3, castleMagic, rotation);
			}
		}

		return true;
	}

	private boolean isBoundingBoxOutOfPlateau(World world, StructureBoundingBox sbb) {
		int minX = this.boundingBox.minX - 1;
		int minZ = this.boundingBox.minZ - 1;
		int maxX = this.boundingBox.maxX + 1;
		int maxZ = this.boundingBox.maxZ + 1;

		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

		for (int x = minX; x <= maxX; x++) {
			for (int z = minZ; z <= maxZ; z++) {
				pos.setPos(x, 0, z);
				if (world.getBiome(pos) != TFBiomes.highlandsCenter && world.getBiome(pos) != TFBiomes.thornlands) {
					return true;
				}
			}
		}

		return false;
	}

	protected EnumDyeColor getRuneMeta(EnumDyeColor forceFieldMeta) {
		return BlockTFCastleMagic.VALID_COLORS.get(forceFieldMeta == BlockTFForceField.VALID_COLORS.get(4) ? 1 : 2);
	}

	protected EnumDyeColor getForceFieldMeta(Random decoRNG) {
		return BlockTFForceField.VALID_COLORS.get(decoRNG.nextInt(2) + 3);
	}

}
