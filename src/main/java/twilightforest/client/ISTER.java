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
import twilightforest.block.BlockKeepsakeCasket;
import twilightforest.block.BlockTFAbstractTrophy;
import twilightforest.client.renderer.ShaderGrabbagStackRenderer;
import twilightforest.client.renderer.tileentity.TileEntityTFTrophyRenderer;
import twilightforest.compat.ie.ItemTFShaderGrabbag;
import twilightforest.tileentity.TileEntityKeepsakeCasket;

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

					ModelResourceLocation back = new ModelResourceLocation(TwilightForestMod.prefix(((BlockTFAbstractTrophy) block).getVariant().getTrophyType().getModelName()), "inventory");
					IBakedModel modelBack = Minecraft.getInstance().getItemRenderer().getItemModelMesher().getModelManager().getModel(back);

					ms.push();
					ms.translate(0.5F, 0.0F, 0.5F);
					ms.rotate(Vector3f.YP.rotationDegrees(-45));
					ms.translate(0.0F, -0.25F, -0.9F);
					switch(((BlockTFAbstractTrophy) block).getVariant()) {
						case NAGA:
							ms.scale(0.85f, 0.85f, 0.85f);
							break;
						case HYDRA:
							ms.scale(1.1F, 1.1F, 1.1F);
							ms.translate(0.02F, 0.05F, 0.0F);
							break;
						case UR_GHAST:
							ms.translate(0F, -0.65F, 0F);
							break;
						case QUEST_RAM:
							ms.scale(1.1f, 1.1f, 1.1f);
							ms.translate(-0.05F, 0.0F, 0.0F);
							break;
						default:
							break;
					}
					Minecraft.getInstance().getItemRenderer().renderItem(TileEntityTFTrophyRenderer.stack, ItemCameraTransforms.TransformType.GUI, false, ms, buffers, light, overlay, ForgeHooksClient.handleCameraTransforms(ms, modelBack, camera, false));
					ms.pop();

					ms.push();
					ms.translate(0.5F, 0.0F, 0.5F);
					ms.rotate(Vector3f.YP.rotationDegrees(TFConfig.CLIENT_CONFIG.rotateTrophyHeadsGui.get() ? TFClientEvents.rotationTicker : 0));
					ms.translate(-0.5F, 0.0F, -0.5F);
					TileEntityTFTrophyRenderer.render((Direction) null, 180.0F, ((BlockTFAbstractTrophy) block).getVariant(), 0.0F, ms, buffers, light);
					ms.pop();

					//Fun Scaling, may add as a secret later
					//GlStateManager.rotatef(TFClientEvents.rotationTicker, 0F, 0F, 0F);
				} else {
					TileEntityTFTrophyRenderer.render((Direction) null, 180.0F, ((BlockTFAbstractTrophy) block).getVariant(), 0.0F, ms, buffers, light);
				}
			} else if (block instanceof BlockKeepsakeCasket) {
				TileEntityRendererDispatcher.instance.renderItem(new TileEntityKeepsakeCasket(), ms, buffers, light, overlay);
			} else {
				TileEntityRenderer<TileEntity> renderer = TileEntityRendererDispatcher.instance.getRenderer(dummy);
				renderer.render(null, 0, ms, buffers, light, overlay);
			}
		}
	}
}
