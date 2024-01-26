package twilightforest.client.model.block.forcefield;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.ChunkRenderTypeSet;
import net.neoforged.neoforge.client.RenderTypeGroup;
import net.neoforged.neoforge.client.model.IDynamicBakedModel;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.data.ModelProperty;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import twilightforest.block.ForceFieldBlock;

import java.util.*;
import java.util.function.Function;

public class ForceFieldModel implements IDynamicBakedModel {
    private static final ModelProperty<ForceFieldData> DATA = new ModelProperty<>();
    private static final FaceBakery FACE_BAKERY = new FaceBakery();

    private final Map<BlockElement, ForceFieldModelLoader.Condition> parts;
    private final Function<Material, TextureAtlasSprite> spriteFunction;
    private final IGeometryBakingContext context;
    private final TextureAtlasSprite particle;
    private final ItemOverrides overrides;
    private final ChunkRenderTypeSet blockRenderTypes;
    private final List<RenderType> itemRenderTypes;
    private final List<RenderType> fabulousItemRenderTypes;

    public ForceFieldModel(Map<BlockElement, ForceFieldModelLoader.Condition> parts, Function<Material, TextureAtlasSprite> spriteFunction, IGeometryBakingContext context, ItemOverrides overrides) {
        this.parts = parts;
        this.spriteFunction = spriteFunction;
        this.context = context;
        this.particle = spriteFunction.apply(context.getMaterial("particle"));
        this.overrides = overrides;
        ResourceLocation renderTypeHint = context.getRenderTypeHint();
        RenderTypeGroup group = renderTypeHint != null ? context.getRenderType(renderTypeHint) : RenderTypeGroup.EMPTY;
        this.blockRenderTypes = !group.isEmpty() ? ChunkRenderTypeSet.of(group.block()) : null;
        this.itemRenderTypes = !group.isEmpty() ? List.of(group.entity()) : null;
        this.fabulousItemRenderTypes = !group.isEmpty() ? List.of(group.entityFabulous()) : null;
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction cullFace, @NotNull RandomSource rand, @NotNull ModelData extraData, @Nullable RenderType renderType) {
        List<BakedQuad> quads = new ArrayList<>();
        ForceFieldData data = extraData.get(DATA);

        if (data != null) {
            if (cullFace == null) {
                for (Direction direction : Direction.values()) {
                    quads = this.getQuads(quads, direction, data, false);
                }
            } else return this.getQuads(quads, cullFace, data, true);
        }

        return quads;
    }

    public @NotNull List<BakedQuad> getQuads(List<BakedQuad> quads, Direction side, ForceFieldData data, boolean cull) {
        for (Map.Entry<BlockElement, ForceFieldModelLoader.Condition> entry : this.parts.entrySet()) {
            BlockElementFace blockelementface = entry.getKey().faces.get(side);
            if (blockelementface != null && blockelementface.cullForDirection != null == cull) { // IntelliJ will try to tell you cullForDirection is never null, it's gaslighting you
                if (ForceFieldModel.skipRender(data.directions(), entry.getValue().direction(), entry.getValue().b(), entry.getValue().parents(), side)) continue;

                TextureAtlasSprite sprite = this.spriteFunction.apply(context.getMaterial(blockelementface.texture));
                quads.add(FACE_BAKERY.bakeQuad(
                        entry.getKey().from,
                        entry.getKey().to,
                        blockelementface,
                        sprite,
                        side,
                        BlockModelRotation.X0_Y0,
                        null,
                        false,
                        new ResourceLocation(sprite.atlasLocation().getNamespace(), sprite.atlasLocation().getPath() + "_" + side.name().toLowerCase(Locale.ROOT)))
                );
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

    @Override
    public @NotNull ModelData getModelData(@NotNull BlockAndTintGetter level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull ModelData modelData) {
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

    @Override
    public boolean useAmbientOcclusion() {
        return this.context.useAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return this.context.isGui3d();
    }

    @Override
    public boolean usesBlockLight() {
        return this.context.useBlockLight();
    }

    @Override
    public boolean isCustomRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return this.particle;
    }

    @Override
    public ItemOverrides getOverrides() {
        return this.overrides;
    }

    @NotNull
    @Override
    @SuppressWarnings("deprecation")
    public ItemTransforms getTransforms() {
        return this.context.getTransforms();
    }

    @NotNull
    @Override
    public ChunkRenderTypeSet getRenderTypes(@NotNull BlockState state, @NotNull RandomSource rand, @NotNull ModelData data) {
        return this.blockRenderTypes != null ? this.blockRenderTypes : IDynamicBakedModel.super.getRenderTypes(state, rand, data);
    }

    @NotNull
    @Override
    public List<RenderType> getRenderTypes(@NotNull ItemStack stack, boolean fabulous) {
        if (!fabulous) {
            if (this.itemRenderTypes != null) {
                return this.itemRenderTypes;
            }
        } else if (this.fabulousItemRenderTypes != null) {
            return this.fabulousItemRenderTypes;
        }

        return IDynamicBakedModel.super.getRenderTypes(stack, fabulous);
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

        @SuppressWarnings("deprecation")
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
        public static ExtraDirection byName(@Nullable String name) {
            return CODEC.byName(name);
        }
    }

    //modeldata holder
    public record ForceFieldData(Map<ExtraDirection, List<Direction>> directions) {}
}
