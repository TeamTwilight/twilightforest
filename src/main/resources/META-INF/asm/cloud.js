// noinspection ES6ConvertVarToLetConst

var ASM = Java.type('net.neoforged.coremod.api.ASMAPI');
var Opcodes = Java.type('org.objectweb.asm.Opcodes');

var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');

// noinspection JSUnusedGlobalSymbols
function initializeCoreMod() {
    ASM.loadFile('META-INF/asm/util/util.js');
    return {
        'cloud': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.world.level.Level',
                'methodName': 'isRainingAt',
                'methodDesc': '(Lnet/minecraft/core/BlockPos;)Z'
            },
            'transformer': function (/*org.objectweb.asm.tree.MethodNode*/ methodNode) {
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                findAllInstructions(methodNode, Opcodes.IRETURN).forEach((/*org.objectweb.asm.tree.AbstractInsnNode*/ value) => {
                    instructions.insertBefore(
                        value,
                        ASM.listOf(
                            new VarInsnNode(Opcodes.ALOAD, 0),
                            new VarInsnNode(Opcodes.ALOAD, 1),
                            new MethodInsnNode(
                                Opcodes.INVOKESTATIC,
                                'twilightforest/ASMHooks',
                                'cloud',
                                '(ZLnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Z',
                                false
                            )
                        )
                    )
                });
                return methodNode;
            }
        }
    }
}
