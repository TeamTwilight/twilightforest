package twilightforest.client.model.block.forcefield;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Quadrant;
import net.minecraft.client.resources.model.cuboid.CuboidModelElement;
import net.minecraft.client.resources.model.cuboid.CuboidFace;
import net.minecraft.client.resources.model.cuboid.CuboidRotation;
import net.minecraft.client.resources.model.cuboid.FaceBakery;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.client.model.ExtraFaceData;
import net.neoforged.neoforge.client.model.generators.template.CustomLoaderBuilder;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.block.forcefield.ForceFieldModel.ExtraDirection;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class ForceFieldModelBuilder extends CustomLoaderBuilder {

	private boolean defaultShade = true;
	private int brightnessOverride = 0;
	private int tint = -1;
	protected List<ForceFieldElementBuilder> elements = new ArrayList<>();

	public static ForceFieldModelBuilder begin() {
		return new ForceFieldModelBuilder();
	}

	private ForceFieldModelBuilder self() {
		return this;
	}

	public ForceFieldModelBuilder() {
		super(TwilightForestMod.prefix("force_field"), false);
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

	@Override
	protected CustomLoaderBuilder copyInternal() {
		ForceFieldModelBuilder builder = new ForceFieldModelBuilder();
		builder.elements = this.elements;
		builder.defaultShade = this.defaultShade;
		builder.brightnessOverride = this.brightnessOverride;
		builder.tint = this.tint;
		return builder;
	}

	@Override
	public JsonObject toJson(JsonObject json) {
		json = super.toJson(json);
		json.addProperty("render_type", "minecraft:translucent");
		if (!this.elements.isEmpty()) {
			JsonArray elements = new JsonArray();
			this.elements.forEach(forceFieldElementBuilder -> {
				CuboidModelElement part = forceFieldElementBuilder.build();
				JsonObject partObj = new JsonObject();

				if (forceFieldElementBuilder.condition != null) {
					JsonObject condition = new JsonObject();
					condition.addProperty("if", forceFieldElementBuilder.condition.getSecond());
					condition.addProperty("direction", forceFieldElementBuilder.condition.getFirst().getSerializedName());

					JsonArray parents = new JsonArray();
					for (ExtraDirection parent : forceFieldElementBuilder.parents) {
						parents.add(parent.getSerializedName());
					}
					condition.add("parents", parents);

					partObj.add("condition", condition);
				}

				partObj.add("from", serializeVector3f(new Vector3f(part.from())));
				partObj.add("to", serializeVector3f(new Vector3f(part.to())));

				if (part.rotation() != null) {
					JsonObject rotation = new JsonObject();
					rotation.add("origin", serializeVector3f(new Vector3f(part.rotation().origin())));
					if (part.rotation().value() instanceof CuboidRotation.SingleAxisRotation singleAxis) {
						rotation.addProperty("axis", singleAxis.axis().getSerializedName());
						rotation.addProperty("angle", singleAxis.angle());
					}
					if (part.rotation().rescale()) {
						rotation.addProperty("rescale", true);
					}
					partObj.add("rotation", rotation);
				}

				if (!part.shade()) {
					partObj.addProperty("shade", part.shade());
				}

				if (part.lightEmission() != 0) {
					partObj.addProperty("light_emission", part.lightEmission());
				}

				JsonObject faces = new JsonObject();
				for (Direction dir : Direction.values()) {
					CuboidFace face = part.faces().get(dir);
					if (face == null) continue;

					JsonObject faceObj = new JsonObject();
					faceObj.addProperty("texture", serializeLocOrKey(face.texture()));
					CuboidFace.UVs defaultUVs = FaceBakery.defaultFaceUV(part.from(), part.to(), dir);
					if (face.uvs() != null && !(face.uvs().minU() == defaultUVs.minU() && face.uvs().minV() == defaultUVs.minV() && face.uvs().maxU() == defaultUVs.maxU() && face.uvs().maxV() == defaultUVs.maxV())) {
						faceObj.add("uv", new Gson().toJsonTree(new float[]{face.uvs().minU(), face.uvs().minV(), face.uvs().maxU(), face.uvs().maxV()}));
					}
					if (face.cullForDirection() != null) {
						faceObj.addProperty("cullface", face.cullForDirection().getSerializedName());
					}
					if (face.rotation() != Quadrant.R0) {
						faceObj.addProperty("rotation", switch (face.rotation()) {
							case R0 -> 0;
							case R90 -> 90;
							case R180 -> 180;
							case R270 -> 270;
						});
					}
					if (face.tintIndex() != -1) {
						faceObj.addProperty("tintindex", face.tintIndex());
					}
					faces.add(dir.getSerializedName(), faceObj);
				}
				if (!part.faces().isEmpty()) {
					partObj.add("faces", faces);
				}
				elements.add(partObj);
			});
			json.add("elements", elements);
		}
		return json;
	}

	private static String serializeLocOrKey(String tex) {
		if (tex.charAt(0) == '#') {
			return tex;
		}
		return Identifier.parse(tex).toString();
	}

	private static JsonArray serializeVector3f(Vector3f vec) {
		JsonArray ret = new JsonArray();
		ret.add(serializeFloat(vec.x()));
		ret.add(serializeFloat(vec.y()));
		ret.add(serializeFloat(vec.z()));
		return ret;
	}

	private static Number serializeFloat(float f) {
		if ((int) f == f) return (int) f;
		return f;
	}


	/**
	 * Forge copy of ElementBuilder, with some things changed
	 */
	public class ForceFieldElementBuilder {
		private Vector3f from = new Vector3f();
		private Vector3f to = new Vector3f(16, 16, 16);
		private final Map<Direction, ForceFieldElementBuilder.FaceBuilder> faces = new LinkedHashMap<>();
		@Nullable
		private ForceFieldElementBuilder.RotationBuilder rotation;
		private boolean shade;
		private int light;
		private int tint;
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

		public ForceFieldElementBuilder.FaceBuilder face(Direction dir) {
			Preconditions.checkNotNull(dir, "Direction must not be null");
			return this.faces.computeIfAbsent(dir, direction -> new FaceBuilder(this.tint));
		}

		public ForceFieldElementBuilder.RotationBuilder rotation() {
			if (this.rotation == null) {
				this.rotation = new ForceFieldElementBuilder.RotationBuilder();
			}
			return this.rotation;
		}

		public ForceFieldElementBuilder shade(boolean shade) {
			this.shade = shade;
			return this;
		}

		public ForceFieldElementBuilder allFaces(BiConsumer<Direction, ForceFieldElementBuilder.FaceBuilder> action) {
			Arrays.stream(Direction.values()).forEach(d -> action.accept(d, this.face(d)));
			return this;
		}

		public ForceFieldElementBuilder faces(BiConsumer<Direction, ForceFieldElementBuilder.FaceBuilder> action) {
			this.faces.forEach(action);
			return this;
		}

		public ForceFieldElementBuilder textureAll(String texture) {
			return this.allFaces(this.addTexture(texture));
		}

		public ForceFieldElementBuilder texture(String texture) {
			return this.faces(this.addTexture(texture));
		}

		public ForceFieldElementBuilder cube(String texture) {
			return this.allFaces(this.addTexture(texture).andThen((dir, f) -> f.cullface(dir)));
		}

		public ForceFieldElementBuilder emissivity(int light) {
			this.light = light;
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

		private BiConsumer<Direction, ForceFieldElementBuilder.FaceBuilder> addTexture(String texture) {
			return (direction, builder) -> builder.texture(texture);
		}

		CuboidModelElement build() {
			Map<Direction, CuboidFace> faces = this.faces.entrySet().stream()
				.collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().build(), (direction, face) -> {
					throw new IllegalArgumentException();
				}, LinkedHashMap::new));
			return new CuboidModelElement(this.from, this.to, faces, this.rotation == null ? null : this.rotation.build(), this.shade, this.light, ExtraFaceData.DEFAULT);
		}

		public ForceFieldModelBuilder end() {
			return self();
		}

		public class FaceBuilder {
			@Nullable
			private Direction cullface;
			private int tintindex;
			@Nullable
			private String texture = MissingTextureAtlasSprite.getLocation().toString();
			private float@Nullable[] uvs;
			private FaceRotation rotation = FaceRotation.ZERO;

			FaceBuilder(int tint) {
				this.tintindex = tint;
			}

			public ForceFieldElementBuilder.FaceBuilder cullface(@Nullable Direction dir) {
				this.cullface = dir;
				return this;
			}

			public ForceFieldElementBuilder.FaceBuilder tintindex(int index) {
				this.tintindex = index;
				return this;
			}

			public ForceFieldElementBuilder.FaceBuilder texture(String texture) {
				Preconditions.checkNotNull(texture, "Texture must not be null");
				this.texture = texture;
				return this;
			}

			public ForceFieldElementBuilder.FaceBuilder uvs(float u1, float v1, float u2, float v2) {
				this.uvs = new float[]{u1, v1, u2, v2};
				return this;
			}

			public ForceFieldElementBuilder.FaceBuilder rotation(FaceRotation rot) {
				Preconditions.checkNotNull(rot, "Rotation must not be null");
				this.rotation = rot;
				return this;
			}

			CuboidFace build() {
				if (this.texture == null) {
					throw new IllegalStateException("A model face must have a texture");
				}
				CuboidFace.UVs uv;
				if (this.uvs != null) {
					uv = new CuboidFace.UVs(this.uvs[0], this.uvs[1], this.uvs[2], this.uvs[3]);
				} else {
					uv = null;
				}
				Quadrant quadRotation = Quadrant.parseJson(this.rotation.rotation);
				return new CuboidFace(this.cullface, this.tintindex, this.texture, uv, quadRotation);
			}

			public ForceFieldElementBuilder end() {
				return ForceFieldElementBuilder.this;
			}
		}

		public class RotationBuilder {

			@Nullable
			private Vector3f origin;
			@Nullable
			private Direction.Axis axis;
			private float angle;
			private boolean rescale;

			public ForceFieldElementBuilder.RotationBuilder origin(float x, float y, float z) {
				this.origin = new Vector3f(x, y, z);
				return this;
			}

			public ForceFieldElementBuilder.RotationBuilder axis(Direction.Axis axis) {
				Preconditions.checkNotNull(axis, "Axis must not be null");
				this.axis = axis;
				return this;
			}

			public ForceFieldElementBuilder.RotationBuilder angle(float angle) {
				// Same logic from BlockPart.Deserializer#parseAngle
				Preconditions.checkArgument(angle == 0.0F || Mth.abs(angle) == 22.5F || Mth.abs(angle) == 45.0F, "Invalid rotation %f found, only -45/-22.5/0/22.5/45 allowed", angle);
				this.angle = angle;
				return this;
			}

			public ForceFieldElementBuilder.RotationBuilder rescale(boolean rescale) {
				this.rescale = rescale;
				return this;
			}

			CuboidRotation build() {
				Preconditions.checkNotNull(this.origin, "No origin specified");
				Preconditions.checkNotNull(this.axis, "No axis specified");
				return new CuboidRotation(this.origin, new CuboidRotation.SingleAxisRotation(this.axis, this.angle), this.rescale);
			}

			public ForceFieldElementBuilder end() {
				return ForceFieldElementBuilder.this;
			}
		}
	}

	public enum FaceRotation {
		ZERO(0),
		CLOCKWISE_90(90),
		UPSIDE_DOWN(180),
		COUNTERCLOCKWISE_90(270);

		final int rotation;

		FaceRotation(int rotation) {
			this.rotation = rotation;
		}
	}
}
