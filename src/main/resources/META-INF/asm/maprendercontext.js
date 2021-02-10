function initializeCoreMod() {
    return {
        'maprendercontext': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.client.gui.MapItemRenderer$Instance',
                'methodName': Java.type("net.minecraftforge.coremod.api.ASMAPI").mapMethod('func_228089_a_'),
                'methodDesc': '(Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;ZI)V'
            },
            'transformer': function (methodNode) {
                if (methodNode instanceof org.objectweb.asm.tree.MethodNode) { // Stupid way to cast in JS to avoid warnings and fix autocomplete
                    var ASM = Java.type('net.minecraftforge.coremod.api.ASMAPI');
                    var Opcodes = Java.type('org.objectweb.asm.Opcodes');
                    methodNode.instructions.insertBefore(
                        ASM.findFirstInstruction(methodNode, Opcodes.ISTORE),
                        ASM.listOf(
                            new org.objectweb.asm.tree.VarInsnNode(Opcodes.ALOAD, 1),
                            new org.objectweb.asm.tree.VarInsnNode(Opcodes.ALOAD, 2),
                            new org.objectweb.asm.tree.VarInsnNode(Opcodes.ILOAD, 4),
                            new org.objectweb.asm.tree.MethodInsnNode(
                                Opcodes.INVOKESTATIC,
                                'twilightforest/ASMHooks',
                                'mapRenderContext',
                                '(Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;I)V',
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
