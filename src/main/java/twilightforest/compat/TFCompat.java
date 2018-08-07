package twilightforest.compat;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.tool.RailgunHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.commons.lang3.tuple.Pair;
import team.chisel.api.ChiselAPIProps;
import team.chisel.api.IMC;
import twilightforest.TFSounds;
import twilightforest.TwilightForestMod;
import twilightforest.block.TFBlocks;
import twilightforest.compat.ie.ItemTFShaderGrabbag;
import twilightforest.entity.boss.*;
import twilightforest.enums.*;
import twilightforest.item.TFRegisterItemEvent;

@Optional.InterfaceList({
        @Optional.Interface(modid = "chisel", iface = "team.chisel.api.ChiselAPIProps"),
        @Optional.Interface(modid = "chisel", iface = "team.chisel.api.IMC")
})
public enum TFCompat {
    BAUBLES("Baubles") {},
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
                addBlockToCarvingGroup("mazestone", new ItemStack(TFBlocks.maze_stone, 1, variant.ordinal()));

            for (UnderBrickVariant variant : UnderBrickVariant.values())
                addBlockToCarvingGroup("underbrick", new ItemStack(TFBlocks.underbrick, 1, variant.ordinal()));

            for (TowerWoodVariant variant : TowerWoodVariant.values())
                addBlockToCarvingGroup("towerwood", new ItemStack(TFBlocks.tower_wood, 1, variant.ordinal()));

            for (DeadrockVariant variant : DeadrockVariant.values())
                addBlockToCarvingGroup("deadrock", new ItemStack(TFBlocks.deadrock, 1, variant.ordinal()));

            for (CastleBrickVariant variant : CastleBrickVariant.values())
                addBlockToCarvingGroup("castlebrick", new ItemStack(TFBlocks.castle_brick, 1, variant.ordinal()));

            for (int i = 0; i < 4; i++)
                addBlockToCarvingGroup("castlebrick", new ItemStack(TFBlocks.castle_pillar, 1, i));

