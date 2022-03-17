import { Col, Menu, Row } from "antd";
import { Link, NavLink, useLocation } from "react-router-dom";

import { route } from "../../Routes";
import UserPopover from "./UserPopover";

function AppBar() {
  const location = useLocation();
  const { pathname } = location;

  return (
    <>
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
            <Menu.Item key={route.events()}>
              <NavLink to={route.events()}>Events</NavLink>
            </Menu.Item>
            <UserPopover />
          </Menu>
        </Col>
      </Row>
    </>
  );
}

export default AppBar;
