package twilightforest.compat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.commons.lang3.tuple.Pair;
import twilightforest.TFSounds;
import twilightforest.TwilightForestMod;
import twilightforest.block.TFBlocks;
import twilightforest.compat.ie.IEShaderRegister;
import twilightforest.compat.ie.ItemTFShader;
import twilightforest.compat.ie.ItemTFShaderGrabbag;
import twilightforest.entity.boss.*;
import twilightforest.enums.*;
import twilightforest.item.RegisterItemEvent;

import java.util.Locale;

public enum TFCompat {

    BAUBLES("Baubles"),
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
            FMLInterModComms.sendMessage(team.chisel.api.ChiselAPIProps.MOD_ID, team.chisel.api.IMC.ADD_VARIATION_V2.toString(), nbt);
        }
    },
    FORESTRY("Forestry"),
    IMMERSIVEENGINEERING("Immersive Engineering") {

        @Override
        protected void initItems(RegisterItemEvent.ItemRegistryHelper items) {
            items.register("shader", ItemTFShader.shader.setTranslationKey("tfEngineeringShader"));
            items.register("shader_bag", ItemTFShaderGrabbag.shader_bag.setTranslationKey("tfEngineeringShaderBag"));

            new IEShaderRegister(); // Calling to initialize it all
        }

        @Override
        protected void init() {
            // Yeah, it's a thing! https://twitter.com/AtomicBlom/status/1004931868012056583
            blusunrize.immersiveengineering.api.tool.RailgunHandler.projectilePropertyMap.add(Pair.of(blusunrize.immersiveengineering.api.ApiUtils.createIngredientStack(TFBlocks.cicada), new blusunrize.immersiveengineering.api.tool.RailgunHandler.RailgunProjectileProperties(2, 0.25){
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
    JEI("Just Enough Items"),
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
            MinecraftForge.EVENT_BUS.register(Thaumcraft.class);
            return true;
        }
    };

    private static final TFCompat[] VALUES = values();

    protected boolean preInit() { return true; }
    protected void init() {}
    protected void postInit() {}

    protected void initItems(RegisterItemEvent.ItemRegistryHelper items) {}

    private final String modName;

    private boolean isActivated = false;

    public boolean isActivated() {
        return isActivated;
    }

    TFCompat(String modName) {
        this.modName = modName;
    }

    public static void initCompatItems(RegisterItemEvent.ItemRegistryHelper items) {
        for (TFCompat compat : VALUES) {
            if (compat.isActivated) {
                try {
                    compat.initItems(items);
                } catch (Exception e) {
                    compat.isActivated = false;
                    TwilightForestMod.LOGGER.error("Had a {} error loading {} compatibility in initializing items!", e.getLocalizedMessage(), compat.modName);
                    TwilightForestMod.LOGGER.catching(e.fillInStackTrace());
                }
            }
        }
    }

    public static void preInitCompat() {
        for (TFCompat compat : VALUES) {
            if (Loader.isModLoaded(compat.name().toLowerCase(Locale.ROOT))) {
                try {
                    compat.isActivated = compat.preInit();

                    if (compat.isActivated) {
                        TwilightForestMod.LOGGER.info("Loaded compatibility for mod {}.", compat.modName);
                    } else {
                        TwilightForestMod.LOGGER.warn("Couldn't activate compatibility for mod {}!", compat.modName);
                    }
                } catch (Exception e) {
                    compat.isActivated = false;
                    TwilightForestMod.LOGGER.error("Had a {} error loading {} compatibility in preInit!", e.getLocalizedMessage(), compat.modName);
                    TwilightForestMod.LOGGER.catching(e.fillInStackTrace());
                }
            } else {
                compat.isActivated = false;
                TwilightForestMod.LOGGER.info("Skipped compatibility for mod {}.", compat.modName);
            }
        }
    }

    public static void initCompat() {
        for (TFCompat compat : VALUES) {
            if (compat.isActivated) {
                try {
                    compat.init();
                } catch (Exception e) {
                    compat.isActivated = false;
                    TwilightForestMod.LOGGER.error("Had a {} error loading {} compatibility in init!", e.getLocalizedMessage(), compat.modName);
                    TwilightForestMod.LOGGER.catching(e.fillInStackTrace());
                }
            }
        }
    }

    public static void postInitCompat() {
        for (TFCompat compat : VALUES) {
            if (compat.isActivated) {
                try {
                    compat.postInit();
                } catch (Exception e) {
                    compat.isActivated = false;
                    TwilightForestMod.LOGGER.error("Had a {} error loading {} compatibility in postInit!", e.getLocalizedMessage(), compat.modName);
                    TwilightForestMod.LOGGER.catching(e.fillInStackTrace());
                }
            }
        }
    }

    static void registerSidedHandler(Side side, Object handler) {
        if (FMLCommonHandler.instance().getSide() == side) {
            MinecraftForge.EVENT_BUS.register(handler);
        }
    }
}
