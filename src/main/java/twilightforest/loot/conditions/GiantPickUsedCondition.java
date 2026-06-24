package twilightforest.loot.conditions;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import twilightforest.init.TFDataAttachments;

import java.util.Set;

public record GiantPickUsedCondition(LootContext.EntityTarget target) implements LootItemCondition {

	public static final MapCodec<GiantPickUsedCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(LootContext.EntityTarget.CODEC.fieldOf("entity").forGetter(o -> o.target)).apply(instance, GiantPickUsedCondition::new));

	@Override
	public MapCodec<? extends LootItemCondition> codec() {
		return CODEC;
	}

	@Override
	public Set<ContextKey<?>> getReferencedContextParams() {
		return ImmutableSet.of(this.target.contextParam());
	}

	@Override
	public boolean test(LootContext context) {
		if (context.getOptionalParameter(this.target.contextParam()) instanceof Player player) {
			var attachment = player.getData(TFDataAttachments.GIANT_PICKAXE_MINING);
			return player.level().getGameTime() == attachment.getMining() && attachment.canMakeGiantBlock();
		}
		return false;
	}

	public static LootItemCondition.Builder builder(LootContext.EntityTarget target) {
		return () -> new GiantPickUsedCondition(target);
	}
}