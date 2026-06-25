package twilightforest.util.jigsaw;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.FrontAndTop;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Util;
import net.minecraft.world.level.block.JigsawBlock;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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
return planesMatch && source.getOpposite() == target;
} else {
return planesMatch;
}
}
public static List<StructureTemplate.StructureBlockInfo> readConnectableJigsaws(StructureTemplateManager manager, Identifier templateLocation, StructurePlaceSettings settings, @Nullable RandomSource random) {
return readConnectableJigsaws(manager.getOrCreate(templateLocation), settings, random);
}
public static List<StructureTemplate.StructureBlockInfo> readConnectableJigsaws(@Nullable StructureTemplate template, StructurePlaceSettings settings, @Nullable RandomSource random) {
if (template == null || BlockPos.ZERO.equals(template.getSize())) {
return List.of();
}
List<StructureTemplate.JigsawBlockInfo> jigsaws = template.getJigsaws(BlockPos.ZERO, settings.getRotation());
if (random != null) {
Util.shuffle(jigsaws, random);
SinglePoolElement.sortBySelectionPriority(jigsaws);
}
return jigsaws.stream().map(StructureTemplate.JigsawBlockInfo::info).collect(Collectors.toCollection(ArrayList::new));
}
}