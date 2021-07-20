package twilightforest.loot;

import com.google.common.collect.Maps;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSyntaxException;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootFunction;
import net.minecraft.loot.LootFunctionType;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.functions.ILootFunction;
import net.minecraft.util.JSONUtils;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.IRegistryDelegate;

import java.util.Map;

// Loot condition for checking that if a mod exists, then swap original item with its deserialized item.
public class LootFunctionModItemSwap extends LootFunction {

    private final Item item;
    private final Item oldItem;
    private final boolean success;

    protected LootFunctionModItemSwap(ILootCondition[] conditionsIn, Item itemIn, Item old, boolean success) {
        super(conditionsIn);
        this.item = itemIn;
        this.oldItem = old;
        this.success = success;
    }

    @Override
    public LootFunctionType getFunctionType() {
        return TFTreasure.ITEM_OR_DEFAULT;
    }

    @Override
    public ItemStack doApply(ItemStack stack, LootContext context) {
        ItemStack newStack = new ItemStack(item, stack.getCount());

        newStack.setTag(stack.getTag());

        return newStack;
    }

    public static LootFunctionModItemSwap.Builder builder() {
        return new LootFunctionModItemSwap.Builder();
    }

    public static class Builder extends LootFunction.Builder<LootFunctionModItemSwap.Builder> {
        private String idtocheck;
        private Item item;
        private Item oldItem;

        protected LootFunctionModItemSwap.Builder doCast() {
            return this;
        }

        public LootFunctionModItemSwap.Builder apply(String modid, Item item, Item old) {
            this.idtocheck = modid;
            this.item = item;
            this.oldItem = old;
            return this;
        }

        public ILootFunction build() {
            return new LootFunctionModItemSwap(this.getConditions(), item, oldItem, ModList.get().isLoaded(idtocheck));
        }
    }

    public static class Serializer extends LootFunction.Serializer<LootFunctionModItemSwap> {

		@Override
		public void serialize(JsonObject object, LootFunctionModItemSwap function, JsonSerializationContext serializationContext) {
			if (function.success)
				object.addProperty("item", function.item.getRegistryName().toString());
			else
				object.addProperty("default", function.item.getRegistryName().toString());
			object.addProperty("default", function.oldItem.getRegistryName().toString());
		}

		@Override
        public LootFunctionModItemSwap deserialize(JsonObject object, JsonDeserializationContext deserializationContext, ILootCondition[] conditionsIn) {
            Item item;
            boolean success;

            try {
                item = JSONUtils.getItem(object, "item");
                success = true;
            } catch (JsonSyntaxException e) {
                item = JSONUtils.getItem(object, "default");
                success = false;
            }

            return new LootFunctionModItemSwap(conditionsIn, item, JSONUtils.getItem(object, "default"), success);
        }
    }
}
