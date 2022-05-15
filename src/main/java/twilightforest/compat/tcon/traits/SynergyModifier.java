package twilightforest.compat.tcon.traits;


import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.FakePlayer;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.tools.helper.ToolDamageUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import twilightforest.block.TFBlocks;
import twilightforest.item.TFItems;
import twilightforest.util.TFItemStackUtils;
import twilightforest.util.TwilightItemTier;

public class SynergyModifier extends NoLevelsModifier {

	private static final float REPAIR_DAMPENER = 1f / 256f;

	@Override
	public float getRepairFactor(IToolStackView toolStack, int level, float factor) {
		return super.getRepairFactor(toolStack, level, factor);
	}

	@Override
	public void onInventoryTick(IToolStackView tool, int level, Level world, LivingEntity holder, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
		if (!world.isClientSide() && holder instanceof Player player && !(holder instanceof FakePlayer)) {
			if (!isCorrectSlot) return;
			if (!needsRepair(tool)) return;

			int healPower = 0;

			NonNullList<ItemStack> playerInv = player.getInventory().items;

			for (int i = 0; i < 9; i++) {
				if (i != itemSlot) {
					ItemStack invSlot = playerInv.get(i);
					if (invSlot.is(TFItems.STEELEAF_INGOT.get())) {
						healPower += invSlot.getCount();
					} else if (invSlot.is(TFBlocks.STEELEAF_BLOCK.get().asItem())) {
						healPower += invSlot.getCount() * 9;
					} else if (TFItemStackUtils.hasToolMaterial(invSlot, TwilightItemTier.STEELEAF)) {
						healPower += 1;
					}
				}
			}

			ToolDamageUtil.repair(tool, averageInt(healPower * REPAIR_DAMPENER));
		}
	}

	private static boolean needsRepair(IToolStackView tool) {
		return tool.getDamage() > 0 && !tool.isBroken();
	}

	private static int averageInt(float value) {
		double floor = Math.floor(value);
		double rem = value - floor;
		return (int) floor + (Math.random() < rem ? 1 : 0);
	}
}
