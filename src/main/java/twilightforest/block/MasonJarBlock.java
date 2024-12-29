package twilightforest.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.common.world.AuxiliaryLightManager;
import twilightforest.block.entity.MasonJarBlockEntity;
import twilightforest.init.TFSounds;

import java.util.List;

public class MasonJarBlock extends JarBlock implements SimpleWaterloggedBlock {
	public static final MapCodec<MasonJarBlock> CODEC = simpleCodec(MasonJarBlock::new);

	public MasonJarBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return CODEC;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new MasonJarBlockEntity(pos, state);
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		if (super.useItemOn(stack, state, level, pos, player, hand, hitResult) == ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION) {
			if (level.getBlockEntity(pos) instanceof MasonJarBlockEntity blockEntity) {
				if (level instanceof ServerLevel serverLevel) {
					MasonJarBlockEntity.MasonJarItemStackHandler handler = blockEntity.getItemHandler();
					if (stack.isEmpty()) {
						ItemStack test = handler.extractItem(0, Integer.MAX_VALUE, true);
						if (!test.isEmpty()) {
							if (player.isSecondaryUseActive()) {
								player.displayClientMessage(Component.literal(test.getItem().getName(test).getString() + " x" + test.getCount()), true);
								serverLevel.playSound(null, pos, TFSounds.JAR_WIGGLE.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
								blockEntity.wobble(DecoratedPotBlockEntity.WobbleStyle.NEGATIVE);
							} else {
								ItemStack attainedStack = handler.extractItem(0, Integer.MAX_VALUE, false);
								player.setItemInHand(hand, attainedStack);
								serverLevel.playSound(null, pos, TFSounds.JAR_REMOVE.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
								serverLevel.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
							}
						} else {
							serverLevel.playSound(null, pos, TFSounds.JAR_WIGGLE.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
							blockEntity.wobble(DecoratedPotBlockEntity.WobbleStyle.NEGATIVE);
						}
					} else if (handler.insertItem(0, stack, true).getCount() < stack.getCount()) {
						blockEntity.setItemRotation(RotationSegment.convertToSegment(player.getYRot() + 180.0F));
						player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
						ItemStack inserted = stack.copy();
						ItemStack returned = handler.insertItem(0, stack, false);

						player.setItemInHand(hand, returned);
						float pitch = (float) (inserted.getCount() - returned.getCount()) / (float) inserted.getMaxStackSize();
						serverLevel.playSound(null, pos, TFSounds.JAR_INSERT.get(), SoundSource.BLOCKS, 1.0F, 0.7F + 0.5F * pitch);

						serverLevel.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
					} else {
						serverLevel.playSound(null, pos, TFSounds.JAR_WIGGLE.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
						blockEntity.wobble(DecoratedPotBlockEntity.WobbleStyle.NEGATIVE);
					}
				}
				return ItemInteractionResult.SUCCESS;
			} else {
				return ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
			}
		}
		return ItemInteractionResult.SUCCESS;
	}

	@Override
	protected List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
		if (params.getOptionalParameter(LootContextParams.BLOCK_ENTITY) instanceof MasonJarBlockEntity jarBlockEntity) {
			params = params.withDynamicDrop(ShulkerBoxBlock.CONTENTS, stackConsumer ->
				stackConsumer.accept(jarBlockEntity.getItemHandler().getItem()));
		}

		return super.getDrops(state, params);
	}

	@Override
	@SuppressWarnings("deprecation")
	protected boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	public boolean hasDynamicLightEmission(BlockState state) {
		return true;
	}

	@Override
	public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
		AuxiliaryLightManager lightManager = level.getAuxLightManager(pos);
		if (lightManager != null) return lightManager.getLightAt(pos);
		return super.getLightEmission(state, level, pos);
	}

	@Override
	@SuppressWarnings("deprecation")
	protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
		if (level.getBlockEntity(pos) instanceof MasonJarBlockEntity jarBlockEntity) {
			ItemStack itemstack = jarBlockEntity.getItemHandler().getItem();
			return Mth.lerpDiscrete(itemstack.isEmpty() ? 0 : (float) itemstack.getCount() / (float) itemstack.getMaxStackSize(), 0, 15);
		}
		return 0;
	}
}
