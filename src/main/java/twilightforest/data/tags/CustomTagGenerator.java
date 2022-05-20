package twilightforest.data.tags;

import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.common.data.ExistingFileHelper;
import twilightforest.TwilightForestMod;

import javax.annotation.Nullable;

import static twilightforest.TFConstants.MOD_ID;

//a place to hold all custom tags, since I imagine we wont have a lot of them
public class CustomTagGenerator {

	public static class EnchantmentTagGenerator extends TagsProvider<Enchantment> {

		public static final TagKey<Enchantment> PHANTOM_ARMOR_BANNED_ENCHANTS = TagKey.create(Registry.ENCHANTMENT_REGISTRY, TwilightForestMod.prefix("phantom_armor_banned_enchants"));

		public EnchantmentTagGenerator(DataGenerator generatorIn, @Nullable ExistingFileHelper existingFileHelper) {
			super(generatorIn, Registry.ENCHANTMENT, MOD_ID, existingFileHelper);
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
