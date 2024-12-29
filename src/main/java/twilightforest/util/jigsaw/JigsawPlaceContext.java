package twilightforest.util.jigsaw;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.FrontAndTop;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.JigsawBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.jetbrains.annotations.Nullable;
import twilightforest.util.RotationUtil;

import java.util.List;
import java.util.function.UnaryOperator;

public record JigsawPlaceContext(BlockPos templatePos, StructurePlaceSettings placementSettings, JigsawRecord seedJigsaw, List<JigsawRecord> spareJigsaws, ResourceLocation templateLocation) {
	@Nullable
	public static JigsawPlaceContext pickPlaceableJunction(BlockPos parentStructureTemplatePos, BlockPos sourceJigsawPos, FrontAndTop sourceOrientation, StructureTemplateManager structureManager, @Nullable ResourceLocation templateLocation, String jigsawNameLabel, RandomSource random) {
		if (templateLocation == null)
			return null;

		List<StructureTemplate.StructureBlockInfo> connectables = JigsawUtil.readConnectableJigsaws(
			structureManager,
			templateLocation,
			new StructurePlaceSettings(),
			random
		);

		return pickPlaceableJunction(connectables, parentStructureTemplatePos.offset(sourceJigsawPos), sourceOrientation, jigsawNameLabel, random, templateLocation);
	}

	@Nullable
	private static JigsawPlaceContext pickPlaceableJunction(List<StructureTemplate.StructureBlockInfo> connectableJigsaws, BlockPos sourceTemplatePos, FrontAndTop sourceOrientation, String jigsawNameLabel, RandomSource random, ResourceLocation templateLocation) {
		StructureTemplate.StructureBlockInfo connectable = null;

		for (int i = 0; i < connectableJigsaws.size(); i++) {
			StructureTemplate.StructureBlockInfo info = connectableJigsaws.get(i);
			CompoundTag nbt = info.nbt();
			if (nbt != null && jigsawNameLabel.equals(nbt.getString("name")) && JigsawUtil.canRearrangeForConnection(sourceOrientation, info)) {
				connectable = info;
				connectableJigsaws.remove(i);
				break;
			}
		}

		if (connectable != null) {
			boolean useVertical = sourceOrientation.front().getAxis().isVertical();
			return generateAtJunction(useVertical, random, sourceTemplatePos, sourceOrientation, connectable, connectableJigsaws, templateLocation);
		}

		return null;
	}

	private static JigsawPlaceContext generateAtJunction(boolean useVertical, RandomSource random, BlockPos sourceTemplatePos, FrontAndTop sourceState, StructureTemplate.StructureBlockInfo otherJigsaw, List<StructureTemplate.StructureBlockInfo> spareJigsaws, ResourceLocation templateLocation) {
		Direction sourceFront = sourceState.front();
		BlockPos otherOffset = otherJigsaw.pos();

		if (useVertical) {
			Direction sourceTop = sourceState.top();
			Direction otherTop = JigsawBlock.getTopFacing(otherJigsaw.state());

			return getPlacement(sourceTemplatePos, otherOffset, sourceFront, RotationUtil.getRelativeRotation(otherTop, sourceTop), otherJigsaw, spareJigsaws, random, templateLocation);
		} else {
			Direction otherFront = JigsawBlock.getFrontFacing(otherJigsaw.state());

			return getPlacement(sourceTemplatePos, otherOffset, sourceFront, RotationUtil.getRelativeRotation(otherFront.getOpposite(), sourceFront), otherJigsaw, spareJigsaws, random, templateLocation);
		}
	}

	private static JigsawPlaceContext getPlacement(BlockPos centerPos, BlockPos otherOffset, Direction sourceFront, Rotation relativeRotation, StructureTemplate.StructureBlockInfo seedJigsaw, List<StructureTemplate.StructureBlockInfo> unconnectedJigsaws, RandomSource random, ResourceLocation templateLocation) {
		BlockPos placePos = centerPos.relative(sourceFront).subtract(otherOffset);

		StructurePlaceSettings placementSettings = new StructurePlaceSettings();
		placementSettings.setMirror(Mirror.NONE);
		placementSettings.setRotation(relativeRotation);
		placementSettings.setRotationPivot(otherOffset);

		// The unconnectedJigsaws list was created without StructurePlaceSettings configuration, so the list needs processing while also applying StructurePlaceSettings
		List<JigsawRecord> spareJigsaws = JigsawRecord.fromUnprocessedInfos(unconnectedJigsaws, placementSettings, random);

		return new JigsawPlaceContext(placePos, placementSettings, JigsawRecord.fromUnconfiguredJigsaw(seedJigsaw, placementSettings), spareJigsaws, templateLocation);
	}

	@Nullable
	public JigsawRecord findFirst(String name) {
		for (JigsawRecord record : this.spareJigsaws) {
			if (record.name().equals(name)) {
				return record;
			}
		}

		return null;
	}

	public BoundingBox makeBoundingBox(StructureTemplateManager manager) {
		return this.makeBoundingBox(manager.getOrCreate(this.templateLocation));
	}

	public BoundingBox makeBoundingBox(StructureTemplate template) {
		return template.getBoundingBox(this.templatePos, this.placementSettings.getRotation(), this.placementSettings.getRotationPivot(), this.placementSettings.getMirror());
	}

	public boolean isWithoutCollision(StructureTemplateManager manager, StructurePieceAccessor pieceAccessor, UnaryOperator<BoundingBox> transformer) {
		return pieceAccessor.findCollisionPiece(transformer.apply(this.makeBoundingBox(manager))) == null;
	}

	public boolean isWithoutCollision(StructureTemplateManager manager, StructurePieceAccessor pieceAccessor) {
		return this.isWithoutCollision(manager, pieceAccessor, b -> b);
	}
}
