import { useEffect } from "react";
import { useUser } from "../providers/UserProvider";
import { useUserLogin } from ".";

const useUserInitialLogin = () => {
  const { login, loading } = useUserLogin();
  const { state } = useUser();

  useEffect(() => {
    const uid = localStorage.getItem("userUid");
    if (uid && !state && !loading) {
      login(uid);
    }
  }, [login, state, loading]);
};

export default useUserInitialLogin;
