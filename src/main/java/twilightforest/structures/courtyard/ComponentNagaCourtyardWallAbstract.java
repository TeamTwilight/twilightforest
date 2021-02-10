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
import net.minecraft.world.gen.feature.template.IntegrityProcessor;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;
import twilightforest.TFFeature;
import twilightforest.structures.MossyCobbleTemplateProcessor;
import twilightforest.structures.StructureTFComponentTemplate;

import java.util.Random;

public class ComponentNagaCourtyardWallAbstract extends StructureTFComponentTemplate {

    private final ResourceLocation WALL;
    private final ResourceLocation WALL_DECAYED;

    private Template decayTemplate;

    public ComponentNagaCourtyardWallAbstract(TemplateManager manager, IStructurePieceType piece, CompoundNBT nbt, ResourceLocation wall, ResourceLocation wall_decayed) {
        super(manager, piece, nbt);
        this.WALL = wall;
        this.WALL_DECAYED = wall_decayed;
    }

    public ComponentNagaCourtyardWallAbstract(IStructurePieceType type, TFFeature feature, int i, int x, int y, int z, Rotation rotation, ResourceLocation wall, ResourceLocation wall_decayed) {
        super(type, feature, i, x, y, z, rotation);
        this.WALL = wall;
        this.WALL_DECAYED = wall_decayed;
    }

    @Override
    protected void loadTemplates(TemplateManager templateManager) {
        TEMPLATE = templateManager.getTemplate(WALL);
        decayTemplate = templateManager.getTemplate(WALL_DECAYED);
    }

	@Override
	public boolean func_230383_a_(ISeedReader world, StructureManager manager, ChunkGenerator generator, Random random, MutableBoundingBox structureBoundingBox, ChunkPos chunkPosIn, BlockPos blockPos) {
		placeSettings.setBoundingBox(structureBoundingBox);
		TEMPLATE.func_237146_a_(world, rotatedPosition, rotatedPosition, placeSettings.addProcessor(new CourtyardWallTemplateProcessor(0.2F)).addProcessor(new IntegrityProcessor(ComponentNagaCourtyardMain.WALL_INTEGRITY)), random, 18);
		decayTemplate.func_237146_a_(world, rotatedPosition, rotatedPosition, placeSettings.addProcessor(new MossyCobbleTemplateProcessor(0.2F)).addProcessor(new IntegrityProcessor(ComponentNagaCourtyardMain.WALL_DECAY)), random, 18);
		return true;
	}
}
