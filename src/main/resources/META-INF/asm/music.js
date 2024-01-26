// noinspection ES6ConvertVarToLetConst

var ASM = Java.type('net.neoforged.coremod.api.ASMAPI');
var Opcodes = Java.type('org.objectweb.asm.Opcodes');

var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');

// noinspection JSUnusedGlobalSymbols
function initializeCoreMod() {
    return {
        'music': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.client.sounds.MusicManager',
                'methodName': 'tick',
                'methodDesc': '()V'
            },
            'transformer': function (/*org.objectweb.asm.tree.MethodNode*/ methodNode) {
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                instructions.insert(
                    ASM.findFirstInstruction(methodNode, Opcodes.INVOKEVIRTUAL),
                    ASM.listOf(
                        new MethodInsnNode(
                            Opcodes.INVOKESTATIC,
                            'twilightforest/ASMHooks',
                            'music',
                            '(Lnet/minecraft/sounds/Music;)Lnet/minecraft/sounds/Music;',
                            false
                            )
                        )
                    );
                return methodNode;
            }
        }
    }
}
