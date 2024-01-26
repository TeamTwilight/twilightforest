package twilightforest.world.components.structures.lichtowerrevamp;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import twilightforest.world.components.structures.TwilightTemplateStructurePiece;


public final class CentralTowerAttachment extends TwilightTemplateStructurePiece {
    private final int width;
    @SuppressWarnings("FieldCanBeLocal") // TODO Allow this to be dynamic per piece
    private final int length = 2; // Determines how far out the piece should gen

    public CentralTowerAttachment(StructurePieceSerializationContext ctx, CompoundTag compoundTag) {
        super(TFStructurePieceTypes.CENTRAL_TO_SIDE_TOWER.get(), compoundTag, ctx, TwilightTemplateStructurePiece.readSettings(compoundTag));
        this.width = compoundTag.getInt("width");
    }

    private CentralTowerAttachment(StructureTemplateManager structureManager, Rotation rotation, String name, BlockPos startPosition, int width) {
        this(structureManager, TwilightForestMod.prefix("lich_tower/attachments/central/" + name), makeSettings(rotation), startPosition.relative(rotation.rotate(Direction.EAST), -(width - 5 >> 1)), width);
    }

    private CentralTowerAttachment(StructureTemplateManager structureManager, ResourceLocation templateLocation, StructurePlaceSettings placeSettings, BlockPos startPosition, int width) {
        super(TFStructurePieceTypes.CENTRAL_TO_SIDE_TOWER.get(), 0, structureManager, templateLocation, placeSettings, startPosition);
        this.width = width;
    }

    public static CentralTowerAttachment startRandomAttachment(StructureTemplateManager structureManager, Rotation rotation, BlockPos startPosition, RandomSource random) {
        float weight = random.nextFloat() * random.nextFloat();

        if (weight < 1/3f)
            return smallAttachment(structureManager, rotation, startPosition);

        if (weight < 2/3f)
            return mediumAttachment(structureManager, rotation, startPosition);

        return largeAttachment(structureManager, rotation, startPosition);
    }

    public static CentralTowerAttachment smallAttachment(StructureTemplateManager structureManager, Rotation rotation, BlockPos startPosition) {
        return new CentralTowerAttachment(structureManager, rotation, "central_to_small", startPosition, 5);
    }

    public static CentralTowerAttachment mediumAttachment(StructureTemplateManager structureManager, Rotation rotation, BlockPos startPosition) {
        return new CentralTowerAttachment(structureManager, rotation, "central_to_medium", startPosition, 7);
    }

    public static CentralTowerAttachment largeAttachment(StructureTemplateManager structureManager, Rotation rotation, BlockPos startPosition) {
        return new CentralTowerAttachment(structureManager, rotation, "central_to_large", startPosition, 9);
    }

    @Override
    public void addChildren(StructurePiece parent, StructurePieceAccessor structureStart, RandomSource random) {
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
    protected void handleDataMarker(String label, BlockPos pos, ServerLevelAccessor levelAccessor, RandomSource random, BoundingBox boundingBox) {

    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext ctx, CompoundTag structureTag) {
        super.addAdditionalSaveData(ctx, structureTag);

        structureTag.putInt("width", this.width);
    }
}
