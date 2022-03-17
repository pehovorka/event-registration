import { useState } from "react";
import { Alert, Button, Input, Menu, Popover } from "antd";
import {
  useUser,
  useUserLogin,
  useUserRegistration,
} from "../../providers/UserProvider";
import { Link } from "react-router-dom";
import { route } from "../../Routes";

function UserPopover() {
  const [mainVisible, setMainVisible] = useState(false);
  const [loginVisible, setLoginVisible] = useState(false);

  const handleMainVisibleChange = () => {
    setMainVisible(!mainVisible);
    if (mainVisible) {
      setLoginVisible(false);
    }
  };

  const handleLoginVisibleChange = () => {
    setLoginVisible(!loginVisible);
  };

  const [uidInput, setUidInput] = useState<string>("");
  const { state: user, dispatch: userDispatch } = useUser();
  const {
    register,
    loading: registerLoading,
    error: registerError,
  } = useUserRegistration();
  const { login, loading: loginLoading, error: loginError } = useUserLogin();

  return (
    <Popover
      content={
        <>
          <Menu selectable={false}>
            {!user ? (
              <>
                <Popover
                  content={
                    <>
                      <Input.Group compact>
                        <Input
                          placeholder="UID"
                          style={{ width: "calc(100% - 70px)" }}
                          value={uidInput}
                          onChange={(e) => setUidInput(e.target.value)}
                        />
                        <Button
                          type="primary"
                          onClick={() => login(uidInput)}
                          loading={loginLoading}
                          disabled={!uidInput.length}
                        >
                          Login
                        </Button>
                      </Input.Group>
                      {loginError && (
                        <Alert
                          style={{ margin: "1rem 0" }}
                          type="error"
                          message={`${loginError}`}
                        />
                      )}
                    </>
                  }
                  title="Login user with UID"
                  trigger="click"
                  visible={loginVisible}
                  placement="left"
                >
                  <Menu.Item key="0" onClick={() => handleLoginVisibleChange()}>
                    Login
                  </Menu.Item>
                </Popover>
                <Menu.Item key="1" onClick={register}>
                  {registerLoading ? "Registering..." : "Register random user"}
                </Menu.Item>
              </>
            ) : (
              <>
                <Menu.Item key="3">
                  <Link to={route.profile()}>My Profile</Link>
                </Menu.Item>
                <Menu.Item
                  key="4"
                  onClick={() => userDispatch({ type: "logout" })}
                >
                  Log out
                </Menu.Item>
              </>
            )}
          </Menu>
          {registerError && (
            <Alert
              style={{ margin: "1rem 0" }}
              type="error"
              message={`${registerError}`}
            />
          )}
        </>
      }
      trigger="click"
      visible={mainVisible}
      onVisibleChange={handleMainVisibleChange}
      placement="bottomRight"
    >
      <Menu.Item style={{ marginLeft: "auto" }} key={route.profile()}>
        {user ? user.name : "Anonymous"}
      </Menu.Item>
    </Popover>
  );
}

export default UserPopover;
