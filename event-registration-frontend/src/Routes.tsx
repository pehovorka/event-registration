import { Navigate, Route, Routes as Switch } from "react-router-dom";

import { Events, Profile } from "./pages";
import { useInitialLogin } from "./providers/UserProvider";

export const route = {
  events: () => "/events",
  profile: () => "/profile",
};

export function Routes() {
  useInitialLogin();
  return (
    <Switch>
      <Route path={route.events()} element={<Events />} />
      <Route path={route.profile()} element={<Profile />} />
      <Route path="*" element={<Navigate to={route.events()} />} />
    </Switch>
  );
}
