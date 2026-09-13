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
 * {@link twilightforest.asmhooks.EntityHooks#processLiquidCollisionShape}
 */
public class WaterCollisionTransformer extends SimpleMethodProcessor {

	@Override
	public ProcessorName name() {
		return ASMUtil.named("water_collision");
	}

	@Override
	public void transform(MethodNode node, SimpleTransformationContext context) {
		ASMUtil.findInstructions(node, Opcodes.ARETURN).forEach(
			(instruction) -> node.instructions.insertBefore(instruction, ASMUtil.listOf(
				new VarInsnNode(Opcodes.ALOAD, 0),
				new MethodInsnNode(
					Opcodes.INVOKESTATIC,
					"twilightforest/asmhooks/EntityHooks",
					"processLiquidCollisionShape",
					"(Lnet/minecraft/world/phys/shapes/VoxelShape;Lnet/minecraft/world/entity/LivingEntity;)Lnet/minecraft/world/phys/shapes/VoxelShape;",
					false
				))));
	}

	@Override
	public Set<Target> targets() {
		return Set.of(new Target(
			"net.minecraft.world.entity.LivingEntity",
			"getLiquidCollisionShape",
			"()Lnet/minecraft/world/phys/shapes/VoxelShape;"
		));
	}
}
