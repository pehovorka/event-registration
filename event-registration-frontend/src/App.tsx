import React from "react";
import "./App.css";

import { BrowserRouter } from "react-router-dom";
import { Routes } from "./Routes";
import { UserProvider } from "./providers/UserProvider";

function App() {
  return (
    <UserProvider>
      <BrowserRouter>
        <Routes />
      </BrowserRouter>
    </UserProvider>
  );
}

export default App;
