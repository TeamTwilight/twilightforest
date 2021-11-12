package twilightforest.block;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.ServerAdvancementManager;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.items.ItemHandlerHelper;
import twilightforest.TwilightForestMod;
import twilightforest.item.TFItems;

import java.util.Random;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import twilightforest.util.TFStats;

public class Experiment115Block extends Block {

    public static final IntegerProperty BITES_TAKEN = IntegerProperty.create("omnomnom", 0, 7);
    public static final BooleanProperty REGENERATE = BooleanProperty.create("regenerate");

    private static final VoxelShape QUARTER_SHAPE = box(1, 0, 1, 8, 8, 8);
    private static final VoxelShape HALF_SHAPE = box(1, 0, 1, 8, 8, 15);
    private static final VoxelShape THREE_QUARTER_SHAPE = Shapes.join(HALF_SHAPE, box(8, 0, 8, 15, 8, 15), BooleanOp.OR);
    private static final VoxelShape FULL_SHAPE = box(1, 0, 1, 15, 8, 15);

    public Experiment115Block() {
        super(Properties.of(Material.CAKE, MaterialColor.METAL).strength(0.5F).sound(SoundType.WOOL).randomTicks());
        this.registerDefaultState(this.stateDefinition.any().setValue(BITES_TAKEN, 7).setValue(REGENERATE, false));
    }

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return switch (state.getValue(BITES_TAKEN)) {
			default -> FULL_SHAPE;
			case 2, 3 -> THREE_QUARTER_SHAPE;
			case 4, 5 -> HALF_SHAPE;
			case 6, 7 -> QUARTER_SHAPE;
		};
	}

	@Override
	@Deprecated
	public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		int bitesTaken = state.getValue(BITES_TAKEN);
		ItemStack stack = player.getItemInHand(hand);

		if (!player.isShiftKeyDown()) {
			if (bitesTaken > 0 && stack.getItem() == TFItems.EXPERIMENT_115.get()) {
				worldIn.setBlockAndUpdate(pos, state.setValue(BITES_TAKEN, bitesTaken - 1));
				if (!player.isCreative()) stack.shrink(1);
				if (player instanceof ServerPlayer) CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer) player, pos, stack);
				return InteractionResult.SUCCESS;
			} else if (((!state.getValue(REGENERATE)) && stack.getItem() == Items.REDSTONE) && (player.isCreative() || bitesTaken == 0)) {
				worldIn.setBlockAndUpdate(pos, state.setValue(REGENERATE,true));
				if (!player.isCreative()) stack.shrink(1);
				if (player instanceof ServerPlayer) {
					player.awardStat(Stats.ITEM_USED.get(Items.REDSTONE));

					//fallback if the advancement criteria doesnt work since its inconsistent
					PlayerAdvancements advancements = ((ServerPlayer) player).getAdvancements();
					ServerAdvancementManager manager = ((ServerLevel)player.getCommandSenderWorld()).getServer().getAdvancements();
					Advancement advancement = manager.getAdvancement(TwilightForestMod.prefix("experiment_115_self_replenishing"));
					if(advancement != null) {
						advancements.award(advancement, "place_complete_e115");
					}
				}
				return InteractionResult.SUCCESS;
			}
		} else {
			if(!state.getValue(REGENERATE) && player.getUseItem().isEmpty()) {
				if (bitesTaken < 7) {
					worldIn.setBlockAndUpdate(pos, state.setValue(BITES_TAKEN, bitesTaken + 1));
				} else {
					worldIn.removeBlock(pos, false);
				}
				if(!player.isCreative()) ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(TFItems.EXPERIMENT_115.get()));
				return InteractionResult.SUCCESS;
			}
		}
		return this.eatCake(worldIn, pos, state, player);
	}

	private InteractionResult eatCake(Level world, BlockPos pos, BlockState state, Player player) {
        if (!player.canEat(false)) return InteractionResult.PASS;
        else {
            player.awardStat(TFStats.E115_SLICES_EATEN);
            player.getFoodData().eat(4, 0.3F);
			world.playSound(null, pos, SoundEvents.GENERIC_EAT, SoundSource.PLAYERS, 0.5F, world.random.nextFloat() * 0.1F + 0.9F);
            int i = state.getValue(BITES_TAKEN);

            if (i < 7) {
            	world.setBlock(pos, state.setValue(BITES_TAKEN, i + 1), 3);
            } else {
            	world.removeBlock(pos, false);
            }

            if (player instanceof ServerPlayer) {
				CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer) player, new ItemStack(TFItems.EXPERIMENT_115.get(), 8 - i));
				player.awardStat(Stats.ITEM_USED.get(TFItems.EXPERIMENT_115.get()));
			}

            return InteractionResult.SUCCESS;
        }
    }

	@Override
	@Deprecated
	public void randomTick(BlockState state, ServerLevel worldIn, BlockPos pos, Random random) {
		if (state.getValue(REGENERATE) && state.getValue(BITES_TAKEN) != 0) {
			worldIn.setBlockAndUpdate(pos, state.setValue(BITES_TAKEN, state.getValue(BITES_TAKEN) - 1));
		}
	}

	@Override
	@Deprecated
	public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
		return worldIn.getBlockState(pos.below()).getMaterial().isSolid();
	}

	@Override
	@Deprecated
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
		return facing == Direction.DOWN && !stateIn.canSurvive(worldIn, currentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(BITES_TAKEN, REGENERATE);
	}

    @Override
    @Deprecated
    public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
        return 15-(state.getValue(BITES_TAKEN)*2);
    }

    @Override
	@Deprecated
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
	@Deprecated
    public boolean isSignalSource(BlockState state) {
        return state.getValue(REGENERATE);
    }

	@Override
	@Deprecated
	public int getSignal(BlockState state, BlockGetter blockAccess, BlockPos pos, Direction side) {
		return state.getValue(REGENERATE) ? 15-(state.getValue(BITES_TAKEN)*2) : 0;
	}
}
