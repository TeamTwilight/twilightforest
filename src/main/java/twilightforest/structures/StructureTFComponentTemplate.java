package twilightforest.structures;

import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;
import twilightforest.TFFeature;

/**

 Copied a few things from net.minecraft.world.gen.structure.StructureComponentTemplate.java

 */

public abstract class StructureTFComponentTemplate extends StructureTFComponent {
    protected PlacementSettings placeSettings = new PlacementSettings().setReplacedBlock(Blocks.STRUCTURE_VOID);
    protected BlockPos templatePosition = BlockPos.ORIGIN;
    protected Template TEMPLATE;

    public StructureTFComponentTemplate() {
        super();
        this.rotation = Rotation.NONE;
        this.mirror = Mirror.NONE;
    }

    public StructureTFComponentTemplate(TFFeature feature, int i, int x, int y, int z, Rotation rotation) {
        super(i);
        this.feature = feature;
        this.rotation = rotation;
        this.mirror = Mirror.NONE;
        this.placeSettings.setRotation(rotation);
        this.templatePosition = new BlockPos(x, y, z);
        this.boundingBox = new StructureBoundingBox(x, y, z, x, y, z);
    }

    @Override
    protected void writeStructureToNBT(NBTTagCompound tagCompound) {
        super.writeStructureToNBT(tagCompound);
        tagCompound.setInteger("TPX", this.templatePosition.getX());
        tagCompound.setInteger("TPY", this.templatePosition.getY());
        tagCompound.setInteger("TPZ", this.templatePosition.getZ());
    }

    @Override
    protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager manager) {
        super.readStructureFromNBT(tagCompound, manager);
        this.templatePosition = new BlockPos(tagCompound.getInteger("TPX"), tagCompound.getInteger("TPY"), tagCompound.getInteger("TPZ"));
    }

    protected final BlockPos getModifiedTemplatePositionFromRotation() {
        Rotation rotation = this.placeSettings.getRotation();
        BlockPos size = this.TEMPLATE.transformedSize(rotation);
        BlockPos toReturn = new BlockPos(this.templatePosition.getX(), this.templatePosition.getY(), this.templatePosition.getZ());

        if (rotation == Rotation.CLOCKWISE_90 || rotation == Rotation.CLOCKWISE_180)
            toReturn = toReturn.east(size.getZ()-1);

        if (rotation == Rotation.CLOCKWISE_180 || rotation == Rotation.COUNTERCLOCKWISE_90)
            toReturn = toReturn.south(size.getX()-1);

        return toReturn;
    }

    protected final void setBoundingBoxFromTemplate(BlockPos pos) {
        Rotation rotation = this.placeSettings.getRotation();
        BlockPos size = this.TEMPLATE.transformedSize(rotation);
        Mirror mirror = this.placeSettings.getMirror();
        this.boundingBox = new StructureBoundingBox(0, 0, 0, size.getX(), size.getY() - 1, size.getZ());

        switch (rotation)
        {
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

        switch (mirror)
        {
            case NONE:
            default:
                break;
            case FRONT_BACK:
                BlockPos blockpos2 = BlockPos.ORIGIN;

                if (rotation != Rotation.CLOCKWISE_90 && rotation != Rotation.COUNTERCLOCKWISE_90)
                {
                    if (rotation == Rotation.CLOCKWISE_180)
                    {
                        blockpos2 = blockpos2.offset(EnumFacing.EAST, size.getX());
                    }
                    else
                    {
                        blockpos2 = blockpos2.offset(EnumFacing.WEST, size.getX());
                    }
                }
                else
                {
                    blockpos2 = blockpos2.offset(rotation.rotate(EnumFacing.WEST), size.getZ());
                }

                this.boundingBox.offset(blockpos2.getX(), 0, blockpos2.getZ());
                break;
            case LEFT_RIGHT:
                BlockPos blockpos1 = BlockPos.ORIGIN;

                if (rotation != Rotation.CLOCKWISE_90 && rotation != Rotation.COUNTERCLOCKWISE_90)
                {
                    if (rotation == Rotation.CLOCKWISE_180)
                    {
                        blockpos1 = blockpos1.offset(EnumFacing.SOUTH, size.getZ());
                    }
                    else
                    {
                        blockpos1 = blockpos1.offset(EnumFacing.NORTH, size.getZ());
                    }
                }
                else
                {
                    blockpos1 = blockpos1.offset(rotation.rotate(EnumFacing.NORTH), size.getX());
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
        this.boundingBox = new StructureBoundingBox(0, 0, 0, size.getX(), size.getY() - 1, size.getZ());

        switch (rotation)
        {
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

        switch (mirror)
        {
            case NONE:
            default:
                break;
            case FRONT_BACK:
                BlockPos blockpos2 = BlockPos.ORIGIN;

                if (rotation != Rotation.CLOCKWISE_90 && rotation != Rotation.COUNTERCLOCKWISE_90)
                {
                    if (rotation == Rotation.CLOCKWISE_180)
                    {
                        blockpos2 = blockpos2.offset(EnumFacing.EAST, size.getX());
                    }
                    else
                    {
                        blockpos2 = blockpos2.offset(EnumFacing.WEST, size.getX());
                    }
                }
                else
                {
                    blockpos2 = blockpos2.offset(rotation.rotate(EnumFacing.WEST), size.getZ());
                }

                this.boundingBox.offset(blockpos2.getX(), 0, blockpos2.getZ());
                break;
            case LEFT_RIGHT:
                BlockPos blockpos1 = BlockPos.ORIGIN;

                if (rotation != Rotation.CLOCKWISE_90 && rotation != Rotation.COUNTERCLOCKWISE_90)
                {
                    if (rotation == Rotation.CLOCKWISE_180)
                    {
                        blockpos1 = blockpos1.offset(EnumFacing.SOUTH, size.getZ());
                    }
                    else
                    {
                        blockpos1 = blockpos1.offset(EnumFacing.NORTH, size.getZ());
                    }
                }
                else
                {
                    blockpos1 = blockpos1.offset(rotation.rotate(EnumFacing.NORTH), size.getX());
                }

                this.boundingBox.offset(blockpos1.getX(), 0, blockpos1.getZ());
        }

        this.boundingBox.offset(this.templatePosition.getX(), this.templatePosition.getY(), this.templatePosition.getZ());
    }
}
