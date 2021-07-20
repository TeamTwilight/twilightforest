package twilightforest.compat.ie;

import blusunrize.immersiveengineering.api.shader.ShaderLayer;
import blusunrize.immersiveengineering.api.shader.impl.ShaderCaseShield;
import net.minecraft.item.ItemStack;

import java.util.Collection;

public class TFShaderCaseShield extends ShaderCaseShield {
    private final int STACK_BREAK;

    public TFShaderCaseShield(int stackBreak, Collection<ShaderLayer> layers) {
        super(layers);
        this.STACK_BREAK = stackBreak;
    }

    @Override
    public boolean shouldRenderGroupForPass(String part, int pass) {
        return !"flash".equals(part) && !"shock".equals(part) || pass >= STACK_BREAK - 1;

    }
}
