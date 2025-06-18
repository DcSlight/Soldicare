import { useEffect, useState } from "react";
import type { Soldier } from "../interface/soldier.interface";
import type { Snapshot } from "../interface/alert.interface";
import type { UnitData } from "../interface/UnitData.interface";
import { getSoldierSnapshot, getSoldierUnit } from "../services/SoldierService";

export const useSoldierData = (
  soldiers: Soldier[],
  selectedId: string | null,
  systemID: string,
  email: string
) => {
  const [activeSoldier, setActiveSoldier] = useState<Soldier | null>(null);
  const [soldierUnit, setSoldierUnit] = useState<UnitData | null>(null);
  const [snapshotHistory, setSnapshotHistory] = useState<Snapshot[]>([]);
  const [loading, setLoading] = useState<boolean>(false);

  useEffect(() => {
    const fetchSoldierData = async () => {
      setLoading(true);

      const soldier =
        soldiers.find((s) => s.id.objectId === selectedId) ??
        soldiers[0] ??
        null;

      setActiveSoldier(soldier);

      if (soldier) {
        try {
          const [unitRaw, snapshots] = await Promise.all([
            getSoldierUnit(
              soldier.id.systemID,
              soldier.id.objectId,
              systemID,
              email
            ),
            getSoldierSnapshot(
              soldier.id.systemID,
              soldier.id.objectId,
              systemID,
              email
            ),
          ]);

          if (unitRaw) {
            const unit: UnitData = {
              id: unitRaw.id,
              name: unitRaw.objectDetails.name,
              color: unitRaw.objectDetails.color,
              polygon: unitRaw.objectDetails.polygon,
              hotspots: unitRaw.objectDetails.hotspots,
              status: unitRaw.status,
              severelyWounded: unitRaw.objectDetails.severelyWounded,
              lightlyWounded: unitRaw.objectDetails.lightlyWounded,
              immobileUnconscious: unitRaw.objectDetails.immobileUnconscious,
              dead: unitRaw.objectDetails.dead,
              combatReadyTroops: unitRaw.objectDetails.combatReadyTroops,
              soldiers: [],
            };
            setSoldierUnit(unit);
          } else {
            setSoldierUnit(null);
          }

          setSnapshotHistory(snapshots);
        } catch (error) {
          console.error("Failed to fetch soldier data:", error);
          setSoldierUnit(null);
          setSnapshotHistory([]);
        }
      }

      setLoading(false);
    };

    fetchSoldierData();
  }, [selectedId, soldiers, systemID, email]);

  return {
    loading,
    activeSoldier,
    soldierUnit,
    snapshotHistory,
  };
};
