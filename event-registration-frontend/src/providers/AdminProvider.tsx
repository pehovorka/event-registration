import { useContext, createContext, useReducer } from "react";
import { AdminTokens } from "../interfaces";

type Action = { type: "login"; data: AdminTokens } | { type: "logout" };

type Dispatch = (action: Action) => void;
type State = AdminTokens | undefined;
type AdminProviderProps = { children: React.ReactNode };

const AdminContext = createContext<
  { state: State; dispatch: Dispatch } | undefined
>(undefined);

function AdminProvider({ children }: AdminProviderProps) {
  const [state, dispatch] = useReducer(adminReducer, undefined);
  const value = { state, dispatch };
  return (
    <AdminContext.Provider value={value}>{children}</AdminContext.Provider>
  );
}

const adminReducer = (state: State, action: Action): State => {
  switch (action.type) {
    case "login": {
      return action.data;
    }
    case "logout": {
      localStorage.removeItem("adminAccessToken");
      localStorage.removeItem("adminRefreshToken");
      return undefined;
    }
    default: {
      throw new Error(`Unhandled action type.`);
    }
  }
};

const useAdmin = () => {
  const context = useContext(AdminContext);
  if (context === undefined) {
    throw new Error("useAdmin must be used within a AdminProvider");
  }
  return context;
};

export { AdminProvider, useAdmin };
