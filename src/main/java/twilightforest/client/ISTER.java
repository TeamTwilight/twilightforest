package twilightforest.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.registries.ForgeRegistries;
import twilightforest.TFConfig;
import twilightforest.TwilightForestMod;
import twilightforest.block.BlockTFAbstractTrophy;
import twilightforest.client.renderer.tileentity.TileEntityTFTrophyRenderer;
import twilightforest.enums.BossVariant;

public class ISTER extends ItemStackTileEntityRenderer {
	private final ResourceLocation typeId;
	private TileEntity dummy;

	// When this is called from Item register, TEType register has not run yet so we can't pass the actual object
	public ISTER(ResourceLocation typeId) {
		this.typeId = typeId;
	}

	@Override
	public void func_239207_a_(ItemStack stack, ItemCameraTransforms.TransformType camera, MatrixStack ms, IRenderTypeBuffer buffers, int light, int overlay) {

		if (dummy == null) {
			dummy = ForgeRegistries.TILE_ENTITIES.getValue(typeId).create();
		}
		Item item = stack.getItem();
		if (item instanceof BlockItem) {
			Block block = ((BlockItem) item).getBlock();
			if (block instanceof BlockTFAbstractTrophy) {
				if(camera == ItemCameraTransforms.TransformType.GUI) {

					boolean silver = ((BlockTFAbstractTrophy) block).getVariant() == BossVariant.KNIGHT_PHANTOM || ((BlockTFAbstractTrophy) block).getVariant() == BossVariant.MINOSHROOM;
					boolean quest = ((BlockTFAbstractTrophy) block).getVariant() == BossVariant.QUEST_RAM;
					boolean naga = ((BlockTFAbstractTrophy) block).getVariant() == BossVariant.NAGA;
					boolean hydra = ((BlockTFAbstractTrophy) block).getVariant() == BossVariant.HYDRA;
					boolean ghast = ((BlockTFAbstractTrophy) block).getVariant() == BossVariant.UR_GHAST;
					ModelResourceLocation gold_back = new ModelResourceLocation(TwilightForestMod.prefix("trophy"), "inventory");
					ModelResourceLocation silver_back = new ModelResourceLocation(TwilightForestMod.prefix("trophy_minor"), "inventory");
					ModelResourceLocation ironwood_back = new ModelResourceLocation(TwilightForestMod.prefix("trophy_quest"), "inventory");
					IBakedModel modelBack = Minecraft.getInstance().getItemRenderer().getItemModelMesher().getModelManager().getModel(quest ? ironwood_back : silver ? silver_back : gold_back);

					float scale1 = naga ? 0.85F : hydra || quest ? 1.15F : 1.0F;
					float scale2 = naga ? 1.15F : hydra || quest ? 0.85F : 1.0F;

					ms.translate(0.5F, 0.0F, 0.5F);
					ms.rotate(Vector3f.YP.rotationDegrees(-45));
					ms.translate(quest ? -0.05F : -0.025F, ghast ? -0.77F : -0.12F, -0.75F);
					ms.scale(scale1, scale1, scale1);
					Minecraft.getInstance().getItemRenderer().renderItem(TileEntityTFTrophyRenderer.stack, ItemCameraTransforms.TransformType.GUI, false, ms, buffers, light, overlay, ForgeHooksClient.handleCameraTransforms(ms, modelBack, camera, false));
					ms.scale(scale2, scale2, scale2);
					ms.translate(quest ? 0.05F : 0.025F, ghast ? 0.77F : 0.12F, 0.75F);
					ms.rotate(Vector3f.YP.rotationDegrees(45));
					ms.rotate(Vector3f.YP.rotationDegrees(TFConfig.CLIENT_CONFIG.rotateTrophyHeadsGui.get() ? TFClientEvents.rotationTicker : 0));
					ms.translate(-0.5F, 0.0F, -0.5F);

					//Fun Scaling, may add as a secret later
					//GlStateManager.rotatef(TFClientEvents.rotationTicker, 0F, 0F, 0F);
				}
				TileEntityTFTrophyRenderer.render((Direction) null, 180.0F, ((BlockTFAbstractTrophy) block).getVariant(), 0.0F, ms, buffers, light);
			} else {
				TileEntityRenderer<TileEntity> renderer = TileEntityRendererDispatcher.instance.getRenderer(dummy);
				renderer.render(null, 0, ms, buffers, light, overlay);
			}
		}
	}
}
