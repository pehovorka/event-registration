import { Table, Space } from "antd";
import { ColumnsType } from "antd/lib/table";
import { Event } from "../../interfaces/Event";
import { Registration } from "../../interfaces/Registration";

interface Props {
  events?: Event[];
  registrations?: Registration[];
}

function EventsList({ events, registrations }: Props) {
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
          timeStyle: "short",
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
  ];

  if (registrations) {
    columns.push({
      title: "Action",
      key: "action",
      render: (text, record: any) => {
        if (
          registrations?.some(
            (registration) => registration.event.id === record.id
          )
        ) {
          return (
            <Space size="middle">
              <a>Cancel registration</a>
            </Space>
          );
        } else if (record.capacity === record.registered) {
          return <Space size="middle">Event is full</Space>;
        } else {
          return (
            <Space size="middle">
              <a>Register</a>
            </Space>
          );
        }
      },
    });
  }

  return (
    <Table
      columns={columns}
      dataSource={
        events ?? registrations?.map((registration) => registration.event)
      }
      pagination={false}
    />
  );
}

export default EventsList;
