import { Col, Menu, Row } from "antd";
import { Link, NavLink, useLocation } from "react-router-dom";

import { route } from "../Routes";
import { useUser, useUserRegistration } from "../providers/UserProvider";

function AppBar() {
  const { state: user, dispatch: userDispatch } = useUser();
  const { register, loading } = useUserRegistration();

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

            {user ? (
              <>
                <Menu.Item key="registrations">
                  <NavLink to={"/"}>My Registrations</NavLink>
                </Menu.Item>
                <Menu.Item key={route.profile()}>
                  <NavLink to={route.profile()}>My Profile</NavLink>
                </Menu.Item>
                <Menu.Item
                  style={{ marginLeft: "auto" }}
                  onClick={() => userDispatch({ type: "logout" })}
                >
                  Log out {user.name}
                </Menu.Item>
              </>
            ) : (
              <Menu.Item
                style={{ marginLeft: "auto" }}
                onClick={() => register()}
                disabled={loading}
              >
                Register random user
              </Menu.Item>
            )}
          </Menu>
        </Col>
      </Row>
    </>
  );
}

export default AppBar;
