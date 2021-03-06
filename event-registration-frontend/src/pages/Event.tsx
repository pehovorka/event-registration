import { Alert, Skeleton } from "antd";
import { useParams } from "react-router-dom";
import { PageLayout } from "../components";
import { EventDetails, RegistrationsList } from "../components/events";
import { API_ROUTES } from "../config/api";
import { useFetchData } from "../hooks";
import { Methods } from "../hooks/useFetchData";
import { Event as EventInterface } from "../interfaces";

function Event() {
  const { id } = useParams();

  const { data, loading, error, refetch } = useFetchData<EventInterface>({
    method: Methods.get,
    path: `${API_ROUTES.events}/${id}`,
    withAdminAuth: true,
  });

  return (
    <PageLayout title={"Event detail"}>
      <>
        {loading && !data && <Skeleton />}
        {error && <Alert type="error" message={`${error}`} />}
        {data && (
          <>
            <EventDetails event={data} />
            <RegistrationsList
              eventId={data.id}
              registrations={data.registrations || []}
              validation={{ refetchEventAttendees: refetch }}
            />
          </>
        )}
      </>
    </PageLayout>
  );
}

export default Event;
