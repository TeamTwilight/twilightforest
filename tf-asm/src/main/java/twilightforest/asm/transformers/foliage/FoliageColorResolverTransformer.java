package twilightforest.asm.transformers.foliage;

import net.neoforged.neoforgespi.transformation.SimpleTransformationContext;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import twilightforest.asm.ASMUtil;
import twilightforest.asm.SimpleMethodTransformer;

/**
 * {@link twilightforest.asmhooks.BlockHooks#resolveFoliageColor}
 */
public class FoliageColorResolverTransformer extends SimpleMethodTransformer {

	public FoliageColorResolverTransformer() {
		super(
			"net.minecraft.client.renderer.BiomeColors",
			"lambda$static$0",
			"(Lnet/minecraft/world/level/biome/Biome;DD)I"
		);
	}

	@Override
	protected void transform(ClassNode classNode, MethodNode method, SimpleTransformationContext context) {
		ASMUtil.findInstructions(
			method,
			Opcodes.IRETURN
		).findFirst().ifPresent(target -> method.instructions.insertBefore(
			target,
			ASMUtil.listOf(
				new VarInsnNode(Opcodes.ALOAD, 0),
				new VarInsnNode(Opcodes.DLOAD, 1),
				new VarInsnNode(Opcodes.DLOAD, 3),
				new MethodInsnNode(
					Opcodes.INVOKESTATIC,
					"twilightforest/asmhooks/BlockHooks",
					"resolveFoliageColor",
					"(ILnet/minecraft/world/level/biome/Biome;DD)I"
				)
			)
		));
	}

}
