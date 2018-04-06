package twilightforest.compat;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.relauncher.Side;
import slimeknights.tconstruct.library.MaterialIntegration;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.client.material.MaterialRenderInfoLoader;
import slimeknights.tconstruct.library.materials.*;
import slimeknights.tconstruct.library.utils.HarvestLevels;
import slimeknights.tconstruct.tools.TinkerTraits;
import team.chisel.api.ChiselAPIProps;
import team.chisel.api.IMC;
import twilightforest.TwilightForestMod;
import twilightforest.block.RegisterBlockEvent;
import twilightforest.block.TFBlocks;
import twilightforest.compat.tcon.texture.FieryInfoDeserializer;
import twilightforest.compat.tcon.texture.GradientMapInfoDeserializer;
import twilightforest.enums.*;
import twilightforest.item.TFItems;

@Optional.InterfaceList({
        @Optional.Interface(modid = "chisel", iface = "team.chisel.api.ChiselAPIProps"),
        @Optional.Interface(modid = "chisel", iface = "team.chisel.api.IMC"),
        @Optional.Interface(modid = "tconstruct", iface = "slimeknights.tconstruct.library.TinkerRegistry"),
        @Optional.Interface(modid = "tconstruct", iface = "slimeknights.tconstruct.library.client.material.MaterialRenderInfoLoader"),
        @Optional.Interface(modid = "tconstruct", iface = "slimeknights.tconstruct.library.materials.ArrowShaftMaterialStats"),
        @Optional.Interface(modid = "tconstruct", iface = "slimeknights.tconstruct.library.materials.BowMaterialStats"),
        @Optional.Interface(modid = "tconstruct", iface = "slimeknights.tconstruct.library.materials.ExtraMaterialStats"),
        @Optional.Interface(modid = "tconstruct", iface = "slimeknights.tconstruct.library.materials.HandleMaterialStats"),
        @Optional.Interface(modid = "tconstruct", iface = "slimeknights.tconstruct.library.materials.HeadMaterialStats"),
        @Optional.Interface(modid = "tconstruct", iface = "slimeknights.tconstruct.library.materials.FletchingMaterialStats"),
        @Optional.Interface(modid = "tconstruct", iface = "slimeknights.tconstruct.library.materials.Material"),
        @Optional.Interface(modid = "tconstruct", iface = "slimeknights.tconstruct.library.utils.HarvestLevels"),
        @Optional.Interface(modid = "tconstruct", iface = "slimeknights.tconstruct.tools.TinkerTraits"),
        @Optional.Interface(modid = "tconstruct", iface = "slimeknights.tconstruct.library.traits.ITrait")
})
public enum TFCompat {
    CHISEL("Chisel") {
        @Override
        public void init() {
            addBlockToCarvingGroup("stonebrick", new ItemStack(TFBlocks.spiral_bricks));
            addBlockToCarvingGroup("stonebrick", new ItemStack(TFBlocks.etched_nagastone));
            addBlockToCarvingGroup("stonebrick", new ItemStack(TFBlocks.nagastone_pillar));
            addBlockToCarvingGroup("stonebrick", new ItemStack(TFBlocks.etched_nagastone_mossy));
            addBlockToCarvingGroup("stonebrick", new ItemStack(TFBlocks.nagastone_pillar_mossy));
            addBlockToCarvingGroup("stonebrick", new ItemStack(TFBlocks.etched_nagastone_weathered));
            addBlockToCarvingGroup("stonebrick", new ItemStack(TFBlocks.nagastone_pillar_weathered));

            for (MazestoneVariant variant : MazestoneVariant.values())
                addBlockToCarvingGroup("mazestone", new ItemStack(TFBlocks.mazestone, 1, variant.ordinal()));

            for (UnderBrickVariant variant : UnderBrickVariant.values())
                addBlockToCarvingGroup("underbrick", new ItemStack(TFBlocks.underBrick, 1, variant.ordinal()));

            for (TowerWoodVariant variant : TowerWoodVariant.values())
                addBlockToCarvingGroup("towerwood", new ItemStack(TFBlocks.towerWood, 1, variant.ordinal()));

            for (DeadrockVariant variant : DeadrockVariant.values())
                addBlockToCarvingGroup("deadrock", new ItemStack(TFBlocks.deadrock, 1, variant.ordinal()));

            for (CastleBrickVariant variant : CastleBrickVariant.values())
                addBlockToCarvingGroup("castlebrick", new ItemStack(TFBlocks.castleBlock, 1, variant.ordinal()));

            for (int i = 0; i < 4; i++)
                addBlockToCarvingGroup("castlebrick", new ItemStack(TFBlocks.castlePillar, 1, i));

            addBlockToCarvingGroup("castlebrickstairs", new ItemStack(TFBlocks.castleStairs, 1, 0));
            addBlockToCarvingGroup("castlebrickstairs", new ItemStack(TFBlocks.castleStairs, 1, 8));
        }

        private void addBlockToCarvingGroup(String group, ItemStack stack) {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setString("group", group);
            nbt.setTag("stack", stack.serializeNBT());
            FMLInterModComms.sendMessage(ChiselAPIProps.MOD_ID, IMC.ADD_VARIATION_V2.toString(), nbt);
        }
    }, // TODO Forestry
    JEI("Just Enough Items") {

    },
    @SuppressWarnings("WeakerAccess")
    TCONSTRUCT("Tinkers' Construct") {
        @Optional.Method(modid = "tconstruct")
        @Override
        protected void preInit() {
            TinkerRegistry.addMaterialStats(TConObjects.nagascale,
                    new HeadMaterialStats(460, 8.9f, 4.3f, HarvestLevels.IRON),
                    new BowMaterialStats(0.6f, 2f, 0),
                    new ArrowShaftMaterialStats(1.4f, 20));
            TinkerRegistry.integrate(TConObjects.nagascale).preInit();

            TinkerRegistry.addMaterialStats(TConObjects.steeleaf,
                    new HeadMaterialStats(180, 8f, 7f, HarvestLevels.DIAMOND),
                    new HandleMaterialStats(0.8f, 100),
                    new ExtraMaterialStats(100),
                    new BowMaterialStats(1.4f, 1.8f, 4),
                    new ArrowShaftMaterialStats(0.6f, 10),
                    new FletchingMaterialStats(1f, 0.8f));
            TinkerRegistry.integrate(new MaterialIntegration(TConObjects.steeleaf, null, "Steeleaf")).toolforge().preInit();

            TinkerRegistry.addMaterialStats(TConObjects.fierymetal,
                    new HeadMaterialStats(720, 8f, 7.6f, HarvestLevels.OBSIDIAN),
                    new HandleMaterialStats(0.7f, 400),
                    new ExtraMaterialStats(200),
                    new BowMaterialStats(1f, 0.9f, 2),
                    new ArrowShaftMaterialStats(0.8f, 0));
            TinkerRegistry.integrate(new MaterialIntegration(TConObjects.fierymetal, RegisterBlockEvent.moltenFiery, "Fiery")).toolforge().preInit();

            TinkerRegistry.addMaterialStats(TConObjects.knightmetal,
                    new HeadMaterialStats(1200, 8f, 7f, HarvestLevels.COBALT),
                    new HandleMaterialStats(1.5f, 100),
                    new ExtraMaterialStats(550));
            TinkerRegistry.integrate(new MaterialIntegration(TConObjects.knightmetal, RegisterBlockEvent.moltenKnightmetal, "Knightmetal")).preInit();

            TinkerRegistry.addMaterialStats(TConObjects.ravenFeather,
                    new FletchingMaterialStats(0.95f, 1.15f));
            TinkerRegistry.integrate(TConObjects.ravenFeather).preInit();

            if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
                MaterialRenderInfoLoader.addRenderInfo("gradient_map_colors", GradientMapInfoDeserializer.class);
                MaterialRenderInfoLoader.addRenderInfo("fierymetal", FieryInfoDeserializer.class);
            }
        }

