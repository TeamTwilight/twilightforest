package twilightforest.structures.courtyard;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoubleStoneSlab;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.template.ITemplateProcessor;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;
import twilightforest.TFFeature;
import twilightforest.TwilightForestMod;
import twilightforest.structures.StructureTFComponent;
import twilightforest.structures.StructureTFComponentTemplate;

import java.util.Random;

public class ComponentNagaCourtyardTerrace extends StructureTFComponentTemplate {
    private static final ResourceLocation TERRACE = new ResourceLocation(TwilightForestMod.ID, "courtyard/terrace_fire");

    private static final ITemplateProcessor COURTYARD_TERRACE_PROCESSOR = ((worldIn, pos, blockInfo) -> {
        final IBlockState SMOOTHBRICK_SLAB_STATE = Blocks.STONE_SLAB.getDefaultState().withProperty(BlockStoneSlab.VARIANT, BlockStoneSlab.EnumType.SMOOTHBRICK);
        final IBlockState SMOOTHBRICK_STATE = Blocks.STONEBRICK.getDefaultState();

        final IBlockState GRASS = Blocks.GRASS.getDefaultState();
        final IBlockState SEAMLESS_STONE_DOUBLESLAB = Blocks.DOUBLE_STONE_SLAB.getDefaultState().withProperty(BlockDoubleStoneSlab.VARIANT, BlockStoneSlab.EnumType.STONE).withProperty(BlockDoubleStoneSlab.SEAMLESS, true);

        if (blockInfo.blockState == SMOOTHBRICK_STATE) {
            IBlockState stateCheck = worldIn.getBlockState(pos);
            if (stateCheck == SMOOTHBRICK_SLAB_STATE)
                return new Template.BlockInfo(pos, stateCheck, null);
            else if (stateCheck == SMOOTHBRICK_STATE)
                return blockInfo;
            else if (stateCheck.getMaterial() == Material.AIR)
                return null;
        }

        if (blockInfo.blockState == SMOOTHBRICK_SLAB_STATE) {
            IBlockState stateCheck = worldIn.getBlockState(pos);
            if (stateCheck == SMOOTHBRICK_SLAB_STATE || stateCheck == SMOOTHBRICK_STATE)
                return blockInfo;
            else if (stateCheck.getMaterial() == Material.AIR)
                return null;
        }

        if (blockInfo.blockState == GRASS) {
            IBlockState stateCheck = worldIn.getBlockState(pos);
            if (stateCheck == SEAMLESS_STONE_DOUBLESLAB)
                return new Template.BlockInfo(pos, stateCheck, null);
        }

        return blockInfo;
    });

    @SuppressWarnings({"WeakerAccess", "unused"})
    public ComponentNagaCourtyardTerrace() {
        super();
    }

    @SuppressWarnings("WeakerAccess")
    public ComponentNagaCourtyardTerrace(TFFeature feature, int i, int x, int y, int z, Rotation rotation) {
        super(feature, i, x, y, z, rotation);
    }

    @Override
    public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
        MinecraftServer server = worldIn.getMinecraftServer();
        TemplateManager templateManager = worldIn.getSaveHandler().getStructureTemplateManager();

        TEMPLATE = templateManager.getTemplate(server, TERRACE);

        this.setBoundingBoxFromTemplate();

        PlacementSettings placementSettings = new PlacementSettings()
                .setRotation(this.rotation)
                .setReplacedBlock(Blocks.STRUCTURE_VOID)
                .setBoundingBox(this.boundingBox);

        TEMPLATE.addBlocksToWorld(worldIn, templatePosition, new CourtyardTerraceTemplateProcessor(templatePosition, placementSettings), placementSettings, 2);

        return true;
    }
}
