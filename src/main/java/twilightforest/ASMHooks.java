package twilightforest;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeResolver;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.neoforged.neoforge.common.util.TriState;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import twilightforest.beans.Autowired;
import twilightforest.util.ArmorUtil;
import twilightforest.util.multiparts.MultipartEntityUtil;
import twilightforest.block.CloudBlock;
import twilightforest.block.WroughtIronFenceBlock;
import twilightforest.client.FoliageColorHandler;
import twilightforest.config.TFConfig;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFDataComponents;
import twilightforest.init.custom.ChunkBlanketProcessors;
import twilightforest.util.WorldUtil;
import twilightforest.world.components.biomesources.TFBiomeProvider;
import twilightforest.world.components.structures.CustomDensitySource;
import twilightforest.world.components.structures.util.CustomStructureData;

import java.util.Iterator;

// TODO: Think about reorganizing each group into their own class or subclass of ASMHooks
@SuppressWarnings({"JavadocReference", "unused", "RedundantSuppression", "deprecation"})
public class ASMHooks {

	@Autowired
	private static ArmorUtil armorUtil;

	@Autowired
	private static MultipartEntityUtil multipartEntityUtil;

	@Autowired
	private static FoliageColorHandler foliageColorHandler;

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// armor
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * {@link twilightforest.asm.transformers.armor.ArmorVisibilityRenderingTransformer}<p/>
	 *
	 * Injection Point:<br/>
	 * {@link net.minecraft.world.entity.LivingEntity#getVisibilityPercent(Entity)}
	 */
	public static float modifyArmorVisibility(float o, LivingEntity entity) {
		return o - armorUtil.getShroudedArmorPercentage(entity);
	}

	/**
	 * {@link twilightforest.asm.transformers.armor.CancelArmorRenderingTransformer}<p/>
	 *
	 * Injection Point:<br/>
	 * {@link net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer#renderArmorPiece(PoseStack, MultiBufferSource, LivingEntity, EquipmentSlot, int, HumanoidModel, float, float, float, float, float, float)}
	 */
	public static boolean cancelArmorRendering(boolean o, ItemStack stack) {
		if (o && stack.get(TFDataComponents.EMPERORS_CLOTH) != null) {
			return false;
		}
		return o;
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// beardifier
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * {@link twilightforest.asm.transformers.beardifier.InitializeCustomBeardifierFieldsDuringForStructuresInChunkTransformer}<p/>
	 *
	 * Injection point:<br/>
	 * {@link net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator#createNoiseChunk(ChunkAccess, StructureManager, Blender, RandomState)}
	 */
	public static ObjectListIterator<DensityFunction> gatherCustomTerrain(StructureManager structureManager, ChunkPos chunkPos) {
		ObjectArrayList<DensityFunction> customStructureTerraforms = new ObjectArrayList<>(10);

		for (StructureStart structureStart : structureManager.startsForStructure(chunkPos, s -> s instanceof CustomDensitySource))
			if (structureStart.getStructure() instanceof CustomDensitySource customDensitySource)
				customStructureTerraforms.add(customDensitySource.getStructureTerraformer(chunkPos, structureStart));

		return customStructureTerraforms.iterator();
	}

	/**
	 * {@link twilightforest.asm.transformers.beardifier.BeardifierComputeTransformer}<p/>
	 *
	 * Injection point:<br/>
	 * {@link net.minecraft.world.level.levelgen.Beardifier#compute(DensityFunction.FunctionContext)}
	 */
	public static double getCustomDensity(double o, DensityFunction.FunctionContext context, @Nullable ObjectListIterator<DensityFunction> customDensities) {
		if (customDensities == null)
			return o;

		double newDensity = 0;

		while (customDensities.hasNext()) {
			newDensity += customDensities.next().compute(context);
		}
		customDensities.back(Integer.MAX_VALUE);

		return o + newDensity;
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// book
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * {@link twilightforest.asm.transformers.book.ModifyWrittenBookNameTransformer}<p/>
	 *
	 * Injection Point:<br/>
	 * {@link net.minecraft.world.item.WrittenBookItem#getName(net.minecraft.world.item.ItemStack)}
	 */
	public static Component modifyWrittenBookName(Component component, ItemStack stack) {
		if (stack.has(TFDataComponents.TRANSLATABLE_BOOK)) {
			return Component.translatable(component.getString());
		} else return component;
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// chunk
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * {@link twilightforest.asm.transformers.chunk.ChunkStatusTaskTransformer}<p/>
	 *
	 * Injection Point:<br/>
	 * {@link net.minecraft.world.level.chunk.status.ChunkStatusTasks#generateSurface}
	 */
	public static void chunkBlanketing(ChunkAccess chunkAccess, WorldGenRegion worldGenRegion) {
		ChunkBlanketProcessors.chunkBlanketing(chunkAccess, worldGenRegion);
	}

	/**
	 * {@link twilightforest.asm.transformers.chunk.NoiseBasedChunkGeneratorTransformer}
	 *
	 * Injection Point:<br/>
	 * {@link net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator#doCreateBiomes}
	 */
	public static BiomeResolver tryRecreateBiomeResolver(BiomeResolver resolver) {
		if (resolver instanceof TFBiomeProvider tfBiomeProvider) {
			return tfBiomeProvider.recreate();
		} else {
			return resolver;
		}
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// cloud
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * {@link twilightforest.asm.transformers.cloud.IsRainingAtTransformer}<p/>
	 *
	 * Injection Point:<br/>
	 * {@link net.minecraft.world.level.Level#isRainingAt(BlockPos)}
	 */
	public static boolean isRainingAt(boolean isRaining, Level level, BlockPos pos) {
		if (!isRaining && TFConfig.commonCloudBlockPrecipitationDistance > 0) {
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

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// conquered
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * {@link twilightforest.asm.transformers.conquered.StructureStartLoadStaticTransformer}<p/>
	 *
	 * Injection Point:<br/>
	 * {@link net.minecraft.world.level.levelgen.structure.StructureStart#loadStaticStart(StructurePieceSerializationContext, CompoundTag, long)}<br/>
	 * Targets: {@link net.minecraft.world.level.levelgen.structure.StructureStart#StructureStart(Structure, ChunkPos, int, PiecesContainer)}
	 */
	public static StructureStart loadStaticStart(StructureStart start, PiecesContainer piecesContainer, CompoundTag nbt) {
		if (start.getStructure() instanceof CustomStructureData s)
			return s.forDeserialization(start.getStructure(), start.getChunkPos(), start.getReferences(), piecesContainer, nbt);
		return start;
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// foliage
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * {@link twilightforest.asm.transformers.foliage.FoliageColorResolverTransformer}<p/>
	 *
	 * Injection Point:<br/>
	 * {@link net.minecraft.client.renderer.BiomeColors#FOLIAGE_COLOR_RESOLVER}
	 */
	public static int resolveFoliageColor(int o, Biome biome, double x, double z) {
		return foliageColorHandler.get(o, biome, x, z);
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// lead
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// map
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * {@link twilightforest.asm.transformers.map.ResolveNearestNonRandomSpreadMapStructureTransformer}<p/>
	 *
	 * Injection Point:<br/>
	 * {@link net.minecraft.world.level.chunk.ChunkGenerator#findNearestMapStructure(ServerLevel, HolderSet, BlockPos, int, boolean)}
	 */
	@Nullable
	public static Pair<BlockPos, Holder<Structure>> resolveNearestNonRandomSpreadMapStructure(@Nullable Pair<BlockPos, Holder<Structure>> o, ServerLevel level, HolderSet<Structure> targetStructures, BlockPos pos, int searchRadius, boolean skipKnownStructures) {
		return WorldUtil.findNearestMapLandmark(level, targetStructures, pos, searchRadius, skipKnownStructures).orElse(o);
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// multipart
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * {@link twilightforest.asm.transformers.multipart.ResolveEntitiesForRendereringTransformer}<p/>
	 *
	 * Injection Point:<br/>
	 * {@link net.minecraft.client.renderer.LevelRenderer#renderLevel(DeltaTracker, boolean, Camera, GameRenderer, LightTexture, Matrix4f, Matrix4f)}<br/>
	 * [Targets: {@link net.minecraft.client.multiplayer.ClientLevel#entitiesForRendering}]
	 */
	public static Iterator<Entity> resolveEntitiesForRendering(Iterator<Entity> iter) {
		return multipartEntityUtil.injectTFPartEntities(iter);
	}

	/**
	 * {@link twilightforest.asm.transformers.multipart.ResolveEntityRendererTransformer}<p/>
	 *
	 * Injection Point:<br/>
	 * {@link net.minecraft.client.renderer.entity.EntityRenderDispatcher#getRenderer(Entity)}<br/>
	 * Targets: {@link net.minecraft.client.renderer.entity.EntityRenderDispatcher#renderers}
	 */
	@Nullable
	public static EntityRenderer<?> resolveEntityRenderer(@Nullable EntityRenderer<?> renderer, Entity entity) {
		return multipartEntityUtil.tryLookupTFPartRenderer(renderer, entity);
	}

	/**
	 * {@link twilightforest.asm.transformers.multipart.SendDirtytEntityDataTransformer}<p/>
	 *
	 * Injection Point:<br/>
	 * {@link net.minecraft.server.level.ServerEntity#sendDirtyEntityData}
	 */
	public static Entity sendDirtyEntityData(Entity entity) {
		return multipartEntityUtil.sendDirtyMultipartEntityData(entity);
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// shroom
	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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
}
