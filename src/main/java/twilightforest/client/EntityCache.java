package twilightforest.client;

import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.creaking.Creaking;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twilightforest.TwilightForestMod;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

public class EntityCache {

    private static final Logger LOGGER = LoggerFactory.getLogger(TwilightForestMod.ID + "/EntityCache");
    private static final Map<EntityType<?>, Entity> ENTITY_MAP = new WeakHashMap<>();
    private static final Set<EntityType<?>> IGNORED_ENTITIES = new HashSet<>();

    @Nullable
    public static Entity fetchEntity(EntityType<?> type) {
        Level level = Minecraft.getInstance().level;
        if (level == null || IGNORED_ENTITIES.contains(type)) {
            return null;
        }

        if (type == EntityType.PLAYER) {
            type = EntityType.MANNEQUIN;
        }

        try {
            return ENTITY_MAP.computeIfAbsent(type, t -> {
                long start = System.currentTimeMillis();
                CompoundTag tag = new CompoundTag();
                tag.putString("id", BuiltInRegistries.ENTITY_TYPE.getKey(t).toString());
                Entity created = EntityType.loadEntityRecursive(tag, level, EntitySpawnReason.COMMAND, input -> {
                    input.setId(-1);
                    input.setYRot(0.0F);
                    input.setYHeadRot(0.0F);
                    input.setYBodyRot(0.0F);
                    input.setOldRot();
                    input.setCustomNameVisible(false);
                    input.needsSync = false;
                    input.hurtMarked = false;
                    if (input instanceof Mob mob) {
                        mob.setNoAi(true);
                    }
                    //eye glowing isnt stored via NBT
                    if (input instanceof Creaking creaking) {
                        creaking.setIsActive(true);
                    }
                    return input;
                });
                LOGGER.trace("{} creation took {}ms", t.getDescription().getString(), System.currentTimeMillis() - start);
                return created;
            });
        } catch (Exception e) {
            LOGGER.error("Failed to cache a render for entity {}", type.getDescriptionId(), e);
            addEntityToBlacklist(type);

            return null;
        }
    }

    @Nullable
    public static ItemEntity fetchItemEntity(ItemStack itemStack) {
        if (itemStack.isEmpty() || !(fetchEntity(EntityType.ITEM) instanceof ItemEntity itemEntity))
            return null;

        itemEntity.setItem(itemStack);
        return itemEntity;

    }

    public static void addEntityToBlacklist(EntityType<?> type) {
        IGNORED_ENTITIES.add(type);
        ENTITY_MAP.remove(type);
    }

    public static void clearCache() {
        ENTITY_MAP.clear();
    }

}
