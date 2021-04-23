package twilightforest.item;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.WallOrFloorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import twilightforest.enums.BossVariant;

import javax.annotation.Nullable;
import java.util.Locale;

public class ItemTFTrophy extends WallOrFloorItem {

	public ItemTFTrophy(Block floorBlockIn, Block wallBlockIn, Item.Properties builder) {
		super(floorBlockIn, wallBlockIn, builder);
	}

	@Override
	public boolean canEquip(ItemStack stack, EquipmentSlotType armorType, Entity entity) {
		return armorType == EquipmentSlotType.HEAD;
	}

	@Override
	@Nullable
	public EquipmentSlotType getEquipmentSlot(ItemStack stack) {
		return EquipmentSlotType.HEAD;
	}


	//TODO 1.14: Somehow get this working...
/*	@OnlyIn(Dist.CLIENT)
	@Override
	public void registerModel() {

		ModelResourceLocation itemModelLocation = new ModelResourceLocation(TwilightForestMod.ID + ":trophy_tesr", "inventory");

		TileEntityTFTrophyRenderer tesr = new TileEntityTFTrophyRenderer(itemModelLocation);
		//TileEntityTFTrophyRenderer tesrMinor = new TileEntityTFTrophyRenderer() { @Override protected String getModelRSL() { return TwilightForestMod.ID + ":trophy_minor"; } };

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTFTrophyRenderer.DummyTile.class, tesr);

		for (BossVariant variant : BossVariant.values()) {
			if (variant != BossVariant.ALPHA_YETI) {
				ModelLoader.setCustomModelResourceLocation(this, variant.ordinal(), itemModelLocation);
				ForgeHooksClient.registerTESRItemStack(this, variant.ordinal(), TileEntityTFTrophyRenderer.DummyTile.class);
			}
		}

		ModelBakery.registerItemVariants(this,
				new ModelResourceLocation(new ResourceLocation(TwilightForestMod.ID, "trophy"), "inventory"),
				new ModelResourceLocation(new ResourceLocation(TwilightForestMod.ID, "trophy_minor"), "inventory"),
				new ModelResourceLocation(new ResourceLocation(TwilightForestMod.ID, "trophy_quest"), "inventory")
		);
	}*/
}
