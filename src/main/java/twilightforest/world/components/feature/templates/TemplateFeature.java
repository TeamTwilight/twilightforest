package twilightforest.world.components.feature.templates;

import com.google.common.math.StatsAccumulator;
import com.mojang.serialization.Codec;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.StructureMode;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.jetbrains.annotations.Nullable;
import twilightforest.util.features.FeatureLogic;

public abstract class TemplateFeature<T extends FeatureConfiguration> extends Feature<T> {
	public TemplateFeature(Codec<T> config) {
		super(config);
	}

	@Override // Loosely based on WorldGenFossils
	public final boolean place(FeaturePlaceContext<T> ctx) {
		WorldGenLevel world = ctx.level();
		BlockPos pos = ctx.origin();
		RandomSource random = world.getRandom();
		T config = ctx.config();

		StructureTemplateManager templateManager = world.getLevel().getServer().getStructureManager();
		StructureTemplate template = this.getTemplate(templateManager, random);

		if (template == null)
			return false;

		Rotation rotation = Rotation.getRandom(random);
		Mirror mirror = Util.getRandom(Mirror.values(), random);

		ChunkPos chunkpos = new ChunkPos(pos);
		BoundingBox structureMask = new BoundingBox(chunkpos.getMinBlockX(), world.getMinY(), chunkpos.getMinBlockZ(), chunkpos.getMaxBlockX(), world.getMaxY(), chunkpos.getMaxBlockZ());

		BlockPos posSnap = chunkpos.getWorldPosition().offset(0, pos.getY(), 0);

		Vec3i transformedSize = template.getSize(rotation);
		int dx = random.nextInt(16 - transformedSize.getX());
		int dz = random.nextInt(16 - transformedSize.getZ());
		posSnap = posSnap.offset(dx, 0, dz);

		BlockPos.MutableBlockPos startPos = new BlockPos.MutableBlockPos(posSnap.getX(), posSnap.getY(), posSnap.getZ());

		if (!offsetToAverageGroundLevel(world, startPos, transformedSize)) return false;

		startPos.move(0, this.yLevelOffset(), 0);

		BlockPos placementPos = template.getZeroPositionWithTransform(startPos, mirror, rotation);

		StructurePlaceSettings placementSettings = (new StructurePlaceSettings()).setMirror(mirror).setRotation(rotation).setBoundingBox(structureMask).setRandom(random);
		this.modifySettings(placementSettings.clearProcessors(), random, config);

		template.placeInWorld(world, placementPos, placementPos, placementSettings, random, Block.UPDATE_CLIENTS);

		for (StructureTemplate.StructureBlockInfo info : template.filterBlocks(placementPos, placementSettings, Blocks.STRUCTURE_BLOCK))
			if (info.nbt() != null && StructureMode.valueOf(info.nbt().getString("mode")) == StructureMode.DATA)
				this.processMarkers(info, world, rotation, mirror, random);

		this.postPlacement(world, random, templateManager, rotation, mirror, placementSettings, placementPos, config);

		return true;
	}

	@Nullable
	protected abstract StructureTemplate getTemplate(StructureTemplateManager templateManager, RandomSource random);

	protected void modifySettings(StructurePlaceSettings settings, RandomSource random, T config) {
	}

	protected void processMarkers(StructureTemplate.StructureBlockInfo info, WorldGenLevel world, Rotation rotation, Mirror mirror, RandomSource random) {
	}

	protected void postPlacement(WorldGenLevel world, RandomSource random, StructureTemplateManager templateManager, Rotation rotation, Mirror mirror, StructurePlaceSettings placementSettings, BlockPos placementPos, T config) {
	}

	protected int yLevelOffset() {
		return 0;
	}

	private static boolean offsetToAverageGroundLevel(WorldGenLevel world, BlockPos.MutableBlockPos startPos, Vec3i size) {
		StatsAccumulator heights = new StatsAccumulator();

		for (int dx = 0; dx < size.getX(); dx++) {
			for (int dz = 0; dz < size.getZ(); dz++) {

				int x = startPos.getX() + dx;
				int z = startPos.getZ() + dz;

				int y = world.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);

				while (y >= 0) {
					BlockState state = world.getBlockState(new BlockPos(x, y, z));
					if (FeatureLogic.isBlockNotOk(state)) return false;
					if (FeatureLogic.isBlockOk(state)) break;
					y--;
				}

				if (y < 0) return false;

				heights.add(y);
			}
		}

		if (heights.populationStandardDeviation() > 2.0) {
			return false;
		}

		int baseY = (int) (heights.mean() + 0.5);
		int maxY = (int) heights.max();

		startPos.setY(baseY);

		return isAreaClear(world, startPos.above(maxY - baseY + 1), startPos.offset(size));
	}

	private static boolean isAreaClear(LevelAccessor world, BlockPos min, BlockPos max) {
		for (BlockPos pos : BlockPos.betweenClosed(min, max)) {
			if (!world.getBlockState(pos).canBeReplaced()) {
				return false;
			}
		}
		return true;
	}

	private static boolean isDataBlock(StructureTemplate.StructureBlockInfo info) {
		return StructureMode.DATA.name().equals(info.nbt().getString("mode"));
	}
}
