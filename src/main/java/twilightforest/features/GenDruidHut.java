package twilightforest.features;

import net.minecraft.block.material.Material;
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
        Rotation[] arotation = Rotation.values();
        Rotation rotation = arotation[0];//random.nextInt(arotation.length)];
        ChunkPos chunkpos = new ChunkPos(pos);
        StructureBoundingBox structureboundingbox = new StructureBoundingBox(chunkpos.getXStart(), 0, chunkpos.getZStart(), chunkpos.getXEnd(), 256, chunkpos.getZEnd());
        PlacementSettings placementsettings = (new PlacementSettings()).setRotation(rotation).setBoundingBox(structureboundingbox).setRandom(random);

        BlockPos posSnap = new BlockPos.MutableBlockPos(
                pos.getX() - (pos.getX()%16) - (pos.getX() < 0 ? 16 : 0),
                pos.getY()-1,
                pos.getZ() - (pos.getZ()%16) - (pos.getZ() < 0 ? 16 : 0)
        );

        BlockPos.MutableBlockPos placementPos = new BlockPos.MutableBlockPos(template.getZeroPositionWithTransform(posSnap, Mirror.NONE, rotation));

        if (offsetToAverageGroundLevel(world, placementPos, template.getSize())) {
            template.addBlocksToWorld(world, placementPos, placementsettings, 20);
        }

        return true;
    }

    private static boolean offsetToAverageGroundLevel(World world, BlockPos.MutableBlockPos actualPos, BlockPos size) {
        final int originLevel = 30;

        for (int x = 0; x < size.getX(); x++) {
            for (int z = 0; z < size.getZ(); z++) {
                Material material = world.getBlockState(new BlockPos(actualPos.getX() + x, actualPos.getY(), actualPos.getZ() + z)).getMaterial();
                boolean isOk = material == Material.ROCK || material == Material.GROUND || material == Material.GRASS;

                if (material == Material.WATER) return false;
                if (actualPos.getY() != originLevel && world.getBlockState(new BlockPos(actualPos.getX()+x, originLevel, actualPos.getZ()+z)).getMaterial() == Material.WATER) return false;

                if (!isOk) {
                    for (int y = 1; y < 30; y++) {
                        Material materialAt = world.getBlockState(new BlockPos(actualPos.getX()+x, actualPos.getY() - y, actualPos.getZ()+z)).getMaterial();

                        if (materialAt == Material.WATER) return false;

                        if (materialAt == Material.ROCK || materialAt == Material.GROUND || materialAt == Material.GRASS) {
                            actualPos.setY(actualPos.getY() - y);
                            break;
                        }
                    }
                }

                /*if (shouldLookUp) {
                    for (int y = 1; y < actualPos.getY() + 64; y++) {
                        int heightCheck = actualPos.getY() + y;

                        Material materialAt = world.getBlockState(new BlockPos(actualPos.getX(), heightCheck, actualPos.getZ())).getMaterial();

                        if (materialAt != Material.ROCK && materialAt != Material.GROUND && materialAt != Material.GRASS) {
                            heightCorrection = (heightCorrection + (heightCheck/2F)) / ++iterations;
                            break;
                        }
                    }
                } else {
                    for (int y = 1; y < actualPos.getY() - 64; y++) {
                        int heightCheck = actualPos.getY() - y;

                        Material materialAt = world.getBlockState(new BlockPos(actualPos.getX(), heightCheck, actualPos.getZ())).getMaterial();

                        if (materialAt == Material.ROCK || materialAt == Material.GROUND || materialAt == Material.GRASS) {
                            heightCorrection = (heightCorrection + heightCheck) / ++iterations;
                            break;
                        }
                    }
                }//*/
            }
        }

        return true;
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
