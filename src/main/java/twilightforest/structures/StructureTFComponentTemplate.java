package twilightforest.structures;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;
import twilightforest.TFFeature;

/**
 * Copied a few things from {@link net.minecraft.world.gen.feature.structure.TemplateStructurePiece}
 */
public abstract class StructureTFComponentTemplate extends StructureTFComponent {

    protected PlacementSettings placeSettings = new PlacementSettings().addProcessor(BlockIgnoreStructureProcessor.STRUCTURE_BLOCK);
    protected BlockPos templatePosition = BlockPos.ZERO;
    protected BlockPos rotatedPosition;
    protected Template TEMPLATE;
    public Runnable LAZY_TEMPLATE_LOADER;

    public StructureTFComponentTemplate(TemplateManager manager, IStructurePieceType piece, CompoundNBT nbt) {
        super(piece, nbt);
        this.templatePosition = new BlockPos(nbt.getInt("TPX"), nbt.getInt("TPY"), nbt.getInt("TPZ"));
        this.placeSettings.setRotation(this.rotation);
		LAZY_TEMPLATE_LOADER = () -> setup(manager);
    }

    public StructureTFComponentTemplate(IStructurePieceType type, TFFeature feature, int i, int x, int y, int z, Rotation rotation) {
        super(type, i);
        this.feature = feature;
        this.rotation = rotation;
        this.mirror = Mirror.NONE;
        this.placeSettings.setRotation(rotation);
        this.templatePosition = new BlockPos(x, y, z);
        this.boundingBox = new MutableBoundingBox(x, y, z, x, y, z);
    }

    //TODO: Unused. Remove?
    public StructureTFComponentTemplate(IStructurePieceType type, TFFeature feature, int i, int x, int y, int z, Rotation rotation, Mirror mirror) {
        super(type, i);
        this.feature = feature;
        this.rotation = rotation;
        this.mirror = mirror;
        this.placeSettings.setRotation(rotation);
        this.templatePosition = new BlockPos(x, y, z);
        this.boundingBox = new MutableBoundingBox(x, y, z, x, y, z);
    }

    public final void setup(TemplateManager templateManager) {
        loadTemplates(templateManager);
        setModifiedTemplatePositionFromRotation();
        setBoundingBoxFromTemplate(rotatedPosition);
    }

    protected abstract void loadTemplates(TemplateManager templateManager);

    @Override
    protected void readAdditional(CompoundNBT tagCompound) {
        super.readAdditional(tagCompound);
        tagCompound.putInt("TPX", this.templatePosition.getX());
        tagCompound.putInt("TPY", this.templatePosition.getY());
        tagCompound.putInt("TPZ", this.templatePosition.getZ());
	}

    protected final void setModifiedTemplatePositionFromRotation() {

        Rotation rotation = this.placeSettings.getRotation();
        BlockPos size = this.TEMPLATE.transformedSize(rotation);

        rotatedPosition = new BlockPos(this.templatePosition);

        if (rotation == Rotation.CLOCKWISE_90 || rotation == Rotation.CLOCKWISE_180) {
            rotatedPosition = rotatedPosition.east(size.getZ() - 1);
        }

        if (rotation == Rotation.CLOCKWISE_180 || rotation == Rotation.COUNTERCLOCKWISE_90) {
            rotatedPosition = rotatedPosition.south(size.getX() - 1);
        }
    }

