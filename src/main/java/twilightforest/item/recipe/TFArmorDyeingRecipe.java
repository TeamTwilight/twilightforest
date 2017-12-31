package twilightforest.item.recipe;

import com.google.common.collect.Lists;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.RecipesArmorDyes;
import net.minecraft.world.World;
import twilightforest.item.ItemTFArcticArmor;

import java.util.List;

/* Copied from net.minecraft.item.crafting.RecipesArmorDyes with a bit of modification */
public class TFArmorDyeingRecipe extends RecipesArmorDyes {
    public boolean matches(InventoryCrafting inv, World worldIn) {
        ItemStack itemstack = ItemStack.EMPTY;
        List<ItemStack> list = Lists.<ItemStack>newArrayList();

        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack itemstack1 = inv.getStackInSlot(i);

            if (!itemstack1.isEmpty()) {
                if (itemstack1.getItem() instanceof ItemTFArcticArmor) {
                    itemstack = itemstack1;
                } else {
                    if (!net.minecraftforge.oredict.DyeUtils.isDye(itemstack1)) {
                        return false;
                    }

                    list.add(itemstack1);
                }
            }
        }

        return !itemstack.isEmpty() && !list.isEmpty();
    }

    public ItemStack getCraftingResult(InventoryCrafting inv) {
        ItemStack stackAccumulator = ItemStack.EMPTY;
        int[] aint = new int[3];
        int i = 0;
        int j = 0;
        ItemTFArcticArmor arcticArmor = null;

        for (int k = 0; k < inv.getSizeInventory(); ++k) {
            ItemStack stackInSlot = inv.getStackInSlot(k);

            if (!stackInSlot.isEmpty()) {
                if (stackInSlot.getItem() instanceof ItemTFArcticArmor) {
                    arcticArmor = (ItemTFArcticArmor)stackInSlot.getItem();

                    stackAccumulator = stackInSlot.copy();
                    stackAccumulator.setCount(1);

                    if (arcticArmor.hasColor(stackInSlot)) {
                        int l = arcticArmor.getColor(stackAccumulator);
                        float f = (float)(l >> 16 & 255) / 255.0F;
                        float f1 = (float)(l >> 8 & 255) / 255.0F;
                        float f2 = (float)(l & 255) / 255.0F;
                        i = (int)((float)i + Math.max(f, Math.max(f1, f2)) * 255.0F);
                        aint[0] = (int)((float)aint[0] + f * 255.0F);
                        aint[1] = (int)((float)aint[1] + f1 * 255.0F);
                        aint[2] = (int)((float)aint[2] + f2 * 255.0F);
                        ++j;
                    }
                } else {
                    if (!net.minecraftforge.oredict.DyeUtils.isDye(stackInSlot)) {
                        return ItemStack.EMPTY;
                    }

                    float[] afloat = net.minecraftforge.oredict.DyeUtils.colorFromStack(stackInSlot).get().getColorComponentValues();
                    int l1 = (int)(afloat[0] * 255.0F);
                    int i2 = (int)(afloat[1] * 255.0F);
                    int j2 = (int)(afloat[2] * 255.0F);
                    i += Math.max(l1, Math.max(i2, j2));
                    aint[0] += l1;
                    aint[1] += i2;
                    aint[2] += j2;
                    ++j;
                }
            }
        }

        if (arcticArmor == null) {
            return ItemStack.EMPTY;
        } else {
            int i1 = aint[0] / j;
            int j1 = aint[1] / j;
            int k1 = aint[2] / j;
            float f3 = (float)i / (float)j;
            float f4 = (float)Math.max(i1, Math.max(j1, k1));
            i1 = (int)((float)i1 * f3 / f4);
            j1 = (int)((float)j1 * f3 / f4);
            k1 = (int)((float)k1 * f3 / f4);
            int k2 = (i1 << 8) + j1;
            k2 = (k2 << 8) + k1;
            arcticArmor.setColor(stackAccumulator, k2);
            return stackAccumulator;
        }
    }
}
