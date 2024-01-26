var ASM = Java.type('net.neoforged.coremod.api.ASMAPI');
var Opcodes = Java.type('org.objectweb.asm.Opcodes');

var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');

// noinspection JSUnusedGlobalSymbols
function initializeCoreMod() {
    return {
        'lead': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.world.entity.decoration.LeashFenceKnotEntity',
                'methodName': 'survives',
                'methodDesc': '()Z'
            },
            'transformer': function (/*org.objectweb.asm.tree.MethodNode*/ methodNode) {
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                instructions.insertBefore(
                    ASM.findFirstInstruction(methodNode, Opcodes.IRETURN),
                    ASM.listOf(
                        new VarInsnNode(Opcodes.ALOAD, 0),
                        new MethodInsnNode(
                            Opcodes.INVOKESTATIC,
                            'twilightforest/ASMHooks',
                            'lead',
                            '(ZLnet/minecraft/world/entity/decoration/LeashFenceKnotEntity;)Z',
                            false
                        )
                    )
                );
                return methodNode;
            }
        }
    }
}