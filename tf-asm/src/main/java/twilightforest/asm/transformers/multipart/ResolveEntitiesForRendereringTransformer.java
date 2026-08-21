package twilightforest.asm.transformers.multipart;

import net.neoforged.neoforgespi.transformation.ProcessorName;
import net.neoforged.neoforgespi.transformation.SimpleMethodProcessor;
import net.neoforged.neoforgespi.transformation.SimpleTransformationContext;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import twilightforest.asm.ASMUtil;

import java.util.Optional;
import java.util.Set;

/**
 * {@link twilightforest.asmhooks.MultipartHooks#resolveEntitiesForRendering}
 */
public class ResolveEntitiesForRendereringTransformer extends SimpleMethodProcessor {

	@Override
	public ProcessorName name() {
		return ASMUtil.named("resolve_entities_for_renderering");
	}

	@Override
	public void transform(MethodNode node, SimpleTransformationContext context) {
		ASMUtil.findMethodInstructions(
			node,
			Opcodes.INVOKEVIRTUAL,
			"net/minecraft/client/multiplayer/ClientLevel",
			"entitiesForRendering",
			"()Ljava/lang/Iterable;"
		).map(searchTarget -> ASMUtil.findMethodInstructions(
			node,
			searchTarget,
			Opcodes.INVOKEINTERFACE,
			"java/lang/Iterable",
			"iterator",
			"()Ljava/util/Iterator;"
		).findFirst()).filter(Optional::isPresent).map(Optional::get).forEach(target -> node.instructions.insert(
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

	@Override
	public Set<Target> targets() {
		return Set.of(new Target(
			"net.minecraft.client.renderer.LevelRenderer",
			"renderLevel",
			"(Lnet/minecraft/client/DeltaTracker;ZLnet/minecraft/client/Camera;Lnet/minecraft/client/renderer/GameRenderer;Lnet/minecraft/client/renderer/LightTexture;Lorg/joml/Matrix4f;Lorg/joml/Matrix4f;)V"
		));
	}

}
