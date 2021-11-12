package twilightforest.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;

public class TFDamageSources {
    public static final DamageSource GHAST_TEAR = new DamageSource(tfSource("ghastTear")); //ur-ghast
    public static final DamageSource HYDRA_BITE = new DamageSource(tfSource("hydraBite")); //hydra
    public static final DamageSource HYDRA_FIRE = new DamageSource(tfSource("hydraFire")); //hydra
    public static final DamageSource LICH_BOLT = new DamageSource(tfSource("lichBolt")).setProjectile().bypassArmor().setMagic(); //lich
    public static final DamageSource LICH_BOMB = new DamageSource(tfSource("lichBomb")).setProjectile().bypassArmor().setMagic(); //lich
    public static final DamageSource CHILLING_BREATH = new DamageSource(tfSource("chillingBreath")); //snow queen
    public static final DamageSource SQUISH = new DamageSource(tfSource("squish")); //snow queen
    public static final DamageSource THROWN_AXE = new DamageSource(tfSource("thrownAxe")).setProjectile();
    public static final DamageSource THROWN_PICKAXE = new DamageSource(tfSource("thrownPickaxe")).setProjectile();
    public static final DamageSource THORNS = new DamageSource(tfSource("thorns"));
    public static final DamageSource KNIGHTMETAL = new DamageSource(tfSource("knightmetal"));
    public static final DamageSource FIERY = new DamageSource(tfSource("fiery"));
    public static final DamageSource FIRE_JET = new DamageSource(tfSource("fireJet"));
    public static final DamageSource REACTOR = new DamageSource(tfSource("reactor"));
    public static final DamageSource SLIDER = new DamageSource(tfSource("slider"));

    public static DamageSource axing(LivingEntity mob) {
        return new EntityDamageSource(tfSource("axing"), mob); //minotaur, minoshroom
    }
    //TODO actually figure out how to implement this one
    public static DamageSource yeeted(LivingEntity mob) {
        return new EntityDamageSource(tfSource("yeeted"), mob); //yetis, alpha yeti
    }
    public static DamageSource ant(LivingEntity mob) {
        return new EntityDamageSource(tfSource("ant"), mob); //giants
    }
    public static DamageSource haunt(LivingEntity mob) {
        return new EntityDamageSource(tfSource("haunt"), mob); //wraith, knight phantom
    }
    public static DamageSource clamped(LivingEntity mob) {
        return new EntityDamageSource(tfSource("clamped"), mob); //pinch beetle
    }
    public static DamageSource scorched(LivingEntity mob) {
        return new EntityDamageSource(tfSource("scorched"), mob); //fire beetle
    }
    public static DamageSource frozen(Entity source, LivingEntity mob) {
        return new IndirectEntityDamageSource(tfSource("frozen"), source, mob); //ice bomb
    }
    public static DamageSource spiked(Entity source, LivingEntity mob) {
        return new IndirectEntityDamageSource(tfSource("spiked"), source, mob); //block and chain
    }
    public static DamageSource leafBrain(Entity source, LivingEntity mob) {
        return new IndirectEntityDamageSource(tfSource("leafBrain"), source, mob).setProjectile().bypassArmor().setMagic(); //druid
    }
    public static DamageSource lostWords(Entity source, LivingEntity mob) {
        return new IndirectEntityDamageSource(tfSource("lostWords"), source, mob).setProjectile().bypassArmor().setMagic(); //tome
    }
    public static DamageSource schooled(Entity source, LivingEntity mob) {
        return new IndirectEntityDamageSource(tfSource("schooled"), source, mob).setProjectile().bypassArmor().setMagic(); //tome
    }
    public static DamageSource snowballFight(Entity source, LivingEntity mob) {
        return new IndirectEntityDamageSource(tfSource("snowballFight"), source, mob).setProjectile(); //ice core
    }

    public static String tfSource(String name) {
        return "twilightforest." + name;
    }
}
