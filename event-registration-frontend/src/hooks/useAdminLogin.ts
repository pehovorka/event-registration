import axios, { AxiosError } from "axios";
import { useState } from "react";
import { API_BASE_URL, API_ROUTES } from "../config/api";
import { AdminTokens } from "../interfaces";
import { useAdmin } from "../providers/AdminProvider";
import { useApi } from "../services/api";

const useAdminLogin = () => {
  const adminContext = useAdmin();
  const { api } = useApi();
  const [data, setData] = useState<AdminTokens>();
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<AxiosError | undefined>(undefined);

  const login = async (username: string, password: string) => {
    setLoading(true);
    try {
      const { data: response } = await api.post<AdminTokens>(
        `${API_BASE_URL}${API_ROUTES.admin.login}`,
        {
          username: username,
          password: password,
        }
      );
      setData(response);
      setError(undefined);
      setLoading(false);
      adminContext.dispatch({ type: "login", data: response });
      localStorage.setItem("adminAccessToken", response.accessToken);
      localStorage.setItem("adminRefreshToken", response.refreshToken);
    } catch (error: unknown | AxiosError) {
      setLoading(false);
      localStorage.removeItem("adminAccessToken");
      localStorage.removeItem("adminRefreshToken");
      if (axios.isAxiosError(error)) {
        setError(error);
      } else {
        console.error("Admin login error", error);
      }
    }
  };

  return { login, data, loading, error };
};

export default useAdminLogin;
