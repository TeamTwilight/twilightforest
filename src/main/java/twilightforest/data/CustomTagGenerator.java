package twilightforest.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.common.ForgeTagHandler;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeRegistryTagsProvider;
import net.minecraftforge.registries.ForgeRegistries;
import twilightforest.TwilightForestMod;

import javax.annotation.Nullable;

//a place to hold all custom tags, since I imagine we wont have a lot of them
public class CustomTagGenerator {

	public static class EnchantmentTagGenerator extends ForgeRegistryTagsProvider<Enchantment> {

		public static final Tag.Named<Enchantment> PHANTOM_ARMOR_BANNED_ENCHANTS = ForgeTagHandler.createOptionalTag(ForgeRegistries.ENCHANTMENTS, TwilightForestMod.prefix("phantom_armor_banned_enchants"));

		public EnchantmentTagGenerator(DataGenerator generatorIn, @Nullable ExistingFileHelper existingFileHelper) {
			super(generatorIn, ForgeRegistries.ENCHANTMENTS, TwilightForestMod.ID, existingFileHelper);
		}

		@Override
		protected void addTags() {
			tag(PHANTOM_ARMOR_BANNED_ENCHANTS).add(Enchantments.VANISHING_CURSE, Enchantments.BINDING_CURSE);
		}

		@Override
		public String getName() {
			return "Twilight Forest Enchantment Tags";
		}
	}
}
