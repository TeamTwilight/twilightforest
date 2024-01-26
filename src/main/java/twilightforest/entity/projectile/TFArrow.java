package twilightforest.entity.projectile;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public abstract class TFArrow extends AbstractArrow implements ITFProjectile {

	public TFArrow(EntityType<? extends TFArrow> type, Level level) {
		super(type, level, new ItemStack(Items.ARROW));
	}

	public TFArrow(EntityType<? extends TFArrow> type, Level level, @Nullable Entity shooter, ItemStack stack) {
		super(type, level, stack);
		this.setOwner(shooter);
		if (shooter != null) {
			this.setPos(shooter.getX(), shooter.getEyeY() - 0.1D, shooter.getZ());
		}
	}
}
