import { PageHeader, Tag } from "antd";
import { User } from "../../interfaces/User";
import { EventsList } from "../events";

type Props = { user: User };

function UserInfo({ user }: Props) {
  return (
    <PageHeader ghost={false} title={user.name} subTitle={user.uid}>
      <h3>Interests</h3>
      {user.interests?.length
        ? user.interests.map((interest) => <Tag>{interest.name}</Tag>)
        : "No interests."}
      {user.registrations && (
        <>
          <h3 style={{ marginTop: "1rem" }}>Registrations</h3>
          <EventsList registrations={user.registrations} />
        </>
      )}
    </PageHeader>
  );
}

export default UserInfo;
