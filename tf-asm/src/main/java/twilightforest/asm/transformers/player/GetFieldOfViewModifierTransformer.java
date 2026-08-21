package twilightforest.asm.transformers.player;

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
 * {@link twilightforest.asmhooks.PlayerHooks#straightAheadNullify}
 * {@link twilightforest.asmhooks.PlayerHooks#straightAheadRestore}
 */
public class GetFieldOfViewModifierTransformer extends SimpleMethodProcessor {

	@Override
	public ProcessorName name() {
		return ASMUtil.named("get_field_of_view_modifier");
	}

	@Override
	public void transform(MethodNode node, SimpleTransformationContext context) {
		ASMUtil.findInstructions(node, Opcodes.FCONST_1).findFirst().ifPresent(target -> {
			node.instructions.insertBefore(target, ASMUtil.listOf(
				new VarInsnNode(Opcodes.ALOAD, 0),
				new MethodInsnNode(Opcodes.INVOKESTATIC,
					"twilightforest/asmhooks/PlayerHooks",
					"straightAheadNullify",
					"(Lnet/minecraft/client/player/AbstractClientPlayer;)V",
					false)
			));

			ASMUtil.findInstructions(node, Opcodes.FRETURN).forEach(insn ->
				node.instructions.insertBefore(insn, ASMUtil.listOf(
					new VarInsnNode(Opcodes.ALOAD, 0),
					new MethodInsnNode(Opcodes.INVOKESTATIC,
						"twilightforest/asmhooks/PlayerHooks",
						"straightAheadRestore",
						"(Lnet/minecraft/client/player/AbstractClientPlayer;)V",
						false)
				))
			);
		});
	}

	@Override
	public Set<Target> targets() {
		return Set.of(new Target(
				"net/minecraft/client/player/AbstractClientPlayer",
				"getFieldOfViewModifier",
				"()F"
			)
		);
	}
}
