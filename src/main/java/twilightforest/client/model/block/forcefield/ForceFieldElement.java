package twilightforest.client.model.block.forcefield;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.resources.model.cuboid.CuboidFace;
import net.minecraft.core.Direction;
import net.minecraft.util.ExtraCodecs;
import org.joml.Vector3fc;
import twilightforest.client.model.block.forcefield.ForceFieldModel.ExtraDirection;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public record ForceFieldElement(Vector3fc from, Vector3fc to, Map<Direction, ForceFieldElement.Face> faces, boolean shade, int lightEmission, Optional<ForceFieldElement.Condition> condition) {

	public static final Codec<ForceFieldElement> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		ExtraCodecs.VECTOR3F.fieldOf("from").forGetter(ForceFieldElement::from),
		ExtraCodecs.VECTOR3F.fieldOf("to").forGetter(ForceFieldElement::to),
		Codec.unboundedMap(Direction.CODEC, ForceFieldElement.Face.CODEC).fieldOf("faces").forGetter(ForceFieldElement::faces),
		Codec.BOOL.optionalFieldOf("shade", true).forGetter(ForceFieldElement::shade),
		Codec.intRange(0, 15).optionalFieldOf("light_emission", 0).forGetter(ForceFieldElement::lightEmission),
		ForceFieldElement.Condition.CODEC.optionalFieldOf("condition").forGetter(ForceFieldElement::condition)
	).apply(instance, ForceFieldElement::new));

	public record Face(Optional<Direction> cullFace, Optional<CuboidFace.UVs> uvs, int tintIndex) {

		public static final Codec<CuboidFace.UVs> UVS_CODEC = Codec.FLOAT.listOf(4, 4).xmap(
			uvs -> new CuboidFace.UVs(uvs.get(0), uvs.get(1), uvs.get(2), uvs.get(3)),
			uvs -> List.of(uvs.minU(), uvs.minV(), uvs.maxU(), uvs.maxV())
		);

		public static final Codec<Face> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Direction.CODEC.optionalFieldOf("cullface").forGetter(Face::cullFace),
			UVS_CODEC.optionalFieldOf("uv").forGetter(Face::uvs),
			Codec.INT.optionalFieldOf("tintindex", CuboidFace.NO_TINT).forGetter(Face::tintIndex)
		).apply(instance, Face::new));
	}

	public record Condition(ExtraDirection direction, boolean value, List<ExtraDirection> parents) {

		public static final Codec<Condition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ExtraDirection.CODEC.fieldOf("direction").forGetter(Condition::direction),
			Codec.BOOL.optionalFieldOf("if", true).forGetter(Condition::value),
			ExtraDirection.CODEC.listOf().optionalFieldOf("parents", List.of()).forGetter(Condition::parents)
		).apply(instance, Condition::new));
	}
}
