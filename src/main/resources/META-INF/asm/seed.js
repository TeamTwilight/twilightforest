// noinspection ES6ConvertVarToLetConst

var ASM = Java.type('net.minecraftforge.coremod.api.ASMAPI');
var Opcodes = Java.type('org.objectweb.asm.Opcodes');

var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');

// noinspection JSUnusedGlobalSymbols
function initializeCoreMod() {
    return {
        'worldcreate': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.world.level.levelgen.WorldGenSettings',
                'methodName': '<init>',
                'methodDesc': '(JZZLnet/minecraft/core/Registry;Ljava/util/Optional;)V'
            },
            'transformer': function (/*org.objectweb.asm.tree.MethodNode*/ methodNode) {
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                instructions.insertBefore(
                    ASM.findFirstInstruction(methodNode, Opcodes.PUTFIELD),
                    ASM.listOf(
                        new MethodInsnNode(
                            Opcodes.INVOKESTATIC,
                            'twilightforest/ASMHooks',
                            'seed',
                            '(J)J',
                            false
                            )
                        )
                    );
                return methodNode;
            }
        },
        'worldload': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.world.level.storage.LevelStorageSource',
                'methodName': ASM.mapMethod('m_78204_'), // readWorldGenSettings
                'methodDesc': '(Lcom/mojang/serialization/Dynamic;Lcom/mojang/datafixers/DataFixer;I)Lcom/mojang/datafixers/util/Pair;'
            },
            'transformer': function (/*org.objectweb.asm.tree.MethodNode*/ methodNode) {
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                instructions.insertBefore(
                    ASM.findFirstInstruction(methodNode, Opcodes.ASTORE),
                    ASM.listOf(
                        new MethodInsnNode(
                            Opcodes.INVOKESTATIC,
                            'twilightforest/ASMHooks',
                            'seed',
                            '(Lcom/mojang/serialization/Dynamic;)Lcom/mojang/serialization/Dynamic;',
                            false
                            )
                        )
                    );
                return methodNode;
            }
        }
    }
}

