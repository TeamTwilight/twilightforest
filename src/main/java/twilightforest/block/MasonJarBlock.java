package twilightforest.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
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
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import twilightforest.block.entity.MasonJarBlockEntity;
import twilightforest.init.TFSounds;

import java.util.List;

public class MasonJarBlock extends JarBlock implements SimpleWaterloggedBlock {
	public static final MapCodec<MasonJarBlock> CODEC = simpleCodec(MasonJarBlock::new);
	private static final int SLOT = 0;

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
	protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (super.useItemOn(stack, state, level, pos, player, hand, hit) != InteractionResult.TRY_WITH_EMPTY_HAND)
			return InteractionResult.SUCCESS;
		if (!(level.getBlockEntity(pos) instanceof MasonJarBlockEntity jar))
			return InteractionResult.CONSUME;
		if (!(level instanceof ServerLevel server))
			return InteractionResult.SUCCESS;

		MasonJarBlockEntity.MasonJarItemStackHandler handler = jar.getItemHandler();
		if (stack.isEmpty()) handleEmptyHand(server, pos, player, hand, jar, handler);
		else handleInsert(server, pos, player, hand, jar, handler, stack);
		return InteractionResult.SUCCESS;
	}

	private static void handleEmptyHand(ServerLevel server, BlockPos pos, Player player, InteractionHand hand, MasonJarBlockEntity jar, MasonJarBlockEntity.MasonJarItemStackHandler handler) {
		ItemResource resource = handler.getResource(SLOT);
		if (resource.isEmpty()) {
			wiggle(server, pos, jar);
			return;
		}

		ItemStack preview = resource.toStack(handler.getAmountAsInt(SLOT));
		if (player.isSecondaryUseActive()) {
			player.sendOverlayMessage(Component.literal(preview.getHoverName().getString() + " x" + preview.getCount()));
			wiggle(server, pos, jar);
			return;
		}

		int maxAmount = Math.min(Item.ABSOLUTE_MAX_STACK_SIZE, resource.getItem().getMaxStackSize(resource.toStack()));

		try (Transaction transaction = Transaction.openRoot()) {
			int countExtracted = handler.extract(SLOT, resource, maxAmount, transaction);
			if (countExtracted <= 0) {
				wiggle(server, pos, jar);
				return;
			}

			transaction.commit();
			server.sendBlockUpdated(pos, jar.getBlockState(), jar.getBlockState(), Block.UPDATE_ALL);
			player.setItemInHand(hand, preview);
			server.playSound(null, pos, TFSounds.JAR_REMOVE.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
			server.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
		}
	}

	private static void handleInsert(ServerLevel server, BlockPos pos, Player player, InteractionHand hand, MasonJarBlockEntity jar, MasonJarBlockEntity.MasonJarItemStackHandler handler, ItemStack stack) {
		ItemResource resource = ItemResource.of(stack);
		int amountToInsert = stack.getCount();

		int oldRotation = jar.getItemRotation();
		jar.setItemRotation(RotationSegment.convertToSegment(player.getYRot() + 180.0F));

		try (Transaction transaction = Transaction.openRoot()) {
			int inserted = handler.insert(SLOT, resource, amountToInsert, transaction);

			if (inserted <= 0) {
				jar.setItemRotation(oldRotation);
				wiggle(server, pos, jar);
				return;
			}

			transaction.commit();

			player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
			ItemStack before = stack.copy();
			ItemStack remainder = stack.copy();
			remainder.shrink(inserted);
			player.setItemInHand(hand, player.hasInfiniteMaterials() ? before : remainder);
			float filledRatio = (float) inserted / (float) before.getMaxStackSize();
			server.playSound(null, pos, TFSounds.JAR_INSERT.get(), SoundSource.BLOCKS, 1.0F, 0.7F + 0.5F * filledRatio);
			server.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
		}
	}

	private static void wiggle(ServerLevel server, BlockPos pos, MasonJarBlockEntity jar) {
		server.playSound(null, pos, TFSounds.JAR_WIGGLE.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
		jar.wobble(DecoratedPotBlockEntity.WobbleStyle.NEGATIVE);
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
	protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) {
		if (level.getBlockEntity(pos) instanceof MasonJarBlockEntity jarBlockEntity) {
			ItemStack itemstack = jarBlockEntity.getItemHandler().getItem();
			return Mth.lerpDiscrete(itemstack.isEmpty() ? 0 : (float) itemstack.getCount() / (float) itemstack.getMaxStackSize(), 0, 15);
		}
		return 0;
	}
}
