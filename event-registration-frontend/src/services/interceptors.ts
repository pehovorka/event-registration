import axios, {
  AxiosError,
  AxiosInstance,
  AxiosRequestConfig,
  AxiosResponse,
} from "axios";
import { AdminTokens } from "../interfaces";

const onRequest = (config: AxiosRequestConfig): AxiosRequestConfig => {
  const accessToken = localStorage.getItem("adminAccessToken");
  config.headers = {
    ...config.headers,
    Authorization: `Bearer ${accessToken}`,
  };
  return config;
};

const onRequestError = (error: AxiosError): Promise<AxiosError> => {
  return Promise.reject(error);
};

const onResponse = (response: AxiosResponse): AxiosResponse => {
  return response;
};

const onResponseError = async (error: AxiosError): Promise<AxiosError> => {
  if (error.response) {
    // TODO: Expired access token, request a new one.
    if (
      /*       error.response.status === 401 &&
      error.response.data.message === "jwt expired" */
      error.response.status === 403
    ) {
      const refreshToken = localStorage.getItem("adminRefreshToken");

      try {
        const refreshResponse = await axios.post<AdminTokens>(
          `localhost/auth/refresh`,
          {
            refreshToken: refreshToken,
          }
        );
        const { accessToken } = refreshResponse.data;
        localStorage.setItem("adminAccessToken", accessToken);
      } catch (error) {
        return Promise.reject(error);
      }
    }
  }
  return Promise.reject(error);
};

export const setupInterceptorsTo = (
  axiosInstance: AxiosInstance
): AxiosInstance => {
  axiosInstance.interceptors.request.use(onRequest, onRequestError);
  axiosInstance.interceptors.response.use(onResponse, onResponseError);
  return axiosInstance;
};
