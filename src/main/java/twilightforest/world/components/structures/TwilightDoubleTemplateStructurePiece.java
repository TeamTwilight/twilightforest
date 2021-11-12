package twilightforest.world.components.structures;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.StructureMode;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import twilightforest.TwilightForestMod;

import java.util.Random;

public abstract class TwilightDoubleTemplateStructurePiece extends TwilightTemplateStructurePiece {
    protected ResourceLocation templateOverlayLocation;
    protected StructureTemplate templateOverlay;
    protected StructurePlaceSettings placeSettingsOverlay;

    public TwilightDoubleTemplateStructurePiece(StructurePieceType structurePieceType, CompoundTag compoundTag, ServerLevel serverLevel, StructurePlaceSettings rl2SettingsFunction, StructurePlaceSettings placeSettingsOverlay) {
        super(structurePieceType, compoundTag, serverLevel, rl2SettingsFunction);

        this.templateOverlayLocation = new ResourceLocation(compoundTag.getString("TemplateOverlay"));
        this.templateOverlay = this.structureManager.getOrCreate(this.templateOverlayLocation);
        this.placeSettingsOverlay = placeSettingsOverlay;
    }

    public TwilightDoubleTemplateStructurePiece(StructurePieceType type, int genDepth, StructureManager structureManager, ResourceLocation templateLocation, StructurePlaceSettings placeSettings, ResourceLocation templateOverlayLocation, StructurePlaceSettings placeSettingsOverlay, BlockPos startPosition) {
        super(type, genDepth, structureManager, templateLocation, placeSettings, startPosition);

        this.templateOverlayLocation = templateOverlayLocation;
        this.templateOverlay = this.structureManager.getOrCreate(this.templateOverlayLocation);
        this.placeSettingsOverlay = placeSettingsOverlay;
    }

    @Override
    protected void addAdditionalSaveData(ServerLevel level, CompoundTag structureTag) {
        super.addAdditionalSaveData(level, structureTag);

        structureTag.putString("TemplateOverlay", this.templateOverlayLocation.toString());
    }

    @Override
    public boolean postProcess(WorldGenLevel worldGenLevel, StructureFeatureManager structureManager, ChunkGenerator chunkGenerator, Random random, BoundingBox boundingBox, ChunkPos chunkPos, BlockPos blockPos) {
        boolean result = super.postProcess(worldGenLevel, structureManager, chunkGenerator, random, boundingBox, chunkPos, blockPos);

        // [VanillaCopy] TemplateStructurePiece.postProcess
        //  template -> templateOverlay
        //  placeSettings -> placeSettingsOverlay
        if (this.templateOverlay.placeInWorld(worldGenLevel, this.templatePosition, blockPos, this.placeSettingsOverlay, random, 2)) {
            for(StructureTemplate.StructureBlockInfo structureBlockInfo : this.templateOverlay.filterBlocks(this.templatePosition, this.placeSettingsOverlay, Blocks.STRUCTURE_BLOCK)) {
                if (structureBlockInfo.nbt != null) {
                    StructureMode structureMode = StructureMode.valueOf(structureBlockInfo.nbt.getString("mode"));

                    if (structureMode == StructureMode.DATA)
                        this.handleDataMarker(structureBlockInfo.nbt.getString("metadata"), structureBlockInfo.pos, worldGenLevel, random, boundingBox);
                }
            }

            for(StructureTemplate.StructureBlockInfo structureBlockInfo : this.templateOverlay.filterBlocks(this.templatePosition, this.placeSettingsOverlay, Blocks.JIGSAW)) {
                if (structureBlockInfo.nbt != null) {
                    String s = structureBlockInfo.nbt.getString("final_state");
                    BlockStateParser blockStateParser = new BlockStateParser(new StringReader(s), false);
                    BlockState blockState = Blocks.AIR.defaultBlockState();

                    try {
                        blockStateParser.parse(true);
                        BlockState parserState = blockStateParser.getState();
                        if (parserState != null) {
                            blockState = parserState;
                        } else {
                            TwilightForestMod.LOGGER.error("Error while parsing blockstate {} in jigsaw block @ {}", s, structureBlockInfo.pos);
                        }
                    } catch (CommandSyntaxException commandsyntaxexception) {
                        TwilightForestMod.LOGGER.error("Error while parsing blockstate {} in jigsaw block @ {}", s, structureBlockInfo.pos);
                    }

                    worldGenLevel.setBlock(structureBlockInfo.pos, blockState, 3);
                }
            }
        }

        return result;
    }
}
