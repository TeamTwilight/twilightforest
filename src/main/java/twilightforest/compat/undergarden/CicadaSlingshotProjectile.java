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

public class CicadaSlingshotProjectile extends BugSlingshotProjectile {

	public CicadaSlingshotProjectile(EntityType<? extends BugSlingshotProjectile> type, Level level) {
		super(type, level);
	}

	public CicadaSlingshotProjectile(Level level, LivingEntity shooter) {
		super(UndergardenCompat.CICADA_SLINGSHOT.get(), level, shooter);
	}

	@Override
	protected Item getDefaultItem() {
		return TFBlocks.CICADA.get().asItem();
	}

	@Override
	protected BlockState getBug() {
		return TFBlocks.CICADA.get().defaultBlockState();
	}

	@Override
	protected ItemStack getSquishResult() {
		return Items.GRAY_DYE.getDefaultInstance();
	}
}
