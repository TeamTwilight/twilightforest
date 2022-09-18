package twilightforest.compat.top;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.api.IElementFactory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import twilightforest.TwilightForestMod;

import java.util.Map;

public class QuestRamWoolElement implements IElement {

    public static ResourceLocation ID = TwilightForestMod.prefix("quest_ram_wool");

    private final int colorData;

    public QuestRamWoolElement(int colorData) {
        this.colorData = colorData;
    }

    public QuestRamWoolElement(FriendlyByteBuf buf) {
        this.colorData = buf.readInt();
    }

    public boolean isColorPresent(DyeColor color) {
        return (this.colorData & (1 << color.getId())) > 0;
    }

    @Override
    public void render(PoseStack poseStack, int x, int y) {
        poseStack.pushPose();
        RenderSystem.enableDepthTest();
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        PoseStack modelStack = RenderSystem.getModelViewStack();
        modelStack.translate(3, 10, 0);
        modelStack.scale(0.6f, 0.6f, 0.6f);

        RenderSystem.applyModelViewMatrix();
        int getRenderedWools = 0;
        int colum = 0;
        int row = 0;
        for (Map.Entry<DyeColor, Block> entry : TOPQuestRamWoolProvider.WOOL_TO_DYE.entrySet()) {
            if (!isColorPresent(entry.getKey())) {
                if (getRenderedWools % 8 == 0) {
                    row++;
                    colum = 0;
                } else {
                    colum++;
                }

                itemRenderer.renderGuiItem(new ItemStack(entry.getValue()), x + (colum * 15), y + (row * 15));

                getRenderedWools++;
            }
        }
        RenderSystem.disableDepthTest();
        poseStack.popPose();
    }

    @Override
    public int getWidth() {
        return 4;
    }

    @Override
    public int getHeight() {
        int getRenderedWools = 0;
        int row = 0;
        for (Map.Entry<DyeColor, Block> entry : TOPQuestRamWoolProvider.WOOL_TO_DYE.entrySet()) {
            if (!isColorPresent(entry.getKey())) {
                if (getRenderedWools % 8 == 0) {
                    row++;
                }

                getRenderedWools++;
            }
        }

        return row * 10;
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.colorData);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    public enum Factory implements IElementFactory {
        INSTANCE;

        @Override
        public IElement createElement(FriendlyByteBuf buf) {
            return new QuestRamWoolElement(buf);
        }

        @Override
        public ResourceLocation getId() {
            return ID;
        }
    }
}
