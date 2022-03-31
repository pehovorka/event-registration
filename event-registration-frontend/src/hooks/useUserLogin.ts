import { message } from "antd";
import axios from "axios";
import { useState } from "react";
import { API_BASE_URL, API_ROUTES } from "../config/api";
import { User } from "../interfaces/User";
import { useUser } from "../providers/UserProvider";

const useUserLogin = () => {
  const userContext = useUser();
  const [data, setData] = useState<User>();
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<unknown | null>(null);

  const login = async (userUid: string) => {
    setLoading(true);
    try {
      const { data: response } = await axios.get<User>(
        `${API_BASE_URL}${API_ROUTES.users}/${userUid}`,
        { headers: { "X-User-Uid": userUid } }
      );
      setData(response);
      setError(undefined);
      userContext.dispatch({ type: "login", data: response });
      localStorage.setItem("userUid", response.uid);
      message.success(`Welcome back, ${response.name}!`);
    } catch (error) {
      setError(error);
      localStorage.removeItem("userUid");
    }
    setLoading(false);
  };

  return { login, data, loading, error };
};

export default useUserLogin;
