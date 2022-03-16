import { Table, Space } from "antd";
import { ColumnsType } from "antd/lib/table";

interface Event {
  id: number;
  name: string;
  date: Date;
  duration: number;
  capacity: number;
  registered: number;
}

interface Props {
  events: Event[];
}

function EventsList({ events }: Props) {
  const columns: ColumnsType<object> = [
    {
      title: "Name",
      dataIndex: "name",
      key: "name",
    },
    {
      title: "Date",
      dataIndex: "date",
      key: "date",
      render: (date) =>
        new Date(date).toLocaleString("en-GB", {
          dateStyle: "long",
          timeStyle: "medium",
        }),
    },
    {
      title: "Duration",
      dataIndex: "duration",
      key: "duration",
      render: (duration) => `${duration} minutes`,
    },
    {
      title: "Registered",
      key: "registered",
      dataIndex: ["capacity", "registered"],
      render: (text, record: Partial<Event>) =>
        `${record.registered}/${record.capacity}`,
    },
    {
      title: "Action",
      key: "action",
      render: (text, record) => (
        <Space size="middle">
          <a>Register</a>
        </Space>
      ),
    },
  ];

  return <Table columns={columns} dataSource={events} />;
}

export default EventsList;
