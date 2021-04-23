package twilightforest.entity.boss;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.EntitySize;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.TFSounds;
import twilightforest.client.renderer.entity.RenderTFSnowQueenIceShield;
import twilightforest.entity.TFPartEntity;

public class EntityTFSnowQueenIceShield extends TFPartEntity<EntityTFSnowQueen> {

    public EntityTFSnowQueenIceShield(EntityTFSnowQueen parent) {
        super(parent);
        size = EntitySize.flexible(0.75F, 0.75F);
    }

	@Override
	@OnlyIn(Dist.CLIENT)
	public EntityRenderer<?> renderer(EntityRendererManager manager) {
		return new RenderTFSnowQueenIceShield<>(manager);
	}

	@Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        playSound(TFSounds.SNOW_QUEEN_BREAK, 1.0F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
        return false;
    }

    @Override
    protected void readAdditional(CompoundNBT compound) {

    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {

    }

    @Override
    protected void registerData() {

    }
}
