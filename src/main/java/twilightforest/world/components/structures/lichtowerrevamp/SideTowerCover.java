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
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.world.components.structures.TwilightTemplateStructurePiece;

import java.util.List;


public final class SideTowerCover extends TwilightTemplateStructurePiece {
    private static final List<String> SMALL_COVERS = ImmutableList.of(
            "cobbled_small"
    );
    private static final List<String> MEDIUM_COVERS = ImmutableList.of(
            "cobbled_medium"
    );
    private static final List<String> LARGE_COVERS = ImmutableList.of(
            "cobbled_large"
    );

    private final int width;
    @SuppressWarnings("FieldCanBeLocal") // TODO Allow this to be dynamic per piece
    private final int thickness = 2;

    public SideTowerCover(StructurePieceSerializationContext ctx, CompoundTag compoundTag) {
        super(TFStructurePieceTypes.CENTRAL_TO_SIDE_TOWER.get(), compoundTag, ctx, readSettings(compoundTag));
        this.width = compoundTag.getInt("width");
    }

    private SideTowerCover(StructureTemplateManager structureManager, Rotation rotation, String name, BlockPos startPosition, int width) {
        this(structureManager, TwilightForestMod.prefix("lich_tower/side_tower_covers/" + name), makeSettings(rotation), startPosition, width);
    }

    private SideTowerCover(StructureTemplateManager structureManager, ResourceLocation templateLocation, StructurePlaceSettings placeSettings, BlockPos startPosition, int width) {
        super(TFStructurePieceTypes.CENTRAL_TO_SIDE_TOWER.get(), 0, structureManager, templateLocation, placeSettings, startPosition);
        this.width = width;
    }

    public static SideTowerCover smallCover(StructureTemplateManager structureManager, Rotation rotation, BlockPos startPosition, RandomSource random) {
        return new SideTowerCover(structureManager, rotation, "small/" + Util.getRandom(SMALL_COVERS, random), startPosition, 5);
    }

    public static SideTowerCover mediumCover(StructureTemplateManager structureManager, Rotation rotation, BlockPos startPosition, RandomSource random) {
        return new SideTowerCover(structureManager, rotation, "medium/" + Util.getRandom(MEDIUM_COVERS, random), startPosition, 7);
    }

    public static SideTowerCover largeCover(StructureTemplateManager structureManager, Rotation rotation, BlockPos startPosition, RandomSource random) {
        return new SideTowerCover(structureManager, rotation, "large/" + Util.getRandom(LARGE_COVERS, random), startPosition, 9);
    }

    @Override
	protected void handleDataMarker(String pFunction, BlockPos pPos, ServerLevelAccessor pLevel, RandomSource pRandom, BoundingBox pSbb) {

    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext ctx, CompoundTag structureTag) {
        super.addAdditionalSaveData(ctx, structureTag);

        structureTag.putInt("width", this.width);
    }
}
