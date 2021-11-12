package twilightforest.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;
import twilightforest.TwilightForestMod;
import twilightforest.entity.TFEntities;

import javax.annotation.Nullable;

public class EntityTagGenerator extends EntityTypeTagsProvider {
    public static final Tag.Named<EntityType<?>> BOSSES = EntityTypeTags.bind(TwilightForestMod.prefix("bosses").toString());
    public static final Tag.Named<EntityType<?>> LICH_POPPABLES = EntityTypeTags.bind(TwilightForestMod.prefix("lich_poppables").toString());

    public EntityTagGenerator(DataGenerator dataGenerator, @Nullable ExistingFileHelper existingFileHelper) {
        super(dataGenerator, TwilightForestMod.ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        tag(EntityTypeTags.SKELETONS).add(TFEntities.SKELETON_DRUID);
        tag(EntityTypeTags.ARROWS).add(TFEntities.ICE_ARROW, TFEntities.SEEKER_ARROW);
        tag(EntityTypeTags.FREEZE_HURTS_EXTRA_TYPES).add(TFEntities.FIRE_BEETLE);

        tag(BOSSES).add(
                TFEntities.NAGA,
                TFEntities.LICH,
                TFEntities.MINOSHROOM,
                TFEntities.HYDRA,
                TFEntities.KNIGHT_PHANTOM,
                TFEntities.UR_GHAST,
                TFEntities.ALPHA_YETI,
                TFEntities.SNOW_QUEEN,
                TFEntities.PLATEAU_BOSS
        );

        tag(EntityTypeTags.IMPACT_PROJECTILES).add(
                TFEntities.NATURE_BOLT,
                TFEntities.LICH_BOLT,
                TFEntities.WAND_BOLT,
                TFEntities.LICH_BOMB,
                TFEntities.CICADA_SHOT,
                TFEntities.MOONWORM_SHOT,
                TFEntities.SLIME_BLOB,
                TFEntities.THROWN_WEP,
                TFEntities.THROWN_ICE,
                TFEntities.FALLING_ICE,
                TFEntities.ICE_SNOWBALL
        );

        tag(EntityTypeTags.POWDER_SNOW_WALKABLE_MOBS).add(
                TFEntities.PENGUIN,
                TFEntities.STABLE_ICE_CORE,
                TFEntities.UNSTABLE_ICE_CORE,
                TFEntities.SNOW_GUARDIAN,
                TFEntities.ICE_CRYSTAL
        ).add(
                TFEntities.RAVEN,
                TFEntities.SQUIRREL,
                TFEntities.DWARF_RABBIT,
                TFEntities.TINY_BIRD,
                TFEntities.KOBOLD,
                TFEntities.DEATH_TOME,
                TFEntities.MOSQUITO_SWARM,
                TFEntities.TOWERWOOD_BORER
        );

        tag(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES).add(
                TFEntities.PENGUIN,
                TFEntities.STABLE_ICE_CORE,
                TFEntities.UNSTABLE_ICE_CORE,
                TFEntities.SNOW_GUARDIAN,
                TFEntities.ICE_CRYSTAL
        ).add(
                TFEntities.WRAITH,
                TFEntities.KNIGHT_PHANTOM,
                TFEntities.WINTER_WOLF,
                TFEntities.YETI,
                TFEntities.ALPHA_YETI,
                TFEntities.SNOW_QUEEN
        );

        tag(LICH_POPPABLES).addTag(EntityTypeTags.SKELETONS).add(EntityType.ZOMBIE, EntityType.ENDERMAN, EntityType.SPIDER, EntityType.CREEPER, TFEntities.SWARM_SPIDER);
    }
}
