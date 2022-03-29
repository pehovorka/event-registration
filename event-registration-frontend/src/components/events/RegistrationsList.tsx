import { Button, Card, Table } from "antd";
import { ColumnsType } from "antd/lib/table";
import { Registration } from "../../interfaces";

type Props = {
  registrations: Registration[];
};

function RegistrationsList({ registrations }: Props) {
  const columns: ColumnsType<object> = [
    {
      title: "Person",
      dataIndex: ["user", "name"],
      key: "name",
    },
    {
      title: "Person UID",
      dataIndex: ["user", "uid"],
      key: "name",
    },
    {
      title: "Registered at",
      dataIndex: "registeredAt",
      key: "registeredAt",
      render: (value: Date) =>
        new Date(value).toLocaleString("en-GB", {
          dateStyle: "medium",
          timeStyle: "medium",
        }),
    },
  ];

  return (
    <Card
      title={`Attendees (${registrations.length})`}
      bordered={false}
      style={{ marginTop: "1rem" }}
      extra={<Button>Validate attendees</Button>}
    >
      <Table
        columns={columns}
        dataSource={registrations}
        pagination={false}
        rowKey="id"
      />
    </Card>
  );
}

export default RegistrationsList;
