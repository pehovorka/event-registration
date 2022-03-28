import { Navigate, Route, Routes as Switch } from "react-router-dom";

import { AdminLogin, Events, Profile } from "./pages";
import { useUserInitialLogin } from "./hooks";

export const route = {
  events: "/events",
  profile: "/profile",
  admin: {
    login: "/admin/login",
    events: "/admin/events",
    event: "/admin/event/:id",
  },
};

export function Routes() {
  useUserInitialLogin();
  return (
    <Switch>
      <Route path={route.events} element={<Events />} />
      <Route path={route.profile} element={<Profile />} />
      <Route path={route.admin.login} element={<AdminLogin />} />
      <Route
        path={route.admin.events}
        element={<h1>Admin list of events</h1>}
      />
      <Route path={route.admin.event} element={<h1>Admin event detail</h1>} />
      <Route path="/admin/*" element={<Navigate to={route.admin.events} />} />
      <Route path="*" element={<Navigate to={route.events} />} />
    </Switch>
  );
}
