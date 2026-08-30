package twilightforest.asm.transformers.armor;

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
 * {@link twilightforest.asmhooks.ArmorHooks#modifyArmorVisibility}
 */
public class ArmorVisibilityRenderingTransformer extends SimpleMethodProcessor {

	@Override
	public ProcessorName name() {
		return ASMUtil.named("armor_visibility_rendering");
	}

	@Override
	public void transform(MethodNode node, SimpleTransformationContext context) {
		ASMUtil.findVarInstructions(node, Opcodes.FSTORE, 4)
			.findFirst()
			.ifPresent(target -> node.instructions.insertBefore(
				target,
				ASMUtil.listOf(
					new VarInsnNode(Opcodes.ALOAD, 0),
					new MethodInsnNode(
						Opcodes.INVOKESTATIC,
						"twilightforest/asmhooks/ArmorHooks",
						"modifyArmorVisibility",
						"(FLnet/minecraft/world/entity/LivingEntity;)F"
					)
				)
			));
	}

	@Override
	public Set<Target> targets() {
		return Set.of(new Target(
			"net.minecraft.world.entity.LivingEntity",
			"getVisibilityPercent",
			"(Lnet/minecraft/world/entity/Entity;)D"
		));
	}

}
