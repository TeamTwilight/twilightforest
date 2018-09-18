package twilightforest.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class ItemTFMoonDial extends ItemTF {
    public ItemTFMoonDial() {
        this.addPropertyOverride(new ResourceLocation("phase"), new IItemPropertyGetter() {
            @Override
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World world, @Nullable EntityLivingBase entityBase) {
                boolean flag = entityBase != null;
                Entity entity = flag ? entityBase : stack.getItemFrame();

                if (world == null && entity != null) world = entity.world;

                return world == null ? 0.0F : (float) (world.provider.isSurfaceWorld() ? MathHelper.frac(world.getMoonPhase() / 8.0f) : this.wobble(world, Math.random()));
            }

            @SideOnly(Side.CLIENT)
            double rotation;
            @SideOnly(Side.CLIENT)
            double rota;
            @SideOnly(Side.CLIENT)
            long lastUpdateTick;

            @SideOnly(Side.CLIENT)
            private double wobble(World world, double rotation) {
                if (world.getTotalWorldTime() != this.lastUpdateTick) {
                    this.lastUpdateTick = world.getTotalWorldTime();
                    double delta = rotation - this.rotation;
                    delta = MathHelper.positiveModulo(delta + 0.5D, 1.0D) - 0.5D;
                    this.rota += delta * 0.1D;
                    this.rota *= 0.9D;
                    this.rotation = MathHelper.positiveModulo(this.rotation + this.rota, 1.0D);
                }

                return this.rotation;
            }
        });
    }
}
