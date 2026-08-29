package twilightforest.client.renderer.block.jar;

import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Unit;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;
import tamaized.beanification.Component;
import twilightforest.TFRegistries;
import twilightforest.block.entity.JarBlockEntity;

import java.util.*;

@Component
public final class JarLidResolver {
	private static final RandomSource RANDOM = RandomSource.create(42L);
	private static final Map<Item, Optional<TexturedJarLidPart>> TEXTURE_CACHE = new HashMap<>();

	public @Nullable TexturedJarLidPart resolve(BlockModelResolver blockModelResolver, JarBlockEntity jar, Level level) {
		if (!hasLid(level, jar)) {
			return null;
		}
		return resolve(blockModelResolver, jar.lid);
	}

	public @Nullable TexturedJarLidPart resolve(BlockModelResolver blockModelResolver, Item lid) {
		if (!(lid instanceof BlockItem blockItem)) {
			return null;
		}

		Optional<TexturedJarLidPart> cached = TEXTURE_CACHE.get(lid);
		if (cached != null) {
			return cached.orElse(null);
		}

		TexturedJarLidPart resolved = createLidPart(blockModelResolver, blockItem);
		TEXTURE_CACHE.put(lid, Optional.ofNullable(resolved));

		return resolved;
	}

	private @Nullable TexturedJarLidPart createLidPart(BlockModelResolver blockModelResolver, BlockItem blockItem) {
		BlockStateModelPart base = blockModelResolver.modelManager.getStandaloneModel(JarRenderer.MODEL_KEY);

		if (base == null) {
			return null;
		}

		BlockStateModel sourceModel = blockModelResolver.modelManager.getBlockStateModelSet().get(blockItem.getBlock().defaultBlockState());
		List<BlockStateModelPart> parts = new ArrayList<>();
		sourceModel.collectParts(RANDOM, parts);

		TextureAtlasSprite endSprite = findSprite(parts, Direction.UP);
		TextureAtlasSprite sideSprite = findSprite(parts, Direction.NORTH);

		if (endSprite == null || sideSprite == null) {
			return null;
		}

		return new TexturedJarLidPart(base, endSprite, sideSprite);
	}

	private boolean hasLid(Level level, JarBlockEntity jar) {
		var itemKey = jar.lid.builtInRegistryHolder().key();
		var lidKey = ResourceKey.create(TFRegistries.Keys.JAR_LIDS, itemKey.identifier());
		return level.registryAccess()
			.lookupOrThrow(TFRegistries.Keys.JAR_LIDS)
			.get(lidKey)
			.isPresent();
	}

	private static @Nullable TextureAtlasSprite findSprite(List<BlockStateModelPart> parts, Direction direction) {
		for (BlockStateModelPart part : parts) {
			for (var quad : part.getQuads(direction)) {
				return quad.materialInfo().sprite();
			}
		}
		return null;
	}

	private static void clearCache() {
		TEXTURE_CACHE.clear();
	}

	public static final class TextureCacheInvalidationReloadListener extends SimplePreparableReloadListener<Unit> {

		@Override
		protected Unit prepare(ResourceManager resourceManager, ProfilerFiller profiler) {
			return Unit.INSTANCE;
		}

		@Override
		protected void apply(Unit object, ResourceManager resourceManager, ProfilerFiller profiler) {
			clearCache();
		}
	}
}