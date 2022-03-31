import { Menu, message } from "antd";
import jwtDecode, { JwtPayload } from "jwt-decode";
import React from "react";
import { useAdmin } from "../../providers/AdminProvider";

function LogoutButton() {
  const { state, dispatch } = useAdmin();
  if (state) {
    const adminProps: JwtPayload = jwtDecode(state?.accessToken || "");
    return (
      <Menu.Item
        style={{ marginLeft: "auto" }}
        key={"logoutAdmin"}
        onClick={() => {
          dispatch({ type: "logout" });
          message.success("You were successfully logged out!");
        }}
      >
        Logout {adminProps.sub}
      </Menu.Item>
    );
  } else return <></>;
}

export default LogoutButton;
