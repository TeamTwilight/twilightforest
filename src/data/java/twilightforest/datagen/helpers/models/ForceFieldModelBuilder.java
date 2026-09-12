package twilightforest.datagen.helpers.models;

import com.google.common.base.Preconditions;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.resources.model.cuboid.CuboidFace;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import twilightforest.client.model.block.forcefield.ForceFieldElement;
import twilightforest.client.model.block.forcefield.ForceFieldModel.ExtraDirection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class ForceFieldModelBuilder {

	private boolean defaultShade = true;
	private int brightnessOverride = 0;
	private int tint = CuboidFace.NO_TINT;
	private final List<ForceFieldElementBuilder> elements = new ArrayList<>();

	public static ForceFieldModelBuilder begin() {
		return new ForceFieldModelBuilder();
	}

	public ForceFieldElementBuilder forceFieldElement() {
		ForceFieldElementBuilder ret = new ForceFieldElementBuilder(this.defaultShade, this.brightnessOverride, this.tint);
		this.elements.add(ret);
		return ret;
	}

	public ForceFieldModelBuilder brightnessOverride(int light) {
		this.brightnessOverride = light;
		return this;
	}

	public ForceFieldModelBuilder disableShade() {
		this.defaultShade = false;
		return this;
	}

	public ForceFieldModelBuilder tintAll(int index) {
		this.tint = index;
		return this;
	}

	public List<ForceFieldElement> build() {
		return this.elements.stream().map(ForceFieldElementBuilder::build).toList();
	}

	public class ForceFieldElementBuilder {

		private Vector3f from = new Vector3f();
		private Vector3f to = new Vector3f(16, 16, 16);
		private final Map<Direction, FaceBuilder> faces = new LinkedHashMap<>();
		private final boolean shade;
		private final int light;
		private final int tint;
		@Nullable
		private Pair<ExtraDirection, Boolean> condition = null;
		private final List<ExtraDirection> parents = new ArrayList<>();

		private ForceFieldElementBuilder(boolean defaultShade, int brightnessOverride, int tint) {
			this.shade = defaultShade;
			this.light = brightnessOverride;
			this.tint = tint;
		}

		private static void validateCoordinate(float coord, char name) {
			Preconditions.checkArgument(!(coord < -16.0F) && !(coord > 32.0F), "Position " + name + " out of range, must be within [-16, 32]. Found: %d", coord);
		}

		private static void validatePosition(Vector3f pos) {
			validateCoordinate(pos.x(), 'x');
			validateCoordinate(pos.y(), 'y');
			validateCoordinate(pos.z(), 'z');
		}

		public ForceFieldElementBuilder from(float x, float y, float z) {
			this.from = new Vector3f(x, y, z);
			validatePosition(this.from);
			return this;
		}

		public ForceFieldElementBuilder to(float x, float y, float z) {
			this.to = new Vector3f(x, y, z);
			validatePosition(this.to);
			return this;
		}

		public FaceBuilder face(Direction dir) {
			Preconditions.checkNotNull(dir, "Direction must not be null");
			return this.faces.computeIfAbsent(dir, direction -> new FaceBuilder(this.tint));
		}

		public ForceFieldElementBuilder allFaces(BiConsumer<Direction, FaceBuilder> action) {
			Arrays.stream(Direction.values()).forEach(d -> action.accept(d, this.face(d)));
			return this;
		}

		public ForceFieldElementBuilder faces(BiConsumer<Direction, FaceBuilder> action) {
			this.faces.forEach(action);
			return this;
		}

		public ForceFieldElementBuilder ifState(ExtraDirection condition, boolean b) {
			this.condition = Pair.of(condition, b);
			return this;
		}

		// Returns a new ForceFieldElementBuilder that has the same condition
		public ForceFieldElementBuilder ifSame() {
			ForceFieldElementBuilder newBuilder = this.end().forceFieldElement();
			newBuilder.condition = Pair.of(this.condition.getFirst(), this.condition.getSecond());
			return newBuilder;
		}

		// Returns a new ForceFieldElementBuilder that has the opposite condition
		public ForceFieldElementBuilder ifElse() {
			ForceFieldElementBuilder newBuilder = this.end().forceFieldElement();
			newBuilder.condition = Pair.of(this.condition.getFirst(), !this.condition.getSecond());
			return newBuilder;
		}

		public ForceFieldElementBuilder parents(ExtraDirection... parents) {
			Collections.addAll(this.parents, parents);
			return this;
		}

		ForceFieldElement build() {
			Map<Direction, ForceFieldElement.Face> faces = this.faces.entrySet().stream()
				.collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().build(), (_, _) -> {
					throw new IllegalArgumentException();
				}, LinkedHashMap::new));
			Optional<ForceFieldElement.Condition> condition = this.condition == null
				? Optional.empty()
				: Optional.of(new ForceFieldElement.Condition(this.condition.getFirst(), this.condition.getSecond(), List.copyOf(this.parents)));
			return new ForceFieldElement(this.from, this.to, faces, this.shade, this.light, condition);
		}

		public ForceFieldModelBuilder end() {
			return ForceFieldModelBuilder.this;
		}

		public class FaceBuilder {

			@Nullable
			private Direction cullface;
			private int tintindex;
			private CuboidFace.UVs uvs = new CuboidFace.UVs(0.0F, 0.0F, 16.0F, 16.0F);

			private FaceBuilder(int tint) {
				this.tintindex = tint;
			}

			public FaceBuilder cullface(@Nullable Direction dir) {
				this.cullface = dir;
				return this;
			}

			public FaceBuilder tintindex(int index) {
				this.tintindex = index;
				return this;
			}

			public FaceBuilder uvs(float u1, float v1, float u2, float v2) {
				this.uvs = new CuboidFace.UVs(u1, v1, u2, v2);
				return this;
			}

			ForceFieldElement.Face build() {
				return new ForceFieldElement.Face(Optional.ofNullable(this.cullface), Optional.of(this.uvs), this.tintindex);
			}

			public ForceFieldElementBuilder end() {
				return ForceFieldElementBuilder.this;
			}
		}
	}
}
