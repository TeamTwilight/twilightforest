package twilightforest.compat.jade;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec2;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElementHelper;
import twilightforest.TwilightForestMod;
import twilightforest.entity.passive.QuestRam;
import twilightforest.util.ColorUtil;

import java.util.Map;

public enum QuestRamWoolProvider implements IEntityComponentProvider {

	INSTANCE;

	@Override
	public void appendTooltip(ITooltip tooltip, EntityAccessor entityAccessor, IPluginConfig pluginConfig) {
		if (entityAccessor.getEntity() instanceof QuestRam ram) {
			int getRenderedWools = 0;
			for (Map.Entry<DyeColor, Block> entry : ColorUtil.WOOL_TO_DYE_IN_RAM_ORDER.entrySet()) {
				if (!ram.isColorPresent(entry.getKey())) {
					if (getRenderedWools % 8 == 0) {
						tooltip.add(IElementHelper.get().smallItem(new ItemStack(entry.getValue())).size(new Vec2(4.0F, 4.0F)));
					} else {
						tooltip.append(IElementHelper.get().smallItem(new ItemStack(entry.getValue())).size(new Vec2(4.0F, 4.0F)));
					}
					tooltip.append(Component.literal(" "));
					getRenderedWools++;
				}
			}
		}
	}

	@Override
	public ResourceLocation getUid() {
		return TwilightForestMod.prefix("quest_ram_wool");
	}
}
