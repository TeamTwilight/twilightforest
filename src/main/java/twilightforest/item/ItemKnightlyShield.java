package twilightforest.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class ItemKnightlyShield extends ShieldItem {

    public ItemKnightlyShield(Properties props) {
    	super(props);
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
		return new TranslationTextComponent(this.getTranslationKey(stack));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {}

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return repair.getItem() == TFItems.knightmetal_ingot.get() || !ItemTags.PLANKS.contains(repair.getItem()) && super.getIsRepairable(toRepair, repair);
    }

    @Override
    public boolean isShield(ItemStack stack, @Nullable LivingEntity entity) {
        return true;
    }
}
