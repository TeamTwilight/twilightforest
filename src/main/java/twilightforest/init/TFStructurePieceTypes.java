package twilightforest.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.structures.*;
import twilightforest.world.components.structures.courtyard.*;
import twilightforest.world.components.structures.darktower.*;
import twilightforest.world.components.structures.finalcastle.*;
import twilightforest.world.components.structures.icetower.*;
import twilightforest.world.components.structures.lichtower.*;
import twilightforest.world.components.structures.lichtowerrevamp.*;
import twilightforest.world.components.structures.minotaurmaze.*;
import twilightforest.world.components.structures.mushroomtower.*;
import twilightforest.world.components.structures.stronghold.*;
import twilightforest.world.components.structures.trollcave.*;

import java.util.Locale;

public class TFStructurePieceTypes {
    public static final DeferredRegister<StructurePieceType> STRUCTURE_PIECE_TYPES = DeferredRegister.create(Registries.STRUCTURE_PIECE, TwilightForestMod.ID);

    // Single-Piece Structures
    //IStructurePieceTypes that can be referred to
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFHill = registerPieceType("TFHill", HollowHillComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFHedge = registerPieceType("TFHedge", HedgeMazeComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFQuestGrove = registerPieceType("TFQuest1", QuestGrove::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFHydra = registerPieceType("TFHydra", HydraLairComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFYeti = registerPieceType("TFYeti", YetiCaveComponent::new);

    // Mushroom Castle
    //public static final DeferredHolder<StructurePieceType, StructurePieceType> TFMT = registerPieceType("TFMT", StructureStartMushroomTower::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFMTMai = registerPieceType("TFMTMai", MushroomTowerMainComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFMTWin = registerPieceType("TFMTWin", MushroomTowerWingComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFMTBri = registerPieceType("TFMTBri", MushroomTowerBridgeComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFMTMB = registerPieceType("TFMTMB", MushroomTowerMainBridgeComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFMTRoofMush = registerPieceType("TFMTRoofMush", TowerRoofMushroomComponent::new);

    // Naga Courtyard
    //public static final DeferredHolder<StructurePieceType, StructurePieceType> TFNC = registerPieceType("TFNC", StructureStartCourtyard::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFNCMn = registerPieceType("TFNCMn", CourtyardMain::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFNCCp = registerPieceType("TFNCCp", NagaCourtyardHedgeCapComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFNCCpP = registerPieceType("TFNCCpP", NagaCourtyardHedgeCapPillarComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFNCCr = registerPieceType("TFNCCr", NagaCourtyardHedgeCornerComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFNCLn = registerPieceType("TFNCLn", NagaCourtyardHedgeLineComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFNCT = registerPieceType("TFNCT", NagaCourtyardHedgeTJunctionComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFNCIs = registerPieceType("TFNCIs", NagaCourtyardHedgeIntersectionComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFNCPd = registerPieceType("TFNCPd", NagaCourtyardHedgePadderComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFNCTr = registerPieceType("TFNCTr", CourtyardTerraceBrazier::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFNCDu = registerPieceType("TFNCDu", CourtyardTerraceDuct::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFNCSt = registerPieceType("TFNCSt", CourtyardTerraceStatue::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFNCPa = registerPieceType("TFNCPa", CourtyardPathPiece::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFNCWl = registerPieceType("TFNCWl", CourtyardWall::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFNCWP = registerPieceType("TFNCWP", CourtyardWallPadder::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFNCWC = registerPieceType("TFNCWC", CourtyardWallCornerOuter::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFNCWA = registerPieceType("TFNCWA", CourtyardWallCornerInner::new);

    // Old Lich Tower
    //public static final IStructurePieceType TFLT = TFFeature.registerPiece("TFLT", StructureStartLichTower::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFLTBea = registerPieceType("TFLTBea", TowerBeardComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFLTBA = registerPieceType("TFLTBA", TowerBeardAttachedComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFLTBri = registerPieceType("TFLTBri", TowerBridgeComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFLTMai = registerPieceType("TFLTMai", TowerMainComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFLTOut = registerPieceType("TFLTOut", TowerOutbuildingComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFLTRoo = registerPieceType("TFLTRoo", TowerRoofComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFLTRAS = registerPieceType("TFLTRAS", TowerRoofAttachedSlabComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFLTRF = registerPieceType("TFLTRF", TowerRoofFenceComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFLTRGF = registerPieceType("TFLTRGF", TowerRoofGableForwardsComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFLTRP = registerPieceType("TFLTRP", TowerRoofPointyComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFLTRPO = registerPieceType("TFLTRPO", TowerRoofPointyOverhangComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFLTRS = registerPieceType("TFLTRS", TowerRoofSlabComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFLTRSF = registerPieceType("TFLTRSF", TowerRoofSlabForwardsComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFLTRSt = registerPieceType("TFLTRSt", TowerRoofStairsComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFLTRStO = registerPieceType("TFLTRStO", TowerRoofStairsOverhangComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFLTWin = registerPieceType("TFLTWin", TowerWingComponent::new);

    // New Lich Tower
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TOWER_FOYER = registerPieceType("TFLT" + "TFoy", TowerFoyer::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TOWER_BOSS_ROOM = registerPieceType("TFLT" + "TBoss", BossRoom::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> CENTRAL_TOWER = registerPieceType("TFLT" + "CTSeg", CentralTowerSegment::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> CENTRAL_TO_SIDE_TOWER = registerPieceType("TFLT" + "C2ST", CentralTowerAttachment::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> SIDE_TOWER_ROOM = registerPieceType("TFLT" + "STRm", SideTowerRoom::new);

    // Labyrinth
    //public static final DeferredHolder<StructurePieceType, StructurePieceType> TFLr = registerPieceType("TFLr", StructureStartLabyrinth::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFMMC = registerPieceType("TFMMC", MazeCorridorComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFMMCIF = registerPieceType("TFMMCIF", MazeCorridorIronFenceComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFMMCR = registerPieceType("TFMMCR", MazeCorridorRootsComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFMMCS = registerPieceType("TFMMCS", MazeCorridorShroomsComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFMMDE = registerPieceType("TFMMDE", MazeDeadEndComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFMMDEC = registerPieceType("TFMMDEC", MazeDeadEndChestComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFMMDEF = registerPieceType("TFMMDEF", MazeDeadEndFountainComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFMMDEFL = registerPieceType("TFMMDEFL", MazeDeadEndFountainLavaComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFMMDEP = registerPieceType("TFMMDEP", MazeDeadEndPaintingComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFMMDER = registerPieceType("TFMMDER", MazeDeadEndRootsComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFMMDES = registerPieceType("TFMMDES", MazeDeadEndShroomsComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFMMDET = registerPieceType("TFMMDET", MazeDeadEndTorchesComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFMMDETrC = registerPieceType("TFMMDETrC", MazeDeadEndTrappedChestComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFMMDETC = registerPieceType("TFMMDETC", MazeDeadEndTripwireChestComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFMMES = registerPieceType("TFMMES", MazeEntranceShaftComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFMMMound = registerPieceType("TFMMMound", MazeMoundComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFMMMR = registerPieceType("TFMMMR", MazeMushRoomComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFMMR = registerPieceType("TFMMR", MazeRoomComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFMMRB = registerPieceType("TFMMRB", MazeRoomBossComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFMMRC = registerPieceType("TFMMRC", MazeRoomCollapseComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFMMRE = registerPieceType("TFMMRE", MazeRoomExitComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFMMRF = registerPieceType("TFMMRF", MazeRoomFountainComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFMMRSC = registerPieceType("TFMMRSC", MazeRoomSpawnerChestsComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFMMRV = registerPieceType("TFMMRV", MazeRoomVaultComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFMMRuins = registerPieceType("TFMMRuins", MazeRuinsComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFMMUE = registerPieceType("TFMMUE", MazeUpperEntranceComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFMMaze = registerPieceType("TFMMaze", MinotaurMazeComponent::new);

    // Knight Stronghold
    //public static final DeferredHolder<StructurePieceType, StructurePieceType> TFKSt = registerPieceType("TFKSt", StructureStartKnightStronghold::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFSSH = registerPieceType("TFSSH", StrongholdSmallHallwayComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFSLT = registerPieceType("TFSLT", StrongholdLeftTurnComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFSCr = registerPieceType("TFSCr", StrongholdCrossingComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFSRT = registerPieceType("TFSRT", StrongholdRightTurnComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFSDE = registerPieceType("TFSDE", StrongholdDeadEndComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFSBalR = registerPieceType("TFSBalR", StrongholdBalconyRoomComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFSTR = registerPieceType("TFSTR", StrongholdTrainingRoomComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFSSS = registerPieceType("TFSSS", StrongholdSmallStairsComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFSTC = registerPieceType("TFSTC", StrongholdTreasureCorridorComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFSAt = registerPieceType("TFSAt", StrongholdAtriumComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFSFo = registerPieceType("TFSFo", StrongholdFoundryComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFTreaR = registerPieceType("TFTreaR", StrongholdTreasureRoomComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFSBR = registerPieceType("TFSBR", StrongholdBossRoomComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFSAC = registerPieceType("TFSAC", StrongholdAccessChamberComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFSEnter = registerPieceType("TFSEnter", StrongholdEntranceComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFSUA = registerPieceType("TFSUA", StrongholdUpperAscenderComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFSULT = registerPieceType("TFSULT", StrongholdUpperLeftTurnComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFSURT = registerPieceType("TFSURT", StrongholdUpperRightTurnComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFSUCo = registerPieceType("TFSUCo", StrongholdUpperCorridorComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFSUTI = registerPieceType("TFSUTI", StrongholdUpperTIntersectionComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFSShield = registerPieceType("TFSShield", StrongholdShieldStructure::new);

    // Dark Tower
    //public static final DeferredHolder<StructurePieceType, StructurePieceType> TFDT = registerPieceType("TFDT", StructureStartDarkTower::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFDTBal = registerPieceType("TFDTBal", DarkTowerBalconyComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFDTBea = registerPieceType("TFDTBea", DarkTowerBeardComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFDTBB = registerPieceType("TFDTBB", DarkTowerBossBridgeComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFDTBT = registerPieceType("TFDTBT", DarkTowerBossTrapComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFDTBri = registerPieceType("TFDTBri", DarkTowerBridgeComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFDTEnt = registerPieceType("TFDTEnt", DarkTowerEntranceComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFDTEB = registerPieceType("TFDTEB", DarkTowerEntranceBridgeComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFDTMai = registerPieceType("TFDTMai", DarkTowerMainComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFDTMB = registerPieceType("TFDTMB", DarkTowerMainBridgeComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFDTRooS = registerPieceType("TFDTRooS", DarkTowerRoofComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFDTRA = registerPieceType("TFDTRA", DarkTowerRoofAntennaComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFDTRC = registerPieceType("TFDTRC", DarkTowerRoofCactusComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFDTRFP = registerPieceType("TFDTRFP", DarkTowerRoofFourPostComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFDTRR = registerPieceType("TFDTRR", DarkTowerRoofRingsComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFDTWin = registerPieceType("TFDTWin", DarkTowerWingComponent::new);

    // Aurora Palace
    //public static final DeferredHolder<StructurePieceType, StructurePieceType> TFAP = registerPieceType("TFAP", StructureStartAuroraPalace::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFITMai = registerPieceType("TFITMai", IceTowerMainComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFITWin = registerPieceType("TFITWin", IceTowerWingComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFITRoof = registerPieceType("TFITRoof", IceTowerRoofComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFITBea = registerPieceType("TFITBea", IceTowerBeardComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFITBoss = registerPieceType("TFITBoss", IceTowerBossWingComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFITEnt = registerPieceType("TFITEnt", IceTowerEntranceComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFITBri = registerPieceType("TFITBri", IceTowerBridgeComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFITSt = registerPieceType("TFITSt", IceTowerStairsComponent::new);

    // Troll Cave
    //public static final DeferredHolder<StructurePieceType, StructurePieceType> TFTC = registerPieceType("TFTC", StructureStartTrollCave::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFTCMai = registerPieceType("TFTCMai", TrollCaveMainComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFTCCon = registerPieceType("TFTCCon", TrollCaveConnectComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFTCGard = registerPieceType("TFTCGard", TrollCaveGardenComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFTCloud = registerPieceType("TFTCloud", TrollCloudComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFClCa = registerPieceType("TFClCa", CloudCastleComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFClTr = registerPieceType("TFClTr", CloudTreeComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFTCVa = registerPieceType("TFTCVa", TrollVaultComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFCloud = registerPieceType("TFCloud", CloudComponent::new);

    // Final Castle
    //public static final DeferredHolder<StructurePieceType, StructurePieceType> TFFC = registerPieceType("TFFC", StructureStartFinalCastle::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFFCMain = registerPieceType("TFFCMain", FinalCastleMainComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFFCStTo = registerPieceType("TFFCStTo", FinalCastleStairTowerComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFFCLaTo = registerPieceType("TFFCLaTo", FinalCastleLargeTowerComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFFCMur = registerPieceType("TFFCMur", FinalCastleMuralComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFFCToF48 = registerPieceType("TFFCToF48", FinalCastleFoundation48Component::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFFCRo48Cr = registerPieceType("TFFCRo48Cr", FinalCastleRoof48CrenellatedComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFFCBoGaz = registerPieceType("TFFCBoGaz", FinalCastleBossGazeboComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFFCSiTo = registerPieceType("TFFCSiTo", FinalCastleMazeTower13Component::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFFCDunSt = registerPieceType("TFFCDunSt", FinalCastleDungeonStepsComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFFCDunEn = registerPieceType("TFFCDunEn", FinalCastleDungeonEntranceComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFFCDunR31 = registerPieceType("TFFCDunR31", FinalCastleDungeonRoom31Component::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFFCDunEx = registerPieceType("TFFCDunEx", FinalCastleDungeonExitComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFFCDunBoR = registerPieceType("TFFCDunBoR", FinalCastleDungeonForgeRoomComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFFCRo9Cr = registerPieceType("TFFCRo9Cr", FinalCastleRoof9CrenellatedComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFFCRo13Cr = registerPieceType("TFFCRo13Cr", FinalCastleRoof13CrenellatedComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFFCRo13Con = registerPieceType("TFFCRo13Con", FinalCastleRoof13ConicalComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFFCRo13Pk = registerPieceType("TFFCRo13Pk", FinalCastleRoof13PeakedComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFFCEnTo = registerPieceType("TFFCEnTo", FinalCastleEntranceTowerComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFFCEnSiTo = registerPieceType("TFFCEnSiTo", FinalCastleEntranceSideTowerComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFFCEnBoTo = registerPieceType("TFFCEnBoTo", FinalCastleEntranceBottomTowerComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFFCEnSt = registerPieceType("TFFCEnSt", FinalCastleEntranceStairsComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFFCBelTo = registerPieceType("TFFCBelTo", FinalCastleBellTower21Component::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFFCBri = registerPieceType("TFFCBri", FinalCastleBridgeComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFFCToF13 = registerPieceType("TFFCToF13", FinalCastleFoundation13Component::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFFCBeF21 = registerPieceType("TFFCBeF21", FinalCastleBellFoundation21Component::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFFCFTh21 = registerPieceType("TFFCFTh21", FinalCastleFoundation13ComponentThorns::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFFCDamT = registerPieceType("TFFCDamT", FinalCastleDamagedTowerComponent::new);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> TFFCWrT = registerPieceType("TFFCWrT", FinalCastleWreckedTowerComponent::new);

    private static DeferredHolder<StructurePieceType, StructurePieceType> registerPieceType(String name, StructurePieceType structurePieceType) {
        return TFStructurePieceTypes.STRUCTURE_PIECE_TYPES.register(name.toLowerCase(Locale.ROOT), () -> structurePieceType);
    }
}
