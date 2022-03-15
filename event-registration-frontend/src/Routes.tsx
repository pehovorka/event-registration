import { Navigate, Route, Routes as Switch } from "react-router-dom";

import { Events } from "./pages";

export const route = {
  events: () => "/events",
};

export function Routes() {
  return (
    <Switch>
      <Route path={route.events()} element={<Events />} />
      <Route path="*" element={<Navigate to={route.events()} />} />
    </Switch>
  );
}
