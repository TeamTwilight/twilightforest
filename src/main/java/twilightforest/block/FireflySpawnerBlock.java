package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import twilightforest.client.particle.TFParticleType;
import twilightforest.item.TFItems;

import java.util.Random;

public class FireflySpawnerBlock extends AbstractParticleSpawnerBlock{

	private static final VoxelShape SHAPE = Block.box(3.0D, 0.0D, 3.0D, 13.0D, 14.0D, 13.0D);

	public FireflySpawnerBlock(Properties properties) {
		super(properties);
	}

	@Override
	public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
		return SHAPE;
	}

	@Override
	public void animateTick(BlockState state, Level world, BlockPos pos, Random rand) {
		super.animateTick(state, world, pos, rand);

		if(rand.nextInt(5) == 0) {
			double dx = pos.getX() + ((rand.nextFloat() - rand.nextFloat()) * 0.2F + 0.5F);
			double dy = pos.getY() + 0.4F + ((rand.nextFloat() - rand.nextFloat()) * 0.3F);
			double dz = pos.getZ() + ((rand.nextFloat() - rand.nextFloat()) * 0.2F + 0.5F);
			world.addParticle(TFParticleType.FIREFLY.get(), dx, dy, dz, 0, 0, 0);
		}
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		ItemStack stack = player.getItemInHand(hand);
		if(stack.getItem() == TFBlocks.FIREFLY.get().asItem() && !player.isShiftKeyDown() && state.getValue(RADIUS) < 10) {
			level.setBlockAndUpdate(pos, state.setValue(RADIUS, state.getValue(RADIUS) + 1));
			if(!player.isCreative()) stack.shrink(1);
			player.displayClientMessage(new TranslatableComponent("block.twilightforest.firefly_spawner_radius", state.getValue(RADIUS) + 1), true);
			return InteractionResult.sidedSuccess(level.isClientSide);
		} else if(player.isShiftKeyDown() && state.getValue(RADIUS) > 1) {
			level.setBlockAndUpdate(pos, state.setValue(RADIUS, state.getValue(RADIUS) - 1));
			ItemEntity bug = new ItemEntity(level, pos.getX() + 0.5D, pos.getY() + 1, pos.getZ() + 0.5D, new ItemStack(TFBlocks.FIREFLY.get()));
			level.addFreshEntity(bug);
			player.displayClientMessage(new TranslatableComponent("block.twilightforest.firefly_spawner_radius", state.getValue(RADIUS) - 1), true);
			return InteractionResult.sidedSuccess(level.isClientSide);
		}
		return super.use(state, level, pos, player, hand, hitResult);
	}

	@Override
	public ParticleType<?> getParticlesToSpawn() {
		return TFParticleType.JAR_WANDERING_FIREFLY.get();
	}

	@Override
	public int getParticleCountPerSpawn(BlockState state) {
		return (int)Math.ceil((double)state.getValue(RADIUS) / 2);
	}
}
