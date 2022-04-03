import { Button, Card, Modal, Table } from "antd";
import { ColumnsType } from "antd/lib/table";
import { Event, Registration } from "../../interfaces";
import { ValidateUsersModal } from "../admin";
import { UserInfo } from "../profile";

interface Props {
  eventId: Event["id"];
  registrations: Registration[];
  title?: string;
  validation?: { refetchEventAttendees: () => void };
}

function RegistrationsList({
  eventId,
  registrations,
  validation,
  title,
}: Props) {
  const columns: ColumnsType<object> = [
    {
      title: "Person",
      dataIndex: ["user", "name"],
      key: "name",
      render: (value: String, record: Partial<Registration>) => {
        return (
          <Button
            type="link"
            onClick={() =>
              Modal.info({
                content: <UserInfo user={record.user!} />,
                width: "50%",
              })
            }
          >
            {value}
          </Button>
        );
      },
    },
    {
      title: "Person UID",
      dataIndex: ["user", "uid"],
      key: "name",
    },
    {
      title: "Registered for the event at",
      dataIndex: "registeredAt",
      key: "registeredAt",
      render: (value: Date) =>
        value.toLocaleString("en-GB", {
          dateStyle: "medium",
          timeStyle: "medium",
        }),
    },
  ];

  return (
    <Card
      title={
        title
          ? `${title} (${registrations.length})`
          : `Attendees (${registrations.length})`
      }
      bordered={false}
      style={{ marginTop: "1rem" }}
      extra={
        validation && (
          <ValidateUsersModal
            eventId={eventId}
            disabled={registrations.length === 0}
            refetchEventAttendees={validation.refetchEventAttendees}
          />
        )
      }
    >
      <Table
        columns={columns}
        dataSource={registrations}
        pagination={false}
        rowKey={(row: Partial<Registration>) => row.user?.uid!}
      />
    </Card>
  );
}

export default RegistrationsList;
