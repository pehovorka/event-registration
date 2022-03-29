import axios from "axios";
import { API_BASE_URL } from "../config/api";
import { setupInterceptorsTo } from "./interceptors";

export const authApi = setupInterceptorsTo(
  axios.create({
    baseURL: API_BASE_URL,
    headers: {
      "Content-Type": "application/json",
    },
  })
);

export const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    "Content-Type": "application/json",
  },
});
