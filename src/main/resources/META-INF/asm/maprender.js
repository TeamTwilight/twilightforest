// noinspection ES6ConvertVarToLetConst

var ASM = Java.type('net.neoforged.coremod.api.ASMAPI');
var Opcodes = Java.type('org.objectweb.asm.Opcodes');

var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var FieldInsnNode = Java.type('org.objectweb.asm.tree.FieldInsnNode');

// noinspection JSUnusedGlobalSymbols
function initializeCoreMod() {
    return {
        'decorations': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.client.gui.MapRenderer$MapInstance',
                'methodName': 'draw',
                'methodDesc': '(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ZI)V'
            },
            'transformer': function (/*org.objectweb.asm.tree.MethodNode*/ methodNode) {
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                var insn = null;
                for (var index = 0; index < instructions.size() - 1; index++) {
                    var /*org.objectweb.asm.tree.VarInsnNode*/ node = instructions.get(index);
                    if (insn == null &&

                        node instanceof VarInsnNode &&

                        node.getOpcode() === Opcodes.ISTORE &&

                        node.var === 5

                    )
                        insn = node;

                }
                instructions.insertBefore(
                    insn,
                    ASM.listOf(
                        new VarInsnNode(Opcodes.ALOAD, 0),
                        new FieldInsnNode(Opcodes.GETFIELD, 'net/minecraft/client/gui/MapRenderer$MapInstance', 'data', 'Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData;'),
                        new VarInsnNode(Opcodes.ALOAD, 1),
                        new VarInsnNode(Opcodes.ALOAD, 2),
                        new VarInsnNode(Opcodes.ILOAD, 4),
                        new MethodInsnNode(
                            Opcodes.INVOKESTATIC,
                            'twilightforest/ASMHooks',
                            'mapRenderDecorations',
                            '(ILnet/minecraft/world/level/saveddata/maps/MapItemSavedData;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)I',
                            false
                            )
                        )
                    );
                return methodNode;
            }
        },
        'render': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.client.renderer.ItemInHandRenderer',
                'methodName': 'renderArmWithItem',
                'methodDesc': '(Lnet/minecraft/client/player/AbstractClientPlayer;FFLnet/minecraft/world/InteractionHand;FLnet/minecraft/world/item/ItemStack;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V'
            },
            'transformer': function (/*org.objectweb.asm.tree.MethodNode*/ methodNode) {
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                var i = -1;
                for (var index = instructions.size() - 1; index > 0; index--) {
                    var /*org.objectweb.asm.tree.FieldInsnNode*/ node = instructions.get(index);
                    if (i === -1 &&

                        node instanceof FieldInsnNode &&

                        node.getOpcode() === Opcodes.GETSTATIC &&

                        equate(node.owner, 'net/minecraft/world/item/Items') &&

                        equate(node.name, 'FILLED_MAP')

                    )
                        i = index + 1;

                }

                if (i === -1) {
                    // Must be optifine... Optifine checks for instanceof MapItem, so this patch won't be needed anyway.
                    return methodNode;
                }

                instructions.insert(
                    instructions.get(i),
                    ASM.listOf(
                        new VarInsnNode(Opcodes.ALOAD, 6),
                        new MethodInsnNode(
                            Opcodes.INVOKESTATIC,
                            'twilightforest/ASMHooks',
                            'shouldMapRender',
                            '(ZLnet/minecraft/world/item/ItemStack;)Z',
                            false
                            )
                        )
                    );
                return methodNode;
            }
        },
        'frame': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.world.entity.decoration.ItemFrame',
                'methodName': 'getFramedMapId',
                'methodDesc': '()Ljava/util/OptionalInt;'
            },
            'transformer': function (/*org.objectweb.asm.tree.MethodNode*/ methodNode) {
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                instructions.insertBefore(
                    ASM.findFirstInstruction(methodNode, Opcodes.IFEQ),
                    ASM.listOf(
                        new VarInsnNode(Opcodes.ALOAD, 1),
                        new MethodInsnNode(
                            Opcodes.INVOKESTATIC,
                            'twilightforest/ASMHooks',
                            'shouldMapRender',
                            '(ZLnet/minecraft/world/item/ItemStack;)Z',
                            false
                        )
                    )
                );
                return methodNode;
            }
        },
        'renderdata': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.client.renderer.ItemInHandRenderer',
                'methodName': 'renderMap',
                'methodDesc': '(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/item/ItemStack;)V'
            },
            'transformer': function (/*org.objectweb.asm.tree.MethodNode*/ methodNode) {
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                var insn = null;
                for (var index = 0; index < instructions.size() - 1; index++) {
                    var /*org.objectweb.asm.tree.VarInsnNode*/ node = instructions.get(index);
                    if (insn == null &&

                        node instanceof VarInsnNode &&

                        node.getOpcode() === Opcodes.ASTORE &&

                        node.var === 6

                    )
                        insn = node;

                }
                instructions.insertBefore(
                    insn,
                    ASM.listOf(
                        new VarInsnNode(Opcodes.ALOAD, 4),
                        new VarInsnNode(Opcodes.ALOAD, 0),
                        new FieldInsnNode(Opcodes.GETFIELD, 'net/minecraft/client/renderer/ItemInHandRenderer', 'minecraft', 'Lnet/minecraft/client/Minecraft;'),
                        new FieldInsnNode(Opcodes.GETFIELD, 'net/minecraft/client/Minecraft', 'level', 'Lnet/minecraft/client/multiplayer/ClientLevel;'),
                        new MethodInsnNode(
                            Opcodes.INVOKESTATIC,
                            'twilightforest/ASMHooks',
                            'renderMapData',
                            '(Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;)Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData;',
                            false
                            )
                        )
                    );
                return methodNode;
            }
        },
        'iteminfo': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.world.item.MapItem',
                'methodName': 'appendHoverText',
                'methodDesc': '(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;Ljava/util/List;Lnet/minecraft/world/item/TooltipFlag;)V'
            },
            'transformer': function (/*org.objectweb.asm.tree.MethodNode*/ methodNode) {
                var /*org.objectweb.asm.tree.InsnList*/ instructions = methodNode.instructions;
                var insn = null;
                for (var index = 0; index < instructions.size() - 1; index++) {
                    var /*org.objectweb.asm.tree.VarInsnNode*/ node = instructions.get(index);
                    if (insn == null &&

                        node instanceof VarInsnNode &&

                        node.getOpcode() === Opcodes.ASTORE &&

                        node.var === 6

                    )
                        insn = node;

                }
                instructions.insertBefore(
                    insn,
                    ASM.listOf(
                        new VarInsnNode(Opcodes.ALOAD, 1),
                        new VarInsnNode(Opcodes.ALOAD, 2),
                        new MethodInsnNode(
                            Opcodes.INVOKESTATIC,
                            'twilightforest/ASMHooks',
                            'renderMapData',
                            '(Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;)Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData;',
                            false
                            )
                        )
                    );
                return methodNode;
            }
        }
    }
}

function equate(/*java.lang.Object*/ a, b) {
    return a.equals(b);
}
