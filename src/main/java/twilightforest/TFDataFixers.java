package twilightforest;

import com.google.common.collect.ImmutableMap;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IFixableData;
import net.minecraftforge.common.util.ModFixs;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.Map;

public class TFDataFixers {
    public static final int DATA_FIXER_VERSION = 1;

    public static void init() {
        ModFixs fixes = FMLCommonHandler.instance().getDataFixer().init(TwilightForestMod.ID, DATA_FIXER_VERSION);

        fixes.registerFix(FixTypes.BLOCK_ENTITY, new nameSpaceTEFixer());
        fixes.registerFix(FixTypes.STRUCTURE, new structureStartIDDataFixer());
    }

    private static class nameSpaceTEFixer implements IFixableData {
        // array only needs to cover legacy tile entity ids, no need to add future tile entity ids to list.
        private final Map<String, String> tileEntityNames;

        {
            ImmutableMap.Builder<String, String> nameMap = ImmutableMap.builder();

            nameMap
                    .put("minecraft:firefly"                 , "twilightforest:firefly"                 )
                    .put("minecraft:cicada"                  , "twilightforest:cicada"                  )
                    .put("minecraft:moonworm"                , "twilightforest:moonworm"                )
                    .put("minecraft:naga_spawner"            , "twilightforest:naga_spawner"            )
                    .put("minecraft:lich_spawner"            , "twilightforest:lich_spawner"            )
                    .put("minecraft:hydra_spawner"           , "twilightforest:hydra_spawner"           )
                    .put("minecraft:smoker"                  , "twilightforest:smoker"                  )
                    .put("minecraft:popping_jet"             , "twilightforest:popping_jet"             )
                    .put("minecraft:flame_jet"               , "twilightforest:flame_jet"               )
                    .put("minecraft:tower_builder"           , "twilightforest:tower_builder"           )
                    .put("minecraft:tower_reverter"          , "twilightforest:tower_reverter"          )
                    .put("minecraft:trophy"                  , "twilightforest:trophy"                  )
                    .put("minecraft:tower_boss_spawner"      , "twilightforest:tower_boss_spawner"      )
                    .put("minecraft:ghast_trap_inactive"     , "twilightforest:ghast_trap_inactive"     )
                    .put("minecraft:ghast_trap_active"       , "twilightforest:ghast_trap_active"       )
                    .put("minecraft:carminite_reactor_active", "twilightforest:carminite_reactor_active")
                    .put("minecraft:knight_phantom_spawner"  , "twilightforest:knight_phantom_spawner"  )
                    .put("minecraft:snow_queen_spawner"      , "twilightforest:snow_queen_spawner"      )
                    .put("minecraft:cinder_furnace"          , "twilightforest:cinder_furnace"          )
                    .put("minecraft:minoshroom_spawner"      , "twilightforest:minoshroom_spawner"      )
                    .put("minecraft:alpha_yeti_spawner"      , "twilightforest:alpha_yeti_spawner"      )
                    .put(          "firefly"                 , "twilightforest:firefly"                 )
                    .put(          "cicada"                  , "twilightforest:cicada"                  )
                    .put(          "moonworm"                , "twilightforest:moonworm"                )
                    .put(          "naga_spawner"            , "twilightforest:naga_spawner"            )
                    .put(          "lich_spawner"            , "twilightforest:lich_spawner"            )
                    .put(          "hydra_spawner"           , "twilightforest:hydra_spawner"           )
                    .put(          "smoker"                  , "twilightforest:smoker"                  )
                    .put(          "popping_jet"             , "twilightforest:popping_jet"             )
                    .put(          "flame_jet"               , "twilightforest:flame_jet"               )
                    .put(          "tower_builder"           , "twilightforest:tower_builder"           )
                    .put(          "tower_reverter"          , "twilightforest:tower_reverter"          )
                    .put(          "trophy"                  , "twilightforest:trophy"                  )
                    .put(          "tower_boss_spawner"      , "twilightforest:tower_boss_spawner"      )
                    .put(          "ghast_trap_inactive"     , "twilightforest:ghast_trap_inactive"     )
                    .put(          "ghast_trap_active"       , "twilightforest:ghast_trap_active"       )
                    .put(          "carminite_reactor_active", "twilightforest:carminite_reactor_active")
                    .put(          "knight_phantom_spawner"  , "twilightforest:knight_phantom_spawner"  )
                    .put(          "snow_queen_spawner"      , "twilightforest:snow_queen_spawner"      )
                    .put(          "cinder_furnace"          , "twilightforest:cinder_furnace"          )
                    .put(          "minoshroom_spawner"      , "twilightforest:minoshroom_spawner"      )
                    .put(          "alpha_yeti_spawner"      , "twilightforest:alpha_yeti_spawner"      );

            tileEntityNames = nameMap.build();
        }

        @Override
        public int getFixVersion() {
            return 1;
        }

        @Override
        public NBTTagCompound fixTagCompound(NBTTagCompound compound) {
            String tileEntityLocation = compound.getString("id");

            compound.setString("id", tileEntityNames.getOrDefault(tileEntityLocation, tileEntityLocation));

            return compound;
        }
    }

    private static class structureStartIDDataFixer implements IFixableData {
        private final String[] startIDs = {
                "TFNothing", // Nothing
                "TFHill"   , // Small Hill
                "TFHill"   , // Medium
                "TFHill"   , // Large
                "TFHedge"  , // Hedge Maze
                "TFNC"     , // Courtyard
                "TFLT"     , // Lich Tower
                "TFAP"     , // Aurora Palace
                "TFNothing", // (Quest Island)
                "TFQuest1" , // Quest Grove
                "TFNothing", // (Druid Grove)
                "TFNothing", // (Floating Ruins)
                "TFHydra"  , // Hydra Lair
                "TFLr"     , // Labyrinth
                "TFDT"     , // Dark Tower
                "TFKSt"    , // Knight Stronghold
                "TFNothing", // (World Tree)
                "TFYeti"   , // Yeti Cave
                "TFTC"     , // Troll cave
                "TFFC"     , // Final Castle
                "TFMT"       // Mushroom Tower
        };

        @Override
        public int getFixVersion() {
            return 1;
        }

        // Basically we just need to shove the structure ID from the `FeatureID` key to the regular `id`.
        @Override
        public NBTTagCompound fixTagCompound(NBTTagCompound compound) {
            int featureID = compound.getInteger("FeatureID");

            compound.setString("id", featureID < startIDs.length ? startIDs[featureID] : "TFNothing");

            return compound;
        }
    }
}
