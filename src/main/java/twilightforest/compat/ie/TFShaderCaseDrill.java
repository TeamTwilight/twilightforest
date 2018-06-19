package twilightforest.compat.ie;

import blusunrize.immersiveengineering.api.shader.ShaderCase;
import blusunrize.immersiveengineering.api.shader.ShaderCaseDrill;
import net.minecraft.item.ItemStack;

import java.util.Collection;

public class TFShaderCaseDrill extends ShaderCaseDrill {
    private final int STACK_BREAK;
    private int headLayers = 1;

    public TFShaderCaseDrill(int stackBreak, Collection<ShaderCase.ShaderLayer> layers) {
        super(layers);
        this.STACK_BREAK = stackBreak;
    }

    @Override
    public boolean renderModelPartForPass(ItemStack shader, ItemStack item, String modelPart, int pass) {
        if("drill_head".equals(modelPart) || "upgrade_damage0".equals(modelPart) || "upgrade_damage1".equals(modelPart) || "upgrade_damage2".equals(modelPart) || "upgrade_damage3".equals(modelPart) || "upgrade_damage4".equals(modelPart))
            return pass >= STACK_BREAK + 1;

        if(pass >= STACK_BREAK + 1 ) //Last pass on drills is just for the head and augers
            return false;

        if("upgrade_speed".equals(modelPart) || "upgrade_waterproof".equals(modelPart))//Upgrades only render on the uncoloured pass
            return pass >= STACK_BREAK - 1;

        if("drill_grip".equals(modelPart))
            return pass == 0;

        return pass != 0;
    }

    @Override
    public ShaderCaseDrill addHeadLayers(ShaderLayer... addedLayers) {
        headLayers+=addedLayers.length;
        return super.addHeadLayers();
    }
}
