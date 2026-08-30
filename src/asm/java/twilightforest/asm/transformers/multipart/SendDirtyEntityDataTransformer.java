package twilightforest.asm.transformers.multipart;

import net.neoforged.neoforgespi.transformation.ProcessorName;
import net.neoforged.neoforgespi.transformation.SimpleMethodProcessor;
import net.neoforged.neoforgespi.transformation.SimpleTransformationContext;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import twilightforest.asm.ASMUtil;

import java.util.Set;

/**
 * {@link twilightforest.asmhooks.MultipartHooks#sendDirtyEntityData}
 */
public class SendDirtyEntityDataTransformer extends SimpleMethodProcessor {

	@Override
	public ProcessorName name() {
		return ASMUtil.named("send_dirty_entity_data");
	}

	@Override
	public void transform(MethodNode node, SimpleTransformationContext context) {
		ASMUtil.findFieldInstructions(
			node,
			Opcodes.GETFIELD,
			"net/minecraft/server/level/ServerEntity",
			"entity"
		).findFirst().ifPresent(target -> node.instructions.insert(
			target,
			ASMUtil.listOf(
				new MethodInsnNode(
					Opcodes.INVOKESTATIC,
					"twilightforest/asmhooks/MultipartHooks",
					"sendDirtyEntityData",
					"(Lnet/minecraft/world/entity/Entity;)Lnet/minecraft/world/entity/Entity;"
				)
			)
		));
	}

	@Override
	public Set<Target> targets() {
		return Set.of(new Target(
			"net.minecraft.server.level.ServerEntity",
			"sendDirtyEntityData",
			"()V"
		));
	}

}
