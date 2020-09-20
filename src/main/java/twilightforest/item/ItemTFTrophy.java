package twilightforest.item;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.WallOrFloorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import twilightforest.enums.BossVariant;

import java.util.Locale;

public class ItemTFTrophy extends WallOrFloorItem {

	private final BossVariant bossVariant;

	public ItemTFTrophy(Block floorBlockIn, Block wallBlockIn, Item.Properties builder, BossVariant variant) {
		super(floorBlockIn, wallBlockIn, builder);
		bossVariant = variant;
	}

	@Override
	public ITextComponent getDisplayName(ItemStack stack) {
		return new TranslationTextComponent(this.getTranslationKey(stack), new TranslationTextComponent("entity.twilightforest." + bossVariant.getString().toLowerCase(Locale.ROOT) + ".name"));
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
