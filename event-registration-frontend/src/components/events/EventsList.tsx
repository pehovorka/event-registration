import { Table, Space, Button } from "antd";
import { ColumnsType } from "antd/lib/table";
import { useState } from "react";
import { useEventRegistration } from "../../hooks/";
import { Event, Registration } from "../../interfaces";

interface Props {
  events?: Event[];
  registrations?: Registration[];
  refetch?: () => void;
}

function EventsList({ events, registrations, refetch }: Props) {
  const { createRegistration, deleteRegistration, loading } =
    useEventRegistration();
  const [idLoading, setIdLoading] = useState<Event["id"]>();

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
      width: "13rem",
      render: (text, record: any) => {
        if (
          registrations?.some(
            (registration) => registration.event.id === record.id
          )
        ) {
          return (
            <Space size="middle">
              <Button
                type="default"
                danger
                loading={record.id === idLoading ? loading : false}
                onClick={() => {
                  setIdLoading(record.id);
                  deleteRegistration({ eventId: record.id }).then(
                    () => refetch && refetch()
                  );
                }}
              >
                Cancel registration
              </Button>
            </Space>
          );
        } else if (record.capacity === record.registered) {
          return (
            <Button type="default" disabled>
              Event is full
            </Button>
          );
        } else {
          return (
            <Space size="middle">
              <Button
                type="default"
                loading={record.id === idLoading ? loading : false}
                onClick={() => {
                  setIdLoading(record.id);
                  createRegistration({ eventId: record.id }).then(
                    () => refetch && refetch()
                  );
                }}
              >
                Register
              </Button>
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
      rowKey="id"
    />
  );
}

export default EventsList;
