package twilightforest.world.components.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.InclusiveRange;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFStructureProcessors;

import java.util.Map;
import java.util.Optional;

@SuppressWarnings("OptionalIsPresent")
public class SpawnerProcessor extends StructureProcessor {
	public static final MapCodec<SpawnerProcessor> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
		Codec.SHORT.optionalFieldOf("range").forGetter(SpawnerProcessor::serializeRange),
		Codec.FLOAT.optionalFieldOf("start_delay_factor").forGetter(SpawnerProcessor::getDelayFactor),
		Codec.FLOAT.optionalFieldOf("entity_width_max").forGetter(SpawnerProcessor::getEntityWidthMax),
		SpawnData.LIST_CODEC.fieldOf("entities").forGetter(SpawnerProcessor::getPossibleEntities)
	).apply(inst, SpawnerProcessor::new));

	private final Optional<Short> range;
	private final Optional<Float> startDelayFactor;
	private final Optional<Float> entityWidthMax;
	private final SimpleWeightedRandomList<SpawnData> entities;

	public static SpawnerProcessor compile(int range, Object2IntMap<EntityType<?>> weightMap) {
		return compile(range, 0, weightMap);
	}

	public static SpawnerProcessor compile(int range, float maxWidth, Object2IntMap<EntityType<?>> weightMap) {
		SimpleWeightedRandomList.Builder<SpawnData> entities = SimpleWeightedRandomList.builder();

		for (Map.Entry<EntityType<?>, Integer> entry : weightMap.entrySet()) {
			CompoundTag entityInfo = new CompoundTag();

			entityInfo.putString("id", BuiltInRegistries.ENTITY_TYPE.getKey(entry.getKey()).toString());

			entities.add(new SpawnData(entityInfo, Optional.of(new SpawnData.CustomSpawnRules(new InclusiveRange<>(0, 7), new InclusiveRange<>(0, 15))), Optional.empty()), entry.getValue());
		}

		return new SpawnerProcessor(range <= 0 ? Optional.empty() : Optional.of((short) range), Optional.of(0.25f), maxWidth <= 0 ? Optional.empty() : Optional.of(maxWidth), entities.build());
	}

	public SpawnerProcessor(Optional<Short> range, Optional<Float> startDelayFactor, Optional<Float> entityWidthMax, SimpleWeightedRandomList<SpawnData> entities) {
		this.range = range;
		this.startDelayFactor = startDelayFactor;
		this.entities = entities;
		this.entityWidthMax = entityWidthMax.isEmpty() ? Optional.empty() : entityWidthMax.get() <= 0 ? Optional.empty() : entityWidthMax;
	}

	@Nullable
	@Override
	public StructureTemplate.StructureBlockInfo process(LevelReader level, BlockPos offset, BlockPos piecePos, StructureTemplate.StructureBlockInfo originalInfo, StructureTemplate.StructureBlockInfo modifiedInfo, StructurePlaceSettings placeSettings, @Nullable StructureTemplate template) {
		CompoundTag nbtInfo = modifiedInfo.nbt();

		if (nbtInfo != null && (modifiedInfo.state().is(Blocks.SPAWNER) || modifiedInfo.state().is(TFBlocks.SINISTER_SPAWNER))) {
			if (this.range.isPresent()) {
				nbtInfo.putShort("SpawnRange", this.range.get());
			}
			if (this.startDelayFactor.isPresent()) {
				nbtInfo.putShort("Delay", (short) Math.round(nbtInfo.getShort("MinSpawnDelay") * this.startDelayFactor.get()));
			}

			if (!nbtInfo.contains("SpawnData") || nbtInfo.getList("SpawnData", Tag.TAG_COMPOUND).isEmpty()) {
				Optional<SpawnData> randomSpawn = this.entities.getRandomValue(placeSettings.getRandom(modifiedInfo.pos()));

				if (randomSpawn.isPresent()) {
					SpawnData spawn = randomSpawn.get();

					Tag entitySpawnData = SpawnData.CODEC
						.encodeStart(NbtOps.INSTANCE, spawn)
						.getOrThrow(s -> new IllegalStateException("Invalid SpawnData in processing: " + s));

					if (this.entityWidthMax.isPresent() && entitySpawnData instanceof CompoundTag compoundTag) {
						// give @p command_block[block_entity_data={id:command_block,auto:1,Command:"/setblock ~ ~ ~ spawner{SpawnCount:4,MaxNearbyEntities:6,SpawnRange:4,Delay:1,MinSpawnDelay:200,MaxSpawnDelay:760,RequiredPlayerRange:16,SpawnData:{entity:{id:zombie,attributes:[{id:\"generic.scale\",base:2f}]}}} replace"}] 1

						Optional<EntityType<?>> type = BuiltInRegistries.ENTITY_TYPE.getOptional(ResourceLocation.parse(spawn.entityToSpawn().getString("id")));
						float newScale = this.rescaleToFitWidth(type.map(EntityType::getWidth).orElse(0f));

						if (Float.isFinite(newScale) && newScale != 1) {
							CompoundTag entityCompound = compoundTag.getCompound("entity");
							ListTag attributes = entityCompound.getList("attributes", Tag.TAG_COMPOUND);
							// Example of tag schema: SpawnData:{entity:{id:zombie,attributes:[{id:\"generic.scale\",base:2f}]}}
							CompoundTag scale = new CompoundTag();
							scale.putString("id", "generic.scale");
							scale.putFloat("base", newScale);

							attributes.add(scale);

							entityCompound.put("attributes", attributes);
							compoundTag.put("entity", entityCompound);
						}
					}

					nbtInfo.put("SpawnData", entitySpawnData);
				}
			}
		}

		return modifiedInfo;
	}

	public float rescaleToFitWidth(float entityWidth) {
		if (this.entityWidthMax.isEmpty() || entityWidth == 0)
			return 1; // No rescale, use default scale if NaN will be encountered

		float maxWidth = this.entityWidthMax.get();
		return entityWidth < maxWidth ? 1 : maxWidth / entityWidth;
	}

	@Override
	protected StructureProcessorType<?> getType() {
		return TFStructureProcessors.SPAWNER_PROCESSOR.value();
	}

	private Optional<Short> serializeRange() {
		return this.range;
	}

	private Optional<Float> getDelayFactor() {
		return this.startDelayFactor;
	}

	private Optional<Float> getEntityWidthMax() {
		return this.entityWidthMax;
	}

	private SimpleWeightedRandomList<SpawnData> getPossibleEntities() {
		return this.entities;
	}
}
