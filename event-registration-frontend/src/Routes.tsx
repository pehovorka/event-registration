import { Navigate, Route, Routes as Switch } from "react-router-dom";

import { AdminLogin, Events, Profile, Event } from "./pages";
import { useUserInitialLogin } from "./hooks";
import { AuthGate } from "./components/admin/";

export const route = {
  events: "/events",
  profile: "/profile",
  admin: {
    login: "/admin/login",
    events: "/admin/events",
    event: "/admin/events/:id",
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
        element={
          <AuthGate>
            <Events isAdmin />
          </AuthGate>
        }
      />
      <Route
        path={route.admin.event}
        element={
          <AuthGate>
            <Event />
          </AuthGate>
        }
      />
      <Route path="/admin/*" element={<Navigate to={route.admin.events} />} />

      <Route path="*" element={<Navigate to={route.events} />} />
    </Switch>
  );
}
