import {
  Button,
  Card,
  Col,
  DatePicker,
  Form,
  Input,
  InputNumber,
  Row,
} from "antd";
import moment, { Moment } from "moment";
import { useNavigate } from "react-router-dom";
import { useEventActions } from "../../hooks";
import { Event } from "../../interfaces";
import { route } from "../../Routes";

interface EventWithMomentDate extends Event {
  momentDate?: Moment;
}

interface Props {
  initialState?: EventWithMomentDate;
}

function EventForm({ initialState }: Props) {
  const { createEvent, editEvent } = useEventActions();
  const navigate = useNavigate();

  if (initialState) {
    initialState.momentDate = moment(initialState.date);
  }

  const onFinish = (values: Partial<EventWithMomentDate>) => {
    const eventValues: Partial<Event> = {
      ...(initialState && { id: initialState.id }),
      date: values.momentDate?.toDate(),
      name: values.name,
      duration: values.duration,
      capacity: values.capacity,
    };
    if (initialState) {
      editEvent(eventValues).then((data) => {
        navigate(`${route.admin.events}/${data.id}`);
      });
    } else {
      createEvent(eventValues).then((data) => {
        navigate(`${route.admin.events}/${data.id}`);
      });
    }
  };

  return (
    <Card>
      <Form
        labelCol={{ span: 6 }}
        wrapperCol={{ span: 14 }}
        layout="horizontal"
        initialValues={initialState}
        onFinish={onFinish}
        style={{ width: "100%" }}
      >
        <Form.Item
          label="Name"
          name="name"
          rules={[{ required: true, message: "Please input a name!" }]}
        >
          <Input />
        </Form.Item>
        <Form.Item
          label="Date and time"
          name="momentDate"
          rules={[{ required: true, message: "Please select a date!" }]}
        >
          <DatePicker showTime={true} format="DD/MM/YYYY HH:mm" />
        </Form.Item>
        <Form.Item
          label="Duration (minutes)"
          name="duration"
          rules={[{ required: true, message: "Please input a duration!" }]}
        >
          <InputNumber min={0} />
        </Form.Item>
        <Form.Item
          label="Capacity (people)"
          name="capacity"
          rules={[
            {
              required: true,
              message: "Please input a capacity!",
            },
          ]}
        >
          <InputNumber min={initialState?.registered} />
        </Form.Item>
        <Form.Item>
          <Row>
            <Col span={9} />
            <Col>
              <Button type="primary" htmlType="submit">
                {initialState ? "Save changes" : "Create event"}
              </Button>
            </Col>
          </Row>
        </Form.Item>
      </Form>
    </Card>
  );
}

export default EventForm;
