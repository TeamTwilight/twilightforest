package twilightforest.client.renderer.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.fluid.Fluids;
import net.minecraft.tileentity.IChestLid;
import net.minecraft.tileentity.TileEntityMerger;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.TwilightForestMod;
import twilightforest.block.BlockKeepsakeCasket;
import twilightforest.block.TFBlocks;
import twilightforest.enums.BlockLoggingEnum;
import twilightforest.tileentity.TileEntityKeepsakeCasket;

import java.util.Locale;

/**
 * Keepsake Casket Model - MCVinnyq
 * Created using Tabula 8.0.0
 */
//Most of the other stuff is derived from ChestTileEntityRenderer
@OnlyIn(Dist.CLIENT)
public class TileEntityTFCasketRenderer<T extends TileEntityKeepsakeCasket & IChestLid> extends TileEntityRenderer<T> {

    public ModelRenderer base;
    public ModelRenderer lid;

    public TileEntityTFCasketRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);

        this.lid = new ModelRenderer(64, 64, 0, 0);
        this.lid.setRotationPoint(0.0F, -6.0F, 6.0F);
        this.lid.addBox(-8.0F, -8.0F, -13.0F, 16.0F, 10.0F, 14.0F, 0.0F, 0.0F, 0.0F);
        this.lid.setTextureOffset(0, 46).addBox(-8.0F, -10.0F, -13.0F, 16.0F, 2.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        this.lid.setTextureOffset(2, 34).addBox(-7.99F, -10.0F, -12.0F, 0.0F, 2.0F, 14.0F, 0.0F, 0.0F, 0.0F);
        this.lid.setTextureOffset(2, 36).addBox(7.99F, -10.0F, -12.0F, 0.0F, 2.0F, 14.0F, 0.0F, 0.0F, 0.0F);
        this.base = new ModelRenderer(64, 64, 0, 0);
        this.base.setRotationPoint(0.0F, -0.01F, 0.0F);
        this.base.setTextureOffset(1, 28).addBox(-7.0F, -10.0F, -2.0F, 14.0F, 10.0F, 8.0F, 0.0F, 0.0F, 0.0F);
        this.base.setTextureOffset(0, 26).addBox(-7.0F, -10.0F, -6.0F, 1.0F, 6.0F, 4.0F, 0.0F, 0.0F, 0.0F);
        this.base.setTextureOffset(40, 26).addBox(6.0F, -10.0F, -6.0F, 1.0F, 6.0F, 4.0F, 0.0F, 0.0F, 0.0F);
        this.base.setTextureOffset(0, 56).addBox(-7.0F, -4.0F, -6.0F, 14.0F, 4.0F, 4.0F, 0.0F, 0.0F, 0.0F);
    }

    @Override
    public void render(T tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        World world = tileEntityIn.getWorld();
        boolean flag = world != null;
        BlockState blockstate = flag ? tileEntityIn.getBlockState() : TFBlocks.keepsake_casket.get().getDefaultState();
        Block block = blockstate.getBlock();
        if (block instanceof BlockKeepsakeCasket) {
            BlockLoggingEnum type = blockstate.get(BlockLoggingEnum.MULTILOGGED);
            int damage = blockstate.get(BlockKeepsakeCasket.BREAKAGE);
            boolean solid = type.getBlock() != Blocks.AIR && type.getFluid() == Fluids.EMPTY;
            float facing = blockstate.get(HorizontalBlock.HORIZONTAL_FACING).getHorizontalAngle();

            if(solid) {
                matrixStackIn.push();
                matrixStackIn.translate(0.5D, 0.5D, 0.5D);
                matrixStackIn.rotate(Vector3f.YP.rotationDegrees(-facing));
                matrixStackIn.translate(-0.5D, -0.5D, -0.5D);
                ResourceLocation BLOCK = TwilightForestMod.prefix("block/casket_" + type.getBlock().getRegistryName().getPath().toLowerCase(Locale.ROOT));
                IBakedModel blockrender = Minecraft.getInstance().getModelManager().getModel(BLOCK);
                BlockRendererDispatcher render = Minecraft.getInstance().getBlockRendererDispatcher();
                render.getBlockModelRenderer().renderModel(world, blockrender, type.getBlock().getDefaultState(), tileEntityIn.getPos(), matrixStackIn, bufferIn.getBuffer(RenderType.getTranslucent()), false, world.rand, MathHelper.getPositionRandom(BlockPos.ZERO), OverlayTexture.NO_OVERLAY);
                matrixStackIn.pop();
            }

            matrixStackIn.push();
            matrixStackIn.scale(-1, -1, -1);
            matrixStackIn.translate(-0.5D, 0.0D, -0.5D);
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(-facing));

            TileEntityMerger.ICallbackWrapper<? extends TileEntityKeepsakeCasket> icallbackwrapper = TileEntityMerger.ICallback::func_225537_b_;
            float f1 = icallbackwrapper.apply(BlockKeepsakeCasket.getLidRotationCallback(tileEntityIn)).get(partialTicks);
            f1 = 1.0F - f1;
            f1 = 1.0F - f1 * f1 * f1;

            ResourceLocation CASKET = TwilightForestMod.getModelTexture("casket/keepsake_casket_" + damage + ".png");
            this.renderModels(matrixStackIn, bufferIn.getBuffer(RenderType.getEntityCutoutNoCull(CASKET)), this.lid, this.base, f1, combinedLightIn, combinedOverlayIn);
            matrixStackIn.pop();
        }
    }

    private void renderModels(MatrixStack matrixStackIn, IVertexBuilder bufferIn, ModelRenderer lid, ModelRenderer base, float lidAngle, int combinedLightIn, int combinedOverlayIn) {
        lid.rotateAngleX = -(lidAngle * ((float)Math.PI / 2F));
        lid.render(matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
        base.render(matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
    }
}
