// noinspection ES6ConvertVarToLetConst

var ASM = Java.type('net.neoforged.coremod.api.ASMAPI');
var Opcodes = Java.type('org.objectweb.asm.Opcodes');

var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');

// noinspection JSUnusedGlobalSymbols
function initializeCoreMod() {
    return {
        'book': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.world.item.WrittenBookItem',
                'methodName': 'getName',
                'methodDesc': '(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/network/chat/Component;'
            },
            'transformer': function (/*org.objectweb.asm.tree.MethodNode*/ methodNode) {
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                instructions.insertBefore(
                    ASM.findFirstInstruction(methodNode, Opcodes.ARETURN),
                    ASM.listOf(
                        new VarInsnNode(Opcodes.ALOAD, 2),
                        new MethodInsnNode(
                            Opcodes.INVOKESTATIC,
                            'twilightforest/ASMHooks',
                            'book',
                            '(Lnet/minecraft/network/chat/Component;Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/network/chat/Component;',
                            false
                            )
                        )
                    );
                return methodNode;
            }
        }
    }
}
