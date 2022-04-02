import { Button, Descriptions, Modal, PageHeader } from "antd";
import { useNavigate } from "react-router-dom";
import { useEventActions } from "../../hooks";
import { Event } from "../../interfaces";
import { route } from "../../Routes";

type Props = { event: Event };

function EventDetails({ event }: Props) {
  const { deleteEvent } = useEventActions();
  const navigate = useNavigate();
  return (
    <PageHeader
      ghost={false}
      title={event.name}
      onBack={() => window.history.back()}
      subTitle={new Date(event.date).toLocaleString("en-GB", {
        dateStyle: "full",
        timeStyle: "short",
      })}
      extra={
        <>
          <Button
            onClick={() => navigate(`${route.admin.events}/${event.id}/edit`)}
          >
            Edit
          </Button>
          <Button
            danger
            onClick={() => {
              Modal.confirm({
                title: event.name,
                content: (
                  <p>
                    Are you sure you want to delete the event? All registrations
                    will be cancelled!
                  </p>
                ),
                type: "warn",
                onOk: () =>
                  deleteEvent(event.id!).then(() =>
                    navigate(route.admin.events)
                  ),
              });
            }}
          >
            Delete
          </Button>
        </>
      }
    >
      <Descriptions size="small">
        <Descriptions.Item label="Duration">
          {event.duration} minutes
        </Descriptions.Item>
        <Descriptions.Item label="Registered">
          {event.registered} {event.registered === 1 ? "person" : "people"}
        </Descriptions.Item>
        <Descriptions.Item label="Capacity">
          {event.capacity} {event.capacity === 1 ? "person" : "people"}
        </Descriptions.Item>
      </Descriptions>
    </PageHeader>
  );
}

export default EventDetails;
