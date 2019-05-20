package twilightforest.structures.courtyard;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.template.TemplateManager;
import twilightforest.TFFeature;
import twilightforest.TwilightForestMod;
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
    protected void loadTemplates(TemplateManager templateManager, MinecraftServer server) {
        TEMPLATE = templateManager.getTemplate(server, PATH);
    }

    @Override
    public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBoundingBox) {
        placeSettings.setBoundingBox(structureBoundingBox);
        TEMPLATE.addBlocksToWorld(world, templatePosition, new CourtyardWallTemplateProcessor(templatePosition, placeSettings), placeSettings, 18);
        return true;
    }
}
