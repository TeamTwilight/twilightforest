package twilightforest.compat;

import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import mezz.jei.api.recipe.transfer.IRecipeTransferInfo;
import net.minecraft.inventory.Slot;
import twilightforest.inventory.ContainerTFUncrafting;

import java.util.ArrayList;
import java.util.List;

public class UncraftingRecipeTransferHandler implements IRecipeTransferInfo<ContainerTFUncrafting> {
    @Override
    public Class<ContainerTFUncrafting> getContainerClass() {
        return ContainerTFUncrafting.class;
    }

    @Override
    public String getRecipeCategoryUid() {
        return VanillaRecipeCategoryUid.CRAFTING;
    }

    @Override
    public boolean canHandle(ContainerTFUncrafting container) {
        return true;
    }

    @Override
    public List<Slot> getRecipeSlots(ContainerTFUncrafting container) {
        List<Slot> slots = new ArrayList<>();

        for(int i = 11; i < 20; i++)
            slots.add(container.getSlot(i));

        return slots;
    }

    @Override
    public List<Slot> getInventorySlots(ContainerTFUncrafting container) {
        List<Slot> slots = new ArrayList<>();

        for(int i = 20; i < container.inventorySlots.size(); i++)
            slots.add(container.getSlot(i));

        return slots;
    }
}