    protected final void setBoundingBoxFromTemplate(BlockPos pos) {
        Rotation rotation = this.placeSettings.getRotation();
        BlockPos size = this.TEMPLATE.transformedSize(rotation);
        Mirror mirror = this.placeSettings.getMirror();
        this.boundingBox = new MutableBoundingBox(0, 0, 0, size.getX(), size.getY() - 1, size.getZ());

        switch (rotation) {
            case NONE:
            default:
                break;
            case CLOCKWISE_90:
                this.boundingBox.offset(-size.getX(), 0, 0);
                break;
            case COUNTERCLOCKWISE_90:
                this.boundingBox.offset(0, 0, -size.getZ());
                break;
            case CLOCKWISE_180:
                this.boundingBox.offset(-size.getX(), 0, -size.getZ());
        }

        switch (mirror) {
            case NONE:
            default:
                break;
            case FRONT_BACK:
                BlockPos blockpos2 = BlockPos.ZERO;

                if (rotation != Rotation.CLOCKWISE_90 && rotation != Rotation.COUNTERCLOCKWISE_90) {
                    if (rotation == Rotation.CLOCKWISE_180) {
                        blockpos2 = blockpos2.offset(Direction.EAST, size.getX());
                    } else {
                        blockpos2 = blockpos2.offset(Direction.WEST, size.getX());
                    }
                } else {
                    blockpos2 = blockpos2.offset(rotation.rotate(Direction.WEST), size.getZ());
                }

                this.boundingBox.offset(blockpos2.getX(), 0, blockpos2.getZ());
                break;
            case LEFT_RIGHT:
                BlockPos blockpos1 = BlockPos.ZERO;

                if (rotation != Rotation.CLOCKWISE_90 && rotation != Rotation.COUNTERCLOCKWISE_90) {
                    if (rotation == Rotation.CLOCKWISE_180) {
                        blockpos1 = blockpos1.offset(Direction.SOUTH, size.getZ());
                    } else {
                        blockpos1 = blockpos1.offset(Direction.NORTH, size.getZ());
                    }
                } else {
                    blockpos1 = blockpos1.offset(rotation.rotate(Direction.NORTH), size.getX());
                }

                this.boundingBox.offset(blockpos1.getX(), 0, blockpos1.getZ());
        }

        this.boundingBox.offset(pos.getX(), pos.getY(), pos.getZ());
    }

    @Deprecated
    protected final void setTemplatePositionFromRotation() {
        Rotation rotation = this.placeSettings.getRotation();
        BlockPos size = this.TEMPLATE.transformedSize(rotation);

        if (rotation == Rotation.CLOCKWISE_90 || rotation == Rotation.CLOCKWISE_180)
            this.templatePosition = this.templatePosition.east(size.getZ()-1);

        if (rotation == Rotation.CLOCKWISE_180 || rotation == Rotation.COUNTERCLOCKWISE_90)
            this.templatePosition = this.templatePosition.south(size.getX()-1);
    }

    @Deprecated
    protected final void setBoundingBoxFromTemplate() {
        Rotation rotation = this.placeSettings.getRotation();
        BlockPos size = this.TEMPLATE.transformedSize(rotation);
        Mirror mirror = this.placeSettings.getMirror();
        this.boundingBox = new MutableBoundingBox(0, 0, 0, size.getX(), size.getY() - 1, size.getZ());

        switch (rotation) {
            case NONE:
            default:
                break;
            case CLOCKWISE_90:
                this.boundingBox.offset(-size.getX(), 0, 0);
                break;
            case COUNTERCLOCKWISE_90:
                this.boundingBox.offset(0, 0, -size.getZ());
                break;
            case CLOCKWISE_180:
                this.boundingBox.offset(-size.getX(), 0, -size.getZ());
        }

        switch (mirror) {
            case NONE:
            default:
                break;
            case FRONT_BACK:
                BlockPos blockpos2 = BlockPos.ZERO;

                if (rotation != Rotation.CLOCKWISE_90 && rotation != Rotation.COUNTERCLOCKWISE_90) {
                    if (rotation == Rotation.CLOCKWISE_180) {
                        blockpos2 = blockpos2.offset(Direction.EAST, size.getX());
                    } else {
                        blockpos2 = blockpos2.offset(Direction.WEST, size.getX());
                    }
                } else {
                    blockpos2 = blockpos2.offset(rotation.rotate(Direction.WEST), size.getZ());
                }

                this.boundingBox.offset(blockpos2.getX(), 0, blockpos2.getZ());
                break;
            case LEFT_RIGHT:
                BlockPos blockpos1 = BlockPos.ZERO;

                if (rotation != Rotation.CLOCKWISE_90 && rotation != Rotation.COUNTERCLOCKWISE_90) {
                    if (rotation == Rotation.CLOCKWISE_180) {
                        blockpos1 = blockpos1.offset(Direction.SOUTH, size.getZ());
                    } else {
                        blockpos1 = blockpos1.offset(Direction.NORTH, size.getZ());
                    }
                } else {
                    blockpos1 = blockpos1.offset(rotation.rotate(Direction.NORTH), size.getX());
                }

                this.boundingBox.offset(blockpos1.getX(), 0, blockpos1.getZ());
        }

        this.boundingBox.offset(this.templatePosition.getX(), this.templatePosition.getY(), this.templatePosition.getZ());
    }
}
