package twilightforest.init.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import twilightforest.TFRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFBiomes;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFStructures;
import twilightforest.world.components.chunkblanketing.CanopyBlanketProcessor;
import twilightforest.world.components.chunkblanketing.ChunkBlanketProcessor;
import twilightforest.world.components.chunkblanketing.ChunkBlanketType;
import twilightforest.world.components.chunkblanketing.GlacierBlanketProcessor;

import java.util.Iterator;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

public final class ChunkBlanketProcessors {
	public static final DeferredRegister<ChunkBlanketType> CHUNK_BLANKETING_TYPES = DeferredRegister.create(TFRegistries.Keys.CHUNK_BLANKET_TYPE, TwilightForestMod.ID);
	public static final Codec<ChunkBlanketType> TYPE_CODEC = Codec.lazyInitialized(TFRegistries.CHUNK_BLANKET_TYPES::byNameCodec);
	public static final Codec<ChunkBlanketProcessor> DISPATCH_CODEC = TYPE_CODEC.dispatch("type", ChunkBlanketProcessor::getType, ChunkBlanketType::getCodec);

	public static final DeferredHolder<ChunkBlanketType, ChunkBlanketType> CANOPY = registerType("canopy", CanopyBlanketProcessor.CODEC);
	public static final DeferredHolder<ChunkBlanketType, ChunkBlanketType> GLACIER = registerType("glacier", GlacierBlanketProcessor.CODEC);

	public static final ResourceKey<ChunkBlanketProcessor> DARK_FOREST_CANOPY = ResourceKey.create(TFRegistries.Keys.CHUNK_BLANKET_PROCESSORS, TwilightForestMod.prefix("dark_forest_canopy"));
	public static final ResourceKey<ChunkBlanketProcessor> SNOWY_FOREST_GLACIER = ResourceKey.create(TFRegistries.Keys.CHUNK_BLANKET_PROCESSORS, TwilightForestMod.prefix("snowy_forest_glacier"));

	public static DeferredHolder<ChunkBlanketType, ChunkBlanketType> registerType(String name, MapCodec<? extends ChunkBlanketProcessor> codec) {
		return CHUNK_BLANKETING_TYPES.register(name, () -> () -> codec);
	}

	public static void bootstrap(BootstrapContext<ChunkBlanketProcessor> context) {
		HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
		HolderGetter<Structure> structures = context.lookup(Registries.STRUCTURE);

		context.register(DARK_FOREST_CANOPY, new CanopyBlanketProcessor(HolderSet.direct(biomes.getOrThrow(TFBiomes.DARK_FOREST), biomes.getOrThrow(TFBiomes.DARK_FOREST_CENTER)), BlockStateProvider.simple(TFBlocks.HARDENED_DARK_LEAVES.value()), 14, HolderSet.direct(structures.getOrThrow(TFStructures.DARK_TOWER))));
		context.register(SNOWY_FOREST_GLACIER, new GlacierBlanketProcessor(HolderSet.direct(biomes.getOrThrow(TFBiomes.GLACIER)), BlockStateProvider.simple(Blocks.PACKED_ICE), BlockStateProvider.simple(Blocks.ICE), 32));
	}

	public static void chunkBlanketing(ChunkAccess chunkAccess, WorldGenRegion worldGenRegion) {
		ChunkPos chunkPos = chunkAccess.getPos();

		Set<Holder<Biome>> biomesInChunk = new ObjectArraySet<>();

		for (LevelChunkSection levelchunksection : worldGenRegion.getChunk(chunkPos.x, chunkPos.z).getSections()) {
			levelchunksection.getBiomes().getAll(biomesInChunk::add);
		}

		Iterator<ChunkBlanketProcessor> modifierIterator = worldGenRegion.registryAccess()
			.lookup(TFRegistries.Keys.CHUNK_BLANKET_PROCESSORS)
			.map(Registry::stream)
			.orElseGet(Stream::empty)
			.filter(modifier -> modifier.biomesForApplication().stream().anyMatch(biomesInChunk::contains))
			.iterator();

		Function<BlockPos, Holder<Biome>> biomeGetter = worldGenRegion::getBiome;

		while (modifierIterator.hasNext()) {
			modifierIterator.next().processChunk(worldGenRegion.getRandom().fork(), biomeGetter, chunkAccess);
		}
	}
}
