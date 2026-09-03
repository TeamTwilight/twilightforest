package twilightforest.asm.transformers.entity;

import net.neoforged.neoforgespi.transformation.ProcessorName;
import net.neoforged.neoforgespi.transformation.SimpleMethodProcessor;
import net.neoforged.neoforgespi.transformation.SimpleTransformationContext;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import twilightforest.asm.ASMUtil;

import java.util.Set;

/**
 * {@link twilightforest.asmhooks.EntityHooks#resetFactorWithUnrestrained}
 */
public class UnrestrainedBlockSpeedAndJumpFactorTransformer extends SimpleMethodProcessor {

	@Override
	public ProcessorName name() {
		return ASMUtil.named("unrestrained_block_speed_and_jump_factor");
	}

	@Override
	public void transform(MethodNode node, SimpleTransformationContext context) {
		ASMUtil.findInstructions(
			node,
			Opcodes.FRETURN
		).forEach(target -> node.instructions.insertBefore(
			target,
			ASMUtil.listOf(
				new VarInsnNode(Opcodes.ALOAD, 0),
				new MethodInsnNode(
					Opcodes.INVOKESTATIC,
					"twilightforest/asmhooks/EntityHooks",
					"resetFactorWithUnrestrained",
					"(FLnet/minecraft/world/entity/Entity;)F"
				)
			)
		));
	}

	@Override
	public Set<Target> targets() {
		return Set.of(new Target(
			"net.minecraft.world.entity.Entity",
			"getBlockJumpFactor",
			"()F"
		), new Target(
			"net.minecraft.world.entity.Entity",
			"getBlockSpeedFactor",
			"()F"
		));
	}
}
