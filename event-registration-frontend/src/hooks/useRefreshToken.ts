import axios from "axios";
import { API_BASE_URL, API_ROUTES } from "../config/api";
import { AdminTokens } from "../interfaces";
import { useAdmin } from "../providers/AdminProvider";

const useRefreshToken = () => {
  const { state, dispatch } = useAdmin();
  const getNewAccessToken = async () => {
    try {
      const refreshResponse = await axios.get<AdminTokens>(
        API_BASE_URL + API_ROUTES.admin.refresh,
        {
          headers: {
            Authorization: `Bearer ${state?.refreshToken}`,
          },
        }
      );
      const { accessToken, refreshToken } = refreshResponse.data;
      localStorage.setItem("adminAccessToken", accessToken);
      localStorage.setItem("adminRefreshToken", refreshToken);
      dispatch({
        type: "login",
        data: { accessToken: accessToken, refreshToken: refreshToken },
      });
      return { accessToken, refreshToken };
    } catch (error) {
      if (axios.isAxiosError(error)) {
        if (
          error.response?.status === 401 &&
          error.response.data?.cause === "TOKEN_EXPIRED"
        ) {
          dispatch({ type: "logout" });
        }
      }
      return Promise.reject(error);
    }
  };

  return { getNewAccessToken };
};

export default useRefreshToken;
