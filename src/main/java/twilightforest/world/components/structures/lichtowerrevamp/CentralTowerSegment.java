package twilightforest.world.components.structures.lichtowerrevamp;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import twilightforest.TwilightForestMod;
import twilightforest.util.BoundingBoxUtils;
import twilightforest.world.components.processors.BoxCuttingProcessor;
import twilightforest.world.components.structures.TwilightTemplateStructurePiece;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class CentralTowerSegment extends TwilightTemplateStructurePiece {
    static final int SIDE_LENGTH = 30;
    static final int HEIGHT = 4;
    static final int ATTACHMENT_POINT_START = 6;
    static final int ATTACHMENT_POINT_RANGE = 13;

    public CentralTowerSegment(ServerLevel serverLevel, CompoundTag compoundTag) {
        super(LichTowerRevampPieces.CENTRAL_TOWER, compoundTag, serverLevel, readSettings(compoundTag).addProcessor(BoxCuttingProcessor.fromNBT(compoundTag.getList("cutouts", Tag.TAG_COMPOUND))));
    }

    public CentralTowerSegment(StructureManager structureManager, Rotation rotation, BoxCuttingProcessor sideTowerStarts, BlockPos startPosition) {
        this(structureManager, TwilightForestMod.prefix("lich_tower/central_tower"), makeSettings(rotation).addProcessor(sideTowerStarts), startPosition);
    }

    private CentralTowerSegment(StructureManager structureManager, ResourceLocation templateLocation, StructurePlaceSettings placeSettings, BlockPos startPosition) {
        super(LichTowerRevampPieces.CENTRAL_TOWER, 0, structureManager, templateLocation, placeSettings, startPosition);
    }

    @Override
    protected void handleDataMarker(String label, BlockPos pos, ServerLevelAccessor levelAccessor, Random random, BoundingBox boundingBox) {

    }

    @Override
    protected void addAdditionalSaveData(ServerLevel level, CompoundTag structureTag) {
        super.addAdditionalSaveData(level, structureTag);

        BoxCuttingProcessor cuttingProcessor = null;
        for (StructureProcessor processor : this.placeSettings.getProcessors()) {
            if (processor instanceof BoxCuttingProcessor first) {
                // Welcome to jank
                cuttingProcessor = first;
                break;
            }
        }

        if (cuttingProcessor == null) return;

        List<BoundingBox> filtering = new ArrayList<>(cuttingProcessor.cutouts);
        filtering.removeIf(bb -> bb.maxX() < this.templatePosition.getY() || bb.minY() > this.templatePosition.getY() + CentralTowerSegment.HEIGHT);

        ListTag boxTagList = new ListTag();

        for (BoundingBox box : filtering) boxTagList.add(BoundingBoxUtils.boundingBoxToNBT(box));

        structureTag.put("cutouts", boxTagList);
    }
}
