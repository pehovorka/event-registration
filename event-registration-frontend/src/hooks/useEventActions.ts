import { message } from "antd";
import { API_BASE_URL, API_ROUTES } from "../config/api";
import { Event } from "../interfaces";
import { useApi } from "../services/api";

const useEventActions = () => {
  const { authApi } = useApi();

  const deleteEvent = async (eventId: Event["id"]) => {
    try {
      await authApi.delete(`${API_BASE_URL}${API_ROUTES.events}/${eventId}`);
      message.success("Event was deleted!");
      return;
    } catch (error) {
      message.error(`${error}`);
      return error;
    }
  };

  const editEvent = async (event: Partial<Event>) => {
    try {
      const { data } = await authApi.put<Event>(
        `${API_BASE_URL}${API_ROUTES.events}/${event.id}`,
        event
      );
      message.success(`${event.name} was updated!`);
      return data;
    } catch (error) {
      message.error(`${error}`);
      throw error;
    }
  };

  const createEvent = async (event: Partial<Event>) => {
    try {
      const { data } = await authApi.post<Event>(
        `${API_BASE_URL}${API_ROUTES.events}`,
        event
      );
      message.success(`${event.name} was created!`);
      return data;
    } catch (error) {
      message.error(`${error}`);
      throw error;
    }
  };

  return { deleteEvent, editEvent, createEvent };
};

export default useEventActions;
