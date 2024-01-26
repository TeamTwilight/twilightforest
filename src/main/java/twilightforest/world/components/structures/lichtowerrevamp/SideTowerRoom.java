package twilightforest.world.components.structures.lichtowerrevamp;

import com.google.common.collect.ImmutableList;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.util.ArrayUtil;
import twilightforest.world.components.structures.TwilightTemplateStructurePiece;

import java.util.EnumMap;
import java.util.List;


public final class SideTowerRoom extends TwilightTemplateStructurePiece {
    // TODO make more advanced, assign sided configurations of what covers can be used (walls/windows/bridges)
    private static final List<String> SMALL_ROOMS = ImmutableList.of(
            "empty_small", // Remove later
            "library_steps_small"
    );
    private static final List<String> MEDIUM_ROOMS = ImmutableList.of(
            "empty_medium", // Remove later
            "stacked_library_elbow_medium"
    );
    private static final List<String> LARGE_ROOMS = ImmutableList.of(
            "empty_large", // Remove later
            "illegal_blockstate_kitchen", // Remove later
            "library_plus_large"
    );

    private static final EnumMap<Rotation, BlockPos> OFFSETS = new EnumMap<>(Rotation.class);
    static {
        OFFSETS.put(Rotation.NONE, BlockPos.ZERO);
        OFFSETS.put(Rotation.CLOCKWISE_90, new BlockPos(1, 0, 0));
        OFFSETS.put(Rotation.CLOCKWISE_180, new BlockPos(1, 0, 1));
        OFFSETS.put(Rotation.COUNTERCLOCKWISE_90, new BlockPos(0, 0, 1));
    }

    private final int squareDiameter;
    private final Rotation externalRotation;

    public SideTowerRoom(StructurePieceSerializationContext ctx, CompoundTag compoundTag) {
        super(TFStructurePieceTypes.SIDE_TOWER_ROOM.get(), compoundTag, ctx, readSettings(compoundTag));
        this.squareDiameter = compoundTag.getInt("square_diameter");
        this.externalRotation = ArrayUtil.wrapped(Rotation.values(), compoundTag.getInt("ext_rotation"));
    }

    private SideTowerRoom(StructureTemplateManager structureManager, Rotation roomRotation, Rotation exteriorRotation, String name, BlockPos startPosition, int squareDiameter) {
        this(
                structureManager,
                TwilightForestMod.prefix("lich_tower/side_tower_rooms/" + name),
                makeSettings(roomRotation),
                startPosition.offset(OFFSETS.get(roomRotation).multiply(squareDiameter - 1)).offset(OFFSETS.get(exteriorRotation).multiply(1 - squareDiameter)),
                squareDiameter,
                exteriorRotation
        );
    }

    private SideTowerRoom(StructureTemplateManager structureManager, ResourceLocation templateLocation, StructurePlaceSettings placeSettings, BlockPos startPosition, int squareDiameter, Rotation externalRotation) {
        super(TFStructurePieceTypes.SIDE_TOWER_ROOM.get(), 0, structureManager, templateLocation, placeSettings, startPosition);
        this.squareDiameter = squareDiameter;
        this.externalRotation = externalRotation;
    }

    public static SideTowerRoom smallRoom(StructureTemplateManager structureManager, Rotation exteriorRotation, BlockPos startPosition, RandomSource random) {
        return new SideTowerRoom(structureManager, Rotation.getRandom(random), exteriorRotation, "small/" + Util.getRandom(SMALL_ROOMS, random), startPosition, 5);
    }

    public static SideTowerRoom mediumRoom(StructureTemplateManager structureManager, Rotation exteriorRotation, BlockPos startPosition, RandomSource random) {
        return new SideTowerRoom(structureManager, Rotation.getRandom(random), exteriorRotation, "medium/" + Util.getRandom(MEDIUM_ROOMS, random), startPosition, 7);
    }

    public static SideTowerRoom largeRoom(StructureTemplateManager structureManager, Rotation exteriorRotation, BlockPos startPosition, RandomSource random) {
        return new SideTowerRoom(structureManager, Rotation.getRandom(random), exteriorRotation, "large/" + Util.getRandom(LARGE_ROOMS, random), startPosition, 9);
    }

    @Override
    public void addChildren(StructurePiece parent, StructurePieceAccessor structureStart, RandomSource random) {
        super.addChildren(parent, structureStart, random);

        BlockPos center = this.boundingBox.getCenter();
        BlockPos offset = new BlockPos(-(this.squareDiameter - 1 >> 1), -4, (this.squareDiameter - 1 >> 1) + 1);

        this.placeCover(structureStart, center, offset, random, Rotation.COUNTERCLOCKWISE_90);
        this.placeCover(structureStart, center, offset, random, Rotation.NONE);
        this.placeCover(structureStart, center, offset, random, Rotation.CLOCKWISE_90);
    }

    private void placeCover(StructurePieceAccessor structureStart, BlockPos center, BlockPos offset, RandomSource random, Rotation rotation) {
        Rotation relativeRotation = this.externalRotation.getRotated(rotation);

        BlockPos pos = center.offset(offset.rotate(relativeRotation));

        switch (this.squareDiameter) {
            case 9 -> {
                SideTowerCover largeCover = SideTowerCover.largeCover(this.structureManager, relativeRotation, pos, random);
                largeCover.addChildren(this, structureStart, random);
                structureStart.addPiece(largeCover);
            }
            case 7 -> {
                SideTowerCover mediumCover = SideTowerCover.mediumCover(this.structureManager, relativeRotation, pos, random);
                mediumCover.addChildren(this, structureStart, random);
                structureStart.addPiece(mediumCover);
            }
            case 5 -> {
                SideTowerCover smallCover = SideTowerCover.smallCover(this.structureManager, relativeRotation, pos, random);
                smallCover.addChildren(this, structureStart, random);
                structureStart.addPiece(smallCover);
            }
        }
    }

    @Override
	protected void handleDataMarker(String label, BlockPos pPos, ServerLevelAccessor pLevel, RandomSource pRandom, BoundingBox pSbb) {
        // TODO spawner
        // TODO candle
        //  random: regular or skull
        //  _n / _nw / _w / _sw / _s / _se / _e / _ne
        //  random +1 or -1 increment of rotation
        // TODO chest
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext ctx, CompoundTag structureTag) {
        super.addAdditionalSaveData(ctx, structureTag);

        structureTag.putInt("square_diameter", this.squareDiameter);
        structureTag.putInt("ext_rotation", this.externalRotation.ordinal());
    }
}
