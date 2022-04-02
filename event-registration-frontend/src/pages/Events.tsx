import { PlusCircleOutlined } from "@ant-design/icons";
import { Alert, Button, Skeleton } from "antd";
import { useNavigate } from "react-router-dom";
import { PageLayout } from "../components";
import { EventsList } from "../components/events";
import { API_ROUTES } from "../config/api";
import useFetchData, { Methods } from "../hooks/useFetchData";
import { Event } from "../interfaces/Event";
import { useUser } from "../providers/UserProvider";
import { route } from "../Routes";

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
  const navigate = useNavigate();

  return (
    <PageLayout
      title="Events"
      titleExtra={
        isAdmin ? (
          <Button
            icon={<PlusCircleOutlined />}
            type="primary"
            onClick={() => navigate(route.admin.createEvent)}
          >
            Create new
          </Button>
        ) : (
          <></>
        )
      }
    >
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
