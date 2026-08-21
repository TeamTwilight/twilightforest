package twilightforest.asm.transformers.multipart;

import net.neoforged.neoforgespi.transformation.ProcessorName;
import net.neoforged.neoforgespi.transformation.SimpleMethodProcessor;
import net.neoforged.neoforgespi.transformation.SimpleTransformationContext;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import twilightforest.asm.ASMUtil;

import java.util.Optional;
import java.util.Set;

/**
 * {@link twilightforest.asmhooks.MultipartHooks#resolveEntityRenderer}
 */
public class ResolveEntityRendererTransformer extends SimpleMethodProcessor {

	@Override
	public ProcessorName name() {
		return ASMUtil.named("resolve_entity_renderer");
	}

	@Override
	public void transform(MethodNode node, SimpleTransformationContext context) {
		ASMUtil.findFieldInstructions(
				node,
				Opcodes.GETFIELD,
				"net/minecraft/client/renderer/entity/EntityRenderDispatcher",
				"renderers"
			).map(searchTarget -> ASMUtil.findMethodInstructions(
				node,
				searchTarget,
				Opcodes.INVOKEINTERFACE,
				"java/util/Map",
				"get",
				"(Ljava/lang/Object;)Ljava/lang/Object;"
			).findFirst().flatMap(searchTarget2 -> ASMUtil.findInstructions(
				node,
				searchTarget2,
				Opcodes.CHECKCAST
			).findFirst())).filter(Optional::isPresent).map(Optional::get)
			.forEach(target -> node.instructions.insert(
				target,
				ASMUtil.listOf(
					new VarInsnNode(Opcodes.ALOAD, 1),
					new MethodInsnNode(
						Opcodes.INVOKESTATIC,
						"twilightforest/asmhooks/MultipartHooks",
						"resolveEntityRenderer",
						"(Lnet/minecraft/client/renderer/entity/EntityRenderer;Lnet/minecraft/world/entity/Entity;)Lnet/minecraft/client/renderer/entity/EntityRenderer;"
					)
				)
			));
	}

	@Override
	public Set<Target> targets() {
		return Set.of(new Target(
			"net.minecraft.client.renderer.entity.EntityRenderDispatcher",
			"getRenderer",
			"(Lnet/minecraft/world/entity/Entity;)Lnet/minecraft/client/renderer/entity/EntityRenderer;"
		));
	}

}
