package twilightforest.world.components.structures;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

/**
 * Copied a few things from {@link net.minecraft.world.level.levelgen.structure.TemplateStructurePiece}
 */
@Deprecated // Should extend TemplateStructurePiece instead
public abstract class TFStructureComponentTemplate extends TFStructureComponent {

	protected final StructurePlaceSettings placeSettings = new StructurePlaceSettings().addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
	protected BlockPos templatePosition;
	protected BlockPos rotatedPosition;
	protected StructureTemplate TEMPLATE;
	public Runnable LAZY_TEMPLATE_LOADER;

	public TFStructureComponentTemplate(StructurePieceSerializationContext ctx, StructurePieceType piece, CompoundTag nbt) {
		super(piece, nbt);
		this.templatePosition = new BlockPos(nbt.getInt("TPX"), nbt.getInt("TPY"), nbt.getInt("TPZ"));
		this.placeSettings.setRotation(this.rotation);
		LAZY_TEMPLATE_LOADER = () -> setup(ctx.structureTemplateManager());
	}

	public TFStructureComponentTemplate(StructurePieceType type, int i, int x, int y, int z, BoundingBox boundingBox) {
		super(type, i, boundingBox);
		this.mirror = Mirror.NONE;
		this.templatePosition = new BlockPos(x, y, z);
	}

	@Deprecated
	public TFStructureComponentTemplate(StructureTemplateManager manager, StructurePieceType type, int i, int x, int y, int z, Rotation rotation) {
		super(type, i, new BoundingBox(x, y, z, x, y, z));
		this.rotation = rotation;
		this.mirror = Mirror.NONE;
		this.placeSettings.setRotation(rotation);
		this.templatePosition = new BlockPos(x, y, z);
		LAZY_TEMPLATE_LOADER = () -> setup(manager);
	}

	public final void setup(StructureTemplateManager templateManager) {
		loadTemplates(templateManager);
		setModifiedTemplatePositionFromRotation();
		setBoundingBoxFromTemplate(rotatedPosition);
	}

	protected abstract void loadTemplates(StructureTemplateManager templateManager);

	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext ctx, CompoundTag tagCompound) {
		super.addAdditionalSaveData(ctx, tagCompound);
		tagCompound.putInt("TPX", this.templatePosition.getX());
		tagCompound.putInt("TPY", this.templatePosition.getY());
		tagCompound.putInt("TPZ", this.templatePosition.getZ());
	}

	protected final void setModifiedTemplatePositionFromRotation() {
		Rotation rotation = this.placeSettings.getRotation();
		Vec3i size = this.TEMPLATE.getSize(rotation);

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
		Vec3i size = this.TEMPLATE.getSize(rotation);
		Mirror mirror = this.placeSettings.getMirror();
		this.boundingBox = new BoundingBox(0, 0, 0, size.getX(), size.getY() - 1, size.getZ());

		switch (rotation) {
			case CLOCKWISE_90 -> this.boundingBox.move(-size.getX(), 0, 0);
			case COUNTERCLOCKWISE_90 -> this.boundingBox.move(0, 0, -size.getZ());
			case CLOCKWISE_180 -> this.boundingBox.move(-size.getX(), 0, -size.getZ());
			default -> {
			}
		}

		switch (mirror) {
			case FRONT_BACK -> {
				BlockPos blockpos2 = BlockPos.ZERO;
				if (rotation != Rotation.CLOCKWISE_90 && rotation != Rotation.COUNTERCLOCKWISE_90) {
					if (rotation == Rotation.CLOCKWISE_180) {
						blockpos2 = blockpos2.relative(Direction.EAST, size.getX());
					} else {
						blockpos2 = blockpos2.relative(Direction.WEST, size.getX());
					}
				} else {
					blockpos2 = blockpos2.relative(rotation.rotate(Direction.WEST), size.getZ());
				}
				this.boundingBox.move(blockpos2.getX(), 0, blockpos2.getZ());
			}
			case LEFT_RIGHT -> {
				BlockPos blockpos1 = BlockPos.ZERO;
				if (rotation != Rotation.CLOCKWISE_90 && rotation != Rotation.COUNTERCLOCKWISE_90) {
					if (rotation == Rotation.CLOCKWISE_180) {
						blockpos1 = blockpos1.relative(Direction.SOUTH, size.getZ());
					} else {
						blockpos1 = blockpos1.relative(Direction.NORTH, size.getZ());
					}
				} else {
					blockpos1 = blockpos1.relative(rotation.rotate(Direction.NORTH), size.getX());
				}
				this.boundingBox.move(blockpos1.getX(), 0, blockpos1.getZ());
			}
			default -> {
			}
		}

		this.boundingBox.move(pos.getX(), pos.getY(), pos.getZ());
	}

	@Deprecated
	protected final void setTemplatePositionFromRotation() {
		Rotation rotation = this.placeSettings.getRotation();
		Vec3i size = this.TEMPLATE.getSize(rotation);

		if (rotation == Rotation.CLOCKWISE_90 || rotation == Rotation.CLOCKWISE_180)
			this.templatePosition = this.templatePosition.east(size.getZ() - 1);

		if (rotation == Rotation.CLOCKWISE_180 || rotation == Rotation.COUNTERCLOCKWISE_90)
			this.templatePosition = this.templatePosition.south(size.getX() - 1);
	}
}
