package twilightforest.advancements;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.critereon.AbstractCriterionInstance;
import net.minecraft.advancements.critereon.DamagePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.CombatEntry;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class GroupKillTrigger implements ICriterionTrigger<GroupKillTrigger.Instance> {
    private static final ResourceLocation ID = new ResourceLocation(TwilightForestMod.ID, "player_group_killed_entity");
    private final Map<PlayerAdvancements, GroupKillTrigger.Listeners> listeners = Maps.newHashMap();

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public void addListener(PlayerAdvancements playerAdvancementsIn, Listener<Instance> listener) {
        GroupKillTrigger.Listeners listeners = this.listeners.computeIfAbsent(playerAdvancementsIn, Listeners::new);

        listeners.add(listener);
    }

    @Override
    public void removeListener(PlayerAdvancements playerAdvancementsIn, ICriterionTrigger.Listener<GroupKillTrigger.Instance> listener) {
        GroupKillTrigger.Listeners listeners = this.listeners.get(playerAdvancementsIn);

        if (listeners != null)
        {
            listeners.remove(listener);

            if (listeners.isEmpty())
            {
                this.listeners.remove(playerAdvancementsIn);
            }
        }
    }

    @Override
    public void removeAllListeners(PlayerAdvancements playerAdvancementsIn) {
        this.listeners.remove(playerAdvancementsIn);
    }

    @Override
    public Instance deserializeInstance(JsonObject json, JsonDeserializationContext context) {
        return new GroupKillTrigger.Instance(
                JsonUtils.getFloat(json,"damage_threshold_denominator"),
                EntityPredicate.deserialize(json.get("entity_killed")),
                DamagePredicate.deserialize(json.get("required_attack_type")),
                SimpleConditionalPredicate.deserialize(json, "comparator"),
                MinMaxBounds.deserialize(json.get("player_amount")));
    }

    public void trigger(EntityPlayerMP player, EntityLivingBase entityNotLivingAnymore, List<CombatEntry> listOfAllOffenses, int playerCount) {
        Listeners l = listeners.get(player.getAdvancements());
        if (l != null) {
            l.trigger(player, entityNotLivingAnymore, listOfAllOffenses, playerCount);
        }
    }

    static class Instance extends AbstractCriterionInstance {
        private final float damageThreshold;
        private final EntityPredicate entityToKill;
        private final DamagePredicate damagePredicateRequirement;
        private final SimpleConditionalPredicate comparator;
        private final MinMaxBounds playerAmountRequired;

        Instance(float damageThreshold, EntityPredicate entityToKill, DamagePredicate damagePredicateRequirement, SimpleConditionalPredicate comparator, MinMaxBounds playerAmountRequired) {
            super(GroupKillTrigger.ID);
            this.damageThreshold = damageThreshold;
            this.entityToKill = entityToKill;
            this.damagePredicateRequirement = damagePredicateRequirement;
            this.comparator = comparator;
            this.playerAmountRequired = playerAmountRequired;
        }

        boolean test(EntityPlayerMP player, EntityLivingBase entityNotLivingAnymore, List<CombatEntry> listOfAllOffenses, int playerCount) {
            if (entityToKill.test(player, entityNotLivingAnymore) && playerAmountRequired.test(playerCount)) {
                boolean boolAcculumator = comparator == SimpleConditionalPredicate.AND; // That way, we don't trip as automatic-pass if OR, or automatic-fail if AND
                float damageAccumulator = 0;

                for (CombatEntry entry : listOfAllOffenses) {
                    if (!(comparator == SimpleConditionalPredicate.OR && boolAcculumator)) // Basically, if we've already got true on OR, then we don't want to waste more processing on further comparing
                        boolAcculumator = comparator.compare(boolAcculumator, damagePredicateRequirement.test(player, entry.getDamageSrc(), entry.getDamage(), entry.getDamage(), false));

                    System.out.println("damages:" + damageAccumulator);

                    damageAccumulator += entry.getDamage();
                }

                System.out.println(damageAccumulator + " > (" + "(" + entityNotLivingAnymore.getMaxHealth() + "/" + playerCount + ")/" + damageThreshold + ") = " + ((entityNotLivingAnymore.getMaxHealth()/(float) playerCount)/damageThreshold) + ")");

                return boolAcculumator && damageAccumulator > (entityNotLivingAnymore.getMaxHealth()/(float) playerCount)/damageThreshold;
            }
            return false;
        }
    }

    private static class Listeners {
        private final PlayerAdvancements playerAdvancements;
        private final Set<Listener<GroupKillTrigger.Instance>> listeners = Sets.newHashSet();

        Listeners(PlayerAdvancements playerAdvancements) {
            this.playerAdvancements = playerAdvancements;
        }

        public boolean isEmpty() {
            return this.listeners.isEmpty();
        }

        public void add(ICriterionTrigger.Listener<GroupKillTrigger.Instance> listener) {
            this.listeners.add(listener);
        }

        public void remove(ICriterionTrigger.Listener<GroupKillTrigger.Instance> listener) {
            this.listeners.remove(listener);
        }

        public void trigger(EntityPlayerMP player, EntityLivingBase entityNotLivingAnymore, List<CombatEntry> listOfAllOffenses, int playerCount) {
            List<Listener<GroupKillTrigger.Instance>> list = new ArrayList<>();

            for (ICriterionTrigger.Listener<GroupKillTrigger.Instance> listener : this.listeners)
                if (listener.getCriterionInstance().test(player, entityNotLivingAnymore, listOfAllOffenses, playerCount))
                    list.add(listener);

            for (ICriterionTrigger.Listener<GroupKillTrigger.Instance> listener : list)
                listener.grantCriterion(this.playerAdvancements);
        }
    }

    private enum SimpleConditionalPredicate {
        AND((a, b) -> a && b),
        OR((a, b) -> a || b);

        private final ITeenyComparator comparator;
        public boolean compare(boolean a, boolean b) {
            return this.comparator.compare(a, b);
        }

        SimpleConditionalPredicate(ITeenyComparator comparator) {
            this.comparator = comparator;
        }

        public static SimpleConditionalPredicate deserialize(@Nullable JsonElement element, String targetString) {
            try {
                return SimpleConditionalPredicate.valueOf(JsonUtils.getString(element, targetString).toUpperCase(Locale.ROOT));
            } catch (Exception justTakeIt) {
                TwilightForestMod.LOGGER.warn("Looks like you almost lost a bunch of advancements. The problem was caught for you. Git gud. Fix whatever advancement that uses \"twilightforest:player_group_killed_entity\" yourself please.\n" + justTakeIt);
                return OR;
            }
        }
    }

    private interface ITeenyComparator {
        boolean compare(boolean a, boolean b);
    }
}