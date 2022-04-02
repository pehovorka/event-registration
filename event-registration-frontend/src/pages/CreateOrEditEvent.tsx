import { Alert, Skeleton } from "antd";
import { useParams } from "react-router-dom";
import { PageLayout } from "../components";
import { EventForm } from "../components/events";
import { API_ROUTES } from "../config/api";
import { useFetchData } from "../hooks";
import { Methods } from "../hooks/useFetchData";
import { Event } from "../interfaces";

export enum EVENT_FORM_TYPE {
  NEW,
  EDIT,
}

type Props = { type: EVENT_FORM_TYPE };

function CreateOrEditEvent({ type }: Props) {
  const { id } = useParams();

  const { data, loading, error } = useFetchData<Event>({
    method: Methods.get,
    path: `${API_ROUTES.events}/${id}`,
    withAdminAuth: true,
    skip: type === EVENT_FORM_TYPE.NEW ? true : false,
  });

  return (
    <PageLayout
      title={type === EVENT_FORM_TYPE.NEW ? "Create a new event" : "Edit event"}
    >
      <>
        {loading && !data && <Skeleton />}
        {error && <Alert type="error" message={`${error}`} />}
        {!loading && !error && <EventForm initialState={data} />}
      </>
    </PageLayout>
  );
}

export default CreateOrEditEvent;
