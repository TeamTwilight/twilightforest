package twilightforest.compat.tcon.trait;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.entity.EntityProjectileBase;
import slimeknights.tconstruct.library.traits.AbstractProjectileTrait;
import twilightforest.world.TFWorld;

import javax.annotation.Nullable;
import java.util.List;

public class TraitTwilit extends AbstractProjectileTrait {

    private static final float bonus = 2.0f;

    public TraitTwilit() {
        super("twilit", TextFormatting.GOLD);
    }

    @Override
    public void miningSpeed(ItemStack tool, PlayerEvent.BreakSpeed event) {
        if (TFWorld.isTwilightForest(event.getEntity().world))
            event.setNewSpeed(event.getNewSpeed() + bonus);
    }

    @Override
    public float damage(ItemStack tool, EntityLivingBase player, EntityLivingBase target, float damage, float newDamage, boolean isCritical) {
        if (TFWorld.isTwilightForest(target.world))
            return super.damage(tool, player, target, damage, newDamage, isCritical);
        else
            return super.damage(tool, player, target, damage, newDamage + bonus, isCritical);
    }

    @Override
    public void onLaunch(EntityProjectileBase projectileBase, World world, @Nullable EntityLivingBase shooter) {
        if (!TFWorld.isTwilightForest(projectileBase.world)) return;

        projectileBase.motionX += (projectileBase.motionX * bonus * 0.1f);
        projectileBase.motionY += (projectileBase.motionY * bonus * 0.1f);
        projectileBase.motionZ += (projectileBase.motionZ * bonus * 0.1f);
    }

    @Override
    public List<String> getExtraInfo(ItemStack tool, NBTTagCompound modifierTag) {
        String speed  = String.format(LOC_Extra + ".speed", getModifierIdentifier());
        String damage = String.format(LOC_Extra + ".damage", getModifierIdentifier());

        return ImmutableList.of(
                Util.translateFormatted(speed , Util.df.format(bonus)),
                Util.translateFormatted(damage, Util.df.format(bonus))
        );
    }
}
