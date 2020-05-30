package twilightforest.block;

import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import twilightforest.entity.EntityTFTowerTermite;
import twilightforest.entity.TFEntities;

public class BlockInfestedTowerWood extends BlockFlammable {

	public BlockInfestedTowerWood(int flammability, int spreadSpeed, Properties props) {
		super(flammability, spreadSpeed, props);
	}

	@Override
	@Deprecated
	public void spawnAdditionalDrops(BlockState state, World world, BlockPos pos, ItemStack stack) {
		super.spawnAdditionalDrops(state, world, pos, stack);
		if (!world.isRemote && world.getGameRules().getBoolean(GameRules.DO_TILE_DROPS) && EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) == 0) {
			EntityTFTowerTermite termite = new EntityTFTowerTermite(TFEntities.tower_termite.get(), world);
			termite.setLocationAndAngles(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, 0.0F, 0.0F);
			world.addEntity(termite);
			termite.spawnExplosionParticle();
		}

	}
}
