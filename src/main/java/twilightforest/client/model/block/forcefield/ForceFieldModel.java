package twilightforest.client.model.block.forcefield;

import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.dispatch.ModelState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelDebugName;
import net.minecraft.client.resources.model.cuboid.ItemTransforms;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.geometry.QuadCollection;
import net.minecraft.client.resources.model.geometry.UnbakedGeometry;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.client.resources.model.sprite.TextureSlots;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.quad.MutableQuad;
import net.neoforged.neoforge.model.data.ModelData;
import net.neoforged.neoforge.model.data.ModelProperty;
import org.jetbrains.annotations.Nullable;
import twilightforest.block.ForceFieldBlock;

import java.util.*;
import java.util.function.Function;

public class ForceFieldModel implements UnbakedGeometry {
	private static final ModelProperty<ForceFieldData> DATA = new ModelProperty<>();

	private final Map<String, ForceFieldModelLoader.Condition> parts;
	private final Function<String, String> spriteFunction;
	private final boolean usesAO;
	private final boolean usesBlockLight;
	private final ItemTransforms transforms;
	@Nullable
	private final Set<RenderType> renderTypes;

	public ForceFieldModel(Map<String, ForceFieldModelLoader.Condition> parts, Function<String, String> spriteFunction, boolean useAmbientOcclusion, boolean usesBlockLight, ItemTransforms itemTransforms, @Nullable Set<RenderType> renderTypes) {
		this.parts = parts;
		this.spriteFunction = spriteFunction;
		this.usesAO = useAmbientOcclusion;
		this.usesBlockLight = usesBlockLight;
		this.transforms = itemTransforms;
		this.renderTypes = renderTypes;
	}

	@Override
	public QuadCollection bake(TextureSlots textureSlots, ModelBaker modelBaker, ModelState modelState, ModelDebugName modelDebugName) {
		QuadCollection.Builder builder = new QuadCollection.Builder();

		ForceFieldData defaultData = new ForceFieldData(java.util.Collections.emptyMap());

		List<BakedQuad> unculledQuads = new java.util.ArrayList<>();
		for (Direction direction : Direction.values()) {
			unculledQuads = this.getQuads(unculledQuads, direction, defaultData);
		}
		for (BakedQuad quad : unculledQuads) {
			builder.addUnculledFace(quad);
		}

		for (Direction direction : Direction.values()) {
			List<BakedQuad> culledQuads = new java.util.ArrayList<>();
			culledQuads = this.getQuads(culledQuads, direction, defaultData);
			for (BakedQuad quad : culledQuads) {
				builder.addCulledFace(direction, quad);
			}
		}

		return builder.build();
	}

	public List<BakedQuad> getQuads(List<BakedQuad> quads, Direction side, ForceFieldData data) {
		for (Map.Entry<String, ForceFieldModelLoader.Condition> entry : this.parts.entrySet()) {

			if (ForceFieldModel.skipRender(data.directions(), entry.getValue().direction(), entry.getValue().b(), entry.getValue().parents(), side)) {
				continue;
			}

			String texturePath = this.spriteFunction.apply(entry.getKey() + "_" + side.getName());

			if (texturePath != null) {
				Identifier identifier = Identifier.parse(texturePath);
				Material material = new Material(identifier);

				material = material.withForceTranslucent(true);

				TextureAtlasSprite sprite = net.minecraft.client.Minecraft.getInstance()
					.getAtlasManager().getAtlasOrThrow(net.minecraft.client.renderer.texture.TextureAtlas.LOCATION_BLOCKS)
					.getSprite(material.sprite());

				MutableQuad mutableQuad = new MutableQuad();

				for (int vertexIndex = 0; vertexIndex < 4; vertexIndex++) {
					float x = (vertexIndex == 1 || vertexIndex == 2) ? 1.0f : 0.0f;
					float y = (vertexIndex == 2 || vertexIndex == 3) ? 1.0f : 0.0f;
					float z = 0.5f;

					mutableQuad.setPosition(vertexIndex, new org.joml.Vector3f(x, y, z));

					float u = sprite.getU(x * 16.0f);
					float v = sprite.getV(y * 16.0f);
					mutableQuad.setUv(vertexIndex, u, v);
				}

				mutableQuad.setDirection(side);
				mutableQuad.setShade(this.usesAO);

				if (this.usesBlockLight) {
					mutableQuad.setLightEmission(15);
				}

				quads.add(mutableQuad.toBakedQuad());
			}
		}
		return quads;
	}

	protected static boolean skipRender(Map<ExtraDirection, List<Direction>> directions, @Nullable ExtraDirection direction, boolean supposedToBe, List<ExtraDirection> parents, Direction side) {
		if (direction == null) return false;
		for (ExtraDirection parent : parents) if (!directions.containsKey(parent)) return true;
		boolean hasKey = directions.containsKey(direction);
		if (hasKey != supposedToBe) return true;
		if (hasKey) return directions.get(direction).contains(side);
		return false;
	}

	public ModelData getModelData(BlockAndTintGetter level, BlockPos pos, BlockState state, ModelData modelData) {
		if (modelData == ModelData.EMPTY) {
			Map<ExtraDirection, List<Direction>> map = new HashMap<>();
			for (ExtraDirection extraDirection : getExtraDirections(state, level, pos)) {
				List<Direction> directionList = new ArrayList<>();
				for (Direction dir : Direction.values()) {
					ExtraDirection mirrored = extraDirection.mirrored(dir.getAxis());
					if (mirrored != extraDirection) {
						BlockState other = level.getBlockState(pos.relative(dir));
						if (other.getBlock() instanceof ForceFieldBlock) {
							if (getExtraDirections(other, level, pos.relative(dir)).contains(mirrored)) directionList.add(dir);
						}
					}
				}
				map.put(extraDirection, directionList);
			}

			modelData = ModelData.builder().with(DATA, new ForceFieldData(map)).build();
		}
		return modelData;
	}

