package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import twilightforest.block.entity.TrophyBlockEntity;
import twilightforest.enums.BossVariant;
import twilightforest.init.TFBlockEntities;
import twilightforest.init.TFItems;
import twilightforest.init.TFParticleType;
import twilightforest.init.TFSounds;

import org.jetbrains.annotations.Nullable;

//[VanillaCopy] of AbstractSkullBlock except uses Variants instead of ISkullType and adds Sounds when clicked or powered
public abstract class AbstractTrophyBlock extends BaseEntityBlock {

	private final BossVariant variant;
	private final int comparatorValue;
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

	protected AbstractTrophyBlock(BossVariant variant, int value, BlockBehaviour.Properties builder) {
		super(builder);
		this.variant = variant;
		this.comparatorValue = value;
		this.registerDefaultState(this.getStateDefinition().any().setValue(POWERED, false));
	}

	public int getComparatorValue() {
		return this.comparatorValue;
	}

	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
		if (!level.isClientSide()) {
			boolean flag = level.hasNeighborSignal(pos);
			if (flag != state.getValue(POWERED)) {
				if (flag) {
					this.playSound(level, pos);
				}
				level.setBlockAndUpdate(pos, state.setValue(POWERED, flag));
			}
		}
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player playerIn, InteractionHand handIn, BlockHitResult hit) {
		this.playSound(level, pos);
		this.createParticle(level, pos);
		return InteractionResult.sidedSuccess(level.isClientSide());
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new TrophyBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, TFBlockEntities.TROPHY.get(), TrophyBlockEntity::tick);
	}

	public BossVariant getVariant() {
		return this.variant;
	}

	@Override
	public boolean isPathfindable(BlockState state, BlockGetter getter, BlockPos pos, PathComputationType type) {
		return false;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(POWERED);
	}

	public void playSound(Level world, BlockPos pos) {
		BlockEntity te = world.getBlockEntity(pos);
		if (!world.isClientSide() && te instanceof TrophyBlockEntity) {
			SoundEvent sound = null;
			float volume = 1.0F;
			float pitch = 0.9F;
			switch (this.variant) {
				case NAGA -> {
					sound = TFSounds.NAGA_RATTLE.get();
					volume = 1.25F;
					pitch = 1.2F;
				}
				case LICH -> {
					sound = TFSounds.LICH_AMBIENT.get();
					volume = 0.35F;
					pitch = 1.1F;
				}
				case HYDRA -> {
					sound = TFSounds.HYDRA_GROWL.get();
					pitch = 1.2F;
				}
				case UR_GHAST -> {
					sound = TFSounds.UR_GHAST_AMBIENT.get();
					pitch = 0.6F;
				}
				case SNOW_QUEEN -> sound = TFSounds.SNOW_QUEEN_AMBIENT.get();
				case KNIGHT_PHANTOM -> {
					sound = TFSounds.KNIGHT_PHANTOM_AMBIENT.get();
					pitch = 1.1F;
				}
				case MINOSHROOM -> {
					sound = TFSounds.MINOSHROOM_AMBIENT.get();
					volume = 0.75F;
					pitch = 0.7F;
				}
				case ALPHA_YETI -> {
					sound = world.getRandom().nextInt(50) == 0 ? TFSounds.ALPHA_YETI_ROAR.get() : TFSounds.ALPHA_YETI_GROWL.get();
					volume = 0.75F;
					pitch = 0.75F;
				}
				case QUEST_RAM -> {
					sound = TFSounds.QUEST_RAM_AMBIENT.get();
					pitch = 0.7F;
				}
				default -> {
				}
			}
			if (sound != null) {
				world.playSound(null, pos, sound, SoundSource.BLOCKS, volume, world.getRandom().nextFloat() * 0.1F + pitch);
			}
		}
	}

	public void createParticle(Level level, BlockPos pos) {
		BlockEntity te = level.getBlockEntity(pos);
		if (te instanceof TrophyBlockEntity) {
			RandomSource rand = level.getRandom();
			if (level instanceof ServerLevel server) {
				switch (this.variant) {
					case NAGA:
						for (int daze = 0; daze < 10; daze++) {
							server.sendParticles(ParticleTypes.CRIT,
									((double) pos.getX() + rand.nextFloat() * 0.5 * 2.0F),
									(double) pos.getY() + 0.25,
									((double) pos.getZ() + rand.nextFloat() * 0.5 * 2.0F),
									1, 0, 0, 0, rand.nextGaussian() * 0.02D);
						}
						break;
					case LICH:
						for (int a = 0; a < 5; a++) {
							server.sendParticles(ParticleTypes.ANGRY_VILLAGER,
									(double) pos.getX() + rand.nextFloat() * 0.5 * 2.0F,
									(double) pos.getY() + 0.5 + rand.nextFloat() * 0.25,
									(double) pos.getZ() + rand.nextFloat() * 0.5 * 2.0F, 1,
									rand.nextGaussian() * 0.02D, rand.nextGaussian() * 0.02D, rand.nextGaussian() * 0.02D, 0);
						}
						break;
					case MINOSHROOM:
						for (int g = 0; g < 10; g++) {
							server.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, level.getBlockState(pos.below())),
									(double) pos.getX() + rand.nextFloat() * 10F - 5F,
									(double) pos.getY() + 0.1F + rand.nextFloat() * 0.3F,
									(double) pos.getZ() + rand.nextFloat() * 10F - 5F,
									1, 0, 0, 0, 0);
						}
						break;
					case KNIGHT_PHANTOM:
						for (int brek = 0; brek < 10; brek++) {
							server.sendParticles(new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(TFItems.KNIGHTMETAL_SWORD.get())),
									pos.getX() + 0.5 + (rand.nextFloat() - 0.5),
									pos.getY() + rand.nextFloat() + 0.5,
									pos.getZ() + 0.5 + (rand.nextFloat() - 0.5),
									1, 0, .25, 0, 0);

						}
						break;
					case UR_GHAST:
						for (int red = 0; red < 10; red++) {
							server.sendParticles(DustParticleOptions.REDSTONE,
									(double) pos.getX() + (rand.nextDouble() * 1) - 0.25,
									(double) pos.getY() + rand.nextDouble() * 0.5 + 0.5,
									(double) pos.getZ() + (rand.nextDouble() * 1),
									1, 0, 0, 0, 0);
						}
						break;
					case ALPHA_YETI:
						for (int sweat = 0; sweat < 10; sweat++) {
							server.sendParticles(ParticleTypes.SPLASH,
									(double) pos.getX() + (rand.nextDouble() * 1) - 0.25,
									(double) pos.getY() + rand.nextDouble() * 0.5 + 0.5,
									(double) pos.getZ() + (rand.nextDouble() * 1),
									1, 0, 0, 0, 0);
						}
						break;
					case SNOW_QUEEN:
						for (int b = 0; b < 20; b++) {
							server.sendParticles(TFParticleType.SNOW_WARNING.get(),
									(double) pos.getX() - 1 + (rand.nextDouble() * 3.25),
									(double) pos.getY() + 5,
									(double) pos.getZ() - 1 + (rand.nextDouble() * 3.25), 1,
									0, 1, 0, 0);
						}
						break;
					case QUEST_RAM:
						for (int p = 0; p < 10; p++) {
							server.sendParticles(ParticleTypes.ENTITY_EFFECT,
									(double) pos.getX() + 0.5 + (rand.nextDouble() - 0.5),
									(double) pos.getY() + (rand.nextDouble() - 0.5),
									(double) pos.getZ() + 0.5 + (rand.nextDouble() - 0.5), 1,
									rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), 1);
						}
						break;
					default:
						break;
				}
			}
		}
	}
}
