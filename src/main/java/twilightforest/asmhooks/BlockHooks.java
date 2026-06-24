package twilightforest.asmhooks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.TriState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import tamaized.beanification.Autowired;
import twilightforest.block.CloudBlock;
import twilightforest.block.SnowLoggable;
import twilightforest.block.WroughtIronFenceBlock;
import twilightforest.client.FoliageColorHandler;
import twilightforest.config.TFConfig;
import twilightforest.init.TFBlocks;
import twilightforest.init.custom.TravellersModifiersManager;

@SuppressWarnings({"JavadocReference", "unused"})
public class BlockHooks {

	@Autowired
	private static FoliageColorHandler foliageColorHandler;

	/**
	 * {@link twilightforest.asm.transformers.cloud.IsRainingAtTransformer}<p/>
	 *
	 * Injection Point:<br/>
	 * {@link net.minecraft.world.level.Level#isRainingAt(BlockPos)}
	 */
	public static boolean isRainingAt(boolean isRaining, Level level, BlockPos pos) {
		if (!isRaining && TFConfig.commonCloudBlockPrecipitationDistance > 0) {
			if (!level.hasChunkAt(pos)) return false; //do NOT try to load new chunks when checking for rain. This can cause deadlocks if mobs ry to spawn in unloaded areas and check for rain
			LevelChunk chunk = level.getChunkAt(pos);
			for (int y = pos.getY(); y < pos.getY() + TFConfig.commonCloudBlockPrecipitationDistance; y++) {
				BlockPos newPos = pos.atY(y);
				BlockState state = chunk.getBlockState(newPos);
				if (state.getBlock() instanceof CloudBlock cloudBlock && cloudBlock.getCurrentPrecipitation(newPos, level, level.getRainLevel(1.0F)).getLeft() == Biome.Precipitation.RAIN) {
					return true;
				}
				if (Heightmap.Types.MOTION_BLOCKING.isOpaque().test(state)) {
					return false;
				}
			}
		}
		return isRaining;
	}

	/**
	 * {@link twilightforest.asm.transformers.snow.KeepGrassSnowyForSnowloggableBlocksTransformer}
	 *
	 * Injection Point:<br/>
	 * {@link net.minecraft.world.level.block.SnowyDirtBlock.isSnowySetting(BlockState)}<br/>
	 * Targets: IRETURN
	 */
	public static boolean keepSnowyStateForSnowloggableBlocks(boolean o, BlockState state) {
		return o || (state.getBlock() instanceof SnowLoggable && state.getValue(SnowLoggable.SNOW_LAYERS) > 0);
	}

	/**
	 * {@link twilightforest.asm.transformers.lead.LeashFenceKnotSurvivesTransformer}<p/>
	 *
	 * Injection Point:<br/>
	 * {@link net.minecraft.world.entity.decoration.LeashFenceKnotEntity#survives()}
	 */
	public static boolean leashFenceKnotSurvives(boolean o, LeashFenceKnotEntity entity) {
		if (o)
			return true; // Short-circuit to avoid an unnecessary #getBlockState call
		BlockState fenceState = entity.level().getBlockState(entity.getPos());
		return fenceState.is(TFBlocks.WROUGHT_IRON_FENCE) && fenceState.getValue(WroughtIronFenceBlock.POST) != WroughtIronFenceBlock.PostState.NONE;
	}

	/**
	 * {@link twilightforest.asm.transformers.shroom.ModifySoilDecisionForMushroomBlockSurvivabilityTransformer}<p/>
	 *
	 * Injection Point:<br/>
	 * {@link net.minecraft.world.level.block.MushroomBlock#canSurvive(BlockState, LevelReader, BlockPos)}<br/>
	 * Targets: {@link BlockState#canSustainPlant(BlockGetter, BlockPos, Direction, BlockState)}
	 */
	public static TriState modifySoilDecisionForMushroomBlockSurvivability(TriState o, LevelReader level, BlockPos pos) {
		if (!o.isDefault())
			return o; // Short-circuit - We should not override non-default soil behaviour otherwise this would allow Mushrooms to survive on ALL blocks
		for (int x = -1; x <= 1; x++) {
			for (int z = -1; z <= 1; z++) {
				if (x == 0 && z == 0)
					continue;
				if (level.getBlockState(pos.offset(x, -1, z)).is(TFBlocks.TWILIGHT_PORTAL))
					return TriState.TRUE;
			}
		}
		return o;
	}

	/**
	 * {@link twilightforest.asm.transformers.foliage.FoliageColorResolverTransformer}<p/>
	 *
	 * Injection Point:<br/>
	 * {@link net.minecraft.client.renderer.BiomeColors#FOLIAGE_COLOR_RESOLVER}
	 */
	public static int resolveFoliageColor(int o, Biome biome, double x, double z) {
		return foliageColorHandler.get(o, biome, x, z);
	}

	/**
	 * {@link twilightforest.asm.transformers.block.UnrestrainedFrictionTransformer}<p/>
	 *
	 * Injection Point:<br/>
	 * {@link net.neoforged.neoforge.common.extensions.IBlockExtension#getFriction(BlockState, LevelReader, BlockPos, Entity)}
	 * Targets: FRETURN
	 */
	public static float resetBlockFrictionWithUnrestrained(float o, Entity entity) {
		return TravellersModifiersManager.isModifierActive(entity, TravellersModifiersManager.UNRESTRAINED_MODIFIER) ? 0.6F : o;
	}

	/**
	 * {@link twilightforest.asm.transformers.block.SlimeBlockMomentumTransformer}<p/>
	 *
	 * Injection Point:<br/>
	 * {@link net.minecraft.world.level.block.SlimeBlock#stepOn(Level, BlockPos, BlockState, Entity)}
	 * Targets: {@link Entity#isSteppingCarefully()}
	 */
	public static boolean resetSlimeMomentumWithUnrestrained(boolean o, Entity entity) {
		if (TravellersModifiersManager.isModifierActive(entity, TravellersModifiersManager.UNRESTRAINED_MODIFIER)) {
			return true; //dont return false here as the original check is looking that an entity is NOT stepping carefully
		}
		return o;
	}

	/**
	 * {@link twilightforest.asm.transformers.block.SlimeBlockBounceUpTransformer}<p/>
	 * <p>
	 * Injection Point:<br/>
	 * {@link net.minecraft.world.level.block.SlimeBlock#bounceUp(Entity)}
	 */
	public static void stopBouncing(Entity entity) {
		if (TravellersModifiersManager.isModifierActive(entity, TravellersModifiersManager.UNRESTRAINED_MODIFIER) && entity.getDeltaMovement().y() > -0.08)
			entity.setDeltaMovement(new Vec3(entity.getDeltaMovement().x, Math.max(0, entity.getDeltaMovement().y), entity.getDeltaMovement().z));
	}
}
