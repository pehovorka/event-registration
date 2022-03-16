import { Alert } from "antd";
import React from "react";
import { PageLayout } from "../components";
import { UserInfo } from "../components/profile";
import { useUser } from "../providers/UserProvider";

type Props = {};

function Profile({}: Props) {
  const { state: user } = useUser();
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
