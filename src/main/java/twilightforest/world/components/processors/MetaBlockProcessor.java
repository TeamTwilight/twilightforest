package twilightforest.world.components.processors;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.MapCodec;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFStructureProcessors;

// Jigsaws have no right being replaced as late as they are, so this processor avoids complications involving neighboring block updates such as with fences or walls
public final class MetaBlockProcessor extends StructureProcessor {
	public static final MetaBlockProcessor INSTANCE = new MetaBlockProcessor();
	public static final MapCodec<MetaBlockProcessor> CODEC = MapCodec.unit(INSTANCE);

	private MetaBlockProcessor() {
	}

	@Nullable
	@Override
	public StructureTemplate.StructureBlockInfo process(LevelReader level, BlockPos offset, BlockPos piecePos, StructureTemplate.StructureBlockInfo originalInfo, StructureTemplate.StructureBlockInfo modifiedInfo, StructurePlaceSettings placeSettings, @Nullable StructureTemplate template) {
		CompoundTag nbtInfo = modifiedInfo.nbt();
		if (nbtInfo != null && modifiedInfo.state().is(Blocks.JIGSAW)) {
			String replaceWith = nbtInfo.getString("final_state");

			if ("empty".equals(replaceWith)) {
				return null;
			}

			BlockState blockstate = Blocks.AIR.defaultBlockState();

			try {
				blockstate = BlockStateParser.parseForBlock(level.holderLookup(Registries.BLOCK), replaceWith, false).blockState();
			} catch (CommandSyntaxException syntaxException) {
				TwilightForestMod.LOGGER.error("Error while parsing blockstate {} in jigsaw block @ {}", replaceWith, modifiedInfo.pos());
			}

			return new StructureTemplate.StructureBlockInfo(modifiedInfo.pos(), blockstate, null);
		} else if (modifiedInfo.state().is(Blocks.STRUCTURE_BLOCK)) {
			return new StructureTemplate.StructureBlockInfo(modifiedInfo.pos(), Blocks.AIR.defaultBlockState(), null);
		}

		return modifiedInfo;
	}

	@Override
	protected StructureProcessorType<?> getType() {
		return TFStructureProcessors.META_BLOCK_PROCESSOR.value();
	}
}
