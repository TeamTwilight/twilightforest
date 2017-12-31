package twilightforest.structures.courtyard;

import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;
import twilightforest.TFFeature;
import twilightforest.TwilightForestMod;
import twilightforest.structures.StructureTFComponent;

import java.util.Random;

public class ComponentNagaCourtyardHedgeLine extends StructureTFComponent {
    private static final ResourceLocation HEDGE = new ResourceLocation(TwilightForestMod.ID, "courtyard/hedge_line");
    private static final ResourceLocation HEDGE_BIG = new ResourceLocation(TwilightForestMod.ID, "courtyard/hedge_line_big");

    @SuppressWarnings({"WeakerAccess", "unused"})
    public ComponentNagaCourtyardHedgeLine() {
        super();
    }

    @SuppressWarnings("WeakerAccess")
    public ComponentNagaCourtyardHedgeLine(TFFeature feature, int i, int x, int y, int z, Rotation rotation) {
        super(feature, i);
        this.rotation = rotation;
        this.boundingBox = new StructureBoundingBox(x, y, z, x + 4, y + 4, z + 4);
    }

    @Override
    public boolean addComponentParts(World worldIn, Random random, StructureBoundingBox structureBoundingBoxIn) {
        BlockPos pos = new BlockPos(this.getBoundingBox().minX, this.getBoundingBox().minY, this.getBoundingBox().minZ);

        MinecraftServer server = worldIn.getMinecraftServer();
        TemplateManager templateManager = worldIn.getSaveHandler().getStructureTemplateManager();

        PlacementSettings placementSettings = new PlacementSettings()
                .setRotation(this.rotation)
                .setReplacedBlock(Blocks.STRUCTURE_VOID)
                .setBoundingBox(structureBoundingBoxIn);

        Template template = templateManager.getTemplate(server, HEDGE);
        template.addBlocksToWorld(worldIn, pos, new CourtyardStairsTemplateProcessor(pos, placementSettings), placementSettings, 2);

        Template templateBig = templateManager.getTemplate(server, HEDGE_BIG);
        templateBig.addBlocksToWorld(worldIn, pos, placementSettings.setIntegrity(ComponentNagaCourtyardMain.HEDGE_FLOOF));

        this.setDebugCorners(worldIn);

        return true;
    }
}
