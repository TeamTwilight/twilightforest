package twilightforest.world.components.structures.markerhandler;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import twilightforest.init.custom.TemplateMarkerHandlers;
import twilightforest.util.Codecs;

import java.util.Map;
import java.util.regex.Pattern;

public record SwitchMarkerHandler(char groupDelimiter, String safeGroupSplitter, char entryDelimiter, String safeEntrySplitter, Map<String, Holder<TemplateMarkerHandler>> handlers) implements TemplateMarkerHandler {
	public static final MapCodec<SwitchMarkerHandler> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		Codecs.CHARACTER_CODEC.optionalFieldOf("group_character_delimiter", '|').forGetter(SwitchMarkerHandler::groupDelimiter),
		Codecs.CHARACTER_CODEC.optionalFieldOf("entry_character_delimiter", ':').forGetter(SwitchMarkerHandler::entryDelimiter),
		Codec.unboundedMap(Codec.STRING, TemplateMarkerHandlers.HOLDER_CODEC).fieldOf("element_processor").forGetter(SwitchMarkerHandler::handlers)
	).apply(instance, SwitchMarkerHandler::new));

	public SwitchMarkerHandler(char groupDelimiter, char entryDelimiter, Map<String, Holder<TemplateMarkerHandler>> elementProcessor) {
		this(groupDelimiter, Pattern.quote(String.valueOf(groupDelimiter)), entryDelimiter, Pattern.quote(String.valueOf(entryDelimiter)), elementProcessor);
	}

	public SwitchMarkerHandler(Map<String, Holder<TemplateMarkerHandler>> elementProcessor) {
		this('|', ':', elementProcessor);
	}

	@Override
	public boolean handleDataMarker(String label, BlockPos pos, WorldGenLevel level, RandomSource random, BoundingBox chunkBounds, ChunkGenerator chunkGen, Rotation rotation) {
		String[] groups = label.split(this.safeGroupSplitter);
		if (groups.length < 1) {
			return false;
		}
		// Shuffle
		for (int i = 0; i < groups.length; i++) {
			int newIndex = random.nextInt(groups.length);
			groups[i] = groups[newIndex];
		}
		for (String group : groups) {
			Holder<TemplateMarkerHandler> templateMarkerHandlerHolder = this.handlers.get(group);
			if (templateMarkerHandlerHolder == null) {
				continue;
			}
			String[] entrySplit = group.split(this.safeEntrySplitter, 2);
			String substring = entrySplit.length < 2 ? "" : entrySplit[1];
			if (templateMarkerHandlerHolder.value().handleDataMarker(substring, pos, level, random, chunkBounds, chunkGen, rotation))
				return true;
		}
		return false;
	}

	@Override
	public TemplateMarkerHandlerType getType() {
		return TemplateMarkerHandlers.HANDLER_SWITCH.value();
	}
}
