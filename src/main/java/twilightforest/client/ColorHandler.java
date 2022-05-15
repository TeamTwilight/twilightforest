package twilightforest.client;

import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import twilightforest.TwilightForestMod;
import twilightforest.block.AuroraBrickBlock;
import twilightforest.block.HollowLogClimbable;
import twilightforest.block.TFBlocks;
import twilightforest.compat.IECompat;
import twilightforest.compat.TFCompat;
import twilightforest.enums.HollowLogVariants;
import twilightforest.item.ArcticArmorItem;
import twilightforest.item.TFItems;

import java.awt.*;

@Mod.EventBusSubscriber(modid = TwilightForestMod.ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ColorHandler {

	@SubscribeEvent
	public static void registerBlockColors(ColorHandlerEvent.Block event) {

		BlockColors blockColors = event.getBlockColors();

		blockColors.register((state, worldIn, pos, tintIndex) -> tintIndex > 15 ? 0xFFFFFF : Color.HSBtoRGB(worldIn == null ? 0.45F : AuroraBrickBlock.rippleFractialNoise(2, 128.0f, pos != null ? pos.above(128) : new BlockPos(0, 0, 0), 0.37f, 0.67f, 1.5f), 1.0f, 1.0f), TFBlocks.AURORA_BLOCK.get());
		blockColors.register((state, worldIn, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;

			int normalColor = blockColors.getColor(TFBlocks.AURORA_BLOCK.get().defaultBlockState(), worldIn, pos, tintIndex);

			int red = (normalColor >> 16) & 255;
			int blue = normalColor & 255;
			int green = (normalColor >> 8) & 255;

			float[] hsb = Color.RGBtoHSB(red, blue, green, null);

			return Color.HSBtoRGB(hsb[0], hsb[1] * 0.5F, Math.min(hsb[2] + 0.4F, 0.9F));
		}, TFBlocks.AURORA_PILLAR.get(), TFBlocks.AURORA_SLAB.get(), TFBlocks.AURORALIZED_GLASS.get());
		blockColors.register((state, worldIn, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;

			if (worldIn == null || pos == null) {
				return FoliageColor.getDefaultColor();
			}

			int red = 0;
			int grn = 0;
			int blu = 0;

			for (int dz = -1; dz <= 1; ++dz) {
				for (int dx = -1; dx <= 1; ++dx) {
					//int i2 = worldIn.getBiome(pos.add(dx, 0, dz)).getFoliageColor(pos.add(dx, 0, dz));
					int i2 = BiomeColors.getAverageFoliageColor(worldIn, pos.offset(dx, 0, dz));
					red += (i2 & 16711680) >> 16;
					grn += (i2 & 65280) >> 8;
					blu += i2 & 255;
				}
			}

			return (red / 9 & 255) << 16 | (grn / 9 & 255) << 8 | blu / 9 & 255;
		}, TFBlocks.DARK_LEAVES.get(), TFBlocks.HARDENED_DARK_LEAVES.get(), TFBlocks.GIANT_LEAVES.get());
		blockColors.register((state, worldIn, pos, tintIndex) -> tintIndex > 15 ? 0xFFFFFF : blockColors.getColor(Blocks.GRASS.defaultBlockState(), worldIn, pos, tintIndex), TFBlocks.SMOKER.get(), TFBlocks.FIRE_JET.get());
		blockColors.register((state, worldIn, pos, tintIndex) -> worldIn != null && pos != null ? 2129968 : 7455580, TFBlocks.HUGE_LILY_PAD.get());
		blockColors.register((state, worldIn, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;

			if (worldIn == null || pos == null) {
				return 106 << 16 | 156 << 8 | 23;
			} else {
				int red, green, blue;

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

				return red << 16 | green << 8 | blue;
			}
		}, TFBlocks.TIME_LEAVES.get());
		blockColors.register((state, worldIn, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;

			if (worldIn == null || pos == null) {
				return 108 << 16 | 204 << 8 | 234;
			} else {
				int red, green, blue;

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

				return red << 16 | green << 8 | blue;
			}
		}, TFBlocks.TRANSFORMATION_LEAVES.get());
		blockColors.register((state, worldIn, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;

			if (worldIn == null || pos == null) {
				return 252 << 16 | 241 << 8 | 68;
			} else {
				int red, green, blue;

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

				return red << 16 | green << 8 | blue;
			}
		}, TFBlocks.MINING_LEAVES.get());
		blockColors.register((state, worldIn, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;

			if (worldIn == null || pos == null) {
				return 54 << 16 | 76 << 8 | 3;
			} else {
				int red, green, blue;

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

				return red << 16 | green << 8 | blue;
			}
		}, TFBlocks.SORTING_LEAVES.get());
		blockColors.register((state, worldIn, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;

			if (worldIn == null || pos == null) {
				return -1;
			} else {
				float f = AuroraBrickBlock.rippleFractialNoise(2, 32.0f, pos, 0.4f, 1.0f, 2f);
				return Color.HSBtoRGB(0.1f, 1f - f, (f + 2f) / 3f);
			}
		}, TFBlocks.TOWERWOOD.get(), TFBlocks.CRACKED_TOWERWOOD.get(), TFBlocks.INFESTED_TOWERWOOD.get(), TFBlocks.MOSSY_TOWERWOOD.get());
		blockColors.register((state, world, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;

			if (world == null || pos == null) {
				return 0x48B518;
			} else {
				int red = 0;
				int green = 0;
				int blue = 0;

				for (int dz = -1; dz <= 1; ++dz) {
					for (int dx = -1; dx <= 1; ++dx) {
						//int color = world.getBiome(pos.add(dx, 0, dz)).getFoliageColor(pos);
						int color = BiomeColors.getAverageFoliageColor(world, pos);
						red += (color & 16711680) >> 16;
						green += (color & 65280) >> 8;
						blue += color & 255;
					}
				}

				return (red / 9 & 0xFF) << 16 | (green / 9 & 0xFF) << 8 | blue / 9 & 0xFF;
			}
		}, TFBlocks.TWILIGHT_OAK_LEAVES.get());
		blockColors.register((state, world, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;

			if (world == null || pos == null) {
				return 0x609860;
			} else {
				int red = 0;
				int green = 0;
				int blue = 0;

				for (int dz = -1; dz <= 1; ++dz) {
					for (int dx = -1; dx <= 1; ++dx) {
						//int color = world.getBiome(pos.add(dx, 0, dz)).getFoliageColor(pos);
						int color = BiomeColors.getAverageFoliageColor(world, pos);
						red += (color & 16711680) >> 16;
						green += (color & 65280) >> 8;
						blue += color & 255;
					}
				}

				int normalColor = (red / 9 & 0xFF) << 16 | (green / 9 & 0xFF) << 8 | blue / 9 & 0xFF;
				// canopy colorizer
				return ((normalColor & 0xFEFEFE) + 0x469A66) / 2;
				//return ((normalColor & 0xFEFEFE) + 0x009822) / 2;
			}
		}, TFBlocks.CANOPY_LEAVES.get());
		blockColors.register((state, world, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;

			if (world == null || pos == null) {
				return 0x80A755;
			} else {
				int red = 0;
				int green = 0;
				int blue = 0;

				for (int dz = -1; dz <= 1; ++dz) {
					for (int dx = -1; dx <= 1; ++dx) {
						//int color = world.getBiome(pos.add(dx, 0, dz)).getFoliageColor(pos);
						int color = BiomeColors.getAverageFoliageColor(world, pos);
						red += (color & 16711680) >> 16;
						green += (color & 65280) >> 8;
						blue += color & 255;
					}
				}

				int normalColor = (red / 9 & 0xFF) << 16 | (green / 9 & 0xFF) << 8 | blue / 9 & 0xFF;
				// mangrove colors
				return ((normalColor & 0xFEFEFE) + 0xC0E694) / 2;
			}
		}, TFBlocks.MANGROVE_LEAVES.get());
		blockColors.register((state, world, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;

			if (world == null || pos == null) {
				return 0x48B518;
			} else {
				int red = 0;
				int green = 0;
				int blue = 0;

				for (int dz = -1; dz <= 1; ++dz) {
					for (int dx = -1; dx <= 1; ++dx) {
						//int color = world.getBiome(pos.add(dx, 0, dz)).getFoliageColor(pos);
						int color = BiomeColors.getAverageFoliageColor(world, pos);
						red += (color & 16711680) >> 16;
						green += (color & 65280) >> 8;
						blue += color & 255;
					}
				}

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
			}
		}, TFBlocks.RAINBOW_OAK_LEAVES.get());
		blockColors.register((state, worldIn, pos, tintIndex) -> FoliageColor.getEvergreenColor(), TFBlocks.BEANSTALK_LEAVES.get(), TFBlocks.THORN_LEAVES.get());
		blockColors.register((state, worldIn, pos, tintIndex) -> {
			if (tintIndex != 0) {
				return 0xFFFFFF;
			} else {
				if (worldIn != null && pos != null) {
					return BiomeColors.getAverageFoliageColor(worldIn, pos);
				} else {
					return FoliageColor.getDefaultColor();
				}
			}
		}, TFBlocks.FALLEN_LEAVES.get());
		blockColors.register((state, worldIn, pos, tintIndex) -> {
			if ((tintIndex & 1) == 1) {
				return 0xFFFFFF;
			} else {
				if (worldIn != null && pos != null) {
					return BiomeColors.getAverageGrassColor(worldIn, pos);
				} else {
					return GrassColor.get(0.5D, 1.0D);
				}
			}
		}, TFBlocks.FIDDLEHEAD.get(), TFBlocks.POTTED_FIDDLEHEAD.get());
		blockColors.register((state, worldIn, pos, tintIndex) -> {
			if ((tintIndex & 1) == 0) {
				return 0xFFFFFF;
			} else {
				return GrassColor.get(0.5D, 1.0D);

			}
		}, TFBlocks.HOLLOW_OAK_LOG_HORIZONTAL.get(), TFBlocks.HOLLOW_SPRUCE_LOG_HORIZONTAL.get(), TFBlocks.HOLLOW_BIRCH_LOG_HORIZONTAL.get(), TFBlocks.HOLLOW_JUNGLE_LOG_HORIZONTAL.get(),
				TFBlocks.HOLLOW_ACACIA_LOG_HORIZONTAL.get(), TFBlocks.HOLLOW_DARK_OAK_LOG_HORIZONTAL.get(), TFBlocks.HOLLOW_CRIMSON_STEM_HORIZONTAL.get(), TFBlocks.HOLLOW_WARPED_STEM_HORIZONTAL.get(),
				TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_HORIZONTAL.get(), TFBlocks.HOLLOW_CANOPY_LOG_HORIZONTAL.get(), TFBlocks.HOLLOW_MANGROVE_LOG_HORIZONTAL.get(), TFBlocks.HOLLOW_DARK_LOG_HORIZONTAL.get(),
				TFBlocks.HOLLOW_TIME_LOG_HORIZONTAL.get(), TFBlocks.HOLLOW_TRANSFORMATION_LOG_HORIZONTAL.get(), TFBlocks.HOLLOW_MINING_LOG_HORIZONTAL.get(), TFBlocks.HOLLOW_SORTING_LOG_HORIZONTAL.get());
		blockColors.register((state, worldIn, pos, tintIndex) -> {
			if (state.getValue(HollowLogClimbable.VARIANT) != HollowLogVariants.Climbable.VINE || (tintIndex & 1) == 0) {
				return 0xFFFFFF;
			} else {
				if (worldIn != null && pos != null) {
					return BiomeColors.getAverageFoliageColor(worldIn, pos);
				} else {
					return FoliageColor.getDefaultColor();
				}
			}
		}, TFBlocks.HOLLOW_OAK_LOG_CLIMBABLE.get(), TFBlocks.HOLLOW_SPRUCE_LOG_CLIMBABLE.get(), TFBlocks.HOLLOW_BIRCH_LOG_CLIMBABLE.get(), TFBlocks.HOLLOW_JUNGLE_LOG_CLIMBABLE.get(),
				TFBlocks.HOLLOW_ACACIA_LOG_CLIMBABLE.get(), TFBlocks.HOLLOW_DARK_OAK_LOG_CLIMBABLE.get(), TFBlocks.HOLLOW_CRIMSON_STEM_CLIMBABLE.get(), TFBlocks.HOLLOW_WARPED_STEM_CLIMBABLE.get(),
				TFBlocks.HOLLOW_TWILIGHT_OAK_LOG_CLIMBABLE.get(), TFBlocks.HOLLOW_CANOPY_LOG_CLIMBABLE.get(), TFBlocks.HOLLOW_MANGROVE_LOG_CLIMBABLE.get(), TFBlocks.HOLLOW_DARK_LOG_CLIMBABLE.get(),
				TFBlocks.HOLLOW_TIME_LOG_CLIMBABLE.get(), TFBlocks.HOLLOW_TRANSFORMATION_LOG_CLIMBABLE.get(), TFBlocks.HOLLOW_MINING_LOG_CLIMBABLE.get(), TFBlocks.HOLLOW_SORTING_LOG_CLIMBABLE.get());
		blockColors.register((state, worldIn, pos, tintIndex) -> GrassColor.get(0.5D, 1.0D),
				TFBlocks.TWILIGHT_PORTAL_MINIATURE_STRUCTURE.get(), /*TFBlocks.HEDGE_MAZE_MINIATURE_STRUCTURE.get(), TFBlocks.HOLLOW_HILL_MINIATURE_STRUCTURE.get(), TFBlocks.QUEST_GROVE_MINIATURE_STRUCTURE.get(), TFBlocks.MUSHROOM_TOWER_MINIATURE_STRUCTURE.get(),*/ TFBlocks.NAGA_COURTYARD_MINIATURE_STRUCTURE.get(), TFBlocks.LICH_TOWER_MINIATURE_STRUCTURE.get() //TFBlocks.MINOTAUR_LABYRINTH_MINIATURE_STRUCTURE.get(),
				/*TFBlocks.HYDRA_LAIR_MINIATURE_STRUCTURE.get(), TFBlocks.GOBLIN_STRONGHOLD_MINIATURE_STRUCTURE.get(), TFBlocks.DARK_TOWER_MINIATURE_STRUCTURE.get(), TFBlocks.YETI_CAVE_MINIATURE_STRUCTURE.get(), TFBlocks.AURORA_PALACE_MINIATURE_STRUCTURE.get(), TFBlocks.TROLL_CAVE_COTTAGE_MINIATURE_STRUCTURE.get(), TFBlocks.FINAL_CASTLE_MINIATURE_STRUCTURE.get()*/);
		blockColors.register((state, worldIn, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;
			return 0xFF00FF;
		}, TFBlocks.PINK_CASTLE_RUNE_BRICK.get(), TFBlocks.PINK_CASTLE_DOOR.get());
		blockColors.register((state, worldIn, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;
			return 0x00FFFF;
		}, TFBlocks.BLUE_CASTLE_RUNE_BRICK.get(), TFBlocks.BLUE_CASTLE_DOOR.get());
		blockColors.register((state, worldIn, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;
			return 0xFFFF00;
		}, TFBlocks.YELLOW_CASTLE_RUNE_BRICK.get(), TFBlocks.YELLOW_CASTLE_DOOR.get());
		blockColors.register((state, worldIn, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;
			return 0x4B0082;
		}, TFBlocks.VIOLET_CASTLE_RUNE_BRICK.get(), TFBlocks.VIOLET_CASTLE_DOOR.get());
		blockColors.register((state, worldIn, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;
			return 0x5C1074;
		}, TFBlocks.VIOLET_FORCE_FIELD.get());
		blockColors.register((state, worldIn, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;
			return 0xFA057E;
		}, TFBlocks.PINK_FORCE_FIELD.get());
		blockColors.register((state, worldIn, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;
			return 0xFF5B02;
		}, TFBlocks.ORANGE_FORCE_FIELD.get());
		blockColors.register((state, worldIn, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;
			return 0x89E701;
		}, TFBlocks.GREEN_FORCE_FIELD.get());
		blockColors.register((state, worldIn, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;
			return 0x0DDEFF;
		}, TFBlocks.BLUE_FORCE_FIELD.get());
	}

	@SubscribeEvent
	public static void registerItemColors(ColorHandlerEvent.Item event) {
		ItemColors itemColors = event.getItemColors();
		BlockColors blockColors = event.getBlockColors();

		itemColors.register((stack, tintIndex) -> blockColors.getColor(((BlockItem)stack.getItem()).getBlock().defaultBlockState(), null, null, tintIndex),
				TFBlocks.AURORA_BLOCK.get(), TFBlocks.AURORA_PILLAR.get(), TFBlocks.AURORA_SLAB.get(), TFBlocks.AURORALIZED_GLASS.get(), TFBlocks.DARK_LEAVES.get(), TFBlocks.GIANT_LEAVES.get(), TFBlocks.SMOKER.get(), TFBlocks.FIRE_JET.get(),
				TFBlocks.TIME_LEAVES.get(), TFBlocks.TRANSFORMATION_LEAVES.get(), TFBlocks.MINING_LEAVES.get(), TFBlocks.SORTING_LEAVES.get(), TFBlocks.TWILIGHT_OAK_LEAVES.get(), TFBlocks.CANOPY_LEAVES.get(), TFBlocks.MANGROVE_LEAVES.get(), TFBlocks.RAINBOW_OAK_LEAVES.get(), TFBlocks.THORN_LEAVES.get(), TFBlocks.BEANSTALK_LEAVES.get(),
				TFBlocks.FALLEN_LEAVES.get(), TFBlocks.FIDDLEHEAD.get(), TFBlocks.POTTED_FIDDLEHEAD.get(), TFBlocks.PINK_CASTLE_RUNE_BRICK.get(), TFBlocks.BLUE_CASTLE_RUNE_BRICK.get(), TFBlocks.YELLOW_CASTLE_RUNE_BRICK.get(), TFBlocks.VIOLET_CASTLE_RUNE_BRICK.get(),
				TFBlocks.YELLOW_CASTLE_DOOR.get(), TFBlocks.BLUE_CASTLE_DOOR.get(), TFBlocks.PINK_CASTLE_DOOR.get(), TFBlocks.VIOLET_CASTLE_DOOR.get(), TFBlocks.PINK_FORCE_FIELD.get(), TFBlocks.BLUE_FORCE_FIELD.get(), TFBlocks.GREEN_FORCE_FIELD.get(), TFBlocks.ORANGE_FORCE_FIELD.get(), TFBlocks.VIOLET_FORCE_FIELD.get(), TFBlocks.HUGE_LILY_PAD.get(),
				TFBlocks.TWILIGHT_PORTAL_MINIATURE_STRUCTURE.get(), /*TFBlocks.HEDGE_MAZE_MINIATURE_STRUCTURE.get(), TFBlocks.HOLLOW_HILL_MINIATURE_STRUCTURE.get(), TFBlocks.QUEST_GROVE_MINIATURE_STRUCTURE.get(), TFBlocks.MUSHROOM_TOWER_MINIATURE_STRUCTURE.get(),*/ TFBlocks.NAGA_COURTYARD_MINIATURE_STRUCTURE.get(), TFBlocks.LICH_TOWER_MINIATURE_STRUCTURE.get()//, TFBlocks.MINOTAUR_LABYRINTH_MINIATURE_STRUCTURE.get(),
				/*TFBlocks.HYDRA_LAIR_MINIATURE_STRUCTURE.get(), TFBlocks.GOBLIN_STRONGHOLD_MINIATURE_STRUCTURE.get(), TFBlocks.DARK_TOWER_MINIATURE_STRUCTURE.get(), TFBlocks.YETI_CAVE_MINIATURE_STRUCTURE.get(), TFBlocks.AURORA_PALACE_MINIATURE_STRUCTURE.get(), TFBlocks.TROLL_CAVE_COTTAGE_MINIATURE_STRUCTURE.get(), TFBlocks.FINAL_CASTLE_MINIATURE_STRUCTURE.get()*/);

		itemColors.register((stack, tintIndex) ->
				stack.getItem() instanceof ArcticArmorItem
						? ((ArcticArmorItem) stack.getItem()).getColor(stack, tintIndex)
						: 0xFFFFFF,
				TFItems.ARCTIC_HELMET.get(), TFItems.ARCTIC_CHESTPLATE.get(), TFItems.ARCTIC_LEGGINGS.get(), TFItems.ARCTIC_BOOTS.get());

		itemColors.register((stack, tintIndex) ->
				tintIndex > 0 ? -1 : PotionUtils.getColor(stack),
				TFItems.BRITTLE_FLASK.get(), TFItems.GREATER_FLASK.get());

		if (ModList.get().isLoaded(TFCompat.IE_ID)) {
			IECompat.registerShaderColors(itemColors);
		}
	}
}
