package twilightforest.features;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;
import twilightforest.TwilightForestMod;
import twilightforest.world.TFGenerator;

import java.util.Random;

public class GenDruidHut extends TFGenerator {
    private static final ResourceLocation STRUCTURE = new ResourceLocation(TwilightForestMod.ID, "landscape/druid_hut");

    public boolean generate(World world, Random rand, BlockPos pos) {
        MinecraftServer minecraftserver = world.getMinecraftServer();
        TemplateManager templatemanager = world.getSaveHandler().getStructureTemplateManager();
        Template template = templatemanager.getTemplate(minecraftserver, STRUCTURE);

        Random random = world.getChunkFromBlockCoords(pos).getRandomWithSeed(987234911L);

        Rotation[] rotations = Rotation.values();
        Rotation rotation = rotations[random.nextInt(rotations.length)];

        Mirror[] mirrors = Mirror.values();
        Mirror mirror = mirrors[random.nextInt(mirrors.length+1) % mirrors.length];

        ChunkPos chunkpos = new ChunkPos(pos);
        StructureBoundingBox structureboundingbox = new StructureBoundingBox(chunkpos.getXStart(), 0, chunkpos.getZStart(), chunkpos.getXEnd(), 256, chunkpos.getZEnd());
        PlacementSettings placementsettings = (new PlacementSettings()).setMirror(mirror).setRotation(rotation).setBoundingBox(structureboundingbox).setRandom(random);

        BlockPos posSnap = new BlockPos.MutableBlockPos(
                pos.getX() - (pos.getX()%16) - (pos.getX() < 0 ? 16 : 0),
                pos.getY()-1,
                pos.getZ() - (pos.getZ()%16) - (pos.getZ() < 0 ? 16 : 0)
        );

        BlockPos.MutableBlockPos placementPos = new BlockPos.MutableBlockPos(template.getZeroPositionWithTransform(posSnap, mirror, rotation));

        if (offsetToAverageGroundLevel(world, placementPos, template.getSize())) {
            template.addBlocksToWorld(world, placementPos, placementsettings, 20);
            //for (int i = 0; i < 16; i++) {
            //    world.setBlockState(placementPos, Blocks.WOOL.getStateFromMeta(i));
            //    placementPos.setY(placementPos.getY() + 1);
            //}
        }

        return true;
    }

    private static boolean offsetToAverageGroundLevel(World world, BlockPos.MutableBlockPos actualPos, BlockPos size) {
        final int originLevel = actualPos.getY();

        for (int x = 0; x < size.getX(); x++) {
            for (int z = 0; z < size.getZ(); z++) {
                IBlockState state = world.getBlockState(new BlockPos(actualPos.getX() + x, originLevel, actualPos.getZ() + z));

                if (isBlockNotOk(state)) return false;

                if (!isBlockOk(state)) {
                    for (int y = originLevel - 1; y >= 0; y--) {
                        IBlockState stateAt = world.getBlockState(new BlockPos(actualPos.getX()+x, y, actualPos.getZ()+z));

                        if (isBlockNotOk(stateAt)) return false;

                        if (isBlockOk(stateAt)) {
                            if (y < actualPos.getY()) actualPos.setY(y);
                            break;
                        }
                    }
                }
            }
        }

        return true;
    }

    private static boolean isBlockOk(IBlockState state) {
        Material material = state.getMaterial();
        return material == Material.ROCK || material == Material.GROUND || material == Material.GRASS || material == Material.SAND;
    }

    private static boolean isBlockNotOk(IBlockState state) {
        Material material = state.getMaterial();
        return material == Material.WATER || material == Material.LAVA || state.getBlock() == Blocks.BEDROCK;
    }

    /*@Override
    public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
        int height = this.getAverageGroundLevel(worldIn, structureBoundingBoxIn);

        StructureBoundingBox structureboundingbox = this.getBoundingBox();
        BlockPos blockpos = new BlockPos(structureboundingbox.minX, height, structureboundingbox.minZ);
        Rotation[] arotation = Rotation.values();
        MinecraftServer minecraftserver = worldIn.getMinecraftServer();
        TemplateManager templatemanager = worldIn.getSaveHandler().getStructureTemplateManager();
        PlacementSettings placementsettings = (new PlacementSettings()).setRotation(arotation[randomIn.nextInt(arotation.length)]).setReplacedBlock(Blocks.STRUCTURE_VOID).setBoundingBox(structureboundingbox);
        Template template = templatemanager.getTemplate(minecraftserver, STRUCTURE);
        template.addBlocksToWorldChunk(worldIn, blockpos, placementsettings);

        return true;
    }*/
}
