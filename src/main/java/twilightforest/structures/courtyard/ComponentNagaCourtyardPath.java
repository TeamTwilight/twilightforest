package twilightforest.structures.courtyard;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
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

import java.util.Random;

public class ComponentNagaCourtyardPath extends StructureTFComponent {
    private static final ResourceLocation PATH = new ResourceLocation(TwilightForestMod.ID, "courtyard/pathway");

    private static final ITemplateProcessor COURTYARD_PATH_PROCESSOR = ((worldIn, pos, blockInfo) -> {
        if (blockInfo.blockState.getBlock() == Blocks.GRASS) {
            IBlockState state = worldIn.getBlockState(pos);
            Block block = state.getBlock();
            if (block == Blocks.STONEBRICK || block == Blocks.COBBLESTONE)
                return new Template.BlockInfo(pos, state, null);
        }

        return blockInfo;
    });

    @SuppressWarnings({"WeakerAccess", "unused"})
    public ComponentNagaCourtyardPath() {
        super();
    }

    @SuppressWarnings("WeakerAccess")
    public ComponentNagaCourtyardPath(TFFeature feature, int i, int x, int y, int z) {
        super(feature, i);
        this.boundingBox = new StructureBoundingBox(x, y, z, x + 4, y, z + 4);
    }

    @Override
    public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
        BlockPos pos = new BlockPos(this.getBoundingBox().minX, this.getBoundingBox().minY, this.getBoundingBox().minZ);

        MinecraftServer server = worldIn.getMinecraftServer();
        TemplateManager templateManager = worldIn.getSaveHandler().getStructureTemplateManager();

        PlacementSettings placementSettings = new PlacementSettings()
                .setBoundingBox(structureBoundingBoxIn);

        Template template = templateManager.getTemplate(server, PATH);
        template.addBlocksToWorld(worldIn, pos, COURTYARD_PATH_PROCESSOR, placementSettings, 2);

        return true;
    }
}