            addBlockToCarvingGroup("castlebrickstairs", new ItemStack(TFBlocks.castle_stairs, 1, 0));
            addBlockToCarvingGroup("castlebrickstairs", new ItemStack(TFBlocks.castle_stairs, 1, 8));
        }

        private void addBlockToCarvingGroup(String group, ItemStack stack) {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setString("group", group);
            nbt.setTag("stack", stack.serializeNBT());
            FMLInterModComms.sendMessage(ChiselAPIProps.MOD_ID, IMC.ADD_VARIATION_V2.toString(), nbt);
        }
    }, // TODO Forestry
    IMMERSIVEENGINEERING("Immersive Engineering") {
        //@Override
        //protected boolean preInit() {
        //    //try {
        //    //    VersionRange range = VersionRange.createFromVersionSpec("[0.12-83-407,)");

        //    //    return range.containsVersion(Loader.instance().getIndexedModList().get(this.name().toLowerCase(Locale.ROOT)).getProcessedVersion());
        //    //} catch (InvalidVersionSpecificationException e) {
        //    //    return false;
        //    //}
        //    if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
        //        MinecraftForge.EVENT_BUS.register(ItemTFShaderGrabbag.class);

        //    return true;
        //}
        // FIXME uncomment above and event handlers within and delete method below, when https://github.com/Azanor/thaumcraft-api/issues/38 is fixed.
        @Override
        protected void postInit() {
            Thaumcraft.registerAspects();
        }

        @Override
        protected void initItems(TFRegisterItemEvent.ItemRegistryHelper items) {
            items.register("shader", twilightforest.compat.ie.ItemTFShader.shader.setUnlocalizedName("tfEngineeringShader"));
            items.register("shader_bag", twilightforest.compat.ie.ItemTFShaderGrabbag.shader_bag.setUnlocalizedName("tfEngineeringShaderBag"));

            new twilightforest.compat.ie.IEShaderRegister(); // Calling to initialize it all
        }

        @Override
        protected void init() {
            // Yeah, it's a thing! https://twitter.com/AtomicBlom/status/1004931868012056583
            RailgunHandler.projectilePropertyMap.add(Pair.of(ApiUtils.createIngredientStack(TFBlocks.cicada), new RailgunHandler.RailgunProjectileProperties(2, 0.25){
                @Override
                public boolean overrideHitEntity(Entity entityHit, Entity shooter) {
                    World world = entityHit.getEntityWorld();

                    world.spawnEntity(new EntityFallingBlock(world, entityHit.posX, entityHit.posY, entityHit.posZ, TFBlocks.cicada.getDefaultState()));

                    world.playSound(null, entityHit.posX, entityHit.posY, entityHit.posZ, TFSounds.CICADA, SoundCategory.NEUTRAL, 1.0f, (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F + 1.0F);

                    return false;
                }
            }));

            excludeFromShaderBags(EntityTFNaga.class);

            excludeFromShaderBags(EntityTFLich.class);

            excludeFromShaderBags(EntityTFMinoshroom.class);
            excludeFromShaderBags(EntityTFHydra.class);

            excludeFromShaderBags(EntityTFKnightPhantom.class);
            excludeFromShaderBags(EntityTFUrGhast.class);

            excludeFromShaderBags(EntityTFYetiAlpha.class);
            excludeFromShaderBags(EntityTFSnowQueen.class);
        }

        private void excludeFromShaderBags(Class<? extends Entity> entityClass) {
            FMLInterModComms.sendMessage("immersiveengineering", "shaderbag_exclude", entityClass.getName());
        }
    },
    JEI("Just Enough Items") {},
    @SuppressWarnings("WeakerAccess")
    TCONSTRUCT("Tinkers' Construct") {
        @Override
        protected boolean preInit() {
            TConstruct.preInit();
            return true;
        }

        @Override
        protected void init() {
            TConstruct.init();
        }

        @Override
        protected void postInit() {
            TConstruct.postInit();
        }
    },
    THAUMCRAFT("Thaumcraft") {
        @Override
        protected boolean preInit() {
            //Thaumcraft.init();

            MinecraftForge.EVENT_BUS.register(Thaumcraft.class);

            return true;
        }
    };

    protected boolean preInit() { return true; }
    protected void init() {}
    protected void postInit() {}

    protected void initItems(TFRegisterItemEvent.ItemRegistryHelper items) {}

    final private String modName;

    private boolean isActivated = false;

    public boolean isActivated() {
        return isActivated;
    }

    TFCompat(String modName) {
        this.modName = modName;
    }

    public static void initCompatItems(TFRegisterItemEvent.ItemRegistryHelper items) {
        for (TFCompat compat : TFCompat.values()) {
            if (compat.isActivated) {
                try {
                    compat.initItems(items);
                } catch (Exception e) {
                    compat.isActivated = false;
                    TwilightForestMod.LOGGER.info(TwilightForestMod.ID + " had a " + e.getLocalizedMessage() + " error loading " + compat.modName + " compatibility in initializing items!");
                    TwilightForestMod.LOGGER.catching(e.fillInStackTrace());
                }
            }
        }
    }

    public static void preInitCompat() {
        for (TFCompat compat : TFCompat.values()) {
            if (Loader.isModLoaded(compat.name().toLowerCase())) {
                try {
                    compat.isActivated = compat.preInit();

                    if (compat.isActivated()) {
                        TwilightForestMod.LOGGER.info(TwilightForestMod.ID + " has loaded compatibility for mod " + compat.modName + ".");
                    } else {
                        TwilightForestMod.LOGGER.info(TwilightForestMod.ID + " couldn't activate compatibility for mod " + compat.modName + "!");
                    }
                } catch (Exception e) {
                    compat.isActivated = false;
                    TwilightForestMod.LOGGER.info(TwilightForestMod.ID + " had a " + e.getLocalizedMessage() + " error loading " + compat.modName + " compatibility!");
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
                    TwilightForestMod.LOGGER.info(TwilightForestMod.ID + " had a " + e.getLocalizedMessage() + " error loading " + compat.modName + " compatibility in init!");
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
                    TwilightForestMod.LOGGER.info(TwilightForestMod.ID + " had a " + e.getLocalizedMessage() + " error loading " + compat.modName + " compatibility in postInit!");
                    TwilightForestMod.LOGGER.catching(e.fillInStackTrace());
                }
            }
        }
    }
}
