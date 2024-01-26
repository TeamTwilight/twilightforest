package twilightforest.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.util.EntityExcludedDamageSource;

public class TFDamageTypes {
    public static final ResourceKey<DamageType> GHAST_TEAR = create("ghast_tear"); //Ur-Ghast
    public static final ResourceKey<DamageType> HYDRA_BITE = create("hydra_bite"); //Hydra
    public static final ResourceKey<DamageType> HYDRA_FIRE = create("hydra_fire"); //Hydra

    public static final ResourceKey<DamageType> HYDRA_MORTAR = create("hydra_mortar"); //Hydra
    public static final ResourceKey<DamageType> LICH_BOLT = create("lich_bolt"); //Lich
    public static final ResourceKey<DamageType> LICH_BOMB = create("lich_bomb"); //Lich
    public static final ResourceKey<DamageType> CHILLING_BREATH = create("chilling_breath"); //Snow Queen
    public static final ResourceKey<DamageType> SQUISH = create("squish"); //Snow Queen
    public static final ResourceKey<DamageType> THROWN_AXE = create("thrown_axe");
    public static final ResourceKey<DamageType> THROWN_PICKAXE = create("thrown_pickaxe");
    public static final ResourceKey<DamageType> THORNS = create("thorns");
    public static final ResourceKey<DamageType> KNIGHTMETAL = create("knightmetal");
    public static final ResourceKey<DamageType> FIERY = create("fiery");
    public static final ResourceKey<DamageType> FIRE_JET = create("fire_jet");
    public static final ResourceKey<DamageType> REACTOR = create("reactor");
    public static final ResourceKey<DamageType> SLIDER = create("slider");
    public static final ResourceKey<DamageType> THROWN_BLOCK = create("thrown_block");
    public static final ResourceKey<DamageType> AXING = create("axing"); //Minotaur, Minoshroom
    public static final ResourceKey<DamageType> SLAM = create("slam"); //Minoshroom
    public static final ResourceKey<DamageType> YEETED = create("yeeted"); //Yeti, Alpha Yeti
    public static final ResourceKey<DamageType> ANT = create("ant"); //Giants
    public static final ResourceKey<DamageType> HAUNT = create("haunt"); //Knight Phantom, Wraith
    public static final ResourceKey<DamageType> CLAMPED = create("clamped"); //Pinch Beetle
    public static final ResourceKey<DamageType> SCORCHED = create("scorched"); //Fire Beetle
    public static final ResourceKey<DamageType> FROZEN = create("frozen"); //
    public static final ResourceKey<DamageType> SPIKED = create("spiked"); //Block and Chain
    public static final ResourceKey<DamageType> LEAF_BRAIN = create("leaf_brain"); //Skeleton Druid
    public static final ResourceKey<DamageType> LOST_WORDS = create("lost_words"); //Death Tome
    public static final ResourceKey<DamageType> SCHOOLED = create("schooled"); //Death Tome 2
    public static final ResourceKey<DamageType> SNOWBALL_FIGHT = create("snowball_fight"); //Ice Core
    public static final ResourceKey<DamageType> TWILIGHT_SCEPTER = create("twilight_scepter");
    public static final ResourceKey<DamageType> LIFEDRAIN = create("lifedrain");
    public static final ResourceKey<DamageType> EXPIRED = create("expired");
    public static final ResourceKey<DamageType> FALLING_ICE = create("falling_ice");
    public static final ResourceKey<DamageType> MOONWORM = create("moonworm"); //Moonworm
    public static final ResourceKey<DamageType> ACID_RAIN = create("acid_rain"); //Acid rain Enforcement

