package twilightforest.asm.transformers.player;

import net.neoforged.neoforgespi.transformation.ProcessorName;
import net.neoforged.neoforgespi.transformation.SimpleClassProcessor;
import net.neoforged.neoforgespi.transformation.SimpleTransformationContext;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import twilightforest.asm.ASMUtil;

import java.util.Set;
import java.util.stream.Stream;

/**
 * {@link twilightforest.asmhooks.PlayerHooks#getFoodExhaustion}
 */
public class ReduceMovementFoodExhaustionTransformer extends SimpleClassProcessor {

	private final Set<Target> targets;
	private final ProcessorName name;

	public ReduceMovementFoodExhaustionTransformer() {
		this.targets = Set.of(
			new Target("net.minecraft.server.level.ServerPlayer"),
			new Target("net.minecraft.world.entity.player.Player")
		);
		this.name = new ProcessorName("twilightforest", "player");
	}

	@Override
	public ProcessorName name() {
		return this.name;
	}

	@Override
	public Set<Target> targets() {
		return this.targets;
	}

	@Override
	public void transform(ClassNode classNode, SimpleTransformationContext context) {
		for (MethodNode method : classNode.methods) {
			if (("checkMovementStatistics".equals(method.name) && "(DDD)V".equals(method.desc)) ||
				("jumpFromGround".equals(method.name) && "()V".equals(method.desc))) {
				transformMethod(method);
			}
		}
	}

	private void transformMethod(MethodNode method) {
		Stream.concat(
			ASMUtil.findMethodInstructions(method, Opcodes.INVOKEVIRTUAL,
				"net/minecraft/world/entity/player/Player",
				"causeFoodExhaustion",
				"(F)V"
			),
			ASMUtil.findMethodInstructions(method, Opcodes.INVOKEVIRTUAL,
				"net/minecraft/server/level/ServerPlayer",
				"causeFoodExhaustion",
				"(F)V"
			)
		).forEach(target -> method.instructions.insertBefore(
			target,
			ASMUtil.listOf(
				new VarInsnNode(Opcodes.ALOAD, 0),
				new MethodInsnNode(
					Opcodes.INVOKESTATIC,
					"twilightforest/asmhooks/PlayerHooks",
					"getFoodExhaustion",
					"(FLnet/minecraft/world/entity/player/Player;)F"
				)
			)
		));
	}
}
