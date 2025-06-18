import axios from "axios";
import type { Role } from "../enums/role.enum";
import api from "./api";

export interface LoginData {
  email: string;
  systemID: string;
}

export interface SignupData {
  email: string;
  username: string;
  role: Role;
  avatar: string;
}

export const login = async (data: LoginData) => {
  const url = `${
    import.meta.env.VITE_API_BACKEND_URL
  }/users/login/${encodeURIComponent(data.systemID)}/${encodeURIComponent(
    data.email
  )}`;
  const response: any = await axios.get(url);
  return response.data;
};

export const signup = async (data: SignupData) => {
  // Replace with real API endpoint
  const response = await api.post(
    `${import.meta.env.VITE_API_BACKEND_URL}/users`,
    data
  );
  return response.data;
};
