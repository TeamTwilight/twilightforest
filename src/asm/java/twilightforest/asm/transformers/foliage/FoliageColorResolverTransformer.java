package twilightforest.asm.transformers.foliage;

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
 * {@link twilightforest.asmhooks.BlockHooks#resolveFoliageColor}
 */
public class FoliageColorResolverTransformer extends SimpleMethodProcessor {

	@Override
	public ProcessorName name() {
		return ASMUtil.named("foliage_color_resolver");
	}

	@Override
	public void transform(MethodNode node, SimpleTransformationContext context) {
		ASMUtil.findInstructions(
			node,
			Opcodes.IRETURN
		).findFirst().ifPresent(target -> node.instructions.insertBefore(
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

	@Override
	public Set<Target> targets() {
		return Set.of(new Target(
			"net.minecraft.client.renderer.BiomeColors",
			"lambda$static$0", // FOLIAGE_COLOR_RESOLVER
			"(Lnet/minecraft/world/level/biome/Biome;DD)I"
		));
	}

}
