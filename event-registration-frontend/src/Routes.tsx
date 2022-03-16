import { Navigate, Route, Routes as Switch } from "react-router-dom";

import { Events } from "./pages";

export const route = {
  events: () => "/events",
  profile: () => "/profile",
};

export function Routes() {
  return (
    <Switch>
      <Route path={route.events()} element={<Events />} />
      <Route path={route.profile()} element={<h1>User profile</h1>} />
      <Route path="*" element={<Navigate to={route.events()} />} />
    </Switch>
  );
}
