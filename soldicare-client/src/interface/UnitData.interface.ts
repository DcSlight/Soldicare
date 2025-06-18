import type { LatLngTuple } from "leaflet";
import type { Soldier } from "./soldier.interface";

export interface UnitData {
  id: {
    objectId: string;
    systemID: string;
  };
  name: string;
  color: string;
  polygon: LatLngTuple[];
  hotspots?: LatLngTuple[];
  status: string;
  severelyWounded: number;
  lightlyWounded: number;
  immobileUnconscious: number;
  dead: number;
  combatReadyTroops: number;
  soldiers: Soldier[];
}
