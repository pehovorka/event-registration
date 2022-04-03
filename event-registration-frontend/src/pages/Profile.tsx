import { Alert } from "antd";
import { useEffect } from "react";
import { PageLayout } from "../components";
import { UserInfo } from "../components/profile";
import { useUserLogin } from "../hooks";
import { useUser } from "../providers/UserProvider";

function Profile() {
  const { state: user } = useUser();
  const { refetch } = useUserLogin();

  useEffect(() => {
    refetch();
  }, [refetch]);

  return (
    <PageLayout title="My Profile">
      {user ? (
        <UserInfo user={user} />
      ) : (
        <Alert type="error" message="You are not logged in!" />
      )}
    </PageLayout>
  );
}

export default Profile;
