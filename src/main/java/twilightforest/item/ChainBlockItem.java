package twilightforest.item;

import net.minecraft.core.RegistryAccess;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.jspecify.annotations.Nullable;
import twilightforest.entity.projectile.ChainBlock;
import twilightforest.init.TFDataComponents;
import twilightforest.init.TFEnchantments;
import twilightforest.init.TFEntities;
import twilightforest.init.TFSounds;
import twilightforest.tags.TFBlockTags;

import java.util.List;
import java.util.UUID;

public class ChainBlockItem extends Item {

	@Nullable
	private List<List<Tool.Rule>> tierRules;

	public ChainBlockItem(Properties properties) {
		super(properties);
	}

	@Override
	public void inventoryTick(ItemStack stack, ServerLevel level, Entity owner, @Nullable EquipmentSlot slot) {
		if (!level.isClientSide() && stack.get(TFDataComponents.THROWN_PROJECTILE) != null && this.getThrownEntity(level, stack) == null) {
			stack.remove(TFDataComponents.THROWN_PROJECTILE);
		}
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);

		if (stack.get(TFDataComponents.THROWN_PROJECTILE) != null || !level.getWorldBorder().isWithinBounds(player.blockPosition()))
			return InteractionResult.PASS;

		player.playSound(TFSounds.BLOCK_AND_CHAIN_FIRED.get(), 0.5F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F));

		if (!level.isClientSide()) {
			ChainBlock launchedBlock = new ChainBlock(TFEntities.CHAIN_BLOCK.get(), level, player, hand, stack);
			level.addFreshEntity(launchedBlock);
			stack.set(TFDataComponents.THROWN_PROJECTILE, launchedBlock.getUUID());
		}

		player.startUsingItem(hand);
		return InteractionResult.SUCCESS;
	}

	@Nullable
	private ChainBlock getThrownEntity(Level level, ItemStack stack) {
		if (level instanceof ServerLevel server) {
			UUID id = stack.get(TFDataComponents.THROWN_PROJECTILE);
			if (id != null) {
				Entity e = server.getEntity(id);
				if (e instanceof ChainBlock) {
					return (ChainBlock) e;
				}
			}
		}

		return null;
	}

	@Override
	public int getUseDuration(ItemStack stack, LivingEntity user) {
		return 72000;
	}

	@Override
	public ItemUseAnimation getUseAnimation(ItemStack stack) {
		return ItemUseAnimation.BLOCK;
	}

	@Override
	public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
		//dont try to check harvest level if we arent thrown
		if (stack.get(TFDataComponents.THROWN_PROJECTILE) == null || !state.is(TFBlockTags.MINEABLE_WITH_BLOCK_AND_CHAIN)) return false;
		MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
		if (server != null) {
			int destruction = stack.getEnchantmentLevel(server.registryAccess().holderOrThrow(TFEnchantments.DESTRUCTION));
			if (destruction > 0) return this.canHarvest(server.registryAccess(), state, destruction);
		}
		return false;
	}

	public boolean canHarvest(RegistryAccess access, BlockState state, int destruction) {
		if (this.tierRules == null) {
			this.tierRules = List.of(
				List.of(Tool.Rule.deniesDrops(access.getOrThrow(ToolMaterial.WOOD.incorrectBlocksForDrops())), Tool.Rule.minesAndDrops(access.getOrThrow(TFBlockTags.MINEABLE_WITH_BLOCK_AND_CHAIN), 2.0F)),
				List.of(Tool.Rule.deniesDrops(access.getOrThrow(ToolMaterial.STONE.incorrectBlocksForDrops())), Tool.Rule.minesAndDrops(access.getOrThrow(TFBlockTags.MINEABLE_WITH_BLOCK_AND_CHAIN), 4.0F)),
				List.of(Tool.Rule.deniesDrops(access.getOrThrow(ToolMaterial.IRON.incorrectBlocksForDrops())), Tool.Rule.minesAndDrops(access.getOrThrow(TFBlockTags.MINEABLE_WITH_BLOCK_AND_CHAIN), 6.0F))
			);
		}
		for (Tool.Rule rule : this.tierRules.get(Math.min(destruction - 1, 2))) {
			if (rule.correctForDrops().isPresent() && state.is(rule.blocks())) {
				return rule.correctForDrops().get();
			}
		}
		return false;
	}
}