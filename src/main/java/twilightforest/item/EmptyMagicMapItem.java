package twilightforest.item;

import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ComplexItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import twilightforest.TwilightForestMod;
import twilightforest.world.registration.TFGenerationSettings;

public class EmptyMagicMapItem extends ComplexItem {
	protected EmptyMagicMapItem(Properties props) {
		super(props);
	}

	// [VanillaCopy] ItemEmptyMap.onItemRightClick, edits noted
	@Override
	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
		ItemStack emptyMapStack = playerIn.getItemInHand(handIn);
		if (worldIn.isClientSide)
			return InteractionResultHolder.pass(emptyMapStack);
		if(worldIn instanceof ServerLevel level && !TFGenerationSettings.usesTwilightChunkGenerator(level)) {
			playerIn.displayClientMessage(new TranslatableComponent(TwilightForestMod.ID + ".ui.magicmap.fail"), true);
			return InteractionResultHolder.fail(emptyMapStack);
		}
		// TF - scale at 4
		ItemStack newMapStack = MagicMapItem.setupNewMap(worldIn, Mth.floor(playerIn.getX()), Mth.floor(playerIn.getZ()), (byte) 4, true, false);
		if (!playerIn.getAbilities().instabuild) {
			emptyMapStack.shrink(1);
		}

		if (emptyMapStack.isEmpty()) {
			return InteractionResultHolder.success(newMapStack);
		} else {
			if (!playerIn.getInventory().add(newMapStack.copy())) {
				playerIn.drop(newMapStack, false);
			}

			playerIn.awardStat(Stats.ITEM_USED.get(this));
			playerIn.level.playSound(null, playerIn, SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, playerIn.getSoundSource(), 1.0F, 1.0F);
			return InteractionResultHolder.success(emptyMapStack);
		}
	}
}
