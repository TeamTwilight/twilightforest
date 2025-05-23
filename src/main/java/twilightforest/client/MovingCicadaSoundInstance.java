package twilightforest.client;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import twilightforest.config.TFConfig;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFSounds;

public class MovingCicadaSoundInstance extends AbstractTickableSoundInstance {

	protected final LivingEntity wearer;

	public MovingCicadaSoundInstance(LivingEntity entity) {
		super(TFSounds.CICADA.get(), SoundSource.NEUTRAL, SoundInstance.createUnseededRandom());
		this.wearer = entity;
		this.x = entity.getX();
		this.y = entity.getY();
		this.z = entity.getZ();
		this.looping = true;
		this.delay = entity.getRandom().nextInt(100) + 100;
	}

	@Override
	public void tick() {
		if (!this.wearer.isRemoved() && (this.wearer.getItemBySlot(EquipmentSlot.HEAD).is(TFBlocks.CICADA.asItem()) || this.isWearingCicadaCurio())) {
			this.x = (float) this.wearer.getX();
			this.y = (float) this.wearer.getY();
			this.z = (float) this.wearer.getZ();
		} else {
			this.stop();
		}
	}

	private boolean isWearingCicadaCurio() {
//		if (ModList.get().isLoaded("curios")) {
//			return CuriosCompat.isCurioEquipped(this.wearer, stack -> stack.is(TFBlocks.CICADA.asItem()));
//		}
		return false;
	}

	@Override
	public boolean canPlaySound() {
		return !TFConfig.silentCicadas && !TFConfig.silentCicadasOnHead;
	}
}
