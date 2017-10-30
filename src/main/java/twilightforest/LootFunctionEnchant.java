package twilightforest;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSyntaxException;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.init.Items;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraftforge.common.util.Constants;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

// Similar to EnchantRandomly but applies everything and with exact levels
public class LootFunctionEnchant extends LootFunction {
	private final Map<Enchantment, Short> enchantments;

	protected LootFunctionEnchant(LootCondition[] conditions, Map<Enchantment, Short> enchantments) {
		super(conditions);
		this.enchantments = enchantments;
	}

	@Override
	public ItemStack apply(ItemStack stack, Random rand, LootContext context) {
		for (Map.Entry<Enchantment, Short> e : enchantments.entrySet()) {
			if (stack.getItem() == Items.ENCHANTED_BOOK) {
				ItemEnchantedBook.addEnchantment(stack, new EnchantmentData(e.getKey(), e.getValue()));
			} else {
				addEnchantment(stack, e.getKey(), e.getValue());
			}
		}

		return stack;
	}

	// Not using stack.addEnchantment because it doesn't handle duplicates like enchanted book does
	private void addEnchantment(ItemStack stack, Enchantment e, short level) {
		if (stack.getTagCompound() == null) {
			stack.setTagCompound(new NBTTagCompound());
		}

		final String enchantedCompoundKey = stack.getItem() == Items.ENCHANTED_BOOK ? "StoredEnchantments" : "ench";

		if (!stack.getTagCompound().hasKey(enchantedCompoundKey, Constants.NBT.TAG_LIST)) {
			stack.getTagCompound().setTag(enchantedCompoundKey, new NBTTagList());
		}

		NBTTagList list = stack.getTagCompound().getTagList(enchantedCompoundKey, Constants.NBT.TAG_COMPOUND);

		for (int i = 0; i < list.tagCount(); i++) {
			NBTTagCompound existing = list.getCompoundTagAt(i);
			if (existing.getShort("id") == Enchantment.getEnchantmentID(e)) {
				existing.setShort("lvl", level);
				return;
			}
		}

		NBTTagCompound newCmp = new NBTTagCompound();
		newCmp.setShort("id", (short) Enchantment.getEnchantmentID(e));
		newCmp.setShort("lvl", level);
		list.appendTag(newCmp);
	}

	public static class Serializer extends LootFunction.Serializer<LootFunctionEnchant> {
		protected Serializer() {
			super(new ResourceLocation(TwilightForestMod.ID, "enchant"), LootFunctionEnchant.class);
		}

		@Override
		public void serialize(JsonObject object, LootFunctionEnchant function, JsonSerializationContext ctx) {
			if (!function.enchantments.isEmpty()) {
				JsonObject obj = new JsonObject();

				for (Map.Entry<Enchantment, Short> e : function.enchantments.entrySet()) {
					obj.addProperty(e.getKey().getRegistryName().toString(), e.getValue());
				}

				object.add("enchantments", obj);
			}
		}

		@Override
		public LootFunctionEnchant deserialize(JsonObject object, JsonDeserializationContext ctx, LootCondition[] conditions) {
			Map<Enchantment, Short> enchantments = new HashMap<>();

			if (object.has("enchantments")) {
				JsonObject enchantObj = JsonUtils.getJsonObject(object, "enchantments");

				for (Map.Entry<String, JsonElement> e : enchantObj.entrySet()) {
					Enchantment ench = Enchantment.REGISTRY.getObject(new ResourceLocation(e.getKey()));
					if (ench == null) {
						throw new JsonSyntaxException("Can't find enchantment " + e.getKey());
					}

					short lvl = e.getValue().getAsShort();

					for (Enchantment other : enchantments.keySet()) {
						if (!ench.isCompatibleWith(other)) {
							throw new JsonParseException(String.format("Enchantments %s and %s conflict", ench.getRegistryName(), other.getRegistryName()));
						}
					}

					enchantments.put(ench, lvl);
				}
			}

			return new LootFunctionEnchant(conditions, enchantments);
		}
	}
}
