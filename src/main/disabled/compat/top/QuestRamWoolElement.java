package twilightforest.compat.top;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.api.IElementFactory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import twilightforest.TwilightForestMod;
import twilightforest.util.ColorUtil;

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
    public void render(GuiGraphics graphics, int x, int y) {
        PoseStack stack = graphics.pose();
        stack.pushPose();
        RenderSystem.enableDepthTest();
        PoseStack modelStack = RenderSystem.getModelViewStack();
        modelStack.translate(3, 10, 0);
        modelStack.scale(0.6f, 0.6f, 0.6f);

        RenderSystem.applyModelViewMatrix();
        int getRenderedWools = 0;
        int colum = 0;
        int row = 0;
        for (Map.Entry<DyeColor, Block> entry : ColorUtil.WOOL_TO_DYE_IN_RAM_ORDER.entrySet()) {
            if (!isColorPresent(entry.getKey())) {
                if (getRenderedWools % 8 == 0) {
                    row++;
                    colum = 0;
                } else {
                    colum++;
                }

                graphics.renderItem(new ItemStack(entry.getValue()), x + (colum * 15), y + (row * 15));

                getRenderedWools++;
            }
        }
        RenderSystem.disableDepthTest();
        stack.popPose();
    }

    @Override
    public int getWidth() {
        return 4;
    }

    @Override
    public int getHeight() {
        int getRenderedWools = 0;
        int row = 0;
        for (Map.Entry<DyeColor, Block> entry : ColorUtil.WOOL_TO_DYE_IN_RAM_ORDER.entrySet()) {
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
