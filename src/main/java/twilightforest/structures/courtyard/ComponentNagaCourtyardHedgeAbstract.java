package twilightforest.structures.courtyard;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.IntegrityProcessor;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;
import twilightforest.TFFeature;
import twilightforest.structures.StructureTFComponentTemplate;

import java.util.Random;

public abstract class ComponentNagaCourtyardHedgeAbstract extends StructureTFComponentTemplate {

    private final ResourceLocation HEDGE;
    private final ResourceLocation HEDGE_BIG;

    private Template templateBig;

    public ComponentNagaCourtyardHedgeAbstract(TemplateManager manager, IStructurePieceType piece, CompoundNBT nbt, ResourceLocation hedge, ResourceLocation hedgeBig) {
        super(manager, piece, nbt);
        this.HEDGE = hedge;
        this.HEDGE_BIG = hedgeBig;
    }

    @SuppressWarnings("WeakerAccess")
    public ComponentNagaCourtyardHedgeAbstract(IStructurePieceType type, TFFeature feature, int i, int x, int y, int z, Rotation rotation, ResourceLocation hedge, ResourceLocation hedgeBig) {
        super(type, feature, i, x, y, z, rotation);
        this.HEDGE = hedge;
        this.HEDGE_BIG = hedgeBig;
    }

    @Override
    protected void loadTemplates(TemplateManager templateManager) {
        TEMPLATE = templateManager.getTemplate(HEDGE);
        templateBig = templateManager.getTemplate(HEDGE_BIG);
    }

	@Override
	public boolean func_230383_a_(ISeedReader world, StructureManager manager, ChunkGenerator generator, Random randomIn, MutableBoundingBox structureBoundingBox, ChunkPos chunkPosIn, BlockPos blockPos) {
		placeSettings.setBoundingBox(structureBoundingBox).clearProcessors();
		TEMPLATE.func_237146_a_(world, rotatedPosition, rotatedPosition, placeSettings.clearProcessors().addProcessor(new CourtyardStairsTemplateProcessor(0.0F)), randomIn, 18);
        templateBig.func_237146_a_(world, rotatedPosition, rotatedPosition, placeSettings.addProcessor(new IntegrityProcessor(ComponentNagaCourtyardMain.HEDGE_FLOOF)).addProcessor(BlockIgnoreStructureProcessor.AIR), randomIn, 18);
		return true;
	}
}
