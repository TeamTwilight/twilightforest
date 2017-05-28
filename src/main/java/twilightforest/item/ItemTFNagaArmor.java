package twilightforest.item;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.TwilightForestMod;
import twilightforest.client.ModelRegisterCallback;

public class ItemTFNagaArmor extends ItemArmor implements ModelRegisterCallback {

	public ItemTFNagaArmor(ItemArmor.ArmorMaterial par2EnumArmorMaterial, EntityEquipmentSlot slot) {
		super(par2EnumArmorMaterial, 0, slot);
		this.setCreativeTab(TFItems.creativeTab);
	}

	@Override
	public String getArmorTexture(ItemStack itemstack, Entity entity, EntityEquipmentSlot slot, String layer) {
		
		
        if(itemstack.getItem() == TFItems.plateNaga)
        {
                return TwilightForestMod.ARMOR_DIR + "naga_scale_1.png";
        }
        if(itemstack.getItem() == TFItems.legsNaga)
        {
                return TwilightForestMod.ARMOR_DIR + "naga_scale_2.png";
        }
        return TwilightForestMod.ARMOR_DIR + "naga_scale_1.png";
	}
}
