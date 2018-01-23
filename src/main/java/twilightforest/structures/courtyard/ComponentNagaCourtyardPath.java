package twilightforest.structures.courtyard;

import net.minecraft.block.Block;
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

public class ComponentNagaCourtyardPath extends StructureTFComponentTemplate {
    private static final ResourceLocation PATH = new ResourceLocation(TwilightForestMod.ID, "courtyard/pathway");

    @SuppressWarnings({"WeakerAccess", "unused"})
    public ComponentNagaCourtyardPath() {
        super();
    }

    @SuppressWarnings("WeakerAccess")
    public ComponentNagaCourtyardPath(TFFeature feature, int i, int x, int y, int z) {
        super(feature, i, x, y, z, Rotation.NONE);
    }

    @Override
    public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
        MinecraftServer server = worldIn.getMinecraftServer();
        TemplateManager templateManager = worldIn.getSaveHandler().getStructureTemplateManager();

        TEMPLATE = templateManager.getTemplate(server, PATH);

        this.setBoundingBoxFromTemplate();

        PlacementSettings placementSettings = new PlacementSettings()
                .setBoundingBox(this.boundingBox);

        TEMPLATE.addBlocksToWorld(worldIn, templatePosition, new CourtyardWallTemplateProcessor(templatePosition, placementSettings), placementSettings, 2);

        return true;
    }
}
