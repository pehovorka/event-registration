import { Alert, Skeleton } from "antd";
import { PageLayout } from "../components";
import { EventsList } from "../components/events";
import { API_ROUTES } from "../config/api";
import useFetchData, { Methods } from "../hooks/useFetchData";
import { Event } from "../interfaces/Event";
import { useUser } from "../providers/UserProvider";

interface Props {
  isAdmin?: boolean;
}

function Events({ isAdmin }: Props) {
  const { data, loading, error, refetch } = useFetchData<Event[]>({
    method: Methods.get,
    path: API_ROUTES.events,
    ...(isAdmin && { withAdminAuth: true }),
  });

  const { state: user } = useUser();
  return (
    <PageLayout title="Events">
      <>
        {loading && !data && <Skeleton />}
        {data && (
          <EventsList
            events={data}
            registrations={user?.registrations}
            refetch={refetch}
            isAdmin={isAdmin}
          />
        )}
        {error && <Alert type="error" message={`${error}`} />}
      </>
    </PageLayout>
  );
}

export default Events;
