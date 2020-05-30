package twilightforest.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class ItemTFMoonDial extends Item {
    public ItemTFMoonDial(Properties props) {
        super(props);
        this.addPropertyOverride(new ResourceLocation("phase"), new IItemPropertyGetter() {
            @Override
            @OnlyIn(Dist.CLIENT)
            public float call(ItemStack stack, @Nullable World world, @Nullable LivingEntity entityBase) {
                boolean flag = entityBase != null;
                Entity entity = flag ? entityBase : stack.getItemFrame();

                if (world == null && entity != null) world = entity.world;

                return world == null ? 0.0F : (float) (world.dimension.isSurfaceWorld() ? MathHelper.frac(world.getMoonPhase() / 8.0f) : this.wobble(world, Math.random()));
            }

            @OnlyIn(Dist.CLIENT)
            double rotation;
            @OnlyIn(Dist.CLIENT)
            double rota;
            @OnlyIn(Dist.CLIENT)
            long lastUpdateTick;

            @OnlyIn(Dist.CLIENT)
            private double wobble(World world, double rotation) {
                if (world.getGameTime() != this.lastUpdateTick) {
                    this.lastUpdateTick = world.getGameTime();
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
