package twilightforest.structures.courtyard;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;
import twilightforest.TFFeature;
import twilightforest.structures.StructureTFComponentTemplate;

import java.util.Random;

public abstract class ComponentNagaCourtyardHedgeAbstract extends StructureTFComponentTemplate {

    private final ResourceLocation HEDGE;
    private final ResourceLocation HEDGE_BIG;

    private Template templateBig;

    @SuppressWarnings({"WeakerAccess", "unused"})
    public ComponentNagaCourtyardHedgeAbstract(ResourceLocation hedge, ResourceLocation hedgeBig) {
        super();
        this.HEDGE = hedge;
        this.HEDGE_BIG = hedgeBig;
    }

    @SuppressWarnings("WeakerAccess")
    public ComponentNagaCourtyardHedgeAbstract(TFFeature feature, int i, int x, int y, int z, Rotation rotation, ResourceLocation hedge, ResourceLocation hedgeBig) {
        super(feature, i, x, y, z, rotation);
        this.HEDGE = hedge;
        this.HEDGE_BIG = hedgeBig;
    }

    @Override
    protected void loadTemplates(TemplateManager templateManager, MinecraftServer server) {
        TEMPLATE = templateManager.getTemplate(server, HEDGE);
        templateBig = templateManager.getTemplate(server, HEDGE_BIG);
    }

    @Override
    public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBoundingBox) {
        placeSettings.setBoundingBox(structureBoundingBox);
        TEMPLATE.addBlocksToWorld(world, rotatedPosition, new CourtyardStairsTemplateProcessor(rotatedPosition, placeSettings), placeSettings, 18);
        templateBig.addBlocksToWorld(world, rotatedPosition, placeSettings.copy().setIntegrity(ComponentNagaCourtyardMain.HEDGE_FLOOF), 18);
        return true;
    }
}
