package twilightforest.compat.tcon.trait;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextFormatting;
import slimeknights.tconstruct.library.traits.AbstractTrait;

public class TraitStalwart extends AbstractTrait {
    public TraitStalwart() {
        super("stalwart", TextFormatting.GRAY);
    }

    @Override
    public void onHit(ItemStack tool, EntityLivingBase player, EntityLivingBase target, float damage, boolean isCritical) {
        if (isCritical || random.nextInt(10) == 0)
            player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 600, isCritical ? 0 : 2));
    }
}
