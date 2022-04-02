import { Table, Space, Button, Col, Row, Modal } from "antd";
import { ColumnsType } from "antd/lib/table";
import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useEventRegistration, useEventActions } from "../../hooks/";
import { Event, Registration } from "../../interfaces";

interface Props {
  events?: Event[];
  registrations?: Registration[];
  refetch?: () => void;
  isAdmin?: boolean;
}

function EventsList({ events, registrations, refetch, isAdmin }: Props) {
  const navigate = useNavigate();
  const { createRegistration, deleteRegistration, loading } =
    useEventRegistration();
  const { deleteEvent } = useEventActions();
  const [idLoading, setIdLoading] = useState<Event["id"]>();

  const columns: ColumnsType<object> = [
    {
      title: "Name",
      dataIndex: "name",
      key: "name",
      render: (name, record: Partial<Event>) =>
        !isAdmin ? name : <Link to={record.id?.toString() || "#"}>{name}</Link>,
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

  if (registrations && !isAdmin) {
    columns.push({
      title: "Action",
      key: "action",
      width: "13rem",
      render: (text, record: Partial<Event>) => {
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
                  deleteRegistration({ eventId: record.id! }).then(() => {
                    refetch && refetch();
                  });
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
                  createRegistration({ eventId: record.id! }).then(() => {
                    refetch && refetch();
                  });
                }}
              >
                Register
              </Button>
            </Space>
          );
        }
      },
    });
  } else if (isAdmin) {
    columns.push({
      title: "Action",
      key: "action",
      width: "13rem",
      render: (text, record: Partial<Event>) => (
        <Row gutter={8}>
          <Col>
            <Space size="middle">
              <Button
                type="default"
                onClick={() => navigate(`${record.id}/edit`)}
              >
                Edit
              </Button>
            </Space>
          </Col>
          <Col>
            <Space size="middle">
              <Button
                type="default"
                danger
                onClick={() => {
                  Modal.confirm({
                    title: record.name,
                    content: (
                      <p>
                        Are you sure you want to delete the event? All
                        registrations will be cancelled!
                      </p>
                    ),
                    type: "warn",
                    onOk: () =>
                      deleteEvent(record.id!).then(() => refetch && refetch()),
                  });
                }}
              >
                Delete
              </Button>
            </Space>
          </Col>
        </Row>
      ),
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
