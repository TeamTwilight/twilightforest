package twilightforest.entity.boss;

import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.Structure;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFStructures;

public class PlateauBoss extends BaseTFBoss {

	public PlateauBoss(EntityType<? extends PlateauBoss> type, Level level) {
		super(type, level);
		this.xpReward = 647;
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Monster.createMonsterAttributes();
	}

	@Override
	public int getHomeRadius() {
		return 30;
	}

	@Override
	public ResourceKey<Structure> getHomeStructure() {
		return TFStructures.FINAL_CASTLE;
	}

	@Override
	public Block getDeathContainer(RandomSource random) {
		return TFBlocks.CANOPY_CHEST.get();
	}

	@Override
	public Block getBossSpawner() {
		return TFBlocks.FINAL_BOSS_BOSS_SPAWNER.get();
	}

	@Override
	public int getBossBarColor() {
		return 0xFFFFFF;
	}
}
