package twilightforest.util.jigsaw;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.FrontAndTop;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.JigsawBlock;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class JigsawUtil {
	public static Direction getAbsoluteHorizontal(FrontAndTop orientation) {
		if (orientation.front().getAxis() == Direction.Axis.Y) {
			return orientation.top();
		} else {
			return orientation.front();
		}
	}

	public static FrontAndTop process(FrontAndTop source, StructurePlaceSettings settings) {
		return settings.getRotation().rotation().rotate(source);
	}

	// Determines if other Jigsaw orientation can be rotated in order to match the source Jigsaw orientation
	public static boolean canRearrangeForConnection(FrontAndTop sourceOrientation, StructureTemplate.StructureBlockInfo otherJigsaw) {
		FrontAndTop otherOrientation = otherJigsaw.state().getValue(JigsawBlock.ORIENTATION);

		boolean frontFacesAlignable = canBeRotatedToAlign(sourceOrientation.front(), otherOrientation.front());
		boolean topFacesAlignable = canBeRotatedToAlign(sourceOrientation.top().getOpposite(), otherOrientation.top());

		return frontFacesAlignable && topFacesAlignable;
	}

	private static boolean canBeRotatedToAlign(Direction source, Direction target) {
		Direction.Plane sourcePlane = source.getAxis().getPlane();
		boolean planesMatch = sourcePlane == target.getAxis().getPlane();

		if (sourcePlane == Direction.Plane.VERTICAL) {
			return planesMatch && source.getOpposite() == target; // Cannot vertically flip templates
		} else {
			return planesMatch; // If both Jigsaws are horizontal, then they can simply be rotated
		}
	}

	public static List<StructureTemplate.StructureBlockInfo> readConnectableJigsaws(StructureTemplateManager manager, ResourceLocation templateLocation, StructurePlaceSettings settings, @Nullable RandomSource random) {
		return readConnectableJigsaws(manager.getOrCreate(templateLocation), settings, random);
	}

	public static List<StructureTemplate.StructureBlockInfo> readConnectableJigsaws(@Nullable StructureTemplate template, StructurePlaceSettings settings, @Nullable RandomSource random) {
		if (template == null || BlockPos.ZERO.equals(template.getSize())) {
			return List.of();
		}

		List<StructureTemplate.StructureBlockInfo> returnables = template.filterBlocks(BlockPos.ZERO, settings, Blocks.JIGSAW);

		if (random != null) {
			Util.shuffle(returnables, random);
			// "Stable" sorting - preserves order of "equal" priorities, as arranged by prior shuffling.
			SinglePoolElement.sortBySelectionPriority(returnables);
		}

		return returnables;
	}
}
