package twilightforest.client.properties;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jspecify.annotations.Nullable;
import twilightforest.TwilightForestMod;

import java.util.HashMap;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class SpawnEggTintSource {

	private static final Map<Identifier, int[]> COLORS = new HashMap<>();

	static {
		putColor("alpha_yeti", 0xCDCDCD, 0x29486E);
		putColor("armored_giant", 0x239391, 0x9A9A9A);
		putColor("bighorn_sheep", 0xDBCEAF, 0xD7C771);
		putColor("block_and_chain_goblin", 0xD3E7BC, 0x1F3FFF);
		putColor("boar", 0x83653B, 0xFFEFCA);
		putColor("carminite_broodling", 0x343C14, 0xBAEE02);
		putColor("carminite_ghastguard", 0xBCBCBC, 0xB77878);
		putColor("carminite_ghastling", 0xBCBCBC, 0xA74343);
		putColor("carminite_golem", 0x6B3D20, 0xE2DDDA);
		putColor("death_tome", 0x774E22, 0xDBCDBE);
		putColor("deer", 0x7B4D2E, 0x4B241D);
		putColor("dwarf_rabbit", 0xFEFEEE, 0xCCAA99);
		putColor("fire_beetle", 0x1D0B00, 0xCB6F25);
		putColor("giant_miner", 0x211B52, 0x9A9A9A);
		putColor("hedge_spider", 0x235F13, 0x562653);
		putColor("helmet_crab", 0xFB904B, 0xD3E7BC);
		putColor("hostile_wolf", 0xD7D3D3, 0xAB1E14);
		putColor("hydra", 0x142940, 0x29806B);
		putColor("ice_crystal", 0xDCE9FE, 0xADCAFB);
		putColor("king_spider", 0x2C1A0E, 0xFFC017);
		putColor("knight_phantom", 0xA6673B, 0xD3E7BC);
		putColor("kobold", 0x372096, 0x895D1B);
		putColor("lich", 0xACA489, 0x360472);
		putColor("lower_goblin_knight", 0x566055, 0xD3E7BC);
		putColor("maze_slime", 0xA3A3A3, 0x2A3B17);
		putColor("minoshroom", 0xA81012, 0xAA7D66);
		putColor("minotaur", 0x3F3024, 0xAA7D66);
		putColor("mist_wolf", 0x3A1411, 0xE2C88A);
		putColor("mosquito_swarm", 0x080904, 0x2D2F21);
		putColor("naga", 0xA4D316, 0x1B380B);
		putColor("penguin", 0x5051AD, 0xC1C6E4);
		putColor("pinch_beetle", 0x346A2A, 0xD3843D);
		putColor("quest_ram", 0xEEE1CE, 0xBBB0A9);
		putColor("raven", 0x202020, 0x474747);
		putColor("redcap", 0xB53100, 0x665240);
		putColor("redcap_sapper", 0xB53100, 0xC7B321);
		putColor("skeleton_druid", 0x5D4913, 0xF5F1C1);
		putColor("slime_beetle", 0x242118, 0x9E863F);
		putColor("snow_guardian", 0x8080C7, 0x3E3E5D);
		putColor("snow_queen", 0x4B555A, 0xE9EBED);
		putColor("squirrel", 0x4F361E, 0xE4B58A);
		putColor("stable_ice_core", 0xD19FFC, 0x7538B4);
		putColor("swarm_spider", 0x141414, 0x64140B);
		putColor("tiny_bird", 0x446CAC, 0x212C4B);
		putColor("towerwood_borer", 0x4F3F31, 0x6B636C);
		putColor("troll", 0x7A6A53, 0x37826B);
		putColor("unstable_ice_core", 0xC6E0ED, 0x4C90B2);
		putColor("ur_ghast", 0x4C4651, 0xD6433D);
		putColor("winter_wolf", 0xEFECDC, 0x8196AB);
		putColor("wraith", 0x5D5760, 0xC1AABE);
		putColor("yeti", 0xDEDEDE, 0x4675BB);
	}

	private static void putColor(String name, int primary, int secondary) {
		COLORS.put(TwilightForestMod.prefix(name + "_spawn_egg"), new int[] { primary, secondary });
	}

	public record Primary() implements ItemTintSource {
		public static final MapCodec<Primary> MAP_CODEC = MapCodec.unit(new Primary());

		@Override
		public int calculate(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity owner) {
			Identifier id = BuiltInRegistries.ITEM.getKey(stack.getItem());
			int[] colors = COLORS.get(id);
			return colors != null ? colors[0] : 0xFFFFFF;
		}

		@Override
		public MapCodec<? extends ItemTintSource> type() {
			return MAP_CODEC;
		}
	}

	public record Secondary() implements ItemTintSource {
		public static final MapCodec<Secondary> MAP_CODEC = MapCodec.unit(new Secondary());

		@Override
		public int calculate(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity owner) {
			Identifier id = BuiltInRegistries.ITEM.getKey(stack.getItem());
			int[] colors = COLORS.get(id);
			return colors != null ? colors[1] : 0xFFFFFF;
		}

		@Override
		public MapCodec<? extends ItemTintSource> type() {
			return MAP_CODEC;
		}
	}
}
