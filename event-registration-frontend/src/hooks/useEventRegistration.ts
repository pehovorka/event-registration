import axios from "axios";
import { message } from "antd";
import { useState } from "react";
import { API_BASE_URL, API_ROUTES } from "../config/api";
import { Event } from "../interfaces/Event";
import { Registration } from "../interfaces/Registration";
import { useUser } from "../providers/UserProvider";

const useEventRegistration = () => {
  const { state: user, dispatch } = useUser();
  const [data, setData] = useState<Registration>();
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<unknown | null>(null);

  interface Props {
    eventId: Event["id"];
  }

  const createRegistration = async ({ eventId }: Props) => {
    const body: {
      user: Partial<Registration["user"]>;
      event: Partial<Registration["event"]>;
    } = {
      user: {
        uid: user?.uid ?? "",
      },
      event: {
        id: eventId,
      },
    };

    setLoading(true);
    setError(undefined);
    try {
      const { data: response } = await axios.post<Registration>(
        API_BASE_URL + API_ROUTES.registrations,
        body,
        { headers: { "X-User-Uid": user?.uid ?? "" } }
      );
      setData(response);
      setError(undefined);
      dispatch({ type: "addEventRegistration", data: response });
      message.success(`Successfully registered for ${response.event.name}!`);
    } catch (error) {
      setError(error);
      message.error(`${error}`);
    }
    setLoading(false);
  };

  const deleteRegistration = async ({ eventId }: Props) => {
    const body: {
      user: Partial<Registration["user"]>;
      event: Partial<Registration["event"]>;
    } = {
      user: {
        uid: user?.uid ?? "",
      },
      event: {
        id: eventId,
      },
    };

    setLoading(true);
    setError(undefined);
    try {
      const { data: response } = await axios.delete<Registration>(
        API_BASE_URL + API_ROUTES.registrations,
        { headers: { "X-User-Uid": user?.uid ?? "" }, data: body }
      );
      setData(response);
      setError(undefined);
      dispatch({ type: "deleteEventRegistration", eventId: eventId });
      message.success("Registration was cancelled!");
    } catch (e) {
      setError(e);
      message.error(`${e}`);
    }
    setLoading(false);
  };

  return { createRegistration, deleteRegistration, data, loading, error };
};

export default useEventRegistration;
