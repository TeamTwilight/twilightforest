package twilightforest.world.components.structures.lichtowerrevamp;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.structures.TwilightTemplateStructurePiece;

import java.util.Random;

public final class CentralTowerAttachment extends TwilightTemplateStructurePiece {
    private final int width;
    @SuppressWarnings("FieldCanBeLocal") // TODO Allow this to be dynamic per piece
    private final int length = 2; // Determines how far out the piece should gen

    public CentralTowerAttachment(ServerLevel serverLevel, CompoundTag compoundTag) {
        super(LichTowerRevampPieces.CENTRAL_TO_SIDE_TOWER, compoundTag, serverLevel, TwilightTemplateStructurePiece.readSettings(compoundTag));
        this.width = compoundTag.getInt("width");
    }

    private CentralTowerAttachment(StructureManager structureManager, Rotation rotation, String name, BlockPos startPosition, int width) {
        this(structureManager, TwilightForestMod.prefix("lich_tower/attachments/central/" + name), makeSettings(rotation), startPosition.relative(rotation.rotate(Direction.EAST), -(width - 5 >> 1)), width);
    }

    private CentralTowerAttachment(StructureManager structureManager, ResourceLocation templateLocation, StructurePlaceSettings placeSettings, BlockPos startPosition, int width) {
        super(LichTowerRevampPieces.CENTRAL_TO_SIDE_TOWER, 0, structureManager, templateLocation, placeSettings, startPosition);
        this.width = width;
    }

    public static CentralTowerAttachment startRandomAttachment(StructureManager structureManager, Rotation rotation, BlockPos startPosition, Random random) {
        float weight = random.nextFloat() * random.nextFloat();

        if (weight < 1/3f)
            return smallAttachment(structureManager, rotation, startPosition);

        if (weight < 2/3f)
            return mediumAttachment(structureManager, rotation, startPosition);

        return largeAttachment(structureManager, rotation, startPosition);
    }

    public static CentralTowerAttachment smallAttachment(StructureManager structureManager, Rotation rotation, BlockPos startPosition) {
        return new CentralTowerAttachment(structureManager, rotation, "central_to_small", startPosition, 5);
    }

    public static CentralTowerAttachment mediumAttachment(StructureManager structureManager, Rotation rotation, BlockPos startPosition) {
        return new CentralTowerAttachment(structureManager, rotation, "central_to_medium", startPosition, 7);
    }

    public static CentralTowerAttachment largeAttachment(StructureManager structureManager, Rotation rotation, BlockPos startPosition) {
        return new CentralTowerAttachment(structureManager, rotation, "central_to_large", startPosition, 9);
    }

    @Override
    public void addChildren(StructurePiece parent, StructurePieceAccessor structureStart, Random random) {
        super.addChildren(parent, structureStart, random);

        Direction dir = this.rotation.rotate(Direction.SOUTH);
        BlockPos placement = this.templatePosition.offset(dir.getStepX() * this.length, 1, dir.getStepZ() * this.length);

        switch (this.width) {
            case 9 -> {
                SideTowerRoom largeRoom = SideTowerRoom.largeRoom(this.structureManager, this.rotation, placement, random);
                largeRoom.addChildren(this, structureStart, random);
                structureStart.addPiece(largeRoom);
            }
            case 7 -> {
                SideTowerRoom mediumRoom = SideTowerRoom.mediumRoom(this.structureManager, this.rotation, placement, random);
                mediumRoom.addChildren(this, structureStart, random);
                structureStart.addPiece(mediumRoom);
            }
            case 5 -> {
                SideTowerRoom smallRoom = SideTowerRoom.smallRoom(this.structureManager, this.rotation, placement, random);
                smallRoom.addChildren(this, structureStart, random);
                structureStart.addPiece(smallRoom);
            }
        }
    }

    @Override
    protected void handleDataMarker(String label, BlockPos pos, ServerLevelAccessor levelAccessor, Random random, BoundingBox boundingBox) {

    }

    @Override
    protected void addAdditionalSaveData(ServerLevel level, CompoundTag structureTag) {
        super.addAdditionalSaveData(level, structureTag);

        structureTag.putInt("width", this.width);
    }
}
