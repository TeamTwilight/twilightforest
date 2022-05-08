package twilightforest.compat.undergarden;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.block.TFBlocks;
import twilightforest.compat.UndergardenCompat;

public class MoonwormSlingshotProjectile extends BugSlingshotProjectile {

	public MoonwormSlingshotProjectile(EntityType<? extends BugSlingshotProjectile> type, Level level) {
		super(type, level);
	}

	public MoonwormSlingshotProjectile(Level level, LivingEntity shooter) {
		super(UndergardenCompat.MOONWORM_SLINGSHOT.get(), level, shooter);
		this.setDropItem(true);
	}

	@Override
	protected Item getDefaultItem() {
		return TFBlocks.MOONWORM.get().asItem();
	}

	@Override
	protected BlockState getBug() {
		return TFBlocks.MOONWORM.get().defaultBlockState();
	}

	@Override
	protected ItemStack getSquishResult() {
		return Items.LIME_DYE.getDefaultInstance();
	}
}
