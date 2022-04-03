import { useCallback, useState } from "react";
import { API_BASE_URL, API_ROUTES } from "../config/api";
import { User } from "../interfaces/User";
import { useUser } from "../providers/UserProvider";
import { useApi } from "../services/api";

const useUserLogin = () => {
  const { dispatch, state } = useUser();
  const { api } = useApi();
  const [data, setData] = useState<User>();
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<unknown | null>(null);

  const login = useCallback(
    async (userUid: string) => {
      setLoading(true);
      try {
        const { data: response } = await api.get<User>(
          `${API_BASE_URL}${API_ROUTES.users}/${userUid}`,
          { headers: { "X-User-Uid": userUid } }
        );
        setData(response);
        setError(undefined);
        dispatch({ type: "login", data: response });
        localStorage.setItem("userUid", response.uid);
        setLoading(false);
        return response;
      } catch (error) {
        setError(error);
        setLoading(false);
        localStorage.removeItem("userUid");
      }
    },
    [dispatch, api]
  );

  const refetch = useCallback(() => {
    if (state?.uid) {
      login(state.uid);
    }
  }, [state?.uid, login]);

  return { login, data, loading, error, refetch };
};

export default useUserLogin;