	public static List<ExtraDirection> getExtraDirections(BlockState state, BlockGetter level, BlockPos pos) {
		List<ExtraDirection> directions = new ArrayList<>();

		boolean down = state.getValue(ForceFieldBlock.DOWN);
		boolean up = state.getValue(ForceFieldBlock.UP);
		boolean north = state.getValue(ForceFieldBlock.NORTH);
		boolean south = state.getValue(ForceFieldBlock.SOUTH);
		boolean west = state.getValue(ForceFieldBlock.WEST);
		boolean east = state.getValue(ForceFieldBlock.EAST);

		if (down) {
			directions.add(ExtraDirection.DOWN);
			if (north && ForceFieldBlock.cornerConnects(level, pos, Direction.DOWN, Direction.NORTH)) directions.add(ExtraDirection.DOWN_NORTH);
			if (south && ForceFieldBlock.cornerConnects(level, pos, Direction.DOWN, Direction.SOUTH)) directions.add(ExtraDirection.DOWN_SOUTH);
			if (west && ForceFieldBlock.cornerConnects(level, pos, Direction.DOWN, Direction.WEST)) directions.add(ExtraDirection.DOWN_WEST);
			if (east && ForceFieldBlock.cornerConnects(level, pos, Direction.DOWN, Direction.EAST)) directions.add(ExtraDirection.DOWN_EAST);
		}
		if (up) {
			directions.add(ExtraDirection.UP);
			if (north && ForceFieldBlock.cornerConnects(level, pos, Direction.UP, Direction.NORTH)) directions.add(ExtraDirection.UP_NORTH);
			if (south && ForceFieldBlock.cornerConnects(level, pos, Direction.UP, Direction.SOUTH)) directions.add(ExtraDirection.UP_SOUTH);
			if (west && ForceFieldBlock.cornerConnects(level, pos, Direction.UP, Direction.WEST)) directions.add(ExtraDirection.UP_WEST);
			if (east && ForceFieldBlock.cornerConnects(level, pos, Direction.UP, Direction.EAST)) directions.add(ExtraDirection.UP_EAST);
		}
		if (north) {
			directions.add(ExtraDirection.NORTH);
			if (west && ForceFieldBlock.cornerConnects(level, pos, Direction.NORTH, Direction.WEST)) directions.add(ExtraDirection.NORTH_WEST);
			if (east && ForceFieldBlock.cornerConnects(level, pos, Direction.NORTH, Direction.EAST)) directions.add(ExtraDirection.NORTH_EAST);
		}
		if (south) {
			directions.add(ExtraDirection.SOUTH);
			if (west && ForceFieldBlock.cornerConnects(level, pos, Direction.SOUTH, Direction.WEST)) directions.add(ExtraDirection.SOUTH_WEST);
			if (east && ForceFieldBlock.cornerConnects(level, pos, Direction.SOUTH, Direction.EAST)) directions.add(ExtraDirection.SOUTH_EAST);
		}
		if (west) directions.add(ExtraDirection.WEST);
		if (east) directions.add(ExtraDirection.EAST);

		return directions;
	}

	public enum ExtraDirection implements StringRepresentable {
		DOWN("down", 0, 1, 0),
		UP("up", 1, 0, 1),
		NORTH("north", 2, 2, 3),
		SOUTH("south", 3, 3, 2),
		WEST("west", 5, 4, 4),
		EAST("east", 4, 5, 5),

		DOWN_NORTH("down_north", 6, 10, 7),
		DOWN_SOUTH("down_south", 7, 11, 6),
		DOWN_WEST("down_west", 9, 12, 8),
		DOWN_EAST("down_east", 8, 13, 9),

		UP_NORTH("up_north", 10, 6, 11),
		UP_SOUTH("up_south", 11, 7, 10),
		UP_WEST("up_west", 13, 8, 12),
		UP_EAST("up_east", 12, 9, 13),

		NORTH_WEST("north_west", 15, 14, 16),
		NORTH_EAST("north_east", 14, 15, 17),
		SOUTH_WEST("south_west", 17, 16, 14),
		SOUTH_EAST("south_east", 16, 17, 15);

		public static final EnumCodec<ExtraDirection> CODEC = StringRepresentable.fromEnum(ExtraDirection::values);
		private final String name;
		private final int xAxisMirror;
		private final int yAxisMirror;
		private final int zAxisMirror;

		ExtraDirection(String name, int xAxisMirror, int yAxisMirror, int zAxisMirror) {
			this.name = name;
			this.xAxisMirror = xAxisMirror;
			this.yAxisMirror = yAxisMirror;
			this.zAxisMirror = zAxisMirror;
		}

		@Override
		public String getSerializedName() {
			return this.name;
		}

		public ExtraDirection mirrored(Direction.Axis axis) {
			return switch (axis) {
				case X -> ExtraDirection.values()[this.xAxisMirror];
				case Y -> ExtraDirection.values()[this.yAxisMirror];
				case Z -> ExtraDirection.values()[this.zAxisMirror];
			};
		}

		@Nullable
		public static ExtraDirection byName(String name) {
			return CODEC.byName(name);
		}
	}

	//modeldata holder
	public record ForceFieldData(Map<ExtraDirection, List<Direction>> directions) {
	}
}
