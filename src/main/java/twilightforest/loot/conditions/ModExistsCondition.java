package twilightforest.loot.conditions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.neoforged.fml.ModList;
import twilightforest.init.TFLoot;

// Loot condition for checking if a mod exists.
public class ModExistsCondition implements LootItemCondition {

	public static final Codec<ModExistsCondition> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codec.STRING.fieldOf("mod_id").forGetter(o -> o.modID)).apply(instance, ModExistsCondition::new));

	private final boolean exists;
	private final String modID;

	public ModExistsCondition(String modID) {
		this.exists = ModList.get().isLoaded(modID);
		this.modID = modID;
	}

	@Override
	public LootItemConditionType getType() {
		return TFLoot.MOD_EXISTS.get();
	}

	@Override
	public boolean test(LootContext context) {
		return this.exists;
	}

	public static LootItemCondition.Builder builder(String modid) {
		return () -> new ModExistsCondition(modid);
	}
}
