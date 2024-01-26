// noinspection ES6ConvertVarToLetConst

var ASM = Java.type('net.neoforged.coremod.api.ASMAPI');
var Opcodes = Java.type('org.objectweb.asm.Opcodes');

var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var FieldInsnNode = Java.type('org.objectweb.asm.tree.FieldInsnNode');
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');

// noinspection JSUnusedGlobalSymbols
function initializeCoreMod() {
    return {
        'mount': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.client.player.LocalPlayer',
                'methodName': 'rideTick',
                'methodDesc': '()V'
            },
            'transformer': function (/*org.objectweb.asm.tree.MethodNode*/ methodNode) {
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                instructions.insert(
                    ASM.findFirstInstruction(methodNode, Opcodes.INVOKESPECIAL),
                    ASM.listOf(
                        new VarInsnNode(Opcodes.ALOAD, 0),
                        new FieldInsnNode(
                            Opcodes.GETFIELD,
                            'net/minecraft/client/player/LocalPlayer',
                            'input',
                            'Lnet/minecraft/client/player/Input;'
                            ),
                        new VarInsnNode(Opcodes.ALOAD, 0),
                        new FieldInsnNode(
                            Opcodes.GETFIELD,
                            'net/minecraft/client/player/LocalPlayer',
                            'input',
                            'Lnet/minecraft/client/player/Input;'
                            ),
                        new FieldInsnNode(
                            Opcodes.GETFIELD,
                            'net/minecraft/client/player/Input',
                            'shiftKeyDown',
                            'Z'
                            ),
                        new VarInsnNode(Opcodes.ALOAD, 0),
                        new MethodInsnNode(
                            Opcodes.INVOKEVIRTUAL,
                            'net/minecraft/world/entity/player/Player',
                            'wantsToStopRiding',
                            '()Z',
                            false
                            ),
                        new VarInsnNode(Opcodes.ALOAD, 0),
                        new MethodInsnNode(
                            Opcodes.INVOKEVIRTUAL,
                            'net/minecraft/world/entity/Entity',
                            'isPassenger',
                            '()Z',
                            false
                            ),
                        new MethodInsnNode(
                            Opcodes.INVOKESTATIC,
                            'twilightforest/ASMHooks',
                            'mountFix',
                            '(ZZZ)Z',
                            false
                            ),
                        new FieldInsnNode(
                            Opcodes.PUTFIELD,
                            'net/minecraft/client/player/Input',
                            'shiftKeyDown',
                            'Z'
                            )
                        )
                    );
                return methodNode;
            }
        }
    }
}

