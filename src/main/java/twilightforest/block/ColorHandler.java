package twilightforest.block;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.TwilightForestMod;
import twilightforest.enums.Leaves3Variant;
import twilightforest.enums.LeavesVariant;
import twilightforest.enums.MagicWoodVariant;
import twilightforest.enums.TowerWoodVariant;
import twilightforest.item.ItemTFArcticArmor;
import twilightforest.item.TFItems;

import java.awt.*;

@SideOnly(Side.CLIENT)
public final class ColorHandler {

	public static void init() {
		BlockColors blockColors = Minecraft.getMinecraft().getBlockColors();
		blockColors.registerBlockColorHandler((state, worldIn, pos, tintIndex) -> tintIndex > 15 ? 0xFFFFFF : Color.HSBtoRGB(worldIn == null ? 0.45F : BlockTFAuroraBrick.rippleFractialNoise(3, 256.0f, pos != null ? pos.up(128) : new BlockPos(0, 0, 0), 0.37f, 0.67f, 2.0f), 1.0f, 1.0f), TFBlocks.aurora_block);
		blockColors.registerBlockColorHandler((state, worldIn, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;

			int normalColor = Minecraft.getMinecraft().getBlockColors().colorMultiplier(TFBlocks.aurora_block.getDefaultState(), worldIn, pos, tintIndex);

			int red = (normalColor >> 16) & 255;
			int blue = normalColor & 255;
			int green = (normalColor >> 8) & 255;

			float[] hsb = Color.RGBtoHSB(red, blue, green, null);

			return Color.HSBtoRGB(hsb[0], hsb[1] * 0.5F, Math.min(hsb[2] + 0.4F, 0.9F));
		}, TFBlocks.aurora_pillar, TFBlocks.aurora_slab, TFBlocks.double_aurora_slab);
		blockColors.registerBlockColorHandler((state, worldIn, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;

			if (worldIn == null || pos == null) {
				return ColorizerFoliage.getFoliageColorBasic();
			}

			int red = 0;
			int grn = 0;
			int blu = 0;

			for (int dz = -1; dz <= 1; ++dz) {
				for (int dx = -1; dx <= 1; ++dx) {
					int i2 = worldIn.getBiome(pos.add(dx, 0, dz)).getFoliageColorAtPos(pos.add(dx, 0, dz));
					red += (i2 & 16711680) >> 16;
					grn += (i2 & 65280) >> 8;
					blu += i2 & 255;
				}
			}

			return (red / 9 & 255) << 16 | (grn / 9 & 255) << 8 | blu / 9 & 255;
		}, TFBlocks.dark_leaves, TFBlocks.giant_leaves);
		blockColors.registerBlockColorHandler((state, worldIn, pos, tintIndex) ->  tintIndex > 15 ? 0xFFFFFF :state.getValue(BlockTFFireJet.VARIANT).hasGrassColor ? Minecraft.getMinecraft().getBlockColors().colorMultiplier(Blocks.GRASS.getDefaultState(), worldIn, pos, tintIndex) : 0xFFFFFF, TFBlocks.fire_jet);
		//blockColors.registerBlockColorHandler((state, worldIn, pos, tintIndex) -> 0x208030, TFBlocks.huge_lilypad);
		blockColors.registerBlockColorHandler((state, worldIn, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;

			if (worldIn == null || pos == null) {
				switch (state.getValue(BlockTFMagicLog.VARIANT)) {
					case TIME:
						return 106 << 16 | 156 << 8 | 23;
					case TRANS:
						return 108 << 16 | 204 << 8 | 234;
					case MINE:
						return 252 << 16 | 241 << 8 | 68;
					case SORT:
						return 54 << 16 | 76 << 8 | 3;
					default:
						return -1;
				}
			} else {
				int red = -1, green = -1, blue = -1;
				MagicWoodVariant leafType = state.getValue(BlockTFMagicLog.VARIANT);

				if (leafType == MagicWoodVariant.TIME) {
					int fade = pos.getX() * 16 + pos.getY() * 16 + pos.getZ() * 16;
					if ((fade & 256) != 0) {
						fade = 255 - (fade & 255);
					}
					fade &= 255;

					float spring = (255 - fade) / 255F;
					float fall = fade / 255F;

					red = (int) (spring * 106 + fall * 251);
					green = (int) (spring * 156 + fall * 108);
					blue = (int) (spring * 23 + fall * 27);
				} else if (leafType == MagicWoodVariant.MINE) {
					int fade = pos.getX() * 31 + pos.getY() * 33 + pos.getZ() * 32;
					if ((fade & 256) != 0) {
						fade = 255 - (fade & 255);
					}
					fade &= 255;

					float spring = (255 - fade) / 255F;
					float fall = fade / 255F;

					red = (int) (spring * 252 + fall * 237);
					green = (int) (spring * 241 + fall * 172);
					blue = (int) (spring * 68 + fall * 9);
				} else if (leafType == MagicWoodVariant.TRANS) {
					int fade = pos.getX() * 27 + pos.getY() * 63 + pos.getZ() * 39;
					if ((fade & 256) != 0) {
						fade = 255 - (fade & 255);
					}
					fade &= 255;

					float spring = (255 - fade) / 255F;
					float fall = fade / 255F;

					red = (int) (spring * 108 + fall * 96);
					green = (int) (spring * 204 + fall * 107);
					blue = (int) (spring * 234 + fall * 121);
				} else if (leafType == MagicWoodVariant.SORT) {
					int fade = pos.getX() * 63 + pos.getY() * 63 + pos.getZ() * 63;
					if ((fade & 256) != 0) {
						fade = 255 - (fade & 255);
					}
					fade &= 255;

					float spring = (255 - fade) / 255F;
					float fall = fade / 255F;

					red = (int) (spring * 54 + fall * 168);
					green = (int) (spring * 76 + fall * 199);
					blue = (int) (spring * 3 + fall * 43);
				}

				return red << 16 | green << 8 | blue;
			}
		}, TFBlocks.magic_leaves);
		blockColors.registerBlockColorHandler((state, worldIn, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;

			if (worldIn == null || pos == null || state.getValue(BlockTFTowerWood.VARIANT) == TowerWoodVariant.ENCASED) {
				return -1;
			} else {
				float f = BlockTFAuroraBrick.rippleFractialNoise(2, 32.0f, pos, 0.4f, 1.0f, 2f);
				return Color.HSBtoRGB(0.1f, 1f - f, (f + 2f) / 3f);
			}
		}, TFBlocks.tower_wood);
		blockColors.registerBlockColorHandler((state, world, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;

			if (world == null || pos == null) {
				switch (state.getValue(BlockTFLeaves.VARIANT)) {
					case CANOPY:
						return 0x609860;
					case MANGROVE:
						return 0x80A755;
					default:
					case OAK:
						return 0x48B518;
				}
			} else {
				int red = 0;
				int green = 0;
				int blue = 0;

				for (int var9 = -1; var9 <= 1; ++var9) {
					for (int var10 = -1; var10 <= 1; ++var10) {
						int var11 = world.getBiome(pos.add(var10, 0, var9)).getFoliageColorAtPos(pos);
						red += (var11 & 16711680) >> 16;
						green += (var11 & 65280) >> 8;
						blue += var11 & 255;
					}
				}

				int normalColor = (red / 9 & 0xFF) << 16 | (green / 9 & 0xFF) << 8 | blue / 9 & 0xFF;

				if (state.getValue(BlockTFLeaves.VARIANT) == LeavesVariant.CANOPY) {
					// canopy colorizer
					return ((normalColor & 0xFEFEFE) + 0x469A66) / 2;
					//return ((normalColor & 0xFEFEFE) + 0x009822) / 2;
				} else if (state.getValue(BlockTFLeaves.VARIANT) == LeavesVariant.MANGROVE) {
					// mangrove colors
					return ((normalColor & 0xFEFEFE) + 0xC0E694) / 2;
				} else if (state.getValue(BlockTFLeaves.VARIANT) == LeavesVariant.RAINBOAK) {
					// RAINBOW!
					red = pos.getX() * 32 + pos.getY() * 16;
					if ((red & 256) != 0) {
						red = 255 - (red & 255);
					}
					red &= 255;

					blue = pos.getY() * 32 + pos.getZ() * 16;
					if ((blue & 256) != 0) {
						blue = 255 - (blue & 255);
					}
					blue ^= 255;

					green = pos.getX() * 16 + pos.getZ() * 32;
					if ((green & 256) != 0) {
						green = 255 - (green & 255);
					}
					green &= 255;


					return red << 16 | blue << 8 | green;
				} else {
					return normalColor;
				}
			}
		}, TFBlocks.twilight_leaves);
		blockColors.registerBlockColorHandler((state, worldIn, pos, tintIndex) -> {
			Leaves3Variant variant = state.getValue(BlockTFLeaves3.VARIANT);
			return variant == Leaves3Variant.THORN ? ColorizerFoliage.getFoliageColorPine()
					: variant == Leaves3Variant.BEANSTALK ? ColorizerFoliage.getFoliageColorBirch()
					: -1;
		}, TFBlocks.twilight_leaves_3);
		blockColors.registerBlockColorHandler((state, worldIn, pos, tintIndex) -> tintIndex != 0 ? 0xFFFFFF : state.getValue(BlockTFPlant.VARIANT).isGrassColored ? worldIn != null && pos != null ? BiomeColorHelper.getGrassColorAtPos(worldIn, pos) : ColorizerGrass.getGrassColor(0.5D, 1.0D) : 0xFFFFFF, TFBlocks.twilight_plant);
		blockColors.registerBlockColorHandler((state, worldIn, pos, tintIndex) -> ColorizerGrass.getGrassColor(0.5D, 1.0D), TFBlocks.miniature_structure);
		blockColors.registerBlockColorHandler((state, worldIn, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;

			switch (state.getValue(BlockTFCastleMagic.COLOR)) {
				case PINK:
					return 0xFF00FF;
				case BLUE:
					return 0x00FFFF;
				case YELLOW:
					return 0xFFFF00;
				case PURPLE:
					return 0x4B0082;
				default:
					TwilightForestMod.LOGGER.info("Magic happened. Got " + state.getValue(BlockTFCastleMagic.COLOR).getName() + " for Castle Rune");
					return state.getValue(BlockTFCastleMagic.COLOR).getColorValue();
			}
		}, TFBlocks.castle_rune_brick);
		blockColors.registerBlockColorHandler((state, worldIn, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;

			switch (state.getValue(BlockTFCastleDoor.LOCK_INDEX)) {
				case 0: // YELLOW
					return 0xFFFF00;
				case 1: // PURPLE
					return 0x4B0082;
				case 2: // PINK
					return 0xFF00FF;
				case 3: // BLUE
					return 0x00FFFF;
				default:
					return 0;
			}
		}, TFBlocks.castle_door, TFBlocks.castle_door_vanished);

		blockColors.registerBlockColorHandler((state, worldIn, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;

			switch (state.getValue(BlockTFForceField.COLOR)) {
				case PURPLE:
					return 0x5C1074;
				case PINK:
					return 0xFA057E;
				case ORANGE:
					return 0xFF5B02;
				case GREEN:
					return 0x89E701;
				case BLUE:
					return 0x0DDEFF;
				default:
					TwilightForestMod.LOGGER.info("Magic happened. Got " + state.getValue(BlockTFForceField.COLOR).getName() + " for Forcefield");
					return state.getValue(BlockTFForceField.COLOR).getColorValue();
			}
		}, TFBlocks.force_field);

		ItemColors itemColors = Minecraft.getMinecraft().getItemColors();
		// Atomic: This is one place where getStateFromMeta is still commonly used
		itemColors.registerItemColorHandler((stack, tintIndex) -> blockColors.colorMultiplier(((ItemBlock)stack.getItem()).getBlock().getStateFromMeta(stack.getMetadata()), null, null, tintIndex), TFBlocks.aurora_block, TFBlocks.aurora_pillar, TFBlocks.aurora_slab, TFBlocks.dark_leaves, TFBlocks.giant_leaves, TFBlocks.fire_jet, TFBlocks.magic_leaves, TFBlocks.twilight_leaves, TFBlocks.twilight_leaves_3, TFBlocks.twilight_plant, TFBlocks.castle_rune_brick, TFBlocks.castle_door, TFBlocks.castle_door_vanished, TFBlocks.miniature_structure, TFBlocks.force_field);
		// Honestly I'd say it makes sense in this context. -Drullkus

		itemColors.registerItemColorHandler((ItemStack stack, int tintIndex) ->
				stack.getItem() instanceof ItemTFArcticArmor
						? ((ItemTFArcticArmor) stack.getItem()).getColor(stack, tintIndex)
						: 0xFFFFFF,
				TFItems.arctic_helmet, TFItems.arctic_chestplate, TFItems.arctic_leggings, TFItems.arctic_boots);
	}

	private ColorHandler() {
	}
}
