package twilightforest.datagen.assets.models;

import net.minecraft.client.data.models.ItemModelOutput;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.properties.select.DisplayContext;
import net.minecraft.client.renderer.special.ChestSpecialRenderer;
import net.minecraft.client.renderer.special.ShieldSpecialRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SkullBlock;
import net.neoforged.neoforge.capabilities.IBlockCapabilityProvider;
import org.w3c.dom.Text;
import twilightforest.TwilightForestMod;
import twilightforest.client.renderer.special.*;
import twilightforest.datagen.helpers.ItemModelBuilders;
import twilightforest.enums.BossVariant;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class ItemModelGenerator extends ItemModelBuilders {
	public ItemModelGenerator(ItemModelOutput output, BiConsumer<ResourceLocation, ModelInstance> modelOutput) {
		super(output, modelOutput);
	}

	@Override
	public void run() {
		this.generateKnightmetalShield(TFItems.KNIGHTMETAL_SHIELD.get());

		this.generateSpawnEgg("alpha_yeti", 0xCDCDCD, 0x29486E);
		this.generateSpawnEgg("armored_giant", 0x239391, 0x9A9A9A);
		this.generateSpawnEgg("bighorn_sheep", 0xDBCEAF, 0xD7C771);
		this.generateSpawnEgg("block_and_chain_goblin", 0xD3E7BC, 0x1F3FFF);
		this.generateSpawnEgg("boar", 0x83653B, 0xFFEFCA);
		this.generateSpawnEgg("carminite_broodling", 0x343C14, 0xBAEE02);
		this.generateSpawnEgg("carminite_ghastguard", 0xBCBCBC, 0xB77878);
		this.generateSpawnEgg("carminite_ghastling", 0xBCBCBC, 0xA74343);
		this.generateSpawnEgg("carminite_golem", 0x6B3D20, 0xE2DDDA);
		this.generateSpawnEgg("death_tome", 0x774E22, 0xDBCDBE);
		this.generateSpawnEgg("deer", 0x7B4D2E, 0x4B241D);
		this.generateSpawnEgg("dwarf_rabbit", 0xFEFEEE, 0xCCAA99);
		this.generateSpawnEgg("fire_beetle", 0x1D0B00, 0xCB6F25);
		this.generateSpawnEgg("giant_miner", 0x211B52, 0x9A9A9A);
		this.generateSpawnEgg("hedge_spider", 0x235F13, 0x562653);
		this.generateSpawnEgg("helmet_crab", 0xFB904B, 0xD3E7BC);
		this.generateSpawnEgg("hostile_wolf", 0xD7D3D3, 0xAB1E14);
		this.generateSpawnEgg("hydra", 0x142940, 0x29806B);
		this.generateSpawnEgg("ice_crystal", 0xDCE9FE, 0xADCAFB);
		this.generateSpawnEgg("king_spider", 0x2C1A0E, 0xFFC017);
		this.generateSpawnEgg("knight_phantom", 0xA6673B, 0xD3E7BC);
		this.generateSpawnEgg("kobold", 0x372096, 0x895D1B);
		this.generateSpawnEgg("lich", 0xACA489, 0x360472);
		this.generateSpawnEgg("lower_goblin_knight", 0x566055, 0xD3E7BC);
		this.generateSpawnEgg("maze_slime", 0xA3A3A3, 0x2A3B17);
		this.generateSpawnEgg("minoshroom", 0xA81012, 0xAA7D66);
		this.generateSpawnEgg("minotaur", 0x3F3024, 0xAA7D66);
		this.generateSpawnEgg("mist_wolf", 0x3A1411, 0xE2C88A);
		this.generateSpawnEgg("mosquito_swarm", 0x080904, 0x2D2F21);
		this.generateSpawnEgg("naga", 0xA4D316, 0x1B380B);
		this.generateSpawnEgg("penguin", 0x12151B, 0xF9EDD2);
		this.generateSpawnEgg("pinch_beetle", 0xBC9327, 0x241609);
		this.generateSpawnEgg("quest_ram", 0xFEFEEE, 0x33AADD);
		this.generateSpawnEgg("raven", 0x000011, 0x222233);
		this.generateSpawnEgg("redcap", 0x3B3A6C, 0xAB1E14);
		this.generateSpawnEgg("redcap_sapper", 0x575D21, 0xAB1E14);
		this.generateSpawnEgg("skeleton_druid", 0xA3A3A3, 0x2A3B17);
		this.generateSpawnEgg("slime_beetle", 0x0C1606, 0x60A74C);
		this.generateSpawnEgg("snow_guardian", 0xD3E7BC, 0xFEFEFE);
		this.generateSpawnEgg("snow_queen", 0xB1B2D4, 0x87006E);
		this.generateSpawnEgg("squirrel", 0x904F12, 0xEEEEEE);
		this.generateSpawnEgg("stable_ice_core", 0xA1BFF3, 0x7000F8);
		this.generateSpawnEgg("swarm_spider", 0x32022E, 0x17251E);
		this.generateSpawnEgg("tiny_bird", 0x33AADD, 0x1188EE);
		this.generateSpawnEgg("towerwood_borer", 0x5D2B21, 0xACA03A);
		this.generateSpawnEgg("troll", 0x9EA98F, 0xB0948E);
		this.generateSpawnEgg("unstable_ice_core", 0x9AACF5, 0x9B0FA5);
		this.generateSpawnEgg("ur_ghast", 0xBCBCBC, 0xB77878);
		this.generateSpawnEgg("winter_wolf", 0xDFE3E5, 0xB2BCCA);
		this.generateSpawnEgg("wraith", 0x505050, 0x838383);
		this.generateSpawnEgg("yeti", 0xDEDEDE, 0x4675BB);
	}

	private void generateSpawnEgg(String entityName, int primary, int secondary) {
		this.generateSpawnEgg(BuiltInRegistries.ITEM.getValue(TwilightForestMod.prefix(entityName + "_spawn_egg")), primary, secondary);
	}

	public void generateKnightmetalShield(Item shieldItem) {
		var normal = ItemModelUtils.specialModel(ModelLocationUtils.getModelLocation(shieldItem), new KnightmetalShieldSpecialRenderer.Unbaked());
		var blocking = ItemModelUtils.specialModel(ModelLocationUtils.getModelLocation(shieldItem, "_blocking"), new KnightmetalShieldSpecialRenderer.Unbaked());
		this.generateBooleanDispatch(shieldItem, ItemModelUtils.isUsingItem(), blocking, normal);
	}
}
