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
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.jetbrains.annotations.Nullable;
import twilightforest.util.RotationUtil;

import java.util.List;

public record JigsawPlaceContext(BlockPos templatePos, StructurePlaceSettings placementSettings, JigsawRecord seedJigsaw, List<JigsawRecord> spareJigsaws) {
	@Nullable
	public static JigsawPlaceContext pickPlaceableJunction(BlockPos parentStructureTemplatePos, BlockPos sourceJigsawPos, FrontAndTop sourceOrientation, StructureTemplateManager structureManager, @Nullable ResourceLocation templateLocation, String jigsawLabel, RandomSource random) {
		if (templateLocation == null)
			return null;

		List<StructureTemplate.StructureBlockInfo> connectables = JigsawUtil.readConnectableJigsaws(
			structureManager,
			templateLocation,
			new StructurePlaceSettings(),
			random
		);

		return pickPlaceableJunction(
			connectables, parentStructureTemplatePos, sourceOrientation, sourceJigsawPos, jigsawLabel, random
		);
	}

	@Nullable
	private static JigsawPlaceContext pickPlaceableJunction(List<StructureTemplate.StructureBlockInfo> connectableJigsaws, BlockPos sourceTemplatePos, FrontAndTop sourceOrientation, BlockPos sourceJigsawPos, String jigsawLabel, RandomSource random) {
		StructureTemplate.StructureBlockInfo connectable = null;

		for (int i = 0; i < connectableJigsaws.size(); i++) {
			StructureTemplate.StructureBlockInfo info = connectableJigsaws.get(i);
			CompoundTag nbt = info.nbt();
			if (nbt != null && jigsawLabel.equals(nbt.getString("name")) && JigsawUtil.canRearrangeForConnection(sourceOrientation, info)) {
				connectable = info;
				connectableJigsaws.remove(i);
				break;
			}
		}

		if (connectable != null) {
			boolean useVertical = sourceOrientation.front().getAxis().isVertical();
			return generateAtJunction(useVertical, random, sourceTemplatePos, sourceOrientation, sourceJigsawPos, connectable, connectableJigsaws);
		}

		return null;
	}

	private static JigsawPlaceContext generateAtJunction(boolean useVertical, RandomSource random, BlockPos sourceTemplatePos, FrontAndTop sourceState, BlockPos sourceJigsawPos, StructureTemplate.StructureBlockInfo otherJigsaw, List<StructureTemplate.StructureBlockInfo> spareJigsaws) {
		Direction sourceFront = sourceState.front();
		BlockPos otherOffset = otherJigsaw.pos();

		if (useVertical) {
			Direction sourceTop = sourceState.top();
			Direction otherTop = JigsawBlock.getTopFacing(otherJigsaw.state());

			return getPlacement(sourceTemplatePos, otherOffset, sourceFront, RotationUtil.getRelativeRotation(otherTop, sourceTop), sourceJigsawPos, otherJigsaw, spareJigsaws, random);
		} else {
			Direction otherFront = JigsawBlock.getFrontFacing(otherJigsaw.state());

			return getPlacement(sourceTemplatePos, otherOffset, sourceFront, RotationUtil.getRelativeRotation(otherFront.getOpposite(), sourceFront), sourceJigsawPos, otherJigsaw, spareJigsaws, random);
		}
	}

	private static JigsawPlaceContext getPlacement(BlockPos sourceTemplatePos, BlockPos otherOffset, Direction sourceFront, Rotation relativeRotation, BlockPos sourceJigsawPos, StructureTemplate.StructureBlockInfo seedJigsaw, List<StructureTemplate.StructureBlockInfo> unconnectedJigsaws, RandomSource random) {
		BlockPos placePos = sourceTemplatePos.offset(sourceJigsawPos).relative(sourceFront).subtract(otherOffset);

		StructurePlaceSettings placementSettings = new StructurePlaceSettings();
		placementSettings.setMirror(Mirror.NONE);
		placementSettings.setRotation(relativeRotation);
		placementSettings.setRotationPivot(otherOffset);

		// The unconnectedJigsaws list was created without StructurePlaceSettings configuration, so the list needs processing while also applying StructurePlaceSettings
		List<JigsawRecord> spareJigsaws = JigsawRecord.fromUnprocessedInfos(unconnectedJigsaws, placementSettings, random);

		return new JigsawPlaceContext(placePos, placementSettings, JigsawRecord.fromUnconfiguredJigsaw(seedJigsaw, placementSettings), spareJigsaws);
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

	public BoundingBox makeBoundingBox(StructureTemplate template) {
		return template.getBoundingBox(this.templatePos, this.placementSettings.getRotation(), this.placementSettings.getRotationPivot(), this.placementSettings.getMirror());
	}
}
