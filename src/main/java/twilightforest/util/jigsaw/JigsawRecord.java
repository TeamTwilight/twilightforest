package twilightforest.util.jigsaw;

import net.minecraft.core.BlockPos;
import net.minecraft.core.FrontAndTop;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Util;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.JigsawBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @param priority Determines order in which pieces are generated, before post-processing (which is block placement)
 * @param pos Offset from template origin. Not the world position
 */
public record JigsawRecord(int priority, FrontAndTop orientation, BlockPos pos, String pool, String name, String target) {
	public static List<JigsawRecord> allFromTemplate(StructureTemplateManager structureManager, Identifier templateLocation, StructurePlaceSettings placeSettings) {
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
			info.nbt().getIntOr("selection_priority", 0),
			JigsawUtil.process(info.state().getValue(JigsawBlock.ORIENTATION), settings),
			StructureTemplate.calculateRelativePosition(settings, info.pos()),
			info.nbt().getStringOr("pool", "minecraft:empty"),
			info.nbt().getStringOr("name", ""),
			info.nbt().getStringOr("target", "")
		);
	}

	public static JigsawRecord fromJigsawBlock(StructureTemplate.StructureBlockInfo info) {
		return new JigsawRecord(
			info.nbt().getIntOr("selection_priority", 0),
			info.state().getValue(JigsawBlock.ORIENTATION),
			info.pos(),
			info.nbt().getStringOr("pool", "minecraft:empty"),
			info.nbt().getStringOr("name", ""),
			info.nbt().getStringOr("target", "")
		);
	}

	public static JigsawRecord fromTag(CompoundTag tag) {
		return new JigsawRecord(
			tag.getIntOr("priority", 0),
			FrontAndTop.values()[tag.getIntOr("facing", 0)],
			new BlockPos(tag.getIntOr("x", 0), tag.getIntOr("y", 0), tag.getIntOr("z", 0)),
			tag.getStringOr("pool", ""),
			tag.getStringOr("name", ""),
			tag.getStringOr("target", "")
		);
	}

	public CompoundTag toTag() {
		CompoundTag ret = new CompoundTag();

		ret.putInt("priority", this.priority);
		ret.putInt("facing", this.orientation.ordinal());
		ret.putInt("x", this.pos.getX());
		ret.putInt("y", this.pos.getY());
		ret.putInt("z", this.pos.getZ());
		ret.putString("pool", this.pool);
		ret.putString("name", this.name);
		ret.putString("target", this.target);

		return ret;
	}
}
