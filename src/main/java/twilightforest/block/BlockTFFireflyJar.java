package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.client.particle.TFParticleType;

import java.util.Random;

//@Optional.Interface(modid = "thaumcraft", iface = "thaumcraft.api.crafting.IInfusionStabiliser")
public class BlockTFFireflyJar extends Block /*implements IInfusionStabiliser*/ {

	private static final VoxelShape AABB = VoxelShapes.create(new AxisAlignedBB(0.1875F, 0.0F, 0.1875F, 0.8125F, 1.0F, 0.8125F));

	protected BlockTFFireflyJar(Block.Properties props) {
		super(props);
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return AABB;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState state, World world, BlockPos pos, Random rand) {
		for (int i = 0; i < 2; i++) {
			double dx = pos.getX() + ((rand.nextFloat() - rand.nextFloat()) * 0.2F + 0.5F);
			double dy = pos.getY() + 0.4F + ((rand.nextFloat() - rand.nextFloat()) * 0.3F);
			double dz = pos.getZ() + ((rand.nextFloat() - rand.nextFloat()) * 0.2F + 0.5F);

			world.addParticle(TFParticleType.FIREFLY.get(), dx, dy, dz, 0, 0, 0);
		}
	}

	/*@Override
	public boolean canStabaliseInfusion(World world, BlockPos blockPos) {
		return true;
	}*/
}
