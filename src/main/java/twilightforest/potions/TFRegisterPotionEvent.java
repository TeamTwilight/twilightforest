package twilightforest.potions;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import twilightforest.TwilightForestMod;

@Mod.EventBusSubscriber(modid = TwilightForestMod.ID)
public class TFRegisterPotionEvent {

	@SubscribeEvent
	public static void onRegisterPotions(RegistryEvent.Register<Potion> event) {
		registerPotion("frosted", new PotionFrosted(true, 0x56CBFD).setPotionName(TwilightForestMod.ID + ".effect.frosted").registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, "CE9DBC2A-EE3F-43F5-9DF7-F7F1EE4915A9", -0.15000000596046448D, 2), event);
	}

	public static void registerPotion(String name, Potion potion, RegistryEvent.Register<Potion> event){
		event.getRegistry().register(potion.setRegistryName(new ResourceLocation(TwilightForestMod.ID, name)));
	}

}
