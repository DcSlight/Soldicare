import axios from "axios";
import type { Snapshot } from "../interface/alert.interface";
import type { Soldier } from "../interface/soldier.interface";

export const getSoldiers = async (
  userSystemID: string,
  userEmail: string,
  size: number = 10,
  page: number = 0
) => {
  try {
    const response = await axios.get(
      `${import.meta.env.VITE_API_BACKEND_URL}/objects/search/byType/soldier`,
      {
        params: {
          userSystemID,
          userEmail,
          size,
          page,
        },
        headers: {
          accept: "application/json",
        },
      }
    );

    return response.data;
  } catch (error) {
    console.error("Failed to fetch soldiers:", error);
    throw error;
  }
};

export const getSoldierUnit = async (
  systemID: string,
  objectID: string,
  userSystemID: string,
  userEmail: string,
  size: number = 10,
  page: number = 0
) => {
  try {
    const response = await axios.get(
      `${
        import.meta.env.VITE_API_BACKEND_URL
      }/objects/${systemID}/${objectID}/parents`,
      {
        params: {
          userSystemID,
          userEmail,
          size,
          page,
        },
        headers: {
          accept: "application/json",
        },
      }
    );

    const data = response.data;

    // Filter only armyUnit objects and return the first one
    const unit = Array.isArray(data)
      ? data.find((obj) => obj.type === "armyUnit")
      : null;

    return unit ?? null;
  } catch (error) {
    console.error("Failed to fetch soldier unit:", error);
    return null;
  }
};

export const getSoldierSnapshot = async (
  systemID: string,
  objectID: string,
  userSystemID: string,
  userEmail: string,
  size: number = 10,
  page: number = 0
): Promise<Snapshot[]> => {
  try {
    const response = await axios.get(
      `${
        import.meta.env.VITE_API_BACKEND_URL
      }/objects/${systemID}/${objectID}/children`,
      {
        params: {
          userSystemID,
          userEmail,
          size,
          page,
        },
        headers: {
          accept: "application/json",
        },
      }
    );

    const data = response.data;

    const snapshots: Snapshot[] = Array.isArray(data)
      ? data.filter(
          (item) => item.type === "sensor" && item.alias === "allSensor"
        )
      : [];

    return snapshots;
  } catch (error) {
    console.error("Failed to fetch soldier snapshots:", error);
    return [];
  }
};

export const getSoldiersByUnitId = async (
  unitId: string,
  userSystemID: string,
  userEmail: string,
  size = 20,
  page = 0
): Promise<Soldier[]> => {
  try {
    const response = await axios.get(
      `${
        import.meta.env.VITE_API_BACKEND_URL
      }/objects/${userSystemID}/${unitId}/children`,
      {
        params: {
          userSystemID,
          userEmail,
          size,
          page,
        },
        headers: {
          accept: "application/json",
        },
      }
    );
    return response.data;
  } catch (error) {
    console.error(`Failed to fetch soldiers for unit ${unitId}:`, error);
    return [];
  }
};
