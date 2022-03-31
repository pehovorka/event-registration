import { Col, Menu, Row } from "antd";
import { Link, NavLink, useLocation } from "react-router-dom";

import { route } from "../../Routes";
import { LogoutButton } from "../admin";
import UserPopover from "./UserPopover";

type Props = {
  isAdminPath: boolean;
};

function AppBar({ isAdminPath }: Props) {
  const location = useLocation();
  const { pathname } = location;

  return !isAdminPath ? (
    <Row>
      <Col>
        <span style={{ padding: "0 1rem", fontSize: "1.25rem" }}>
          <Link to={"/"} style={{ color: "#000" }}>
            Event registration
          </Link>
        </span>
      </Col>
      <Col flex="auto">
        <Menu theme="light" mode="horizontal" selectedKeys={[pathname]}>
          <Menu.Item key={route.events}>
            <NavLink to={route.events}>Events</NavLink>
          </Menu.Item>
          <UserPopover />
        </Menu>
      </Col>
    </Row>
  ) : (
    <Row>
      <Col>
        <span style={{ padding: "0 1rem", fontSize: "1.25rem" }}>
          <Link to={"/admin"} style={{ color: "#fff" }}>
            Event registration admin
          </Link>
        </span>
      </Col>
      <Col flex="auto">
        <Menu theme="dark" mode="horizontal" selectedKeys={[pathname]}>
          <Menu.Item key={route.admin.events}>
            <NavLink to={route.admin.events}>Events</NavLink>
          </Menu.Item>
          <LogoutButton />
        </Menu>
      </Col>
    </Row>
  );
}

export default AppBar;
