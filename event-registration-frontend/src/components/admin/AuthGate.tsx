import { Navigate } from "react-router-dom";
import { useAdmin } from "../../providers/AdminProvider";
import { route } from "../../Routes";

interface Props {
  children: JSX.Element;
}

function AuthGate({ children }: Props) {
  const { state: admin } = useAdmin();

  return admin ? children : <Navigate replace={true} to={route.admin.login} />;
}

export default AuthGate;
