package twilightforest.compat.top;

import com.google.common.collect.ImmutableMap;
import mcjty.theoneprobe.api.IProbeHitEntityData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoEntityProvider;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import twilightforest.TwilightForestMod;
import twilightforest.entity.passive.QuestRam;

import java.util.AbstractMap;
import java.util.Map;

public enum TOPQuestRamWoolProvider implements IProbeInfoEntityProvider {

    INSTANCE;

    protected static final Map<DyeColor, Block> WOOL_TO_DYE = ImmutableMap.ofEntries( // TODO: move this to a common class?
            entryOf(DyeColor.WHITE, Blocks.WHITE_WOOL), entryOf(DyeColor.LIGHT_GRAY, Blocks.LIGHT_GRAY_WOOL),
            entryOf(DyeColor.GRAY, Blocks.GRAY_WOOL), entryOf(DyeColor.BLACK, Blocks.BLACK_WOOL),
            entryOf(DyeColor.RED, Blocks.RED_WOOL), entryOf(DyeColor.ORANGE, Blocks.ORANGE_WOOL),
            entryOf(DyeColor.YELLOW, Blocks.YELLOW_WOOL), entryOf(DyeColor.GREEN, Blocks.GREEN_WOOL),
            entryOf(DyeColor.LIME, Blocks.LIME_WOOL), entryOf(DyeColor.BLUE, Blocks.BLUE_WOOL),
            entryOf(DyeColor.CYAN, Blocks.CYAN_WOOL), entryOf(DyeColor.LIGHT_BLUE, Blocks.LIGHT_BLUE_WOOL),
            entryOf(DyeColor.PURPLE, Blocks.PURPLE_WOOL), entryOf(DyeColor.MAGENTA, Blocks.MAGENTA_WOOL),
            entryOf(DyeColor.PINK, Blocks.PINK_WOOL), entryOf(DyeColor.BROWN, Blocks.BROWN_WOOL));

    static <K, V> Map.Entry<K, V> entryOf(K key, V value) {
        return new AbstractMap.SimpleImmutableEntry<>(key, value);
    }

    @Override
    public String getID() {
        return TwilightForestMod.prefix("quest_ram_wool").toString();
    }

    @Override
    public void addProbeEntityInfo(ProbeMode probeMode, IProbeInfo iProbeInfo, Player player, Level level, Entity entity, IProbeHitEntityData iProbeHitEntityData) {
        if (entity instanceof QuestRam ram) {
            iProbeInfo.element(new QuestRamWoolElement(ram.getColorFlags()));
        }
    }
}
