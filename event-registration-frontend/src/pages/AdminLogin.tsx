import { Alert } from "antd";
import { PageLayout } from "../components";
import { LoginForm } from "../components/admin";
import { useAdmin } from "../providers/AdminProvider";

function AdminLogin() {
  const { state: admin } = useAdmin();
  return (
    <PageLayout title="Administrator login">
      {admin ? (
        <Alert type="error" message="You are already logged in!" />
      ) : (
        <LoginForm />
      )}
    </PageLayout>
  );
}

export default AdminLogin;
