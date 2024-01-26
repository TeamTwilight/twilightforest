package twilightforest.dispenser;

import net.minecraft.core.Position;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import twilightforest.entity.projectile.IceBomb;
import twilightforest.entity.projectile.MoonwormShot;
import twilightforest.entity.projectile.TwilightWandBolt;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;
import twilightforest.init.TFSounds;

public class TFDispenserBehaviors {

	public static void init() {
		DispenserBlock.registerBehavior(TFItems.MOONWORM_QUEEN.get(), new DamageableStackDispenseBehavior() {
			@Override
			protected Projectile getProjectileEntity(Level level, Position position, ItemStack stack) {
				return new MoonwormShot(level, position.x(), position.y(), position.z());
			}

			@Override
			protected int getDamageAmount() {
				return 2;
			}

			@Override
			protected SoundEvent getFiredSound() {
				return TFSounds.MOONWORM_SQUISH.get();
			}
		});

		DispenseItemBehavior idispenseitembehavior = new OptionalDispenseItemBehavior() {
			@Override
			protected ItemStack execute(BlockSource source, ItemStack stack) {
				this.setSuccess(ArmorItem.dispenseArmor(source, stack));
				return stack;
			}
		};
		DispenserBlock.registerBehavior(TFBlocks.NAGA_TROPHY.get().asItem(), idispenseitembehavior);
		DispenserBlock.registerBehavior(TFBlocks.LICH_TROPHY.get().asItem(), idispenseitembehavior);
		DispenserBlock.registerBehavior(TFBlocks.MINOSHROOM_TROPHY.get().asItem(), idispenseitembehavior);
		DispenserBlock.registerBehavior(TFBlocks.HYDRA_TROPHY.get().asItem(), idispenseitembehavior);
		DispenserBlock.registerBehavior(TFBlocks.KNIGHT_PHANTOM_TROPHY.get().asItem(), idispenseitembehavior);
		DispenserBlock.registerBehavior(TFBlocks.UR_GHAST_TROPHY.get().asItem(), idispenseitembehavior);
		DispenserBlock.registerBehavior(TFBlocks.ALPHA_YETI_TROPHY.get().asItem(), idispenseitembehavior);
		DispenserBlock.registerBehavior(TFBlocks.SNOW_QUEEN_TROPHY.get().asItem(), idispenseitembehavior);
		DispenserBlock.registerBehavior(TFBlocks.QUEST_RAM_TROPHY.get().asItem(), idispenseitembehavior);
		DispenserBlock.registerBehavior(TFBlocks.CREEPER_SKULL_CANDLE.get().asItem(), idispenseitembehavior);
		DispenserBlock.registerBehavior(TFBlocks.PLAYER_SKULL_CANDLE.get().asItem(), idispenseitembehavior);
		DispenserBlock.registerBehavior(TFBlocks.SKELETON_SKULL_CANDLE.get().asItem(), idispenseitembehavior);
		DispenserBlock.registerBehavior(TFBlocks.WITHER_SKELE_SKULL_CANDLE.get().asItem(), idispenseitembehavior);
		DispenserBlock.registerBehavior(TFBlocks.ZOMBIE_SKULL_CANDLE.get().asItem(), idispenseitembehavior);
		DispenserBlock.registerBehavior(TFBlocks.CICADA.get().asItem(), idispenseitembehavior);
		DispenserBlock.registerBehavior(TFBlocks.FIREFLY.get().asItem(), idispenseitembehavior);
		DispenserBlock.registerBehavior(TFBlocks.MOONWORM.get().asItem(), idispenseitembehavior);

		DispenserBlock.registerBehavior(TFItems.PEACOCK_FEATHER_FAN.get().asItem(), new FeatherFanDispenseBehavior());
		DispenserBlock.registerBehavior(TFItems.CRUMBLE_HORN.get().asItem(), new CrumbleDispenseBehavior());
		DispenserBlock.registerBehavior(TFItems.TRANSFORMATION_POWDER.get().asItem(), new TransformationDispenseBehavior());

		DispenserBlock.registerBehavior(TFItems.TWILIGHT_SCEPTER.get(), new DamageableStackDispenseBehavior() {
			@Override
			protected Projectile getProjectileEntity(Level level, Position position, ItemStack stack) {
				return new TwilightWandBolt(level, position.x(), position.y(), position.z());
			}

			@Override
			protected int getDamageAmount() {
				return 1;
			}

			@Override
			protected SoundEvent getFiredSound() {
				return TFSounds.SCEPTER_PEARL.get();
			}

			@Override
			protected float getProjectileInaccuracy() {
				return 6.0F;
			}
		});

		DispenserBlock.registerBehavior(TFItems.ICE_BOMB.get(), new AbstractProjectileDispenseBehavior() {
			@Override
			protected Projectile getProjectile(Level level, Position pos, ItemStack stack) {
				return new IceBomb(level, pos);
			}
		});

		//store the vanilla values so we can use them in case our stuff fails
		DispenseItemBehavior cachedFlintBehavior = DispenserBlock.DISPENSER_REGISTRY.get(Items.FLINT_AND_STEEL);
		DispenseItemBehavior cachedFireChargeBehavior = DispenserBlock.DISPENSER_REGISTRY.get(Items.FIRE_CHARGE);

		DispenserBlock.registerBehavior(Items.FLINT_AND_STEEL, new IgniteLightableDispenseBehavior(cachedFlintBehavior));
		DispenserBlock.registerBehavior(Items.FIRE_CHARGE, new IgniteLightableDispenseBehavior(cachedFireChargeBehavior));

		//handling tags should be a thing smh
		DispenserBlock.registerBehavior(Items.CANDLE, new SkullCandleDispenseBehavior());
		DispenserBlock.registerBehavior(Items.BLACK_CANDLE, new SkullCandleDispenseBehavior());
		DispenserBlock.registerBehavior(Items.GRAY_CANDLE, new SkullCandleDispenseBehavior());
		DispenserBlock.registerBehavior(Items.LIGHT_GRAY_CANDLE, new SkullCandleDispenseBehavior());
		DispenserBlock.registerBehavior(Items.WHITE_CANDLE, new SkullCandleDispenseBehavior());
		DispenserBlock.registerBehavior(Items.RED_CANDLE, new SkullCandleDispenseBehavior());
		DispenserBlock.registerBehavior(Items.ORANGE_CANDLE, new SkullCandleDispenseBehavior());
		DispenserBlock.registerBehavior(Items.YELLOW_CANDLE, new SkullCandleDispenseBehavior());
		DispenserBlock.registerBehavior(Items.GREEN_CANDLE, new SkullCandleDispenseBehavior());
		DispenserBlock.registerBehavior(Items.LIME_CANDLE, new SkullCandleDispenseBehavior());
		DispenserBlock.registerBehavior(Items.BLUE_CANDLE, new SkullCandleDispenseBehavior());
		DispenserBlock.registerBehavior(Items.CYAN_CANDLE, new SkullCandleDispenseBehavior());
		DispenserBlock.registerBehavior(Items.LIGHT_BLUE_CANDLE, new SkullCandleDispenseBehavior());
		DispenserBlock.registerBehavior(Items.PURPLE_CANDLE, new SkullCandleDispenseBehavior());
		DispenserBlock.registerBehavior(Items.MAGENTA_CANDLE, new SkullCandleDispenseBehavior());
		DispenserBlock.registerBehavior(Items.PINK_CANDLE, new SkullCandleDispenseBehavior());
		DispenserBlock.registerBehavior(Items.BROWN_CANDLE, new SkullCandleDispenseBehavior());
	}
}
