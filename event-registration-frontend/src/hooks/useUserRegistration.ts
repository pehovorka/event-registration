import axios from "axios";
import { useState } from "react";
import { API_BASE_URL, API_ROUTES } from "../config/api";
import { User } from "../interfaces/User";
import { useUser } from "../providers/UserProvider";

const useUserRegistration = () => {
  const userContext = useUser();
  const [data, setData] = useState<User>();
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<unknown | null>(null);

  const register = async () => {
    setLoading(true);
    try {
      const { data: response } = await axios.post<User>(
        `${API_BASE_URL}${API_ROUTES.users}`
      );
      setData(response);
      setError(undefined);
      userContext.dispatch({ type: "register", data: response });
      localStorage.setItem("userUid", response.uid);
    } catch (error) {
      setError(error);
      localStorage.removeItem("userUid");
    }
    setLoading(false);
  };
  return { register, data, loading, error };
};

export default useUserRegistration;