    public static ResourceKey<DamageType> create(String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, TwilightForestMod.prefix(name));
    }

    public static DamageSource getDamageSource(Level level, ResourceKey<DamageType> type, EntityType<?>... toIgnore) {
        return getEntityDamageSource(level, type, null, toIgnore);
    }

    public static DamageSource getEntityDamageSource(Level level, ResourceKey<DamageType> type, @Nullable Entity attacker, EntityType<?>... toIgnore) {
        return getIndirectEntityDamageSource(level, type, attacker, attacker, toIgnore);
    }

    public static DamageSource getIndirectEntityDamageSource(Level level, ResourceKey<DamageType> type, @Nullable Entity attacker, @Nullable Entity indirectAttacker, EntityType<?>... toIgnore) {
        return toIgnore.length > 0 ? new EntityExcludedDamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(type), toIgnore) : new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(type), attacker, indirectAttacker);
    }
            
    public static void bootstrap(BootstapContext<DamageType> context) {
        context.register(GHAST_TEAR, new DamageType("twilightforest.ghastTear", 0.0F));
        context.register(HYDRA_BITE, new DamageType("twilightforest.hydraBite", 0.0F));
        context.register(HYDRA_FIRE, new DamageType("twilightforest.hydraFire", 0.0F));
        context.register(HYDRA_MORTAR, new DamageType("onFire", 0.0F, DamageEffects.BURNING));
        context.register(LICH_BOLT, new DamageType("twilightforest.lichBolt", 0.0F));
        context.register(LICH_BOMB, new DamageType("twilightforest.lichBomb", 0.0F));
        context.register(CHILLING_BREATH, new DamageType("twilightforest.chillingBreath", 0.0F, DamageEffects.FREEZING));
        context.register(SQUISH, new DamageType("twilightforest.squish", 0.0F));
        context.register(THROWN_AXE, new DamageType("twilightforest.thrownAxe", 0.0F));
        context.register(THROWN_PICKAXE, new DamageType("twilightforest.thrownPickaxe", 0.0F));
        context.register(THORNS, new DamageType("twilightforest.thorns", 0.1F));
        context.register(KNIGHTMETAL, new DamageType("twilightforest.knightmetal", 0.1F));
        context.register(FIERY, new DamageType("twilightforest.fiery", 0.1F, DamageEffects.BURNING));
        context.register(FIRE_JET, new DamageType("twilightforest.fireJet", 0.1F, DamageEffects.BURNING));
        context.register(REACTOR, new DamageType("twilightforest.reactor", 0.1F));
        context.register(SLIDER, new DamageType("twilightforest.slider", 0.1F));
        context.register(THROWN_BLOCK, new DamageType("twilightforest.thrownBlock", 0.1F));
        context.register(AXING, new DamageType("twilightforest.axing", 0.1F));
        context.register(SLAM, new DamageType("twilightforest.axing", 0.1F));
        context.register(YEETED, new DamageType("twilightforest.yeeted", 0.1F));
        context.register(ANT, new DamageType("twilightforest.ant", 0.1F));
        context.register(HAUNT, new DamageType("twilightforest.haunt", 0.1F));
        context.register(CLAMPED, new DamageType("twilightforest.clamped", 0.1F));
        context.register(SCORCHED, new DamageType("twilightforest.scorched", 0.1F, DamageEffects.BURNING));
        context.register(FROZEN, new DamageType("twilightforest.frozen", 0.1F, DamageEffects.FREEZING));
        context.register(SPIKED, new DamageType("twilightforest.spiked", 0.1F));
        context.register(LEAF_BRAIN, new DamageType("twilightforest.leafBrain", 0.1F));
        context.register(LOST_WORDS, new DamageType("twilightforest.lostWords", 0.1F));
        context.register(SCHOOLED, new DamageType("twilightforest.schooled", 0.1F));
        context.register(SNOWBALL_FIGHT, new DamageType("twilightforest.snowballFight", 0.1F, DamageEffects.FREEZING));
        context.register(TWILIGHT_SCEPTER, new DamageType("indirectMagic", 0.0F));
        context.register(LIFEDRAIN, new DamageType("twilightforest.lifedrain", 0.0F));
        context.register(EXPIRED, new DamageType("twilightforest.expired", 0.0F));
        context.register(FALLING_ICE, new DamageType("fallingBlock", 0.1F));
        context.register(MOONWORM, new DamageType("twilightforest.moonworm", 0.0F));
        context.register(ACID_RAIN, new DamageType("twilightforest.acid_rain", 0.0F));
    }
}
