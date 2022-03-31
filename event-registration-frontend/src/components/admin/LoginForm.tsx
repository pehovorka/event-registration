import { Button, Card, Form, Input, message, Row } from "antd";
import { useEffect, useState } from "react";
import useAdminLogin from "../../hooks/useAdminLogin";
import { useNavigate } from "react-router-dom";
import { route } from "../../Routes";
import jwtDecode, { JwtPayload } from "jwt-decode";

function LoginForm() {
  const { login, error, data, loading } = useAdminLogin();
  const navigate = useNavigate();
  const [errorMessage, setErrorMessage] = useState<string | undefined>();

  const onFinish = async (data: { username: string; password: string }) => {
    await login(data.username, data.password);
  };

  useEffect(() => {
    if (!loading && !error && data) {
      message.success(
        `Welcome, ${jwtDecode<JwtPayload>(data.accessToken).sub}!`
      );
      navigate(route.admin.events);
    }

    if (error) {
      if (error?.response?.status === 401) {
        setErrorMessage("Incorrect username or password!");
      } else {
        setErrorMessage(`Login error: ${error.message}`);
      }
    }
  }, [error, loading, navigate, data]);

  return (
    <Row justify="center">
      <Card style={{ width: "100%", maxWidth: "50rem", padding: "2rem" }}>
        <Form
          name="login"
          size="large"
          layout="vertical"
          labelCol={{}}
          onFinish={onFinish}
          autoComplete="off"
        >
          <Form.Item
            label="Username"
            name="username"
            rules={[{ required: true, message: "Please input your username!" }]}
            validateStatus={error ? "error" : ""}
            help={errorMessage}
          >
            <Input />
          </Form.Item>

          <Form.Item
            label="Password"
            name="password"
            rules={[{ required: true, message: "Please input your password!" }]}
            validateStatus={error ? "error" : ""}
            help={errorMessage}
          >
            <Input.Password />
          </Form.Item>

          <Form.Item style={{ marginBottom: 0 }}>
            <Button
              type="primary"
              htmlType="submit"
              loading={loading}
              style={{ width: "100%" }}
            >
              Login
            </Button>
          </Form.Item>
        </Form>
      </Card>
    </Row>
  );
}

export default LoginForm;
