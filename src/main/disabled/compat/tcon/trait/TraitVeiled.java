package twilightforest.compat.tcon.trait;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.entity.EntityProjectileBase;
import slimeknights.tconstruct.library.traits.AbstractProjectileTrait;

import javax.annotation.Nullable;

public class TraitVeiled extends AbstractProjectileTrait {
    public TraitVeiled() {
        super("veiled", TextFormatting.GRAY);
    }

    @Override
    public void onLaunch(EntityProjectileBase projectileBase, World world, @Nullable LivingEntity shooter) {
        projectileBase.setInvisible(true);
    }
}
