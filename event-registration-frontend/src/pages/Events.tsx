import { Alert } from "antd";
import { PageLayout } from "../components";
import { EventsList } from "../components/events";
import { API_ROUTES } from "../config/api";
import useFetchData, { Methods } from "../hooks/useFetchData";

function Events() {
  const { data, loading, error } = useFetchData({
    method: Methods.get,
    path: API_ROUTES.events,
  });
  return (
    <PageLayout>
      <>
        <h1>Events</h1>
        {loading && <p>Loading...</p>}
        {data && <EventsList events={data} />}
        {error && <Alert type="error" message={`${error}`} />}
      </>
    </PageLayout>
  );
}

export default Events;
