package twilightforest.structures;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.template.TemplateManager;
import twilightforest.TFFeature;
import twilightforest.entity.TFEntities;
import twilightforest.loot.TFTreasure;
import twilightforest.world.feature.TFBiomeFeatures;
import twilightforest.world.feature.TFGenCaveStalactite;
import twilightforest.world.feature.config.CaveStalactiteConfig;

import java.util.Random;

public class ComponentTFHollowHill extends StructureTFComponentOld {

	int hillSize;
	int radius;

	public ComponentTFHollowHill(TemplateManager manager, CompoundNBT nbt) {
		this(TFFeature.TFHill, nbt);
	}

	public ComponentTFHollowHill(IStructurePieceType piece, CompoundNBT nbt) {
		super(piece, nbt);
		hillSize = nbt.getInt("hillSize");
		this.radius = ((hillSize * 2 + 1) * 8) - 6;
	}

	//TODO: Parameter "rand" is unused. Remove?
	public ComponentTFHollowHill(IStructurePieceType piece, TFFeature feature, Random rand, int i, int size, int x, int y, int z) {
		super(piece, feature, i);

		this.setCoordBaseMode(Direction.SOUTH);

		// get the size of this hill?
		this.hillSize = size;
		radius = ((hillSize * 2 + 1) * 8) - 6;

		// can we determine the size here?
		this.boundingBox = StructureTFComponentOld.getComponentToAddBoundingBox(x + radius / 4, y + 3 + hillSize, z + radius / 4, -radius, -(3 + hillSize), -radius, radius * 2, radius / 2, radius * 2, Direction.SOUTH);
	}

	@Override
	protected void readAdditional(CompoundNBT tagCompound) {
		super.readAdditional(tagCompound);
		tagCompound.putInt("hillSize", hillSize);
	}

	/**
	 * Add in all the blocks we're adding.
	 */
	@Override
	public boolean func_230383_a_(ISeedReader world, StructureManager manager, ChunkGenerator generator, Random rand, MutableBoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
//		int area = (int)(Math.PI * radius * radius);
//		int sn = area / 16; // number of stalactites (there will actually be around twice this number)
		int[] sna = {0, 128, 256, 512};

		int sn = sna[hillSize]; // number of stalactites mga = {0, 3, 9, 18}
		int[] mga = {0, 1, 4, 9};
		int mg = mga[hillSize]; // number of monster generators mga = {0, 3, 9, 18} (reduced due to "natural" spawning)
		int[] tca = {0, 2, 6, 12};
		int tc = tca[hillSize];  // number of treasure chests tca = {0, 2, 6, 12};

		// fill in features

		// monster generators!
		for (int i = 0; i < mg; i++) {
			int[] dest = getCoordsInHill2D(rand);
			EntityType<?> mobID = getMobID(rand);

			setSpawner(world, dest[0], rand.nextInt(4), dest[1], sbb, mobID);
//			placeMobSpawner(dest[0], hy + rand.nextInt(4), dest[1]);
		}
		// treasure chests!!
		for (int i = 0; i < tc; i++) {
			int[] dest = getCoordsInHill2D(rand);
			generateTreasureChest(world, dest[0], 0, dest[1], sbb);
		}

		// ore or glowing stalactites! (smaller, less plentiful)
		for (int i = 0; i < sn; i++) {
			int[] dest = getCoordsInHill2D(rand);
			generateOreStalactite(world, generator, manager, dest[0], 1, dest[1], sbb);
		}
		// stone stalactites!
		for (int i = 0; i < sn; i++) {
			int[] dest = getCoordsInHill2D(rand);
			generateBlockStalactite(world, generator, manager, Blocks.STONE, 1.0F, true, dest[0], 1, dest[1], sbb);
		}
		// stone stalagmites!
		for (int i = 0; i < sn; i++) {
			int[] dest = getCoordsInHill2D(rand);
			generateBlockStalactite(world, generator, manager, Blocks.STONE, 0.9F, false, dest[0], 1, dest[1], sbb);
		}

		// level 3 hills get 2 mid-air wraith spawners
		if (hillSize == 3) {
//			int[] dest = getEmptysInHill(hy + 10, 20);
//			placeWraithSpawner(dest[0], hy + 10, dest[1]);
//			dest = getEmptysInHill(hy + 10, 20);
//			placeWraithSpawner(dest[0], hy + 10, dest[1]);
		}

		return true;
	}

	/**
	 * Make an RNG and attempt to use it to place a treasure chest
	 */
	protected void generateTreasureChest(ISeedReader world, int x, int y, int z, MutableBoundingBox sbb) {
		// generate an RNG for this chest
		//TODO: MOAR RANDOM!
		Random chestRNG = new Random(world.getSeed() + x * z);

		// try placing it
		placeTreasureAtCurrentPosition(world, x, y, z, this.hillSize == 3 ? TFTreasure.hill3 : (this.hillSize == 2 ? TFTreasure.hill2 : TFTreasure.hill1), sbb);

		// make something for it to stand on, if necessary
		setBlockState(world, Blocks.COBBLESTONE.getDefaultState(), x, y - 1, z, sbb);
	}

