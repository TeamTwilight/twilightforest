package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.entity.monster.TowerwoodBorer;
import twilightforest.init.TFEntities;

public class InfestedTowerwoodBlock extends Block {

	public InfestedTowerwoodBlock(Properties properties) {
		super(properties);
	}

	@Override
	public void spawnAfterBreak(BlockState state, ServerLevel level, BlockPos pos, ItemStack stack, boolean sourceIsPlayer) {
		super.spawnAfterBreak(state, level, pos, stack, sourceIsPlayer);
		if (!level.isClientSide() && level.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS) && !EnchantmentHelper.hasTag(stack, EnchantmentTags.PREVENTS_INFESTED_SPAWNS)) {
			TowerwoodBorer termite = TFEntities.TOWERWOOD_BORER.get().create(level, EntitySpawnReason.TRIGGERED);
			termite.moveTo(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, 0.0F, 0.0F);
			level.addFreshEntity(termite);
			termite.spawnAnim();
		}
	}
}
