package twilightforest.features;

import com.google.common.math.StatsAccumulator;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;

import twilightforest.TwilightForestMod;
import twilightforest.world.feature.TFGenerator;

import java.util.Random;

public class GenDruidHut extends TFGenerator {

    private static final ResourceLocation STRUCTURE = new ResourceLocation(TwilightForestMod.ID, "landscape/druid_hut");

    @Override // Loosely based on WorldGenFossils
    public boolean generate(World world, Random rand, BlockPos pos) {

        MinecraftServer minecraftserver = world.getMinecraftServer();
        TemplateManager templatemanager = world.getSaveHandler().getStructureTemplateManager();
        Template template = templatemanager.getTemplate(minecraftserver, STRUCTURE);

        Random random = world.getChunk(pos).getRandomWithSeed(987234911L);

        Rotation[] rotations = Rotation.values();
        Rotation rotation = rotations[random.nextInt(rotations.length)];

        Mirror[] mirrors = Mirror.values();
        Mirror mirror = mirrors[random.nextInt(mirrors.length+1) % mirrors.length];

        ChunkPos chunkpos = new ChunkPos(pos.add(-8, 0, -8));
        StructureBoundingBox structureboundingbox = new StructureBoundingBox(chunkpos.getXStart() + 8, 0, chunkpos.getZStart() + 8, chunkpos.getXEnd() + 8, 255, chunkpos.getZEnd() + 8);
        PlacementSettings placementsettings = (new PlacementSettings()).setMirror(mirror).setRotation(rotation).setBoundingBox(structureboundingbox).setRandom(random);

        BlockPos posSnap = chunkpos.getBlock(8, pos.getY() - 1, 8);

        BlockPos transformedSize = template.transformedSize(rotation);
        int dx = random.nextInt(17 - transformedSize.getX());
        int dz = random.nextInt(17 - transformedSize.getZ());

        BlockPos.MutableBlockPos startPos = new BlockPos.MutableBlockPos(posSnap.add(dx, 0, dz));

        if (!offsetToAverageGroundLevel(world, startPos, transformedSize)) {
            return false;
        }

        BlockPos placementPos = template.getZeroPositionWithTransform(startPos, mirror, rotation);
        template.addBlocksToWorld(world, placementPos, placementsettings, 20);

        return true;
    }

    private static boolean offsetToAverageGroundLevel(World world, BlockPos.MutableBlockPos startPos, BlockPos size) {

        StatsAccumulator heights = new StatsAccumulator();

        for (int dx = 0; dx < size.getX(); dx++) {
            for (int dz = 0; dz < size.getZ(); dz++) {

                int x = startPos.getX() + dx;
                int z = startPos.getZ() + dz;

                int y = world.getHeight(x, z);

                while (y >= 0) {
                    IBlockState state = world.getBlockState(new BlockPos(x, y, z));
                    if (isBlockNotOk(state)) return false;
                    if (isBlockOk(state)) break;
                    y--;
                }

                if (y < 0) return false;

                heights.add(y);
            }
        }

        if (heights.populationStandardDeviation() > 2.0) {
            return false;
        }

        int baseY = (int) Math.round(heights.mean());
        int maxY = (int) heights.max();

        startPos.setY(baseY);

        return isAreaClear(world, startPos.up(maxY - baseY + 1), startPos.add(size));
    }

    private static boolean isAreaClear(IBlockAccess world, BlockPos min, BlockPos max) {
        for (BlockPos pos : BlockPos.getAllInBoxMutable(min, max)) {
            if (!world.getBlockState(pos).getBlock().isReplaceable(world, pos)) {
                return false;
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
