import axios from "axios";
import { useMemo } from "react";
import { API_BASE_URL } from "../config/api";
import { useRefreshToken } from "../hooks";
import { useAdmin } from "../providers/AdminProvider";
import { handleDates } from "../utils/handleDates";

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

  useMemo(
    () =>
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
      ),
    [authApi.interceptors.request, state?.accessToken]
  );

  useMemo(
    () =>
      authApi.interceptors.response.use(
        (response) => {
          // Automatically parse string dates to Date
          handleDates(response.data);
          return response;
        },
        async (error) => {
          if (axios.isAxiosError(error)) {
            if (
              error.response?.status === 401 &&
              error.response.data?.cause === "TOKEN_EXPIRED"
            ) {
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
      ),
    [authApi.interceptors.response, getNewAccessToken]
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

  useMemo(
    () =>
      api.interceptors.response.use((response) => {
        // Automatically parse string dates to Date
        handleDates(response.data);
        return response;
      }),
    [api.interceptors.response]
  );

  axios.interceptors.response.use((response) => {
    // Automatically parse string dates to Date for generic Axios instance
    handleDates(response.data);
    return response;
  });

  return { api, authApi };
};
