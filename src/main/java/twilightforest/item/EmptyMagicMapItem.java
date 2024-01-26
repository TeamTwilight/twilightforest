package twilightforest.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ComplexItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import twilightforest.data.tags.CustomTagGenerator;

public class EmptyMagicMapItem extends ComplexItem {
	public EmptyMagicMapItem(Properties properties) {
		super(properties);
	}

	// [VanillaCopy] ItemEmptyMap.onItemRightClick, edits noted
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack emptyMapStack = player.getItemInHand(hand);
		if (level.isClientSide())
			return InteractionResultHolder.pass(emptyMapStack);

		if (!level.dimensionTypeRegistration().is(CustomTagGenerator.DimensionTypeTagGenerator.ALLOWS_MAGIC_MAP_CHARTING)) {
			return InteractionResultHolder.fail(emptyMapStack);
		}

		// TF - scale at 4
		ItemStack newMapStack = MagicMapItem.setupNewMap(level, Mth.floor(player.getX()), Mth.floor(player.getZ()), (byte) 4, true, false);
		if (!player.getAbilities().instabuild) {
			emptyMapStack.shrink(1);
		}

		if (emptyMapStack.isEmpty()) {
			return InteractionResultHolder.success(newMapStack);
		} else {
			if (!player.getInventory().add(newMapStack.copy())) {
				player.drop(newMapStack, false);
			}

			player.awardStat(Stats.ITEM_USED.get(this));
			player.level().playSound(null, player, SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, player.getSoundSource(), 1.0F, 1.0F);
			return InteractionResultHolder.success(emptyMapStack);
		}
	}
}