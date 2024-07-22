package twilightforest.compat.top;

import mcjty.theoneprobe.api.IProbeHitEntityData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoEntityProvider;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import twilightforest.TwilightForestMod;
import twilightforest.entity.passive.QuestRam;

public enum TOPQuestRamWoolProvider implements IProbeInfoEntityProvider {

	INSTANCE;

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
