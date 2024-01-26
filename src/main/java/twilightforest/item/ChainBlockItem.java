package twilightforest.item;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.TierSortingRegistry;
import org.jetbrains.annotations.Nullable;
import twilightforest.data.tags.ItemTagGenerator;
import twilightforest.entity.projectile.ChainBlock;
import twilightforest.init.TFEnchantments;
import twilightforest.init.TFEntities;
import twilightforest.init.TFSounds;

import java.util.UUID;

public class ChainBlockItem extends Item {

	private static final String THROWN_UUID_KEY = "chainEntity";

	public ChainBlockItem(Properties properties) {
		super(properties);
	}

	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		return this.canApplyEnchantment(EnchantmentHelper.getEnchantments(stack).keySet().toArray(new Enchantment[0])) || super.isBookEnchantable(stack, book);
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		return this.canApplyEnchantment(enchantment) || super.canApplyAtEnchantingTable(stack, enchantment);
	}

	private boolean canApplyEnchantment(Enchantment... enchantments) {
		for (Enchantment enchantment : enchantments) {
			if (enchantment.category == EnchantmentCategory.DIGGER || enchantment.canEnchant(Items.IRON_AXE.getDefaultInstance()))
				return true;
		}
		return false;
	}

	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity holder, int slot, boolean isSelected) {
		if (!level.isClientSide() && getThrownUuid(stack) != null && this.getThrownEntity(level, stack) == null) {
			stack.getTag().remove(THROWN_UUID_KEY);
		}
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);

		if (getThrownUuid(stack) != null)
			return new InteractionResultHolder<>(InteractionResult.PASS, stack);

		player.playSound(TFSounds.BLOCK_AND_CHAIN_FIRED.get(), 0.5F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F));

		if (!level.isClientSide()) {
			ChainBlock launchedBlock = new ChainBlock(TFEntities.CHAIN_BLOCK.get(), level, player, hand, stack);
			level.addFreshEntity(launchedBlock);
			this.setThrownEntity(stack, launchedBlock);
		}

		player.startUsingItem(hand);
		return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
	}

	@Nullable
	public static UUID getThrownUuid(ItemStack stack) {
		if (stack.hasTag() && stack.getTag().hasUUID(THROWN_UUID_KEY)) {
			return stack.getTag().getUUID(THROWN_UUID_KEY);
		}

		return null;
	}

	@Nullable
	private ChainBlock getThrownEntity(Level level, ItemStack stack) {
		if (level instanceof ServerLevel server) {
			UUID id = getThrownUuid(stack);
			if (id != null) {
				Entity e = server.getEntity(id);
				if (e instanceof ChainBlock) {
					return (ChainBlock) e;
				}
			}
		}

		return null;
	}

	private void setThrownEntity(ItemStack stack, ChainBlock cube) {
		stack.getOrCreateTag().putUUID(THROWN_UUID_KEY, cube.getUUID());
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 72000;
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.BLOCK;
	}

	@Override
	public boolean isValidRepairItem(ItemStack stack, ItemStack repairItem) {
		return repairItem.is(ItemTagGenerator.KNIGHTMETAL_INGOTS);
	}

	@Override
	public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
		//dont try to check harvest level if we arent thrown
		if (stack.getTag() == null || !stack.getTag().contains(THROWN_UUID_KEY)) return false;
		if (EnchantmentHelper.getTagEnchantmentLevel(TFEnchantments.DESTRUCTION.get(), stack) > 0) {
			if (state.is(BlockTags.MINEABLE_WITH_PICKAXE) || state.is(BlockTags.MINEABLE_WITH_HOE)
					|| state.is(BlockTags.MINEABLE_WITH_SHOVEL) || state.is(BlockTags.MINEABLE_WITH_AXE))
				return TierSortingRegistry.isCorrectTierForDrops(this.getHarvestLevel(stack), state);
		}
		return false;
	}

	public Tier getHarvestLevel(ItemStack stack) {
		int enchantLevel = EnchantmentHelper.getTagEnchantmentLevel(TFEnchantments.DESTRUCTION.get(), stack);
		if (enchantLevel == 2) {
			return Tiers.STONE;
		} else if (enchantLevel >= 3) {
			return Tiers.IRON;
		} else {
			return Tiers.WOOD;
		}
	}
}