package twilightforest.client;

import net.minecraft.block.Blocks;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.BlockItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.FoliageColors;
import net.minecraft.world.GrassColors;
import net.minecraft.world.biome.BiomeColors;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import twilightforest.TwilightForestMod;
import twilightforest.block.*;
import twilightforest.item.ItemTFArcticArmor;
import twilightforest.item.TFItems;

import java.awt.Color;

@Mod.EventBusSubscriber(modid = TwilightForestMod.ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ColorHandler {

	@SubscribeEvent
	public static void registerBlockColors(ColorHandlerEvent.Block event) {

		BlockColors blockColors = event.getBlockColors();

		blockColors.register((state, worldIn, pos, tintIndex) -> tintIndex > 15 ? 0xFFFFFF : Color.HSBtoRGB(worldIn == null ? 0.45F : BlockTFAuroraBrick.rippleFractialNoise(2, 128.0f, pos != null ? pos.up(128) : new BlockPos(0, 0, 0), 0.37f, 0.67f, 1.5f), 1.0f, 1.0f), TFBlocks.aurora_block.get());
		blockColors.register((state, worldIn, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;

			int normalColor = blockColors.getColor(TFBlocks.aurora_block.get().getDefaultState(), worldIn, pos, tintIndex);

			int red = (normalColor >> 16) & 255;
			int blue = normalColor & 255;
			int green = (normalColor >> 8) & 255;

			float[] hsb = Color.RGBtoHSB(red, blue, green, null);

			return Color.HSBtoRGB(hsb[0], hsb[1] * 0.5F, Math.min(hsb[2] + 0.4F, 0.9F));
		}, TFBlocks.aurora_pillar.get(), TFBlocks.aurora_slab.get(), TFBlocks.auroralized_glass.get());
		blockColors.register((state, worldIn, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;

			if (worldIn == null || pos == null) {
				return FoliageColors.getDefault();
			}

			int red = 0;
			int grn = 0;
			int blu = 0;

			for (int dz = -1; dz <= 1; ++dz) {
				for (int dx = -1; dx <= 1; ++dx) {
					//int i2 = worldIn.getBiome(pos.add(dx, 0, dz)).getFoliageColor(pos.add(dx, 0, dz));
					int i2 = BiomeColors.getFoliageColor(worldIn, pos.add(dx, 0, dz));
					red += (i2 & 16711680) >> 16;
					grn += (i2 & 65280) >> 8;
					blu += i2 & 255;
				}
			}

			return (red / 9 & 255) << 16 | (grn / 9 & 255) << 8 | blu / 9 & 255;
		}, TFBlocks.dark_leaves.get(), TFBlocks.giant_leaves.get());
		blockColors.register((state, worldIn, pos, tintIndex) -> tintIndex > 15 ? 0xFFFFFF : blockColors.getColor(Blocks.GRASS.getDefaultState(), worldIn, pos, tintIndex), TFBlocks.smoker.get(), TFBlocks.fire_jet.get());
		blockColors.register((state, worldIn, pos, tintIndex) -> worldIn != null && pos != null ? 2129968 : 7455580, TFBlocks.huge_lilypad.get());
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
		}, TFBlocks.time_leaves.get());
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
		}, TFBlocks.transformation_leaves.get());
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
		}, TFBlocks.mining_leaves.get());
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
		}, TFBlocks.sorting_leaves.get());
		blockColors.register((state, worldIn, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;

			if (worldIn == null || pos == null) {
				return -1;
			} else {
				float f = BlockTFAuroraBrick.rippleFractialNoise(2, 32.0f, pos, 0.4f, 1.0f, 2f);
				return Color.HSBtoRGB(0.1f, 1f - f, (f + 2f) / 3f);
			}
		}, TFBlocks.tower_wood.get(), TFBlocks.tower_wood_cracked.get(), TFBlocks.tower_wood_infested.get(), TFBlocks.tower_wood_mossy.get());
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
						int color = BiomeColors.getFoliageColor(world, pos);
						red += (color & 16711680) >> 16;
						green += (color & 65280) >> 8;
						blue += color & 255;
					}
				}

				return (red / 9 & 0xFF) << 16 | (green / 9 & 0xFF) << 8 | blue / 9 & 0xFF;
			}
		}, TFBlocks.oak_leaves.get());
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
						int color = BiomeColors.getFoliageColor(world, pos);
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
		}, TFBlocks.canopy_leaves.get());
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
						int color = BiomeColors.getFoliageColor(world, pos);
						red += (color & 16711680) >> 16;
						green += (color & 65280) >> 8;
						blue += color & 255;
					}
				}

				int normalColor = (red / 9 & 0xFF) << 16 | (green / 9 & 0xFF) << 8 | blue / 9 & 0xFF;
				// mangrove colors
				return ((normalColor & 0xFEFEFE) + 0xC0E694) / 2;
			}
		}, TFBlocks.mangrove_leaves.get());
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
						int color = BiomeColors.getFoliageColor(world, pos);
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
		}, TFBlocks.rainboak_leaves.get());
		blockColors.register((state, worldIn, pos, tintIndex) -> FoliageColors.getSpruce(), TFBlocks.beanstalk_leaves.get(), TFBlocks.thorn_leaves.get());
		blockColors.register((state, worldIn, pos, tintIndex) -> {
			if (tintIndex != 0) {
				return 0xFFFFFF;
			} else {
				if (worldIn != null && pos != null) {
					return BiomeColors.getFoliageColor(worldIn, pos);
				} else {
					return GrassColors.get(0.5D, 1.0D);
				}
			}
		}, TFBlocks.fallen_leaves.get());
		blockColors.register((state, worldIn, pos, tintIndex) -> {
			if (tintIndex != 0) {
				return 0xFFFFFF;
			} else {
				if (worldIn != null && pos != null) {
					return BiomeColors.getGrassColor(worldIn, pos);
				} else {
					return GrassColors.get(0.5D, 1.0D);
				}
			}
		}, TFBlocks.fiddlehead.get(), TFBlocks.forest_grass.get());
		blockColors.register((state, worldIn, pos, tintIndex) -> GrassColors.get(0.5D, 1.0D),
				TFBlocks.twilight_portal_miniature_structure.get(), TFBlocks.hedge_maze_miniature_structure.get(), TFBlocks.hollow_hill_miniature_structure.get(), TFBlocks.quest_grove_miniature_structure.get(), TFBlocks.mushroom_tower_miniature_structure.get(), TFBlocks.naga_courtyard_miniature_structure.get(), TFBlocks.lich_tower_miniature_structure.get(), TFBlocks.minotaur_labyrinth_miniature_structure.get(),
				TFBlocks.hydra_lair_miniature_structure.get(), TFBlocks.goblin_stronghold_miniature_structure.get(), TFBlocks.dark_tower_miniature_structure.get(), TFBlocks.yeti_cave_miniature_structure.get(), TFBlocks.aurora_palace_miniature_structure.get(), TFBlocks.troll_cave_cottage_miniature_structure.get(), TFBlocks.final_castle_miniature_structure.get());
		blockColors.register((state, worldIn, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;
			return 0xFF00FF;
		}, TFBlocks.castle_rune_brick_pink.get(), TFBlocks.castle_door_pink.get());
		blockColors.register((state, worldIn, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;
			return 0x00FFFF;
		}, TFBlocks.castle_rune_brick_blue.get(), TFBlocks.castle_door_blue.get());
		blockColors.register((state, worldIn, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;
			return 0xFFFF00;
		}, TFBlocks.castle_rune_brick_yellow.get(), TFBlocks.castle_door_yellow.get());
		blockColors.register((state, worldIn, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;
			return 0x4B0082;
		}, TFBlocks.castle_rune_brick_purple.get(), TFBlocks.castle_door_purple.get());
		blockColors.register((state, worldIn, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;
			return 0x5C1074;
		}, TFBlocks.force_field_purple.get());
		blockColors.register((state, worldIn, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;
			return 0xFA057E;
		}, TFBlocks.force_field_pink.get());
		blockColors.register((state, worldIn, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;
			return 0xFF5B02;
		}, TFBlocks.force_field_orange.get());
		blockColors.register((state, worldIn, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;
			return 0x89E701;
		}, TFBlocks.force_field_green.get());
		blockColors.register((state, worldIn, pos, tintIndex) -> {
			if (tintIndex > 15) return 0xFFFFFF;
			return 0x0DDEFF;
		}, TFBlocks.force_field_blue.get());
	}

	@SubscribeEvent
	public static void registerItemColors(ColorHandlerEvent.Item event) {
		ItemColors itemColors = event.getItemColors();
		BlockColors blockColors = event.getBlockColors();

		itemColors.register((stack, tintIndex) -> blockColors.getColor(((BlockItem)stack.getItem()).getBlock().getDefaultState(), null, null, tintIndex),
				TFBlocks.aurora_block.get(), TFBlocks.aurora_pillar.get(), TFBlocks.aurora_slab.get(), TFBlocks.auroralized_glass.get(), TFBlocks.dark_leaves.get(), TFBlocks.giant_leaves.get(), TFBlocks.smoker.get(), TFBlocks.fire_jet.get(),
				TFBlocks.time_leaves.get(), TFBlocks.transformation_leaves.get(), TFBlocks.mining_leaves.get(), TFBlocks.sorting_leaves.get(), TFBlocks.oak_leaves.get(), TFBlocks.canopy_leaves.get(), TFBlocks.mangrove_leaves.get(), TFBlocks.rainboak_leaves.get(), TFBlocks.thorn_leaves.get(), TFBlocks.beanstalk_leaves.get(),
				TFBlocks.fallen_leaves.get(), TFBlocks.forest_grass.get(), TFBlocks.fiddlehead.get(), TFBlocks.castle_rune_brick_pink.get(), TFBlocks.castle_rune_brick_blue.get(), TFBlocks.castle_rune_brick_yellow.get(), TFBlocks.castle_rune_brick_purple.get(),
				TFBlocks.castle_door_yellow.get(), TFBlocks.castle_door_blue.get(), TFBlocks.castle_door_pink.get(), TFBlocks.castle_door_purple.get(), TFBlocks.force_field_pink.get(), TFBlocks.force_field_blue.get(), TFBlocks.force_field_green.get(), TFBlocks.force_field_orange.get(), TFBlocks.force_field_purple.get(), TFBlocks.huge_lilypad.get(),
				TFBlocks.twilight_portal_miniature_structure.get(), TFBlocks.hedge_maze_miniature_structure.get(), TFBlocks.hollow_hill_miniature_structure.get(), TFBlocks.quest_grove_miniature_structure.get(), TFBlocks.mushroom_tower_miniature_structure.get(), TFBlocks.naga_courtyard_miniature_structure.get(), TFBlocks.lich_tower_miniature_structure.get(), TFBlocks.minotaur_labyrinth_miniature_structure.get(),
				TFBlocks.hydra_lair_miniature_structure.get(), TFBlocks.goblin_stronghold_miniature_structure.get(), TFBlocks.dark_tower_miniature_structure.get(), TFBlocks.yeti_cave_miniature_structure.get(), TFBlocks.aurora_palace_miniature_structure.get(), TFBlocks.troll_cave_cottage_miniature_structure.get(), TFBlocks.final_castle_miniature_structure.get());

		itemColors.register((stack, tintIndex) ->
				stack.getItem() instanceof ItemTFArcticArmor
						? ((ItemTFArcticArmor) stack.getItem()).getColor(stack, tintIndex)
						: 0xFFFFFF,
				TFItems.arctic_helmet.get(), TFItems.arctic_chestplate.get(), TFItems.arctic_leggings.get(), TFItems.arctic_boots.get());

		/*if (TFCompat.IMMERSIVEENGINEERING.isActivated()) {
			itemColors.register(twilightforest.compat.ie.ItemTFShader::getShaderColors, twilightforest.compat.ie.ItemTFShader.shader);
			itemColors.register((stack, tintIndex) -> {
				int c = blusunrize.immersiveengineering.client.ClientUtils.getFormattingColour(ItemTFShaderGrabbag.shader_bag.getRarity(stack).color);

				float d = tintIndex + 1;

				return (int) ((c >> 16 & 0xFF) / d) << 16
						| (int) ((c >> 8 & 0xFF) / d) << 8
						| (int) ((c & 0xFF) / d);
			}, twilightforest.compat.ie.ItemTFShaderGrabbag.shader_bag);
		}*/
	}

	private ColorHandler() {}
}
