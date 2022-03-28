import { Button, Form, Input } from "antd";
import useAdminLogin from "../../hooks/useAdminLogin";

function LoginForm() {
  const { login, error } = useAdminLogin();

  const onFinish = (data: { username: string; password: string }) => {
    login(data.username, data.password);
  };
  const onFinishFailed = () => {};

  return (
    <Form
      name="basic"
      labelAlign="left"
      labelCol={{ span: 3 }}
      wrapperCol={{ span: 10 }}
      onFinish={onFinish}
      onFinishFailed={onFinishFailed}
      autoComplete="off"
    >
      <Form.Item
        label="Username"
        name="username"
        rules={[{ required: true, message: "Please input your username!" }]}
        validateStatus={error ? "error" : ""}
        help={
          error?.response?.status === 401 && "Incorrect username or password!"
        }
      >
        <Input />
      </Form.Item>

      <Form.Item
        label="Password"
        name="password"
        rules={[{ required: true, message: "Please input your password!" }]}
        validateStatus={error ? "error" : ""}
        help={
          error?.response?.status === 401 && "Incorrect username or password!"
        }
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
