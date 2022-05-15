package twilightforest.dispenser;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import twilightforest.TFSounds;
import twilightforest.block.TFBlocks;
import twilightforest.entity.projectile.IceBomb;
import twilightforest.entity.projectile.MoonwormShot;
import twilightforest.entity.projectile.TwilightWandBolt;
import twilightforest.item.TFItems;

public class TFDispenserBehaviors {

	public static void init() {
		DispenserBlock.registerBehavior(TFItems.MOONWORM_QUEEN.get(), new MoonwormDispenseBehavior() {
			@Override
			protected Projectile getProjectileEntity(Level worldIn, Position position, ItemStack stackIn) {
				return new MoonwormShot(worldIn, position.x(), position.y(), position.z());
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

		DispenseItemBehavior pushmobsbehavior = new FeatherFanDispenseBehavior();
		DispenserBlock.registerBehavior(TFItems.PEACOCK_FEATHER_FAN.get().asItem(), pushmobsbehavior);

		DispenseItemBehavior crumblebehavior = new CrumbleDispenseBehavior();
		DispenserBlock.registerBehavior(TFItems.CRUMBLE_HORN.get().asItem(), crumblebehavior);

		DispenseItemBehavior transformbehavior = new TransformationDispenseBehavior();
		DispenserBlock.registerBehavior(TFItems.TRANSFORMATION_POWDER.get().asItem(), transformbehavior);

		DispenserBlock.registerBehavior(TFItems.TWILIGHT_SCEPTER.get(), new MoonwormDispenseBehavior() {
			@Override
			protected Projectile getProjectileEntity(Level worldIn, Position position, ItemStack stackIn) {
				return new TwilightWandBolt(worldIn, position.x(), position.y(), position.z());
			}

			@Override
			protected void playSound(BlockSource source) {
				BlockPos pos = source.getPos();
				source.getLevel().playSound(null, pos, TFSounds.SCEPTER_PEARL, SoundSource.BLOCKS, 1, 1);
			}
		});

		DispenserBlock.registerBehavior(TFItems.ICE_BOMB.get(), new AbstractProjectileDispenseBehavior() {
			@Override
			protected Projectile getProjectile(Level level, Position pos, ItemStack stack) {
				return new IceBomb(level, pos);
			}
		});

		DispenserBlock.registerBehavior(Items.FLINT_AND_STEEL, new IgniteLightableDispenseBehavior());
		DispenserBlock.registerBehavior(Items.FIRE_CHARGE, new IgniteLightableDispenseBehavior());

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
