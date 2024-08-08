package twilightforest.util.jigsaw;

import net.minecraft.Optionull;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.FrontAndTop;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.JigsawBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public record JigsawRecord(int priority, FrontAndTop orientation, BlockPos pos, String name, String target) {
	public static List<JigsawRecord> allFromTemplate(StructureTemplateManager structureManager, ResourceLocation templateLocation, StructurePlaceSettings placeSettings) {
		// StructureTemplate#filterBlocks() does not support mirroring, force NONE
		placeSettings.setMirror(Mirror.NONE);
		return structureManager.getOrCreate(templateLocation).filterBlocks(BlockPos.ZERO, placeSettings, Blocks.JIGSAW).stream().map(JigsawRecord::fromJigsawBlock).toList();
	}

	public static List<JigsawRecord> fromUnprocessedInfos(List<StructureTemplate.StructureBlockInfo> infos, StructurePlaceSettings settings, RandomSource random) {
		List<JigsawRecord> list = new ArrayList<>();
		for (StructureTemplate.StructureBlockInfo info : infos) {
			JigsawRecord jigsawRecord = fromUnconfiguredJigsaw(info, settings);
			list.add(jigsawRecord);
		}

		Util.shuffle(list, random);
		// "Stable" sorting - preserves order of "equal" priorities, as arranged by prior shuffling.
		list.sort(Comparator.comparingInt(JigsawRecord::priority).reversed());

		return list;
	}

	public static JigsawRecord fromUnconfiguredJigsaw(StructureTemplate.StructureBlockInfo info, StructurePlaceSettings settings) {
		return new JigsawRecord(
			Optionull.mapOrDefault(info.nbt(), tag -> tag.getInt("selection_priority"), 0),
			JigsawUtil.process(info.state().getValue(JigsawBlock.ORIENTATION), settings),
			StructureTemplate.calculateRelativePosition(settings, info.pos()),
			Optionull.mapOrDefault(info.nbt(), tag -> tag.getString("name"), ""),
			Optionull.mapOrDefault(info.nbt(), tag -> tag.getString("target"), "")
		);
	}

	public static JigsawRecord fromJigsawBlock(StructureTemplate.StructureBlockInfo info) {
		return new JigsawRecord(
			Optionull.mapOrDefault(info.nbt(), tag -> tag.getInt("selection_priority"), 0),
			info.state().getValue(JigsawBlock.ORIENTATION),
			info.pos(),
			Optionull.mapOrDefault(info.nbt(), tag -> tag.getString("name"), ""),
			Optionull.mapOrDefault(info.nbt(), tag -> tag.getString("target"), "")
		);
	}

	public static JigsawRecord fromTag(CompoundTag tag) {
		return new JigsawRecord(
			tag.getInt("priority"),
			FrontAndTop.values()[tag.getInt("facing")],
			new BlockPos(tag.getInt("x"), tag.getInt("y"), tag.getInt("z")),
			tag.getString("name"),
			tag.getString("target")
		);
	}

	public CompoundTag toTag() {
		CompoundTag ret = new CompoundTag();

		ret.putInt("priority", this.priority);
		ret.putInt("facing", this.orientation.ordinal());
		ret.putInt("x", this.pos.getX());
		ret.putInt("y", this.pos.getY());
		ret.putInt("z", this.pos.getZ());
		ret.putString("name", this.name);
		ret.putString("target", this.target);

		return ret;
	}
}
