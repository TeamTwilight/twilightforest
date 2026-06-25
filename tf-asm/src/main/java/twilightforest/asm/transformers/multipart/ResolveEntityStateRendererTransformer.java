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
 * {@link twilightforest.asmhooks.MultipartHooks#resolveEntityStateRenderer}
 *
 * Hooks into {@link net.minecraft.client.renderer.entity.EntityRenderDispatcher#getRenderer(EntityRenderState)}
 * after the renderer lookup by entityType, allowing PartEntity render states (NagaSegment, HydraHead,
 * HydraNeck, SnowQueenIceShield) to resolve to their correct custom renderers during the submit phase.
 *
 * In 26.1.2, the rendering pipeline is split into extract and submit phases.
 * During submit, the renderer is looked up again via entityRenderState.entityType.
 * For PartEntities, entityType equals the parent's type, so the wrong renderer would be returned.
 * This transformer inserts a hook that checks for PartEntityState.partRendererId and returns
 * the correct PartEntity renderer from BakedMultiPartRenderers.
 */
public class ResolveEntityStateRendererTransformer extends SimpleMethodTransformer {

	public ResolveEntityStateRendererTransformer() {
		super(
			"net.minecraft.client.renderer.entity.EntityRenderDispatcher",
			"getRenderer",
			"(Lnet/minecraft/client/renderer/entity/state/EntityRenderState;)Lnet/minecraft/client/renderer/entity/EntityRenderer;"
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
						"resolveEntityStateRenderer",
						"(Lnet/minecraft/client/renderer/entity/EntityRenderer;Lnet/minecraft/client/renderer/entity/state/EntityRenderState;)Lnet/minecraft/client/renderer/entity/EntityRenderer;"
					)
				)
			));
	}

}