package twilightforest.world.components.structures;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.StructureMode;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import twilightforest.TwilightForestMod;

public abstract class TwilightDoubleTemplateStructurePiece extends TwilightTemplateStructurePiece {
	protected final ResourceLocation templateOverlayLocation;
	protected final StructureTemplate templateOverlay;
	protected final StructurePlaceSettings placeSettingsOverlay;

	public TwilightDoubleTemplateStructurePiece(StructurePieceType structurePieceType, CompoundTag compoundTag, StructurePieceSerializationContext ctx, StructurePlaceSettings rl2SettingsFunction, StructurePlaceSettings placeSettingsOverlay) {
		super(structurePieceType, compoundTag, ctx, rl2SettingsFunction);

		this.templateOverlayLocation = ResourceLocation.parse(compoundTag.getString("TemplateOverlay"));
		this.templateOverlay = this.structureManager.getOrCreate(this.templateOverlayLocation);
		this.placeSettingsOverlay = placeSettingsOverlay;
	}

	public TwilightDoubleTemplateStructurePiece(StructurePieceType type, int genDepth, StructureTemplateManager structureManager, ResourceLocation templateLocation, StructurePlaceSettings placeSettings, ResourceLocation templateOverlayLocation, StructurePlaceSettings placeSettingsOverlay, BlockPos startPosition) {
		super(type, genDepth, structureManager, templateLocation, placeSettings, startPosition);

		this.templateOverlayLocation = templateOverlayLocation;
		this.templateOverlay = this.structureManager.getOrCreate(this.templateOverlayLocation);
		this.placeSettingsOverlay = placeSettingsOverlay;
	}

	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext ctx, CompoundTag structureTag) {
		super.addAdditionalSaveData(ctx, structureTag);

		structureTag.putString("TemplateOverlay", this.templateOverlayLocation.toString());
	}

	@Override
	public void postProcess(WorldGenLevel worldGenLevel, StructureManager structureManager, ChunkGenerator chunkGenerator, RandomSource random, BoundingBox boundingBox, ChunkPos chunkPos, BlockPos blockPos) {
		super.postProcess(worldGenLevel, structureManager, chunkGenerator, random, boundingBox, chunkPos, blockPos);

		// [VanillaCopy] TemplateStructurePiece.postProcess
		//  template -> templateOverlay
		//  placeSettings -> placeSettingsOverlay
		if (this.templateOverlay.placeInWorld(worldGenLevel, this.templatePosition, blockPos, this.placeSettingsOverlay, random, 2)) {
			for (StructureTemplate.StructureBlockInfo structureBlockInfo : this.templateOverlay.filterBlocks(this.templatePosition, this.placeSettingsOverlay, Blocks.STRUCTURE_BLOCK)) {
				if (structureBlockInfo.nbt() != null) {
					StructureMode structureMode = StructureMode.valueOf(structureBlockInfo.nbt().getString("mode"));

					if (structureMode == StructureMode.DATA)
						this.handleDataMarker(structureBlockInfo.nbt().getString("metadata"), structureBlockInfo.pos(), worldGenLevel, random, boundingBox);
				}
			}

			for (StructureTemplate.StructureBlockInfo structureBlockInfo : this.templateOverlay.filterBlocks(this.templatePosition, this.placeSettingsOverlay, Blocks.JIGSAW)) {
				if (structureBlockInfo.nbt() != null) {
					String s = structureBlockInfo.nbt().getString("final_state");
					BlockState blockState = Blocks.AIR.defaultBlockState();
					try {
						BlockState parserState = BlockStateParser.parseForBlock(worldGenLevel.holderLookup(Registries.BLOCK), new StringReader(s), false).blockState();
						if (parserState != null) {
							blockState = parserState;
						} else {
							TwilightForestMod.LOGGER.error("Error while parsing blockstate {} in jigsaw block @ {}", s, structureBlockInfo.pos());
						}
					} catch (CommandSyntaxException e) {
						TwilightForestMod.LOGGER.error("Error while parsing blockstate {} in jigsaw block @ {}", s, structureBlockInfo.pos());
					}

					worldGenLevel.setBlock(structureBlockInfo.pos(), blockState, Block.UPDATE_ALL);
				}
			}
		}
	}
}
