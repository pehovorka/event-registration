import axios from "axios";
import {
  useContext,
  createContext,
  useReducer,
  useState,
  useEffect,
} from "react";
import { API_BASE_URL, API_ROUTES } from "../config/api";
import { User } from "../interfaces/User";

type Action =
  | { type: "register"; data: User }
  | { type: "login"; data: User }
  | { type: "logout" };
type Dispatch = (action: Action) => void;
type State = User | undefined;
type UserProviderProps = { children: React.ReactNode };

const UserContext = createContext<
  { state: State; dispatch: Dispatch } | undefined
>(undefined);

function UserProvider({ children }: UserProviderProps) {
  const [state, dispatch] = useReducer(userReducer, undefined);
  const value = { state, dispatch };
  return <UserContext.Provider value={value}>{children}</UserContext.Provider>;
}

const userReducer = (state: State, action: Action): State => {
  switch (action.type) {
    case "register": {
      return action.data;
    }
    case "login": {
      return action.data;
    }
    case "logout": {
      localStorage.removeItem("userUid");
      return undefined;
    }
    default: {
      throw new Error(`Unhandled action type.`);
    }
  }
};

const useUser = () => {
  const context = useContext(UserContext);
  if (context === undefined) {
    throw new Error("useUser must be used within a UserProvider");
  }
  return context;
};

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
    } catch (error) {
      setError(error);
      localStorage.removeItem("userUid");
    }
    setLoading(false);
  };

  useEffect(() => {
    const uid = localStorage.getItem("userUid");
    if (uid && !userContext.state) {
      login(uid);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  return { login, data, loading, error };
};

export { UserProvider, useUser, useUserRegistration, useUserLogin };