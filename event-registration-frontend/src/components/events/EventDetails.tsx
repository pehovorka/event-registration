import { Button, Descriptions, PageHeader } from "antd";
import { Event } from "../../interfaces";

type Props = { event: Event };

function EventDetails({ event }: Props) {
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
          <Button>Edit</Button>
          <Button danger>Delete</Button>
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
