package twilightforest.world.components.feature.templates;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.properties.StructureMode;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import twilightforest.TwilightForestMod;
import twilightforest.loot.TFTreasure;
import twilightforest.world.components.processors.CobblePlankSwizzler;
import twilightforest.world.components.processors.CobbleVariants;
import twilightforest.world.components.processors.SmartGrassProcessor;
import twilightforest.world.components.processors.StoneBricksVariants;

import javax.annotation.Nullable;
import java.util.Random;

public class FancyWellFeature extends TemplateFeature<NoneFeatureConfiguration> {
    private static final ResourceLocation WELL_TOP = TwilightForestMod.prefix("feature/well/fancy_well_top");
    private static final ResourceLocation WELL_BOTTOM = TwilightForestMod.prefix("feature/well/fancy_well_bottom");

    public FancyWellFeature(Codec<NoneFeatureConfiguration> config) {
        super(config);
    }

    @Nullable
    @Override
    protected StructureTemplate getTemplate(StructureManager templateManager, Random random) {
        return templateManager.getOrCreate(WELL_TOP);
    }

    @Override
    protected void modifySettings(StructurePlaceSettings settings, Random random) {
        settings.addProcessor(new CobblePlankSwizzler(random)).addProcessor(CobbleVariants.INSTANCE).addProcessor(StoneBricksVariants.INSTANCE);
    }

    @Override
    protected void postPlacement(WorldGenLevel world, Random random, StructureManager templateManager, Rotation rotation, Mirror mirror, StructurePlaceSettings placementSettings, BlockPos placementPos) {
        StructureTemplate template = templateManager.getOrCreate(WELL_BOTTOM);

        if (template == null) return;

        placementPos = placementPos.below(template.getSize().getY());//.relative(rotation.rotate(mirror.mirror(Direction.SOUTH)), 1).relative(rotation.rotate(mirror.mirror(Direction.EAST)), 1);
        placementSettings.addProcessor(SmartGrassProcessor.INSTANCE);

        template.placeInWorld(world, placementPos, placementPos, placementSettings, random, 20);

        for (StructureTemplate.StructureBlockInfo info : template.filterBlocks(placementPos, placementSettings, Blocks.STRUCTURE_BLOCK))
            if (info.nbt != null && StructureMode.valueOf(info.nbt.getString("mode")) == StructureMode.DATA)
                this.processMarkers(info, world, rotation, mirror, random);
    }

    @Override
    protected void processMarkers(StructureTemplate.StructureBlockInfo info, WorldGenLevel world, Rotation rotation, Mirror mirror, Random random) {
        String s = info.nbt.getString("metadata");

        if (!s.startsWith("loot")) return;

        world.removeBlock(info.pos, false);

        if (random.nextBoolean()) TFTreasure.FANCY_WELL.generateLootContainer(world, info.pos, Blocks.BARREL.defaultBlockState().setValue(BarrelBlock.FACING, Direction.UP), 16 | 2);
        else world.setBlock(info.pos, random.nextBoolean() ? Blocks.COBBLESTONE.defaultBlockState() : Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 16 | 2);
    }
}
