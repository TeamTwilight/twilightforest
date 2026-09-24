package twilightforest.datagen.data;

import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageType;
import tamaized.beanification.Autowired;
import twilightforest.enums.extensions.TFDamageEffectsEnumExtension;
import twilightforest.init.TFDamageTypes;

public class TFDamageTypeGenerator {

    @Autowired
    private static TFDamageEffectsEnumExtension tfDamageEffectsEnumExtension;

    public static void bootstrap(BootstrapContext<DamageType> context) {
        context.register(TFDamageTypes.GHAST_TEAR, new DamageType("twilightforest.ghastTear", 0.0F));
        context.register(TFDamageTypes.HYDRA_BITE, new DamageType("twilightforest.hydraBite", 0.0F));
        context.register(TFDamageTypes.HYDRA_FIRE, new DamageType("twilightforest.hydraFire", 0.0F));
        context.register(TFDamageTypes.HYDRA_MORTAR, new DamageType("onFire", 0.0F, DamageEffects.BURNING));
        context.register(TFDamageTypes.LICH_BOLT, new DamageType("twilightforest.lichBolt", 0.0F));
        context.register(TFDamageTypes.LICH_BOMB, new DamageType("twilightforest.lichBomb", 0.0F));
        context.register(TFDamageTypes.CHILLING_BREATH, new DamageType("twilightforest.chillingBreath", 0.0F, DamageEffects.FREEZING));
        context.register(TFDamageTypes.SQUISH, new DamageType("twilightforest.squish", 0.0F));
        context.register(TFDamageTypes.THROWN_AXE, new DamageType("twilightforest.thrownAxe", 0.0F));
        context.register(TFDamageTypes.THROWN_PICKAXE, new DamageType("twilightforest.thrownPickaxe", 0.0F));
        context.register(TFDamageTypes.THORNS, new DamageType("twilightforest.thorns", 0.1F));
        context.register(TFDamageTypes.OREBERRY, new DamageType("twilightforest.oreberry", 0.1F));
        context.register(TFDamageTypes.KNIGHTMETAL, new DamageType("twilightforest.knightmetal", 0.1F));
        context.register(TFDamageTypes.FIERY, new DamageType("twilightforest.fiery", 0.1F, DamageEffects.BURNING));
        context.register(TFDamageTypes.FIRE_JET, new DamageType("twilightforest.fireJet", 0.1F, DamageEffects.BURNING));
        context.register(TFDamageTypes.REACTOR, new DamageType("twilightforest.reactor", 0.1F));
        context.register(TFDamageTypes.SLIDER, new DamageType("twilightforest.slider", 0.1F));
        context.register(TFDamageTypes.THROWN_BLOCK, new DamageType("twilightforest.thrownBlock", 0.1F));
        context.register(TFDamageTypes.AXING, new DamageType("twilightforest.axing", 0.1F));
        context.register(TFDamageTypes.SLAM, new DamageType("twilightforest.axing", 0.1F));
        context.register(TFDamageTypes.YEETED, new DamageType("twilightforest.yeeted", 0.1F));
        context.register(TFDamageTypes.ANT, new DamageType("twilightforest.ant", 0.1F));
        context.register(TFDamageTypes.HAUNT, new DamageType("twilightforest.haunt", 0.1F));
        context.register(TFDamageTypes.CLAMPED, new DamageType("twilightforest.clamped", 0.1F, tfDamageEffectsEnumExtension.PINCH));
        context.register(TFDamageTypes.SCORCHED, new DamageType("twilightforest.scorched", 0.1F, DamageEffects.BURNING));
        context.register(TFDamageTypes.FROZEN, new DamageType("twilightforest.frozen", 0.1F, DamageEffects.FREEZING));
        context.register(TFDamageTypes.SPIKED, new DamageType("twilightforest.spiked", 0.1F));
        context.register(TFDamageTypes.LEAF_BRAIN, new DamageType("twilightforest.leafBrain", 0.1F));
        context.register(TFDamageTypes.LOST_WORDS, new DamageType("twilightforest.lostWords", 0.1F));
        context.register(TFDamageTypes.SCHOOLED, new DamageType("twilightforest.schooled", 0.1F));
        context.register(TFDamageTypes.SNOWBALL_FIGHT, new DamageType("twilightforest.snowballFight", 0.1F, DamageEffects.FREEZING));
        context.register(TFDamageTypes.TWILIGHT_SCEPTER, new DamageType("indirectMagic", 0.0F));
        context.register(TFDamageTypes.LIFEDRAIN, new DamageType("twilightforest.lifedrain", 0.0F));
        context.register(TFDamageTypes.EXPIRED, new DamageType("twilightforest.expired", 0.0F));
        context.register(TFDamageTypes.FALLING_ICE, new DamageType("fallingBlock", 0.1F));
        context.register(TFDamageTypes.MOONWORM, new DamageType("twilightforest.moonworm", 0.0F));
        context.register(TFDamageTypes.ACID_RAIN, new DamageType("twilightforest.acid_rain", 0.0F));
        context.register(TFDamageTypes.OMINOUS_FIRE, new DamageType("twilightforest.ominous", 0.1F, DamageEffects.BURNING));
        context.register(TFDamageTypes.FAILED_CHALLENGE, new DamageType("twilightforest.failedChallenge", 0.0F));
        context.register(TFDamageTypes.STALE_SANDWICH, new DamageType("twilightforest.stale_sandwich", 0.0F));
    }

}
