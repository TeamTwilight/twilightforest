package twilightforest.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.nbt.CompoundNBT;

public class EntityTFGoblinChain extends EntityTFBlockGoblin.MultipartGenericsAreDumb {

	public EntityTFGoblinChain(Entity parent) {
		super(parent);
	}

	@Override
	protected void registerData() {
		realSize = EntitySize.flexible(0.75F, 0.75F);
	}

	@Override
	public boolean canBeCollidedWith() {
		return false;
	}

	@Override
	protected void readAdditional(CompoundNBT compound) {

	}

	@Override
	protected void writeAdditional(CompoundNBT compound) {

	}
}
