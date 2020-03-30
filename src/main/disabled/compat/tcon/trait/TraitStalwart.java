package twilightforest.compat.tcon.trait;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.text.TextFormatting;
import slimeknights.tconstruct.library.traits.AbstractTrait;

public class TraitStalwart extends AbstractTrait {
    public TraitStalwart() {
        super("stalwart", TextFormatting.GRAY);
    }

    @Override
    public void onHit(ItemStack tool, LivingEntity player, LivingEntity target, float damage, boolean isCritical) {
        if (isCritical || random.nextInt(10) == 0)
            player.addPotionEffect(new EffectInstance(Effects.RESISTANCE, 200));
    }
}
