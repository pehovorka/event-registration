import axios from "axios";
import { useMemo } from "react";
import { API_BASE_URL } from "../config/api";
import { useRefreshToken } from "../hooks";
import { useAdmin } from "../providers/AdminProvider";

export const useApi = () => {
  const { state } = useAdmin();
  const { getNewAccessToken } = useRefreshToken();

  const authApi = useMemo(
    () =>
      axios.create({
        baseURL: API_BASE_URL,
        headers: {
          "Content-Type": "application/json",
        },
      }),
    []
  );

  authApi.interceptors.request.use(
    (config) => {
      // Add Authorization header to every sent request
      config.headers = {
        ...config.headers,
        Authorization: `Bearer ${state?.accessToken}`,
      };
      return config;
    },
    (error) => {
      return Promise.reject(error);
    }
  );

  authApi.interceptors.response.use(
    (response) => {
      return response;
    },
    async (error) => {
      if (axios.isAxiosError(error)) {
        if (error.response?.status === 403) {
          const { accessToken } = await getNewAccessToken();

          const newReqConfig = {
            ...error.config,
            headers: {
              ...error.config.headers,
              Authorization: `Bearer ${accessToken}`,
            },
          };

          try {
            return await axios.request(newReqConfig);
          } catch (error) {
            return Promise.reject(error);
          }
        }
      }
      return Promise.reject(error);
    }
  );

  const api = useMemo(
    () =>
      axios.create({
        baseURL: API_BASE_URL,
        headers: {
          "Content-Type": "application/json",
        },
      }),
    []
  );

  return { api, authApi };
};
