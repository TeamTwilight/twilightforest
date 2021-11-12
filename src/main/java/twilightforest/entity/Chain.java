package twilightforest.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.nbt.CompoundTag;
import twilightforest.entity.monster.BlockChainGoblin;

public class Chain extends BlockChainGoblin.MultipartGenericsAreDumb {

	public Chain(Entity parent) {
		super(parent);
	}

	@Override
	protected void defineSynchedData() {
		realSize = EntityDimensions.scalable(0.75F, 0.75F);
	}

	@Override
	public boolean isPickable() {
		return false;
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag compound) {

	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compound) {

	}
}
