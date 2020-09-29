package twilightforest.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import twilightforest.IMCHandler;
import twilightforest.TFConfig;
import twilightforest.TwilightForestMod;
import twilightforest.world.TFGenerationSettings;
import twilightforest.world.feature.config.CaveStalactiteConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TFGenCaveStalactite extends Feature<CaveStalactiteConfig> {

	private static final List<StalactiteEntry> largeHillStalactites = new ArrayList<>();
	private static final List<StalactiteEntry> mediumHillStalactites = new ArrayList<>();
	private static final List<StalactiteEntry> smallHillStalactites = new ArrayList<>();

	/**
	 * Initializes a stalactite builder.  Actually also makes stalagmites
	 */
//	public TFGenCaveStalactite(Function<Dynamic<?>, ? extends NoFeatureConfig> configIn, Block block, float size, boolean down) {
//		this(configIn, block.getDefaultState(), size, down);
//	}

//	public TFGenCaveStalactite(Function<Dynamic<?>, ? extends NoFeatureConfig> configIn, BlockState blockState, float size, boolean down) {
//		super(configIn);
//		this.blockState = blockState;
//		this.sizeFactor = size;
//		this.maxLength = -1;
//		this.minHeight = -1;
//		this.hang = down;
//	}

	/**
	 * Initializes a stalactite builder
	 */
//	public TFGenCaveStalactite(Function<Dynamic<?>, ? extends NoFeatureConfig> configIn, BlockState blockState, float size, int maxLength, int minHeight) {
//		super(configIn);
//		this.blockState = blockState;
//		this.sizeFactor = size;
//		this.maxLength = maxLength;
//		this.minHeight = minHeight;
//		this.hang = true;
//	}

	public TFGenCaveStalactite(Codec<CaveStalactiteConfig> configIn) {
		super(configIn);
	}

	/**
	 * Makes a random stalactite appropriate to the cave size
	 * <p>
	 * All include iron, coal and glowstone.
	 * <p>
	 * Gold and redstone appears in size 2 and larger caves.
	 * <p>
	 * Diamonds and lapis only appear in size 3 and larger caves.
	 */
	public static CaveStalactiteConfig makeRandomOreStalactite(Random rand, int hillSize) {
		if (hillSize >= 3 || (hillSize >= 2 && rand.nextInt(5) == 0)) {
			return WeightedRandom.getRandomItem(rand, largeHillStalactites).stalactite;
		}
		if (hillSize >= 2 || (hillSize >= 1 && rand.nextInt(5) == 0)) {
			return WeightedRandom.getRandomItem(rand, mediumHillStalactites).stalactite;
		}
		return WeightedRandom.getRandomItem(rand, smallHillStalactites).stalactite;
	}

	/**
	 * Generates a stalactite at the specified location.
	 * The coordinates should be inside a cave.
	 * This will return false if it can't find a valid ceiling and floor, or if there are other errors.
	 */
	@Override
	public boolean func_241855_a(ISeedReader world, ChunkGenerator generator, Random random, BlockPos pos, CaveStalactiteConfig config) {
		int ceiling = Integer.MAX_VALUE;
		int floor = -1;

		BlockPos.Mutable iterPos = new BlockPos.Mutable(pos.getX(), pos.getY(), pos.getZ());
		// find a ceiling
		for (int ty = pos.getY(); ty < TFGenerationSettings.CHUNKHEIGHT; ty++) {
			iterPos.setY(ty);
			Material m = world.getBlockState(iterPos).getMaterial();
			// if we're in air, continue
			if (m == Material.AIR) {
				continue;
			}
			// if we get something that's not cave material, fail!
			if (m != Material.EARTH && m != Material.ROCK) {
				return false;
			}
			// okay, we found a valid ceiling.
			ceiling = ty;
			break;
		}
		// if we didn't find a ceiling, fail.
		if (ceiling == Integer.MAX_VALUE) {
			return false;
		}

		// find a floor
		for (int ty = pos.getY(); ty > 4; ty--) {
			iterPos.setY(ty);
			Material m = world.getBlockState(iterPos).getMaterial();
			// if we're in air, continue
			if (m == Material.AIR) {
				continue;
			}
			// if we get something that's not cave material, fail!
			// actually stalactites can hang above water or lava
			if (m != Material.EARTH && m != Material.ROCK && (!config.hang && m != Material.WATER) && (!config.hang && m != Material.LAVA)) {
				return false;
			}
			// okay, we found a valid floor.
			floor = ty;
			break;
		}

		int length = (int) ((ceiling - floor) * config.sizeFactor * random.nextFloat());

		// check max length
		if (config.maxLength > -1 && length > config.maxLength) {
			length = config.maxLength;
		}

		// check minimum height
		if (config.minHeight > -1 && ceiling - floor - length < config.minHeight) {
			return false;
		}

		return makeSpike(world, random, new BlockPos(pos.getX(), config.hang ? ceiling : floor, pos.getZ()), length, config);
	}

	public boolean makeSpike(IWorld world, Random random, BlockPos pos, int maxLength, CaveStalactiteConfig config) {

		int diameter = (int) (maxLength / 4.5); // diameter of the base

		// let's see...
		for (int dx = -diameter; dx <= diameter; dx++) {
			for (int dz = -diameter; dz <= diameter; dz++) {
				// determine how long this spike will be.
				int absx = Math.abs(dx);
				int absz = Math.abs(dz);
				int dist = (int) (Math.max(absx, absz) + (Math.min(absx, absz) * 0.5));
				int spikeLength = 0;

				if (dist == 0) {
					spikeLength = maxLength;
				}

				if (dist > 0) {
					spikeLength = random.nextInt((int) (maxLength / (dist + 0.25)));
				}

				int dir = config.hang ? -1 : 1;

				// check if we're generating over anything
				if (!world.getBlockState(pos.add(dx, -dir, dz)).getMaterial().isSolid()) {
					spikeLength = 0;
				}

				for (int dy = 0; dy != (spikeLength * dir); dy += dir) {
					setBlockState(world, pos.add(dx, dy, dz), config.blockState);
				}
			}
		}

		return true;
	}

	public static class StalactiteEntry extends WeightedRandom.Item {

		final CaveStalactiteConfig stalactite;

		StalactiteEntry(CaveStalactiteConfig stalactite, int itemWeight) {
			super(itemWeight);
			this.stalactite = stalactite;
		}

		public StalactiteEntry(BlockState blockState, float size, int maxLength, int minHeight, int itemWeight) {
			this(new CaveStalactiteConfig(blockState, size, maxLength, minHeight, true), itemWeight);
		}
	}

	public static void addStalactite(int hillSize, BlockState blockState, float size, int maxLength, int minHeight, int itemWeight) {
		if (itemWeight > 0) {
			addStalactite(hillSize, new StalactiteEntry(blockState, size, maxLength, minHeight, itemWeight));
		}
	}

	private static void addStalactite(int hillSize, StalactiteEntry entry) {
		if (hillSize <= 1)
			smallHillStalactites.add(entry);
		if (hillSize <= 2)
			mediumHillStalactites.add(entry);
		largeHillStalactites.add(entry);
	}

	/*
	 * Current default weights are as follows:
	 *
	 * Large (total 195 = 13*15):
	 * 2/13 diamond
	 * 2/13 lapis
	 * 1/13 emerald
	 * 8/13 [medium pool]
	 *
	 * Medium (total 120 = 6*20):
	 * 1/6 gold
	 * 1/6 redstone
	 * 3/6 [small pool]
	 *
	 * Small (total 60 = 5*12):
	 * 2/5 iron
	 * 2/5 coal
	 * 1/5 glowstone
	 */
	private static void addDefaultStalactites() {
		addStalactite(3, Blocks.DIAMOND_ORE.getDefaultState(), 0.5F, 4, 16, 30);
		addStalactite(3, Blocks.LAPIS_ORE.getDefaultState(), 0.8F, 8, 1, 30);
		addStalactite(3, Blocks.EMERALD_ORE.getDefaultState(), 0.5F, 3, 12, 15);

		addStalactite(2, Blocks.GOLD_ORE.getDefaultState(), 0.6F, 6, 1, 20);
		addStalactite(2, Blocks.REDSTONE_ORE.getDefaultState(), 0.8F, 8, 1, 40);

		addStalactite(1, Blocks.IRON_ORE.getDefaultState(), 0.7F, 8, 1, 24);
		addStalactite(1, Blocks.COAL_ORE.getDefaultState(), 0.8F, 12, 1, 24);
		addStalactite(1, Blocks.GLOWSTONE.getDefaultState(), 0.5F, 8, 1, 12);
	}

	public static void loadStalactites() {
		smallHillStalactites.clear();
		mediumHillStalactites.clear();
		largeHillStalactites.clear();

		TFConfig.COMMON_CONFIG.DIMENSION.hollowHillStalactites.load();
		if (TFConfig.COMMON_CONFIG.DIMENSION.hollowHillStalactites.useConfigOnly.get()) {
			if (smallHillStalactites.isEmpty()) {
				TwilightForestMod.LOGGER.info("Not all hollow hills are populated with the config, adding fallback");
				addStalactite(1, Blocks.STONE.getDefaultState(), 0.7F, 8, 1, 1);
			}
			return;
		}
		addDefaultStalactites();
		IMCHandler.getStalactites().forEach(TFGenCaveStalactite::addStalactite);
	}
}
