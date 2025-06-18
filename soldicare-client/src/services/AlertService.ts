import axios from "axios";

export const getAlerts = async (
  userSystemID: string,
  userEmail: string,
  size: number = 30,
  page: number = 0
) => {
  const baseUrl = import.meta.env.VITE_API_BACKEND_URL;
  const url = `${baseUrl}/alerts/?userSystemID=${encodeURIComponent(
    userSystemID
  )}&userEmail=${encodeURIComponent(userEmail)}&size=${size}&page=${page}`;

  const response = await axios.get(url);
  return response.data;
};