	/**
	 * Generate a random ore stalactite
	 */
	protected void generateOreStalactite(ISeedReader world, ChunkGenerator generator, StructureManager manager, int x, int y, int z, MutableBoundingBox sbb) {
		// are the coordinates in our bounding box?
		int dx = getXWithOffset(x, z);
		int dy = getYWithOffset(y);
		int dz = getZWithOffset(x, z);
		BlockPos pos = new BlockPos(dx, dy, dz);
		if (sbb.isVecInside(pos) && world.getBlockState(pos).getBlock() != Blocks.SPAWNER) {
			// generate an RNG for this stalactite
			//TODO: MOAR RANDOM!
			Random stalRNG = new Random(world.getSeed() + dx * dz);

			// make the actual stalactite
			CaveStalactiteConfig stalag = TFGenCaveStalactite.makeRandomOreStalactite(stalRNG, hillSize);
			TFBiomeFeatures.CAVE_STALACTITE.get().withConfiguration(stalag).generate(world, generator, stalRNG, pos);
		}
	}

	/**
	 * Make a random stone stalactite
	 */
	protected void generateBlockStalactite(ISeedReader world, ChunkGenerator generator, StructureManager manager, Block blockToGenerate, float length, boolean up, int x, int y, int z, MutableBoundingBox sbb) {
		// are the coordinates in our bounding box?
		int dx = getXWithOffset(x, z);
		int dy = getYWithOffset(y);
		int dz = getZWithOffset(x, z);
		BlockPos pos = new BlockPos(dx, dy, dz);
		if (sbb.isVecInside(pos) && world.getBlockState(pos).getBlock() != Blocks.SPAWNER) {
			// generate an RNG for this stalactite
			//TODO: MOAR RANDOM!
			Random stalRNG = new Random(world.getSeed() + dx * dz);

			if (hillSize == 1) {
				length *= 1.9F;
			}

			// make the actual stalactite
			TFBiomeFeatures.CAVE_STALACTITE.get().withConfiguration(new CaveStalactiteConfig(blockToGenerate.getDefaultState(), length, -1, -1, up)).generate(world, generator, stalRNG, pos);
		}
	}

	/**
	 * @param cx
	 * @param cz
	 * @return true if the coordinates would be inside the hill on the "floor" of the hill
	 */
	boolean isInHill(int cx, int cz) {
		int dx = radius - cx;
		int dz = radius - cz;
		int dist = (int) Math.sqrt(dx * dx + dz * dz);

		return dist < radius;
	}

	/**
	 * @return true if the coordinates are inside the hill in 3D
	 * TODO: Unused. Remove?
	 */
	boolean isInHill(int mapX, int mapY, int mapZ) {
		int dx = boundingBox.minX + radius - mapX;
		int dy = (boundingBox.minY - mapY) * 2; // hill is half as high as it is wide, thus we just double y distance from center.  I *think* that math works!
		int dz = boundingBox.minZ + radius - mapZ;
		int dist = dx * dx + dy * dy + dz * dz;
		return dist < radius * radius;
	}

	int[] getCoordsInHill2D(Random rand) {
		return getCoordsInHill2D(rand, radius);
	}

	/**
	 * @return a two element array containing some coordinates in the hill
	 */
	int[] getCoordsInHill2D(Random rand, int rad) {
		int rx, rz;
		do {
			rx = rand.nextInt(2 * rad);
			rz = rand.nextInt(2 * rad);
		} while (!isInHill(rx, rz));

		int[] coords = {rx, rz};
		return coords;
	}

	/**
	 * Gets the id of a mob appropriate to the current hill size.
	 */
	protected EntityType<?> getMobID(Random rand) {
		return getMobID(rand, this.hillSize);
	}

	/**
	 * Gets the id of a mob appropriate to the specified hill size.
	 *
	 * @param level
	 * @return
	 */
	protected EntityType<?> getMobID(Random rand, int level) {
		if (level == 1) {
			return getLevel1Mob(rand);
		}
		if (level == 2) {
			return getLevel2Mob(rand);
		}
		if (level == 3) {
			return getLevel3Mob(rand);
		}

		return EntityType.SPIDER;
	}

	/**
	 * Returns a mob string appropriate for a level 1 hill
	 */
	public EntityType<?> getLevel1Mob(Random rand) {
		switch (rand.nextInt(10)) {
			case 0:
			case 1:
			case 2:
				return TFEntities.swarm_spider;
			case 3:
			case 4:
			case 5:
				return EntityType.SPIDER;
			case 6:
			case 7:
				return EntityType.ZOMBIE;
			case 8:
				return EntityType.SILVERFISH;
			case 9:
				return TFEntities.redcap;
			default:
				return TFEntities.swarm_spider;
		}
	}

	/**
	 * Returns a mob string appropriate for a level 2 hill
	 */
	public EntityType<?> getLevel2Mob(Random rand) {
		switch (rand.nextInt(10)) {
			case 0:
			case 1:
			case 2:
				return TFEntities.redcap;
			case 3:
			case 4:
			case 5:
				return EntityType.ZOMBIE;
			case 6:
			case 7:
				return EntityType.SKELETON;
			case 8:
				return TFEntities.swarm_spider;
			case 9:
				return EntityType.CAVE_SPIDER;
			default:
				return TFEntities.redcap;
		}
	}

	/**
	 * Returns a mob string appropriate for a level 3 hill.  The level 3 also has 2 mid-air wraith spawners.
	 */
	public EntityType<?> getLevel3Mob(Random rand) {
		switch (rand.nextInt(11)) {
			case 0:
				return TFEntities.slime_beetle;
			case 1:
				return TFEntities.fire_beetle;
			case 2:
				return TFEntities.pinch_beetle;
			case 3:
			case 4:
			case 5:
				return EntityType.SKELETON;
			case 6:
			case 7:
			case 8:
				return EntityType.CAVE_SPIDER;
			case 9:
				return EntityType.CREEPER;
			case 10:
			default:
				return TFEntities.wraith;
		}
	}
}
