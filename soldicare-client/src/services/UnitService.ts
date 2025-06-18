// services/unitService.ts
import axios from "axios";
import type { UnitData } from "../interface/UnitData.interface";
import { getSoldiersByUnitId } from "./SoldierService";

const fetchArmyUnits = async (
  userSystemID: string,
  userEmail: string,
  size = 10,
  page = 0
): Promise<UnitData[]> => {
  const response = await axios.get(
    `${import.meta.env.VITE_API_BACKEND_URL}/objects/search/byType/armyUnit`,
    {
      params: { userSystemID, userEmail, size, page },
      headers: { accept: "application/json" },
    }
  );
  return response.data;
};

export const getArmyUnits = async (
  userSystemID: string,
  userEmail: string,
  size = 10,
  page = 0
) => {
  try {
    return await fetchArmyUnits(userSystemID, userEmail, size, page);
  } catch (error) {
    console.error("Failed to fetch army units:", error);
    throw error;
  }
};

export const getUnitsWithSoldiers = async (
  userSystemID: string,
  userEmail: string,
  size = 10,
  page = 0
): Promise<UnitData[]> => {
  try {
    const units = await fetchArmyUnits(userSystemID, userEmail, size, page);

    return await Promise.all(
      units.map(async (unit) => {
        const soldiers = await getSoldiersByUnitId(
          unit.id.objectId,
          userSystemID,
          userEmail
        );
        return { ...unit, soldiers };
      })
    );
  } catch (error) {
    console.error("Failed to fetch units with soldiers:", error);
    return [];
  }
};
