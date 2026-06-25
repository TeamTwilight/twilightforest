package twilightforest.client.model.block.forcefield;

import com.mojang.math.Quadrant;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.resources.model.cuboid.CuboidModelElement;
import net.minecraft.client.resources.model.cuboid.CuboidFace;
import net.minecraft.client.resources.model.cuboid.CuboidRotation;
import net.minecraft.client.resources.model.cuboid.FaceBakery;
import net.minecraft.client.resources.model.cuboid.ItemTransforms;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.geometry.BakedQuad.MaterialFlags;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelDebugName;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.client.resources.model.sprite.TextureSlots;
import net.minecraft.client.renderer.block.dispatch.ModelState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.DynamicBlockStateModel;
import org.jetbrains.annotations.Nullable;
import twilightforest.block.ForceFieldBlock;
import twilightforest.client.model.block.forcefield.ForceFieldModelLoader.Condition;

import java.util.*;

public class ForceFieldModel implements BlockStateModel, DynamicBlockStateModel {

        private final List<QuadEntry> entries;
        private final Material.Baked particle;
        private final boolean usesAO;
        private final boolean usesBlockLight;

        public ForceFieldModel(Map<CuboidModelElement, Condition> parts, TextureSlots textures, ModelBaker baker, ModelState modelState, boolean useAmbientOcclusion, boolean usesBlockLight, ItemTransforms itemTransforms) {
                List<QuadEntry> entries = new ArrayList<>();

                for (Map.Entry<CuboidModelElement, Condition> entry : parts.entrySet()) {
                        CuboidModelElement element = entry.getKey();
                        Condition condition = entry.getValue();

                        for (Direction side : Direction.values()) {
                                CuboidFace face = element.faces().get(side);
                                if (face == null) continue;

                                // Convert CuboidFace to CuboidFace
                                CuboidFace.UVs uvs;
                                if (face.uvs() != null) {
                                        uvs = new CuboidFace.UVs(face.uvs().minU(), face.uvs().minV(), face.uvs().maxU(), face.uvs().maxV());
                                } else {
                                        uvs = FaceBakery.defaultFaceUV(element.from(), element.to(), side);
                                }

                                Quadrant rotation = face.rotation();

                                CuboidFace cuboidFace = new CuboidFace(
                                        face.cullForDirection(),
                                        face.tintIndex(),
                                        face.texture(),
                                        uvs,
                                        rotation
                                );

                                Material.Baked material = baker.materials().get(textures.getMaterial(face.texture()), () -> "twilightforest:force_field");

                                BakedQuad quad = FaceBakery.bakeQuad(
                                        baker, element.from(), element.to(), cuboidFace, material,
                                        side, modelState, element.rotation(), element.shade(), element.lightEmission()
                                );

                                entries.add(new QuadEntry(quad, side, condition.direction(), condition.b(), condition.parents()));
                        }
                }

                this.particle = baker.materials().get(textures.getMaterial("particle"), () -> "twilightforest:force_field");
                this.entries = entries;
                this.usesAO = useAmbientOcclusion;
                this.usesBlockLight = usesBlockLight;
        }

        @Override
        public void collectParts(BlockAndTintGetter level, BlockPos pos, BlockState state, RandomSource random, List<BlockStateModelPart> parts) {
                @SuppressWarnings({"unchecked", "rawtypes"})
				List<BakedQuad>[] quadsByDirection = new List[6];
                for (int i = 0; i < 6; i++) quadsByDirection[i] = new ArrayList<>();

                Map<ExtraDirection, List<Direction>> directions = new HashMap<>();
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
                        directions.put(extraDirection, directionList);
                }

                for (QuadEntry entry : this.entries) {
                        if (skipRender(directions, entry.direction, entry.supposedToBe, entry.parents, entry.side)) continue;
                        quadsByDirection[entry.side.get3DDataValue()].add(entry.quad);
                }

                parts.add(new ForceFieldPart(quadsByDirection, this.particle, this.usesAO));
        }

        protected static boolean skipRender(Map<ExtraDirection, List<Direction>> directions, @Nullable ExtraDirection direction, boolean supposedToBe, List<ExtraDirection> parents, Direction side) {
                if (direction == null) return false;
                for (ExtraDirection parent : parents) if (!directions.containsKey(parent)) return true;
                boolean hasKey = directions.containsKey(direction);
                if (hasKey != supposedToBe) return true;
                if (hasKey) return directions.get(direction).contains(side);
                return false;
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
        public Material.Baked particleMaterial(BlockAndTintGetter level, BlockPos pos, BlockState state) {
                return this.particle;
        }

        @Override
        public Material.Baked particleMaterial() {
                return this.particle;
        }

        @Override
        public @MaterialFlags int materialFlags(BlockAndTintGetter level, BlockPos pos, BlockState state) {
                return BakedQuad.FLAG_TRANSLUCENT;
        }

        @Override
        public @MaterialFlags int materialFlags() {
                return BakedQuad.FLAG_TRANSLUCENT;
        }

        @Override
        @Deprecated
        public void collectParts(RandomSource random, List<BlockStateModelPart> output) {
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
        public record ForceFieldData(Map<ExtraDirection, List<Direction>> directions) {
        }

        // Pre-baked quad with condition metadata
        private record QuadEntry(BakedQuad quad, Direction side, @Nullable ExtraDirection direction, boolean supposedToBe, List<ExtraDirection> parents) {
        }

        // BlockStateModelPart implementation that holds quads for each direction
        private static final class ForceFieldPart implements BlockStateModelPart {
                private final List<BakedQuad>[] quadsByDirection;
                private final Material.Baked particle;
                private final boolean usesAO;

                @SuppressWarnings("unchecked")
                private ForceFieldPart(List<BakedQuad>[] quadsByDirection, Material.Baked particle, boolean usesAO) {
                        this.quadsByDirection = quadsByDirection;
                        this.particle = particle;
                        this.usesAO = usesAO;
                }

                @Override
                public List<BakedQuad> getQuads(@Nullable Direction direction) {
                        if (direction == null) return List.of();
                        List<BakedQuad> quads = this.quadsByDirection[direction.get3DDataValue()];
                        return quads != null ? quads : List.of();
                }

                @Override
                public boolean useAmbientOcclusion() {
                        return this.usesAO;
                }

                @Override
                public Material.Baked particleMaterial() {
                        return this.particle;
                }

                @Override
                public @MaterialFlags int materialFlags() {
                        return BakedQuad.FLAG_TRANSLUCENT;
                }
        }
}
