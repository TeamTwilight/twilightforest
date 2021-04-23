function initializeCoreMod() {
    return {
        'music': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.client.audio.MusicTicker',
                'methodName': Java.type("net.minecraftforge.coremod.api.ASMAPI").mapMethod('func_73660_a'),
                'methodDesc': '()V'
            },
            'transformer': function (methodNode) {
                if (methodNode instanceof org.objectweb.asm.tree.MethodNode) { // Stupid way to cast in JS to avoid warnings and fix autocomplete
                    var ASM = Java.type('net.minecraftforge.coremod.api.ASMAPI');
                    var Opcodes = Java.type('org.objectweb.asm.Opcodes');
                    methodNode.instructions.insert(
                        ASM.findFirstInstruction(methodNode, Opcodes.INVOKEVIRTUAL),
                        ASM.listOf(
                            new org.objectweb.asm.tree.MethodInsnNode(
                                Opcodes.INVOKESTATIC,
                                'twilightforest/ASMHooks',
                                'music',
                                '(Lnet/minecraft/client/audio/BackgroundMusicSelector;)Lnet/minecraft/client/audio/BackgroundMusicSelector;',
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
