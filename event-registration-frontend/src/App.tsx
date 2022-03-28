import React from "react";
import "./App.css";

import { BrowserRouter } from "react-router-dom";
import { Routes } from "./Routes";
import { UserProvider } from "./providers/UserProvider";
import { AdminProvider } from "./providers/AdminProvider";

function App() {
  return (
    <AdminProvider>
      <UserProvider>
        <BrowserRouter>
          <Routes />
        </BrowserRouter>
      </UserProvider>
    </AdminProvider>
  );
}

export default App;
