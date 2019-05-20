package twilightforest.compat.ie;

import blusunrize.immersiveengineering.api.shader.ShaderCaseChemthrower;
import net.minecraft.item.ItemStack;

import java.util.Collection;

public class TFShaderCaseChemthrower extends ShaderCaseChemthrower {
    private final int STACK_BREAK;

    public TFShaderCaseChemthrower(int stackBreak, Collection<ShaderLayer> layers) {
        super(layers);
        this.STACK_BREAK = stackBreak;
    }

    @Override
    public boolean renderModelPartForPass(ItemStack shader, ItemStack item, String part, int pass) {
        if ("grip".equals(part))
            return pass == 0;

        if (pass == 0)
            return false;

        if (pass == 1 && "cage".equals(part))
            return renderCageOnBase;

        if (tanksUncoloured && "tanks".equals(part))
            return pass >= STACK_BREAK - 1;

        return tanksUncoloured || !"tanks".equals(part) || pass < STACK_BREAK - 1;

    }
}
