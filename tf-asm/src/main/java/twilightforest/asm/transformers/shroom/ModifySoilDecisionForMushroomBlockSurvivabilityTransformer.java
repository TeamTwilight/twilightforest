package twilightforest.asm.transformers.shroom;

import net.neoforged.neoforgespi.transformation.SimpleTransformationContext;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import twilightforest.asm.ASMUtil;
import twilightforest.asm.SimpleMethodTransformer;

/**
 * {@link twilightforest.asmhooks.BlockHooks#modifySoilDecisionForMushroomBlockSurvivability}
 */
public class ModifySoilDecisionForMushroomBlockSurvivabilityTransformer extends SimpleMethodTransformer {

	public ModifySoilDecisionForMushroomBlockSurvivabilityTransformer() {
		super(
			"net.minecraft.world.level.block.MushroomBlock",
			"canSurvive",
			"(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;)Z"
		);
	}

	@Override
	protected void transform(ClassNode classNode, MethodNode method, SimpleTransformationContext context) {
		ASMUtil.findMethodInstructions(
			method,
			Opcodes.INVOKEVIRTUAL,
			"net/minecraft/world/level/block/state/BlockState",
			"canSustainPlant",
			"(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;Lnet/minecraft/world/level/block/state/BlockState;)Lnet/neoforged/neoforge/common/util/TriState;"
		).forEach(target -> method.instructions.insert(
			target,
			ASMUtil.listOf(
				new VarInsnNode(Opcodes.ALOAD, 2),
				new VarInsnNode(Opcodes.ALOAD, 3),
				new MethodInsnNode(
					Opcodes.INVOKESTATIC,
					"twilightforest/asmhooks/BlockHooks",
					"modifySoilDecisionForMushroomBlockSurvivability",
					"(Lnet/neoforged/neoforge/common/util/TriState;Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;)Lnet/neoforged/neoforge/common/util/TriState;"
				)
			)
		));
	}

}
