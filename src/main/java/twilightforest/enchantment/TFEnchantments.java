package twilightforest.enchantment;

import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import twilightforest.item.ChainBlockItem;

import static twilightforest.TFConstants.MOD_ID;

public class TFEnchantments {

	public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, MOD_ID);

	public static final RegistryObject<Enchantment> FIRE_REACT = ENCHANTMENTS.register("fire_react", () -> new FireReactEnchantment(Enchantment.Rarity.UNCOMMON));
	public static final RegistryObject<Enchantment> CHILL_AURA = ENCHANTMENTS.register("chill_aura", () -> new ChillAuraEnchantment(Enchantment.Rarity.UNCOMMON));
	public static final RegistryObject<Enchantment> PRESERVATION = ENCHANTMENTS.register("preservation", () -> new PreservationEnchantment(Enchantment.Rarity.RARE));
	public static final RegistryObject<Enchantment> BLOCK_STRENGTH = ENCHANTMENTS.register("block_strength", () -> new BlockStrengthEnchantment(Enchantment.Rarity.RARE));
	public static final RegistryObject<Enchantment> DESTRUCTION = ENCHANTMENTS.register("destruction", () -> new DestructionEnchantment(Enchantment.Rarity.RARE));

	public static final EnchantmentCategory BLOCK_AND_CHAIN = EnchantmentCategory.create("twilightforest_block_and_chain", item -> item instanceof ChainBlockItem);
}