        @Optional.Method(modid = "tconstruct")
        @Override
        protected void init() {
            TConObjects.nagascale.addItem(TFItems.nagaScale, 1, Material.VALUE_Ingot);
            TConObjects.nagascale
                    .addTrait(TConObjects.twilit)
                    .addTrait(TConObjects.precipitate)
                    .setCraftable(true).setCastable(false)
                    .setRepresentativeItem(TFItems.nagaScale);

            TConObjects.steeleaf.addCommonItems("Steeleaf");
            TConObjects.steeleaf
                    .addTrait(TConObjects.twilit)
                    .addTrait(TConObjects.synergy)
                    .setCraftable(true).setCastable(false)
                    .setRepresentativeItem(TFItems.steeleafIngot);

            TConObjects.fierymetal.addCommonItems("Fiery");
            TConObjects.fierymetal
                    .addTrait(TConObjects.twilit)
                    .addTrait(TinkerTraits.autosmelt, MaterialTypes.HEAD)
                    .addTrait(TinkerTraits.superheat, MaterialTypes.HEAD)
                    .addTrait(TinkerTraits.flammable)
                    .setCraftable(false).setCastable(true)
                    .setRepresentativeItem(TFItems.fieryIngot);

            TConObjects.knightmetal.addCommonItems("Knightmetal");
            TConObjects.knightmetal.addItem(TFItems.armorShard, 1, Material.VALUE_Nugget);
            TConObjects.knightmetal.addItem(TFItems.chainBlock, 1, (Material.VALUE_Ingot * 7) + Material.VALUE_Block);
            TConObjects.knightmetal
                    .addTrait(TConObjects.twilit)
                    .addTrait(TConObjects.valiant)
                    .setCraftable(false).setCastable(true)
                    .setRepresentativeItem(TFItems.knightMetal);

            TConObjects.ravenFeather.addItem(TFItems.feather, 1, Material.VALUE_Ingot);
            TConObjects.ravenFeather
                    .addTrait(TConObjects.twilit)
                    .addTrait(TConObjects.veiled)
                    .setCraftable(true).setCastable(false)
                    .setRepresentativeItem(TFItems.feather);
        }
    },
    THAUMCRAFT("Thaumcraft") {
        // Use the thaumcraft API to register our things with aspects and biomes with values
        // TODO: Reenable once Thaumcraft API is available. Soooon™
        @Override
        protected void postInit() {
            /*try {

                // items
                registerTCObjectTag(TFItems.nagaScale, -1, (new AspectList()).add(Aspect.MOTION, 2).add(Aspect.ARMOR, 3));
                registerTCObjectTag(TFItems.scepterTwilight, -1, (new AspectList()).add(Aspect.MAGIC, 8).add(Aspect.ELDRITCH, 8).add(Aspect.WEAPON, 8));
                registerTCObjectTag(TFItems.scepterLifeDrain, -1, (new AspectList()).add(Aspect.MAGIC, 8).add(Aspect.LIFE, 8).add(Aspect.HUNGER, 8));
                registerTCObjectTag(TFItems.scepterZombie, -1, (new AspectList()).add(Aspect.MAGIC, 8).add(Aspect.UNDEAD, 8).add(Aspect.ENTROPY, 8));
                registerTCObjectTag(TFItems.magicMapFocus, -1, (new AspectList()).add(Aspect.MAGIC, 4).add(Aspect.SENSES, 8));
                registerTCObjectTag(TFItems.mazeMapFocus, -1, (new AspectList()).add(Aspect.MAGIC, 4).add(Aspect.SENSES, 8).add(Aspect.ORDER, 4));
                registerTCObjectTag(TFItems.feather, -1, (new AspectList()).add(Aspect.FLIGHT, 2).add(Aspect.AIR, 1).add(Aspect.DARKNESS, 1));
                registerTCObjectTag(TFItems.liveRoot, -1, (new AspectList()).add(Aspect.MAGIC, 1).add(Aspect.TREE, 2).add(Aspect.LIFE, 2));
                registerTCObjectTag(TFItems.ironwoodIngot, -1, (new AspectList()).add(Aspect.MAGIC, 2).add(Aspect.TREE, 1).add(Aspect.METAL, 4).add(Aspect.CRAFT, 2));
                registerTCObjectTag(TFItems.torchberries, -1, (new AspectList()).add(Aspect.PLANT, 2).add(Aspect.LIGHT, 2));
                registerTCObjectTag(TFItems.fieryBlood, -1, (new AspectList()).add(Aspect.FIRE, 8).add(Aspect.LIFE, 8).add(Aspect.MAGIC, 4));
                registerTCObjectTag(TFItems.trophy, -1, (new AspectList()).add(Aspect.LIFE, 6).add(Aspect.BEAST, 6).add(Aspect.SOUL, 6));
                registerTCObjectTag(TFItems.steeleafIngot, -1, (new AspectList()).add(Aspect.PLANT, 4).add(Aspect.METAL, 2));
                registerTCObjectTag(TFItems.minotaurAxe, -1, (new AspectList()).add(Aspect.TOOL, 2).add(Aspect.WEAPON, 4).add(Aspect.CRYSTAL, 6).add(Aspect.GREED, 8));
                registerTCObjectTag(TFItems.mazebreakerPick, -1, (new AspectList()).add(Aspect.CRYSTAL, 6).add(Aspect.TOOL, 8).add(Aspect.MINE, 8));
                registerTCObjectTag(TFItems.oreMagnet, -1, (new AspectList()).add(Aspect.GREED, 10).add(Aspect.TOOL, 6).add(Aspect.METAL, 8).add(Aspect.MOTION, 6));
                registerTCObjectTag(TFItems.crumbleHorn, -1, (new AspectList()).add(Aspect.ENTROPY, 12).add(Aspect.BEAST, 2));
                registerTCObjectTag(TFItems.peacockFan, -1, (new AspectList()).add(Aspect.AIR, 8).add(Aspect.MOTION, 6).add(Aspect.FLIGHT, 10));
                registerTCObjectTag(TFItems.moonwormQueen, -1, (new AspectList()).add(Aspect.LIGHT, 12).add(Aspect.MAGIC, 1));
                registerTCObjectTag(TFItems.charmOfLife1, -1, (new AspectList()).add(Aspect.LIFE, 2).add(Aspect.HEAL, 2).add(Aspect.GREED, 4));
                registerTCObjectTag(TFItems.charmOfKeeping1, -1, (new AspectList()).add(Aspect.DEATH, 1).add(Aspect.TRAVEL, 2).add(Aspect.GREED, 4));
                registerTCObjectTag(TFItems.towerKey, -1, (new AspectList()).add(Aspect.MECHANISM, 4).add(Aspect.MAGIC, 4));
                registerTCObjectTag(TFItems.transformPowder, -1, (new AspectList()).add(Aspect.MAGIC, 8).add(Aspect.EXCHANGE, 4));
                registerTCObjectTag(TFItems.borerEssence, -1, (new AspectList()).add(Aspect.BEAST, 2).add(Aspect.TREE, 2).add(Aspect.SOUL, 4).add(Aspect.MAGIC, 2));
                registerTCObjectTag(TFItems.armorShard, -1, (new AspectList()).add(Aspect.METAL, 1));
                registerTCObjectTag(TFItems.knightMetal, -1, (new AspectList()).add(Aspect.METAL, 8).add(Aspect.ORDER, 1));
                registerTCObjectTag(TFItems.phantomHelm, -1, (new AspectList()).add(Aspect.METAL, 6).add(Aspect.ARMOR, 6).add(Aspect.UNDEAD, 6));
                registerTCObjectTag(TFItems.phantomPlate, -1, (new AspectList()).add(Aspect.METAL, 8).add(Aspect.ARMOR, 8).add(Aspect.UNDEAD, 8));
                registerTCObjectTag(TFItems.armorShard, -1, (new AspectList()).add(Aspect.METAL, 1));
                registerTCObjectTag(TFItems.lampOfCinders, -1, (new AspectList()).add(Aspect.FIRE, 4).add(Aspect.MAGIC, 4).add(Aspect.TOOL, 4));
                registerTCObjectTag(TFItems.fieryTears, -1, (new AspectList()).add(Aspect.FIRE, 8).add(Aspect.LIFE, 8).add(Aspect.MAGIC, 4));
                registerTCObjectTag(TFItems.alphaFur, -1, (new AspectList()).add(Aspect.COLD, 3).add(Aspect.BEAST, 3).add(Aspect.MAGIC, 4).add(Aspect.ARMOR, 1));
                registerTCObjectTag(TFItems.iceBomb, -1, (new AspectList()).add(Aspect.COLD, 3).add(Aspect.AIR, 1));
                registerTCObjectTag(TFItems.arcticFur, -1, (new AspectList()).add(Aspect.COLD, 3).add(Aspect.BEAST, 3));
                registerTCObjectTag(TFItems.tripleBow, -1, (new AspectList()).add(Aspect.TREE, 6).add(Aspect.BEAST, 6).add(Aspect.CLOTH, 6).add(Aspect.WEAPON, 9).add(Aspect.AIR, 3));
                registerTCObjectTag(TFItems.seekerBow, -1, (new AspectList()).add(Aspect.MIND, 3).add(Aspect.BEAST, 2).add(Aspect.CLOTH, 2).add(Aspect.WEAPON, 3).add(Aspect.AIR, 1));
                registerTCObjectTag(TFItems.iceBow, -1, (new AspectList()).add(Aspect.COLD, 2).add(Aspect.BEAST, 2).add(Aspect.CLOTH, 2).add(Aspect.WEAPON, 3).add(Aspect.AIR, 1));
                registerTCObjectTag(TFItems.enderBow, -1, (new AspectList()).add(Aspect.TRAVEL, 2).add(Aspect.BEAST, 2).add(Aspect.CLOTH, 2).add(Aspect.WEAPON, 3).add(Aspect.AIR, 1));
                registerTCObjectTag(TFItems.iceSword, -1, (new AspectList()).add(Aspect.WEAPON, 4).add(Aspect.CRYSTAL, 4).add(Aspect.COLD, 4));
                registerTCObjectTag(TFItems.glassSword, -1, (new AspectList()).add(Aspect.WEAPON, 5).add(Aspect.CRYSTAL, 4));
                registerTCObjectTag(TFItems.cubeTalisman, -1, (new AspectList()).add(Aspect.VOID, 4).add(Aspect.MAGIC, 4).add(Aspect.ENTROPY, 4));
                registerTCObjectTag(TFItems.cubeOfAnnihilation, -1, (new AspectList()).add(Aspect.VOID, 7).add(Aspect.MAGIC, 7).add(Aspect.ENTROPY, 7));

                // food
                registerTCObjectTag(TFItems.venisonRaw, -1, (new AspectList()).add(Aspect.HUNGER, 2).add(Aspect.FLESH, 4).add(Aspect.BEAST, 2));
                registerTCObjectTag(TFItems.venisonCooked, -1, (new AspectList()).add(Aspect.HUNGER, 4).add(Aspect.FLESH, 4).add(Aspect.CRAFT, 1));
                registerTCObjectTag(TFItems.hydraChop, -1, (new AspectList()).add(Aspect.HUNGER, 6).add(Aspect.FLESH, 6).add(Aspect.LIFE, 4));
                registerTCObjectTag(TFItems.meefRaw, -1, (new AspectList()).add(Aspect.BEAST, 2).add(Aspect.FLESH, 4).add(Aspect.LIFE, 2));
                registerTCObjectTag(TFItems.meefSteak, -1, (new AspectList()).add(Aspect.FIRE, 1).add(Aspect.BEAST, 1).add(Aspect.FLESH, 4).add(Aspect.LIFE, 2));
                registerTCObjectTag(TFItems.meefStroganoff, -1, (new AspectList()).add(Aspect.HUNGER, 4).add(Aspect.BEAST, 2).add(Aspect.FLESH, 4));
                registerTCObjectTag(TFItems.mazeWafer, -1, (new AspectList()).add(Aspect.HUNGER, 2));
                registerTCObjectTag(TFItems.experiment115, -1, (new AspectList()).add(Aspect.HUNGER, 3).add(Aspect.MECHANISM, 1));

                // blocks
                registerTCObjectTag(TFBlocks.firefly, -1, (new AspectList()).add(Aspect.FLIGHT, 1).add(Aspect.LIGHT, 2));
                registerTCObjectTag(TFBlocks.leaves, -1, (new AspectList()).add(Aspect.PLANT, 2));
                registerTCObjectTag(TFBlocks.mazestone, -1, (new AspectList()).add(Aspect.ORDER, 2).add(Aspect.TRAP, 1).add(Aspect.ARMOR, 1));
                registerTCObjectTag(TFBlocks.hedge, 0, (new AspectList()).add(Aspect.PLANT, 1).add(Aspect.WEAPON, 1));
                registerTCObjectTag(TFBlocks.hedge, 1, (new AspectList()).add(Aspect.PLANT, 3).add(Aspect.DARKNESS, 1));
                registerTCObjectTag(TFBlocks.root, -1, (new AspectList()).add(Aspect.TREE, 2));
                registerTCObjectTag(TFBlocks.cicada, -1, (new AspectList()).add(Aspect.SENSES, 2));
                registerTCObjectTag(TFBlocks.uncraftingTable, -1, (new AspectList()).add(Aspect.TREE, 4).add(Aspect.ENTROPY, 8).add(Aspect.EXCHANGE, 12).add(Aspect.CRAFT, 16));
                registerTCObjectTag(TFBlocks.fireJet, -1, (new AspectList()).add(Aspect.FIRE, 4).add(Aspect.AIR, 2).add(Aspect.MOTION, 2));
                registerTCObjectTag(TFBlocks.nagastone, -1, (new AspectList()).add(Aspect.ORDER, 2).add(Aspect.MOTION, 2));
                registerTCObjectTag(TFBlocks.magicLeaves, -1, (new AspectList()).add(Aspect.PLANT, 2));
                registerTCObjectTag(TFBlocks.towerWood, -1, (new AspectList()).add(Aspect.TREE, 2).add(Aspect.MECHANISM, 2));
                registerTCObjectTag(TFBlocks.towerDevice, -1, (new AspectList()).add(Aspect.TREE, 4).add(Aspect.MECHANISM, 4).add(Aspect.MAGIC, 4));
                registerTCObjectTag(TFBlocks.towerTranslucent, -1, (new AspectList()).add(Aspect.TREE, 4).add(Aspect.MECHANISM, 4).add(Aspect.MAGIC, 4).add(Aspect.VOID, 2));
                registerTCObjectTag(TFBlocks.trophy, -1, (new AspectList()).add(Aspect.LIFE, 6).add(Aspect.BEAST, 6).add(Aspect.SOUL, 6));
                registerTCObjectTag(TFBlocks.plant, 3, (new AspectList()).add(Aspect.PLANT, 1));
                registerTCObjectTag(TFBlocks.plant, 4, (new AspectList()).add(Aspect.PLANT, 1));
                registerTCObjectTag(TFBlocks.plant, 5, (new AspectList()).add(Aspect.PLANT, 2));
                registerTCObjectTag(TFBlocks.plant, 8, (new AspectList()).add(Aspect.PLANT, 2).add(Aspect.MAGIC, 1));
                registerTCObjectTag(TFBlocks.plant, 9, (new AspectList()).add(Aspect.PLANT, 2).add(Aspect.DARKNESS, 1).add(Aspect.LIGHT, 1));
                registerTCObjectTag(TFBlocks.plant, 10, (new AspectList()).add(Aspect.PLANT, 2).add(Aspect.DARKNESS, 1));
                registerTCObjectTag(TFBlocks.plant, 11, (new AspectList()).add(Aspect.PLANT, 2).add(Aspect.DARKNESS, 1));
                registerTCObjectTag(TFBlocks.plant, 13, (new AspectList()).add(Aspect.PLANT, 2).add(Aspect.LIGHT, 2));
                registerTCObjectTag(TFBlocks.plant, 14, (new AspectList()).add(Aspect.PLANT, 2));
                registerTCObjectTag(TFBlocks.sapling, -1, (new AspectList()).add(Aspect.PLANT, 4).add(Aspect.TREE, 2));
                registerTCObjectTag(TFBlocks.moonworm, -1, (new AspectList()).add(Aspect.DARKNESS, 2).add(Aspect.LIGHT, 2));
                registerTCObjectTag(TFBlocks.shield, -1, (new AspectList()).add(Aspect.TRAP, 1).add(Aspect.MAGIC, 1).add(Aspect.ARMOR, 1));
                registerTCObjectTag(TFBlocks.trophyPedestal, -1, (new AspectList()).add(Aspect.GREED, 6).add(Aspect.BEAST, 5));
                registerTCObjectTag(TFBlocks.auroraBlock, -1, (new AspectList()).add(Aspect.COLD, 2).add(Aspect.CRYSTAL, 2));
                registerTCObjectTag(TFBlocks.underBrick, -1, (new AspectList()).add(Aspect.DARKNESS, 2).add(Aspect.EARTH, 2));
                registerTCObjectTag(TFBlocks.portal, -1, (new AspectList()).add(Aspect.MAGIC, 1).add(Aspect.MOTION, 2));
                registerTCObjectTag(TFBlocks.trophy, -1, (new AspectList()).add(Aspect.PLANT, 2).add(Aspect.LIGHT, 10));
                registerTCObjectTag(TFBlocks.shield, -1, (new AspectList()).add(Aspect.EARTH, 1).add(Aspect.ORDER, 2).add(Aspect.ARMOR, 2));
                registerTCObjectTag(TFBlocks.thorns, -1, (new AspectList()).add(Aspect.PLANT, 3).add(Aspect.ENTROPY, 2).add(Aspect.TRAP, 2));
                registerTCObjectTag(TFBlocks.thornRose, -1, (new AspectList()).add(Aspect.PLANT, 1).add(Aspect.TRAP, 1).add(Aspect.SENSES, 2));
                registerTCObjectTag(TFBlocks.burntThorns, -1, (new AspectList()).add(Aspect.ENTROPY, 2).add(Aspect.FIRE, 1));
                registerTCObjectTag(TFBlocks.leaves3, -1, (new AspectList()).add(Aspect.PLANT, 2));
                registerTCObjectTag(TFBlocks.deadrock, -1, (new AspectList()).add(Aspect.EARTH, 1).add(Aspect.ENTROPY, 1).add(Aspect.DEATH, 1));
                registerTCObjectTag(TFBlocks.darkleaves, -1, (new AspectList()).add(Aspect.PLANT, 2).add(Aspect.DARKNESS, 2));
                registerTCObjectTag(TFBlocks.auroraPillar, -1, (new AspectList()).add(Aspect.COLD, 2).add(Aspect.ORDER, 2));
                registerTCObjectTag(TFBlocks.auroraSlab, -1, (new AspectList()).add(Aspect.COLD, 2).add(Aspect.ORDER, 2));
                registerTCObjectTag(TFBlocks.auroraDoubleSlab, -1, (new AspectList()).add(Aspect.COLD, 2).add(Aspect.ORDER, 2));
                registerTCObjectTag(TFBlocks.trollSteinn, -1, (new AspectList()).add(Aspect.EARTH, 1).add(Aspect.ORDER, 1).add(Aspect.CRYSTAL, 1));
                registerTCObjectTag(TFBlocks.wispyCloud, -1, (new AspectList()).add(Aspect.WEATHER, 1).add(Aspect.AIR, 1).add(Aspect.FLIGHT, 1));
                registerTCObjectTag(TFBlocks.fluffyCloud, -1, (new AspectList()).add(Aspect.WEATHER, 2).add(Aspect.AIR, 2));
                registerTCObjectTag(TFBlocks.giantCobble, -1, (new AspectList()).add(Aspect.EARTH, 8).add(Aspect.ENTROPY, 8));
                registerTCObjectTag(TFBlocks.giantLog, -1, (new AspectList()).add(Aspect.TREE, 32));
                registerTCObjectTag(TFBlocks.giantLeaves, -1, (new AspectList()).add(Aspect.PLANT, 32));
                registerTCObjectTag(TFBlocks.giantObsidian, -1, (new AspectList()).add(Aspect.FIRE, 16).add(Aspect.DARKNESS, 8).add(Aspect.EARTH, 16));
                registerTCObjectTag(TFBlocks.uberousSoil, -1, (new AspectList()).add(Aspect.EARTH, 4).add(Aspect.SENSES, 2).add(Aspect.PLANT, 2));
                registerTCObjectTag(TFBlocks.hugeStalk, -1, (new AspectList()).add(Aspect.PLANT, 2).add(Aspect.TREE, 2));
                registerTCObjectTag(TFBlocks.hugeGloomBlock, -1, (new AspectList()).add(Aspect.PLANT, 2).add(Aspect.DARKNESS, 1).add(Aspect.LIGHT, 1));
                registerTCObjectTag(TFBlocks.trollVidr, -1, (new AspectList()).add(Aspect.PLANT, 2));
                registerTCObjectTag(TFBlocks.unripeTrollBer, -1, (new AspectList()).add(Aspect.PLANT, 2).add(Aspect.DARKNESS, 1));
                registerTCObjectTag(TFBlocks.trollBer, -1, (new AspectList()).add(Aspect.PLANT, 2).add(Aspect.LIGHT, 4));
                registerTCObjectTag(TFBlocks.knightmetalStorage, -1, (new AspectList()).add(Aspect.METAL, 12).add(Aspect.ORDER, 12));
                registerTCObjectTag(TFBlocks.hugeLilyPad, -1, (new AspectList()).add(Aspect.WATER, 3).add(Aspect.PLANT, 6));
                registerTCObjectTag(TFBlocks.hugeWaterLily, -1, (new AspectList()).add(Aspect.WATER, 2).add(Aspect.PLANT, 2).add(Aspect.SENSES, 2));
                registerTCObjectTag(TFBlocks.slider, -1, (new AspectList()).add(Aspect.MOTION, 4).add(Aspect.TRAP, 6));
                registerTCObjectTag(TFBlocks.castleBlock, -1, (new AspectList()).add(Aspect.ORDER, 2));
                registerTCObjectTag(TFBlocks.castleMagic, -1, (new AspectList()).add(Aspect.ORDER, 2).add(Aspect.MAGIC, 3).add(Aspect.ENERGY, 2));
                registerTCObjectTag(TFBlocks.forceField, -1, (new AspectList()).add(Aspect.MAGIC, 3).add(Aspect.ARMOR, 4));

                FMLLog.info("[TwilightForest] Loaded ThaumcraftApi integration.");
            }
            catch (Exception e)
            {
                FMLLog.warning("[TwilightForest] Had an %s error while trying to register with ThaumcraftApi.", e.getLocalizedMessage());
                // whatever.
            }//*/
        }

        /*// Register a block with Thaumcraft aspects
        private void registerTCObjectTag(Block block, int meta, AspectList list) {
            if (meta == -1) {
                meta = OreDictionary.WILDCARD_VALUE;
            }
            ThaumcraftApi.registerObjectTag(new ItemStack(block, 1, meta), list);
        }

        // Register an item with Thaumcraft aspects
        private void registerTCObjectTag(Item item, int meta, AspectList list) {
            if (meta == -1) {
                meta = OreDictionary.WILDCARD_VALUE;
            }
            ThaumcraftApi.registerObjectTag(new ItemStack(item, 1, meta), list);
        }//*/
    };

    protected void preInit() {}
    protected void init() {}
    protected void postInit() {}

    final private String modName;

    private boolean isActivated = false;

    TFCompat(String modName) {
        this.modName = modName;
    }

    public static void preInitCompat() {
        for (TFCompat compat : TFCompat.values()) {
            if (Loader.isModLoaded(compat.name().toLowerCase())) {
                try {
                    compat.preInit();
                    compat.isActivated = true;
                    TwilightForestMod.LOGGER.info(TwilightForestMod.ID + " has loaded compatibility for mod " + compat.modName + ".");
                } catch (Exception e) {
                    compat.isActivated = false;
                    TwilightForestMod.LOGGER.info(TwilightForestMod.ID + " had an error loading " + compat.modName + " compatibility!");
                    TwilightForestMod.LOGGER.catching(e.fillInStackTrace());
                }
            } else {
                compat.isActivated = false;
                TwilightForestMod.LOGGER.info(TwilightForestMod.ID + " has skipped compatibility for mod " + compat.modName + ".");
            }
        }
    }

    public static void initCompat() {
        for (TFCompat compat : TFCompat.values()) {
            if (compat.isActivated) {
                try {
                    compat.init();
                } catch (Exception e) {
                    compat.isActivated = false;
                    TwilightForestMod.LOGGER.info(TwilightForestMod.ID + " had an error loading " + compat.modName + " compatibility in init!");
                    TwilightForestMod.LOGGER.catching(e.fillInStackTrace());
                }
            }
        }
    }

    public static void postInitCompat() {
        for (TFCompat compat : TFCompat.values()) {
            if (compat.isActivated) {
                try {
                    compat.postInit();
                } catch (Exception e) {
                    compat.isActivated = false;
                    TwilightForestMod.LOGGER.info(TwilightForestMod.ID + " had an error loading " + compat.modName + " compatibility in postInit!");
                    TwilightForestMod.LOGGER.catching(e.fillInStackTrace());
                }
            }
        }
    }
}
