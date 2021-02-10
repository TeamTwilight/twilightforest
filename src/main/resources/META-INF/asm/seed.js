function initializeCoreMod() {
    return {
        'seed': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.world.gen.settings.DimensionGeneratorSettings',
                'methodName': '<init>',
                'methodDesc': '(JZZLnet/minecraft/util/registry/SimpleRegistry;Ljava/util/Optional;)V'
            },
            'transformer': function (methodNode) {
                if (methodNode instanceof org.objectweb.asm.tree.MethodNode) { // Stupid way to cast in JS to avoid warnings and fix autocomplete
                    var ASM = Java.type('net.minecraftforge.coremod.api.ASMAPI');
                    var Opcodes = Java.type('org.objectweb.asm.Opcodes');
                    methodNode.instructions.insertBefore(
                        ASM.findFirstInstruction(methodNode, Opcodes.PUTFIELD),
                        ASM.listOf(
                            new org.objectweb.asm.tree.MethodInsnNode(
                                Opcodes.INVOKESTATIC,
                                'twilightforest/ASMHooks',
                                'seed',
                                '(J)J',
                                false
                                )
                            )
                        );
                }
                return methodNode;
            }
        }
    }
}
