package twilightforest.structures.courtyard;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;
import twilightforest.TFFeature;
import twilightforest.structures.MossyCobbleTemplateProcessor;
import twilightforest.structures.StructureTFComponentTemplate;

import java.util.Random;

public class ComponentNagaCourtyardWallAbstract extends StructureTFComponentTemplate {

    private final ResourceLocation WALL;
    private final ResourceLocation WALL_DECAYED;

    private Template decayTemplate;

    @SuppressWarnings({"WeakerAccess", "unused"})
    public ComponentNagaCourtyardWallAbstract(ResourceLocation wall, ResourceLocation wall_decayed) {
        super();
        this.WALL = wall;
        this.WALL_DECAYED = wall_decayed;
    }

    @SuppressWarnings("WeakerAccess")
    public ComponentNagaCourtyardWallAbstract(TFFeature feature, int i, int x, int y, int z, Rotation rotation, ResourceLocation wall, ResourceLocation wall_decayed) {
        super(feature, i, x, y, z, rotation);
        this.WALL = wall;
        this.WALL_DECAYED = wall_decayed;
    }

    @Override
    protected void loadTemplates(TemplateManager templateManager, MinecraftServer server) {
        TEMPLATE = templateManager.getTemplate(server, WALL);
        decayTemplate = templateManager.getTemplate(server, WALL_DECAYED);
    }

    @Override
    public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBoundingBox) {
        placeSettings.setBoundingBox(structureBoundingBox);
        TEMPLATE.addBlocksToWorld(world, rotatedPosition, new CourtyardWallTemplateProcessor(rotatedPosition, placeSettings), placeSettings.setIntegrity(ComponentNagaCourtyardMain.WALL_INTEGRITY), 18);
        decayTemplate.addBlocksToWorld(world, rotatedPosition, new MossyCobbleTemplateProcessor(rotatedPosition, placeSettings), placeSettings.setIntegrity(ComponentNagaCourtyardMain.WALL_DECAY), 18);
        return true;
    }
}
