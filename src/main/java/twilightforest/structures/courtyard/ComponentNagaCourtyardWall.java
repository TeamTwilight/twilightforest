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
import twilightforest.structures.MossyCobbleTemplateProcessor;
import twilightforest.structures.StructureTFComponent;
import twilightforest.structures.StructureTFComponentTemplate;

import java.util.Random;

public class ComponentNagaCourtyardWall extends StructureTFComponentTemplate {
    private static final ResourceLocation WALL = new ResourceLocation(TwilightForestMod.ID, "courtyard/courtyard_wall");
    private static final ResourceLocation WALL_DECAYED = new ResourceLocation(TwilightForestMod.ID, "courtyard/courtyard_wall_decayed");

    @SuppressWarnings({"WeakerAccess", "unused"})
    public ComponentNagaCourtyardWall() {
        super();
    }

    @SuppressWarnings("WeakerAccess")
    public ComponentNagaCourtyardWall(TFFeature feature, int i, int x, int y, int z, Rotation rotation) {
        super(feature, i, x, y, z, rotation);
    }

    @Override
    public boolean addComponentParts(World worldIn, Random random, StructureBoundingBox structureBoundingBoxIn) {
        MinecraftServer server = worldIn.getMinecraftServer();
        TemplateManager templateManager = worldIn.getSaveHandler().getStructureTemplateManager();

        TEMPLATE = templateManager.getTemplate(server, WALL);

        this.setBoundingBoxFromTemplate();

        PlacementSettings placementSettings = new PlacementSettings()
                .setRotation(this.rotation)
                .setReplacedBlock(Blocks.STRUCTURE_VOID)
                .setBoundingBox(this.boundingBox);

        TEMPLATE.addBlocksToWorld(worldIn, templatePosition, new CourtyardWallTemplateProcessor(templatePosition, placementSettings), placementSettings.setIntegrity(ComponentNagaCourtyardMain.WALL_INTEGRITY), 2);

        Template temDecay = templateManager.getTemplate(server, WALL_DECAYED);
        temDecay.addBlocksToWorld(worldIn, templatePosition, new MossyCobbleTemplateProcessor(templatePosition, placementSettings), placementSettings.setIntegrity(ComponentNagaCourtyardMain.WALL_DECAY), 2);

        //this.setDebugCorners(worldIn);

        //this.setDebugEntity(worldIn, templatePosition, this.getClass().getName() + " : " + this.rotation);

        return true;
    }
}
