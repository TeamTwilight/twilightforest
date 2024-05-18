// noinspection ES6ConvertVarToLetConst

var ASM = Java.type('net.neoforged.coremod.api.ASMAPI');
var Opcodes = Java.type('org.objectweb.asm.Opcodes');

var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');

// noinspection JSUnusedGlobalSymbols
function initializeCoreMod() {
    ASM.loadFile('META-INF/asm/util/util.js');
    return {
        'rendering': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer',
                'methodName': 'renderArmorPiece',
                'methodDesc': '(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;ILnet/minecraft/client/model/HumanoidModel;)V'
            },
            'transformer': function (/*org.objectweb.asm.tree.MethodNode*/ methodNode) {
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                instructions.insert(
                    ASM.findFirstInstruction(methodNode, Opcodes.INSTANCEOF),
                    ASM.listOf(
                        new VarInsnNode(Opcodes.ALOAD, 7),
                        new MethodInsnNode(
                            Opcodes.INVOKESTATIC,
                            'twilightforest/ASMHooks',
                            'cancelArmorRendering',
                            '(ZLnet/minecraft/world/item/ItemStack;)Z',
                            false
                        )
                    )
                );
                return methodNode;
            }
        },
        'coloring': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer',
                'methodName': 'renderArmorPiece',
                'methodDesc': '(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;ILnet/minecraft/client/model/HumanoidModel;)V'
            },
            'transformer': function (/*org.objectweb.asm.tree.MethodNode*/ methodNode) {
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                instructions.insert(
                    ASM.findFirstMethodCall(
                        methodNode,
                        ASM.MethodType.STATIC,
                        'net/minecraft/world/item/component/DyedItemColor',
                        'getOrDefault',
                        '(Lnet/minecraft/world/item/ItemStack;I)I'
                    ),
                    ASM.listOf(
                        new VarInsnNode(Opcodes.ALOAD, 8),
                        new VarInsnNode(Opcodes.ALOAD, 7),
                        new MethodInsnNode(
                            Opcodes.INVOKESTATIC,
                            'twilightforest/ASMHooks',
                            'getArcticArmorColor',
                            '(ILnet/minecraft/world/item/ArmorItem;Lnet/minecraft/world/item/ItemStack;)I',
                            false
                        )
                    )
                );
                return methodNode;
            }
        },
        'visibility': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.world.entity.LivingEntity',
                'methodName': 'getVisibilityPercent',
                'methodDesc': '(Lnet/minecraft/world/entity/Entity;)D'
            },
            'transformer': function (/*org.objectweb.asm.tree.MethodNode*/ methodNode) {
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                instructions.insertBefore(
                    findFirstVarInstruction(methodNode, Opcodes.FSTORE, 4),
                    ASM.listOf(
                        new VarInsnNode(Opcodes.ALOAD, 0),
                        new MethodInsnNode(
                            Opcodes.INVOKESTATIC,
                            'twilightforest/ASMHooks',
                            'modifyClothVisibility',
                            '(FLnet/minecraft/world/entity/LivingEntity;)F',
                            false
                        )
                    )
                );
                return methodNode;
            }
        }
    }
}
