package twilightforest.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.IndirectEntityDamageSource;

public class TFDamageSources {
    public static final DamageSource GHAST_TEAR = new DamageSource("ghastTear"); //ur-ghast
    public static final DamageSource HYDRA_BITE = new DamageSource("hydraBite"); //hydra
    public static final DamageSource HYDRA_FIRE = new DamageSource("hydraFire"); //hydra
    public static final DamageSource LICH_BOLT = new DamageSource("lichBolt").setDamageBypassesArmor().setMagicDamage(); //lich
    public static final DamageSource LICH_BOMB = new DamageSource("lichBomb").setDamageBypassesArmor().setMagicDamage(); //lich
    public static final DamageSource CHILLING_BREATH = new DamageSource("chillingBreath"); //snow queen
    public static final DamageSource SQUISH = new DamageSource("squish"); //snow queen
    public static final DamageSource THROWN_AXE = new DamageSource("thrownAxe");
    public static final DamageSource THROWN_PICKAXE = new DamageSource("thrownPickaxe");
    public static final DamageSource FINAL_BOSS = new DamageSource("finalBoss"); //placeholder
    public static final DamageSource THORNS = new DamageSource("thorns");
    public static final DamageSource KNIGHTMETAL = new DamageSource("knightmetal");
    public static final DamageSource FIERY = new DamageSource("fiery");
    public static final DamageSource FIRE_JET = new DamageSource("fireJet");
    public static final DamageSource REACTOR = new DamageSource("reactor");

    public static DamageSource AXING(LivingEntity mob) {
        return new EntityDamageSource("axing", mob); //minotaur, minoshroom
    }
    public static DamageSource YEETED(LivingEntity mob) {
        return new EntityDamageSource("yeeted", mob); //yetis, alpha yeti
    }
    public static DamageSource ANT(LivingEntity mob) {
        return new EntityDamageSource("ant", mob); //giants
    }
    public static DamageSource HAUNT(LivingEntity mob) {
        return new EntityDamageSource("haunt", mob); //wraith, knight phantom
    }
    public static DamageSource CLAMPED(LivingEntity mob) {
        return new EntityDamageSource("clamped", mob); //pinch beetle
    }
    public static DamageSource TORCHED(LivingEntity mob) {
        return new EntityDamageSource("torched", mob); //fire beetle
    }
    public static DamageSource FROZEN(Entity source, LivingEntity mob) {
        return new IndirectEntityDamageSource("frozen", source, mob); //ice bomb
    }
    public static DamageSource SPIKED(Entity source, LivingEntity mob) {
        return new IndirectEntityDamageSource("spiked", source, mob); //block and chain
    }
    public static DamageSource LEAF_BRAIN(Entity source, LivingEntity mob) {
        return new IndirectEntityDamageSource("leafBrain", source, mob).setDamageBypassesArmor().setMagicDamage(); //druid
    }
    public static DamageSource LOST_WORDS(Entity source, LivingEntity mob) {
        return new IndirectEntityDamageSource("lostWords", source, mob).setDamageBypassesArmor().setMagicDamage(); //tome
    }
    public static DamageSource SNOWBALL_FIGHT(Entity source, LivingEntity mob) {
        return new IndirectEntityDamageSource("snowballFight", source, mob); //ice core
    }
}
