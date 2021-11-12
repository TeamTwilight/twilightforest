package twilightforest.block;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.TFConfig;
import twilightforest.TFSounds;
import twilightforest.client.particle.TFParticleType;

import java.util.Random;

public class JarBlock extends Block {

	private static final VoxelShape JAR = Block.box(3.0D, 0.0D, 3.0D, 13.0D, 14.0D, 13.0D);
	private static final VoxelShape LID = Block.box(4.0D, 14.0D, 4.0D, 12.0D, 16.0D, 12.0D);
	private static final VoxelShape AABB = Shapes.or(JAR, LID);

	protected JarBlock(BlockBehaviour.Properties props) {
		super(props);
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return AABB;
	}

	@Override
	public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		ItemEntity jarStuff = new ItemEntity(worldIn, pos.getX(), pos.getY(), pos.getZ(), this == TFBlocks.FIREFLY_JAR.get() ? TFBlocks.FIREFLY.get().asItem().getDefaultInstance() : TFBlocks.CICADA.get().asItem().getDefaultInstance());
		if(player.isShiftKeyDown()) {
			worldIn.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
			jarStuff.spawnAtLocation(jarStuff.getItem());
			jarStuff.spawnAtLocation(Items.GLASS_BOTTLE);
			return InteractionResult.SUCCESS;
		} else {
			if(player.getItemInHand(hand).getItem() == Blocks.POPPY.asItem() && this == TFBlocks.FIREFLY_JAR.get()) {
				worldIn.setBlockAndUpdate(pos, TFBlocks.FIREFLY_SPAWNER.get().defaultBlockState().setValue(AbstractParticleSpawnerBlock.RADIUS, 1));
				return InteractionResult.SUCCESS;
			}
		}
		return InteractionResult.PASS;
	}

	@Override
	public void randomTick(BlockState state, ServerLevel worldIn, BlockPos pos, Random random) {
		super.randomTick(state, worldIn, pos, random);
		//need to counter a higher random tick speed resulting in so many sounds, so here we go
		if(!TFConfig.CLIENT_CONFIG.silentCicadas.get() && random.nextInt(worldIn.getGameRules().getRule(GameRules.RULE_RANDOMTICKING).get()) <= 3) {
			worldIn.playSound(null, pos, TFSounds.CICADA, SoundSource.BLOCKS, 1.0F, 1.0F);
		}
	}

	@Override
	public void destroy(LevelAccessor pLevel, BlockPos pPos, BlockState pState) {
		super.destroy(pLevel, pPos, pState);
		if(pLevel.isClientSide()) Minecraft.getInstance().getSoundManager().stop(TFSounds.CICADA.getLocation(), SoundSource.NEUTRAL);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState state, Level world, BlockPos pos, Random rand) {
		if(this == TFBlocks.FIREFLY_JAR.get()) {
			for (int i = 0; i < 2; i++) {
				double dx = pos.getX() + ((rand.nextFloat() - rand.nextFloat()) * 0.2F + 0.5F);
				double dy = pos.getY() + 0.4F + ((rand.nextFloat() - rand.nextFloat()) * 0.3F);
				double dz = pos.getZ() + ((rand.nextFloat() - rand.nextFloat()) * 0.2F + 0.5F);

				world.addParticle(TFParticleType.FIREFLY.get(), dx, dy, dz, 0, 0, 0);
			}
		} else {
			double dx = pos.getX() + ((rand.nextFloat() - rand.nextFloat()) * 0.2F + 0.5F);
			double dy = pos.getY() + 0.4F + ((rand.nextFloat() - rand.nextFloat()) * 0.2F);
			double dz = pos.getZ() + ((rand.nextFloat() - rand.nextFloat()) * 0.2F + 0.5F);

			world.addParticle(ParticleTypes.NOTE, dx, dy, dz, 0, 0, 0);
		}
	}
}
