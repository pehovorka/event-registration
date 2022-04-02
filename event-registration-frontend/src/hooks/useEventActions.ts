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

  return { deleteEvent };
};

export default useEventActions;
