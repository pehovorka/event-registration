import { Button, Form, Input } from "antd";
import { useEffect, useState } from "react";
import useAdminLogin from "../../hooks/useAdminLogin";
import { useNavigate } from "react-router-dom";
import { route } from "../../Routes";

function LoginForm() {
  const { login, error, data, loading } = useAdminLogin();
  const navigate = useNavigate();
  const [errorMessage, setErrorMessage] = useState<string | undefined>();

  const onFinish = async (data: { username: string; password: string }) => {
    await login(data.username, data.password);
  };

  useEffect(() => {
    if (!loading && !error && data) {
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
    <Form
      name="basic"
      labelAlign="left"
      labelCol={{ span: 3 }}
      wrapperCol={{ span: 10 }}
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

      <Form.Item wrapperCol={{ offset: 6, span: 3 }}>
        <Button type="primary" htmlType="submit" style={{ width: "100%" }}>
          Login
        </Button>
      </Form.Item>
    </Form>
  );
}

export default LoginForm;
