package twilightforest.asm.transformers.multipart;

import net.neoforged.neoforgespi.transformation.SimpleTransformationContext;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import twilightforest.asm.ASMUtil;
import twilightforest.asm.SimpleMethodTransformer;

import java.util.Optional;

/**
 * {@link twilightforest.asmhooks.MultipartHooks#resolveEntityRenderer}
 */
public class ResolveEntityRendererTransformer extends SimpleMethodTransformer {

	public ResolveEntityRendererTransformer() {
		super(
			"net.minecraft.client.renderer.entity.EntityRenderDispatcher",
			"getRenderer",
			"(Lnet/minecraft/world/entity/Entity;)Lnet/minecraft/client/renderer/entity/EntityRenderer;"
		);
	}

	@Override
	protected void transform(ClassNode classNode, MethodNode method, SimpleTransformationContext context) {
		ASMUtil.findFieldInstructions(
				method,
				Opcodes.GETFIELD,
				"net/minecraft/client/renderer/entity/EntityRenderDispatcher",
				"renderers"
			).map(searchTarget -> ASMUtil.findMethodInstructions(
				method,
				searchTarget,
				Opcodes.INVOKEINTERFACE,
				"java/util/Map",
				"get",
				"(Ljava/lang/Object;)Ljava/lang/Object;"
			).findFirst().flatMap(searchTarget2 -> ASMUtil.findInstructions(
				method,
				searchTarget2,
				Opcodes.CHECKCAST
			).findFirst())).filter(Optional::isPresent).map(Optional::get)
			.forEach(target -> method.instructions.insert(
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

}
