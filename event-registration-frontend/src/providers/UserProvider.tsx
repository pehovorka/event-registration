import { useContext, createContext, useReducer } from "react";
import { Event, Registration, User } from "../interfaces";

type Action =
  | { type: "register"; data: User }
  | { type: "login"; data: User }
  | { type: "logout" }
  | { type: "addEventRegistration"; data: Registration }
  | { type: "deleteEventRegistration"; eventId: Event["id"] };
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
    case "addEventRegistration": {
      state?.registrations?.push(action.data);
      return state;
    }
    case "deleteEventRegistration": {
      const registrations = state?.registrations?.filter(
        (registration) => registration.event.id !== action.eventId
      );
      return { ...state!, registrations: registrations };
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

export { UserProvider, useUser };
