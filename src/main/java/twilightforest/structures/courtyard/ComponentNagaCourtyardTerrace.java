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

public class ComponentNagaCourtyardTerrace extends StructureTFComponent {
    private static final ResourceLocation TERRACE = new ResourceLocation(TwilightForestMod.ID, "courtyard/terrace_fire");

    @SuppressWarnings({"WeakerAccess", "unused"})
    public ComponentNagaCourtyardTerrace() {
        super();
    }

    @SuppressWarnings("WeakerAccess")
    public ComponentNagaCourtyardTerrace(TFFeature feature, int i, int x, int y, int z, Rotation rotation) {
        super(feature, i);
        this.rotation = rotation;
        this.boundingBox = new StructureBoundingBox(x, y, z, x + 16, y + 7, z + 16);
    }

    @Override
    public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
        BlockPos pos = new BlockPos(this.getBoundingBox().minX, this.getBoundingBox().minY, this.getBoundingBox().minZ);

        MinecraftServer server = worldIn.getMinecraftServer();
        TemplateManager templateManager = worldIn.getSaveHandler().getStructureTemplateManager();

        PlacementSettings placementSettings = new PlacementSettings()
                .setRotation(this.rotation)
                .setReplacedBlock(Blocks.STRUCTURE_VOID)
                .setBoundingBox(structureBoundingBoxIn);

        Template template = templateManager.getTemplate(server, TERRACE);
        template.addBlocksToWorld(worldIn, pos, placementSettings);

        return true;
    }
}
