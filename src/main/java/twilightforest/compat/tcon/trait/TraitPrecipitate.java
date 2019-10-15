package twilightforest.compat.tcon.trait;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;
import slimeknights.tconstruct.library.entity.EntityProjectileBase;
import slimeknights.tconstruct.library.traits.AbstractProjectileTrait;

import javax.annotation.Nullable;

public class TraitPrecipitate extends AbstractProjectileTrait {
    public TraitPrecipitate() {
        super("precipitate", TextFormatting.DARK_GREEN);
    }

    @Override
    public void miningSpeed(ItemStack tool, PlayerEvent.BreakSpeed event) {
        event.setNewSpeed(event.getNewSpeed() + (getBonusPercentage(event.getEntityLiving()) * event.getOriginalSpeed()));
    }

    @Override
    public void onLaunch(EntityProjectileBase projectileBase, World world, @Nullable EntityLivingBase shooter) {
        float bonus = getBonusPercentage(shooter);

        projectileBase.motionX += (projectileBase.motionX * bonus);
        projectileBase.motionY += (projectileBase.motionY * bonus);
        projectileBase.motionZ += (projectileBase.motionZ * bonus);
    }

    private float getBonusPercentage(EntityLivingBase entity) {
        if (entity == null) return 0.1f;
        float maxHealth = entity.getMaxHealth();
        return (maxHealth - entity.getHealth()) / maxHealth;
    }
}
