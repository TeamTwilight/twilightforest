package twilightforest.tileentity;

import net.minecraft.entity.Entity;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import twilightforest.TwilightForestMod;
import twilightforest.block.BlockTFFireJet;
import twilightforest.block.TFBlocks;
import twilightforest.enums.FireJetVariant;
import twilightforest.client.particle.TFParticleType;

import java.util.List;

public class TileEntityTFFlameJet extends TileEntity implements ITickable {

	private int counter = 0;
	private FireJetVariant nextVariant;

	public TileEntityTFFlameJet(FireJetVariant variant) {
		this.nextVariant = variant;
	}

	public TileEntityTFFlameJet() {}

	@Override
	public void update() {
		if (++counter > 60) {
			counter = 0;
			// idle again
			if (!world.isRemote && world.getBlockState(pos).getBlock() == TFBlocks.fire_jet) {
				world.setBlockState(pos, TFBlocks.fire_jet.getDefaultState().withProperty(BlockTFFireJet.VARIANT, this.nextVariant));
			}
		} else if (counter % 2 == 0) {
			world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.pos.getX() + 0.5, this.pos.getY() + 1.0, this.pos.getZ() + 0.5, 0.0D, 0.0D, 0.0D);
			TwilightForestMod.proxy.spawnParticle(this.world, TFParticleType.LARGE_FLAME, this.pos.getX() + 0.5, this.pos.getY() + 1.0, this.pos.getZ() + 0.5, 0.0D, 0.5D, 0.0D);
//			TwilightForestMod.proxy.spawnParticle(TFParticleType.LARGE_FLAME, this.pos.getX() + 0.5, this.pos.getY() + 1.0, this.pos.getZ() + 0.5, 
//    				Math.cos(counter / 4.0) * 0.2, 0.35D, Math.sin(counter / 4.0) * 0.2);			
//			TwilightForestMod.proxy.spawnParticle(TFParticleType.LARGE_FLAME, this.pos.getX() + 0.5, this.pos.getY() + 1.0, this.pos.getZ() + 0.5, 
//    				Math.cos(counter / 4.0 + Math.PI) * 0.2, 0.35D, Math.sin(counter / 4.0 + Math.PI) * 0.2);			
//			TwilightForestMod.proxy.spawnParticle(TFParticleType.LARGE_FLAME, this.pos.getX() + 0.5 + Math.cos(counter / 4.0), this.pos.getY() + 1.0, this.pos.getZ() + 0.5 + Math.sin(counter / 4.0), 
//    				Math.sin(counter / 4.0) * 0.05, 0.35D, Math.cos(counter / 4.0) * 0.05);			
//			TwilightForestMod.proxy.spawnParticle(TFParticleType.LARGE_FLAME, this.pos.getX() +  0.5 + Math.cos(counter / 4.0 + Math.PI), this.pos.getY() + 1.0, this.pos.getZ() + 0.5 + Math.sin(counter / 4.0 + Math.PI), 
//    				Math.sin(counter / 4.0 + Math.PI) * 0.05, 0.35D, Math.cos(counter / 4.0 + Math.PI) * 0.05);			

			TwilightForestMod.proxy.spawnParticle(this.world, TFParticleType.LARGE_FLAME, this.pos.getX() - 0.5, this.pos.getY() + 1.0, this.pos.getZ() + 0.5, 0.05D, 0.5D, 0.0D);
			TwilightForestMod.proxy.spawnParticle(this.world, TFParticleType.LARGE_FLAME, this.pos.getX() + 0.5, this.pos.getY() + 1.0, this.pos.getZ() - 0.5, 0.0D, 0.5D, 0.05D);
			TwilightForestMod.proxy.spawnParticle(this.world, TFParticleType.LARGE_FLAME, this.pos.getX() + 1.5, this.pos.getY() + 1.0, this.pos.getZ() + 0.5, -0.05D, 0.5D, 0.0D);
			TwilightForestMod.proxy.spawnParticle(this.world, TFParticleType.LARGE_FLAME, this.pos.getX() + 0.5, this.pos.getY() + 1.0, this.pos.getZ() + 1.5, 0.0D, 0.5D, -0.05D);

		}

		// sounds
		if (counter % 4 == 0) {
			world.playSound(this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5, SoundEvents.ENTITY_GHAST_SHOOT, SoundCategory.BLOCKS, 1.0F + world.rand.nextFloat(), world.rand.nextFloat() * 0.7F + 0.3F, false);

		} else if (counter == 1) {
			world.playSound(this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F + world.rand.nextFloat(), world.rand.nextFloat() * 0.7F + 0.3F, false);
		}

		// actual fire effects
		if (!world.isRemote) {
			if (counter % 5 == 0) {
				// find entities in the area of effect
				List<Entity> entitiesInRange = world.getEntitiesWithinAABB(Entity.class,
						new AxisAlignedBB(pos.add(-2, 0, -2), pos.add(2, 4, 2)));
				// fire!
				for (Entity entity : entitiesInRange) {
					if (!entity.isImmuneToFire()) {
						entity.attackEntityFrom(DamageSource.IN_FIRE, 2);
						entity.setFire(15);
					}
				}
			}
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readFromNBT(par1NBTTagCompound);
		this.nextVariant = FireJetVariant.values()[par1NBTTagCompound.getInteger("NextMeta")];
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setInteger("NextMeta", this.nextVariant.ordinal());
		return par1NBTTagCompound;
	}
}
