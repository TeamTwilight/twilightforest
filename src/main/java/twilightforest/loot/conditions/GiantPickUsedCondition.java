package twilightforest.loot.conditions;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import twilightforest.init.TFDataAttachments;
import twilightforest.init.TFLoot;

import java.util.Set;

public record GiantPickUsedCondition(LootContext.EntityTarget target) implements LootItemCondition {

	public static final MapCodec<GiantPickUsedCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(LootContext.EntityTarget.CODEC.fieldOf("entity").forGetter(o -> o.target)).apply(instance, GiantPickUsedCondition::new));

	@Override
	public LootItemConditionType getType() {
		return TFLoot.GIANT_PICK_USED_CONDITION.get();
	}

	@Override
	public Set<ContextKey<?>> getReferencedContextParams() {
		return ImmutableSet.of(this.target.getParam());
	}

	@Override
	public boolean test(LootContext context) {
		if (context.getOptionalParameter(this.target.getParam()) instanceof Player player) {
			var attachment = player.getData(TFDataAttachments.GIANT_PICKAXE_MINING);
			return player.level().getGameTime() == attachment.getMining() && attachment.canMakeGiantBlock();
		}
		return false;
	}

	public static LootItemCondition.Builder builder(LootContext.EntityTarget target) {
		return () -> new GiantPickUsedCondition(target);
	}
}