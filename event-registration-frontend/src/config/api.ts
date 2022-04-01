export const API_BASE_URL =
  process.env.REACT_APP_API_BASE_URL || "http://localhost:8080/api/v1";

export const API_ROUTES = {
  events: "/events",
  registrations: "/registrations",
  users: "/users",
  admin: {
    login: "/admin/login",
    refresh: "/admin/token/refresh",
  },
};
