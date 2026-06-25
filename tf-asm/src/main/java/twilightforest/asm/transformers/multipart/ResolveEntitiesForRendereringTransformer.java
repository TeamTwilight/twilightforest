package twilightforest.asm.transformers.multipart;

import net.neoforged.neoforgespi.transformation.SimpleTransformationContext;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import twilightforest.asm.ASMUtil;
import twilightforest.asm.SimpleMethodTransformer;

import java.util.Optional;

/**
 * {@link twilightforest.asmhooks.MultipartHooks#resolveEntitiesForRendering}
 *
 * In 26.1.2, entity rendering was moved from renderLevel() to extractVisibleEntities().
 * This transformer hooks into extractVisibleEntities() to inject TFPart entities
 * (NagaSegment, HydraHead, HydraNeck, SnowQueenIceShield) into the entity iteration.
 */
public class ResolveEntitiesForRendereringTransformer extends SimpleMethodTransformer {

	public ResolveEntitiesForRendereringTransformer() {
		super(
			"net.minecraft.client.renderer.LevelRenderer",
			"extractVisibleEntities",
			"(Lnet/minecraft/client/Camera;Lnet/minecraft/client/renderer/culling/Frustum;Lnet/minecraft/client/DeltaTracker;Lnet/minecraft/client/renderer/state/level/LevelRenderState;)V"
		);
	}

	@Override
	protected void transform(ClassNode classNode, MethodNode method, SimpleTransformationContext context) {
		ASMUtil.findMethodInstructions(
			method,
			Opcodes.INVOKEVIRTUAL,
			"net/minecraft/client/multiplayer/ClientLevel",
			"entitiesForRendering",
			"()Ljava/lang/Iterable;"
		).map(searchTarget -> ASMUtil.findMethodInstructions(
			method,
			searchTarget,
			Opcodes.INVOKEINTERFACE,
			"java/lang/Iterable",
			"iterator",
			"()Ljava/util/Iterator;"
		).findFirst()).filter(Optional::isPresent).map(Optional::get).forEach(target -> method.instructions.insert(
			target,
			ASMUtil.listOf(
				new MethodInsnNode(
					Opcodes.INVOKESTATIC,
					"twilightforest/asmhooks/MultipartHooks",
					"resolveEntitiesForRendering",
					"(Ljava/util/Iterator;)Ljava/util/Iterator;"
				)
			)
		));
	}

}
