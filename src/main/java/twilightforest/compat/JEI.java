package twilightforest.compat;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import net.minecraft.item.ItemStack;
import twilightforest.block.TFBlocks;

@JEIPlugin
public class JEI implements IModPlugin {
    @Override
    public void register(IModRegistry registry) {
        registry.addRecipeCatalyst(new ItemStack(TFBlocks.uncrafting_table), VanillaRecipeCategoryUid.CRAFTING);
    }
}
