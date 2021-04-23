package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.TFSounds;
import twilightforest.client.particle.TFParticleType;

import java.util.Random;

//@Optional.Interface(modid = "thaumcraft", iface = "thaumcraft.api.crafting.IInfusionStabiliser")
public class BlockTFJar extends Block /*implements IInfusionStabiliser*/ {

	private static final VoxelShape AABB = VoxelShapes.create(new AxisAlignedBB(0.1875F, 0.0F, 0.1875F, 0.8125F, 1.0F, 0.8125F));

	protected BlockTFJar(Block.Properties props) {
		super(props);
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return AABB;
	}

	@Override
	public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
		if(this == TFBlocks.cicada_jar.get() && rand.nextInt(500) == 1) {
			worldIn.playSound(null, new BlockPos(pos.getX(), pos.getY(), pos.getZ()), TFSounds.CICADA, SoundCategory.BLOCKS, 1.0F, 1.0F);
		}
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		ItemEntity jarStuff = new ItemEntity(worldIn, pos.getX(), pos.getY(), pos.getZ());
		if(player.isSneaking()) {
			worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
			jarStuff.entityDropItem(this == TFBlocks.firefly_jar.get() ? TFBlocks.firefly.get() : TFBlocks.cicada.get());
			jarStuff.entityDropItem(Items.GLASS_BOTTLE);
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.PASS;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState state, World world, BlockPos pos, Random rand) {
		if(this == TFBlocks.firefly_jar.get()) {
			for (int i = 0; i < 2; i++) {
				double dx = pos.getX() + ((rand.nextFloat() - rand.nextFloat()) * 0.2F + 0.5F);
				double dy = pos.getY() + 0.4F + ((rand.nextFloat() - rand.nextFloat()) * 0.3F);
				double dz = pos.getZ() + ((rand.nextFloat() - rand.nextFloat()) * 0.2F + 0.5F);

				world.addParticle(TFParticleType.FIREFLY.get(), dx, dy, dz, 0, 0, 0);
			}
		} else {
			for (int i = 0; i < 1; i++) {
				double dx = pos.getX() + ((rand.nextFloat() - rand.nextFloat()) * 0.2F + 0.5F);
				double dy = pos.getY();
				double dz = pos.getZ() + ((rand.nextFloat() - rand.nextFloat()) * 0.2F + 0.5F);

				world.addParticle(ParticleTypes.NOTE, dx, dy, dz, 0, 0, 0);
			}
		}
	}

	/*@Override
	public boolean canStabaliseInfusion(World world, BlockPos blockPos) {
		return true;
	}*/
}
