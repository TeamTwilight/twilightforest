package twilightforest.world.components.structures.markerhandler;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import twilightforest.init.custom.TemplateMarkerHandlers;
import twilightforest.util.Codecs;
import twilightforest.util.DirectionUtil;
import twilightforest.util.RotationUtil;

import java.util.regex.Pattern;

public record RotationMarkerHandler(char delimiter, String safeSplitter, Holder<TemplateMarkerHandler> rotated) implements TemplateMarkerHandler {
	public static final MapCodec<RotationMarkerHandler> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		Codecs.CHARACTER_CODEC.optionalFieldOf("delimiter", '@').forGetter(RotationMarkerHandler::delimiter),
		TemplateMarkerHandlers.HOLDER_CODEC.fieldOf("rotated").forGetter(RotationMarkerHandler::rotated)
	).apply(instance, RotationMarkerHandler::new));

	public RotationMarkerHandler(char delimiter, Holder<TemplateMarkerHandler> rotated) {
		this(delimiter, Pattern.quote(String.valueOf(delimiter)), rotated);
	}

	public RotationMarkerHandler(Holder<TemplateMarkerHandler> rotated) {
		this('@', rotated);
	}

	@Override
	public boolean handleDataMarker(String label, BlockPos pos, WorldGenLevel level, RandomSource random, BoundingBox chunkBounds, ChunkGenerator chunkGen, Rotation rotation) {
		String[] directionSplit = label.split(this.safeSplitter, 2);

		Rotation dataRotation = directionSplit.length == 1
			? Rotation.CLOCKWISE_180
			: RotationUtil.getRelativeRotation(Direction.NORTH, DirectionUtil.fromStringOrElse(directionSplit[1], Direction.SOUTH));

		return this.rotated.value().handleDataMarker(directionSplit[0], pos, level, random, chunkBounds, chunkGen, rotation.getRotated(dataRotation));
	}

	@Override
	public TemplateMarkerHandlerType getType() {
		return TemplateMarkerHandlers.ROTATION.value();
	}
}
