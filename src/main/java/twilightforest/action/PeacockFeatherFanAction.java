package twilightforest.action;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractCandleBlock;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.FakePlayerFactory;
import net.neoforged.neoforge.event.level.block.BreakBlockEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jspecify.annotations.Nullable;
import tamaized.beanification.Component;
import twilightforest.block.LightableBlock;
import twilightforest.init.TFSounds;
import twilightforest.network.MovePlayerPacket;
import twilightforest.network.ParticlePacket;
import twilightforest.util.WorldUtil;

@Component
public class PeacockFeatherFanAction {

	public int fan(Level level, Vec3 origin, Vec3 direction, Player user, ItemStack stack) {
		return fan(level, origin, direction, user, user, stack);
	}

	public int fan(ServerLevel level, Vec3 origin, Vec3 direction, ItemStack stack) {
		return fan(level, origin, direction, FakePlayerFactory.getMinecraft(level), null, stack);
	}

	public void spawnParticles(Level level, Vec3 origin, Vec3 direction) {
		AABB fanBox = getEffectAABB(origin, direction);

		for (Player other : level.players()) {
			if (other instanceof ServerPlayer serverplayer && serverplayer.distanceToSqr(origin) < 4096.0D) {
				ParticlePacket packet = new ParticlePacket();

				for (int i = 0; i < 30; i++) {
					packet.queueParticle(
						ParticleTypes.CLOUD,
						false,
						true,
						fanBox.minX + level.getRandom().nextFloat() * (fanBox.maxX - fanBox.minX),
						fanBox.minY + level.getRandom().nextFloat() * (fanBox.maxY - fanBox.minY),
						fanBox.minZ + level.getRandom().nextFloat() * (fanBox.maxZ - fanBox.minZ),
						direction.x(),
						direction.y(),
						direction.z()
					);
				}
				PacketDistributor.sendToPlayer(serverplayer, packet);
			}
		}
	}

	public void playWhoosh(Level level, BlockPos pos, SoundSource soundSource) {
		level.playSound(null, pos, TFSounds.FAN_WHOOSH.get(), soundSource, 1.0F + level.getRandom().nextFloat(), level.getRandom().nextFloat() * 0.7F + 0.3F);
	}

	private AABB getEffectAABB(Vec3 origin, Vec3 direction) {
		double range = 3.0D;
		double radius = 2.0D;
		Vec3 destVec = origin.add(direction.scale(range));

		return new AABB(destVec.x() - radius, destVec.y() - radius, destVec.z() - radius, destVec.x() + radius, destVec.y() + radius, destVec.z() + radius);
	}

	private int fan(Level level, Vec3 origin, Vec3 direction, Player breaker, @Nullable Player user, ItemStack stack) {
		AABB fanBox = getEffectAABB(origin, direction);
		return fanEntitiesInAABB(level, user, stack, fanBox, direction) + fanBlocksInAABB(level, fanBox, breaker);
	}

	private int fanEntitiesInAABB(Level level, @Nullable Player user, ItemStack stack, AABB fanBox, Vec3 direction) {
		Vec3 moveVec = direction.scale(2);
		int fannedEntities = 0;

		for (Entity entity : level.getEntitiesOfClass(Entity.class, fanBox)) {
			if (entity.isPushable() || entity instanceof ItemEntity || entity instanceof Projectile) {
				entity.setDeltaMovement(moveVec.x(), moveVec.y(), moveVec.z());
				entity.needsSync = true;
				fannedEntities++;
			}

			if (entity instanceof ServerPlayer pushedPlayer && pushedPlayer != user && !pushedPlayer.isShiftKeyDown()) {
				PacketDistributor.sendToPlayer(pushedPlayer, new MovePlayerPacket(moveVec.x(), moveVec.y(), moveVec.z()));
				if (user != null)
					user.getCooldowns().addCooldown(stack, 40);
				fannedEntities += 2;
			}
		}
		return fannedEntities;
	}

	private int fanBlocksInAABB(Level level, AABB box, Player breaker) {
		int fan = 0;
		for (BlockPos pos : WorldUtil.getAllInBB(box)) {
			fan += fanBlock(level, pos, breaker);
		}
		return fan;
	}

	private int fanBlock(Level level, BlockPos pos, Player breaker) {
		int cost = 0;

		BlockState state = level.getBlockState(pos);
		if (state.getBlock() instanceof FlowerBlock) {
			if (level.getRandom().nextInt(3) == 0) {
				if (!NeoForge.EVENT_BUS.post(new BreakBlockEvent(level, pos, state, breaker)).isCanceled()) {
					level.destroyBlock(pos, true);
					cost++;
				}
			}
		} else if (state.getBlock() instanceof AbstractCandleBlock && state.getValue(AbstractCandleBlock.LIT)) {
			AbstractCandleBlock.extinguish(null, state, level, pos);
		} else if (state.getBlock() instanceof LightableBlock lightable && (state.getValue(LightableBlock.LIGHTING) == LightableBlock.Lighting.NORMAL || state.getValue(LightableBlock.LIGHTING) == LightableBlock.Lighting.DIM)) {
			lightable.extinguish(null, state, level, pos);
		}

		return cost;
	}
}
