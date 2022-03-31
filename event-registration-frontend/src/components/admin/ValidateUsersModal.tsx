import { Alert, Button, Modal, Spin } from "antd";
import Title from "antd/lib/typography/Title";
import { useState } from "react";
import { API_ROUTES } from "../../config/api";
import { useFetchData } from "../../hooks";
import { Methods } from "../../hooks/useFetchData";
import { Event, ValidationResult } from "../../interfaces";
import { RegistrationsList } from "../events";

type Props = {
  disabled: boolean;
  eventId: Event["id"];
  refetchEventAttendees: () => void;
};

export default function ValidateUsersModal({
  disabled,
  eventId,
  refetchEventAttendees,
}: Props) {
  const [isModalVisible, setIsModalVisible] = useState(false);

  const { data, loading, error, refetch } = useFetchData<ValidationResult>({
    method: Methods.post,
    path: `${API_ROUTES.events}/${eventId}/validate-users`,
    withAdminAuth: true,
    skip: true,
  });

  const showModal = () => {
    setIsModalVisible(true);
    refetch();
  };

  const handleOk = () => {
    setIsModalVisible(false);
    refetchEventAttendees();
  };

  return (
    <>
      <Button type="default" disabled={disabled} onClick={showModal}>
        Validate attendees
      </Button>
      <Modal
        title="Attendees validation"
        width={"90%"}
        visible={isModalVisible}
        onCancel={handleOk}
        destroyOnClose={true}
        footer={
          <Button type="primary" onClick={handleOk}>
            OK
          </Button>
        }
      >
        {loading && (
          <div style={{ width: "100%", textAlign: "center" }}>
            <Spin tip="Validating attendees..."></Spin>
          </div>
        )}
        {error && <Alert type="error" message={`${error}`} />}
        {data && !loading && !error && (
          <>
            <Title level={3} style={{ padding: "0 1.5rem" }}>
              Validation result
            </Title>
            <RegistrationsList
              title="Unchanged attendees"
              eventId={eventId}
              registrations={data.unchangedRegistrations}
            />
            <RegistrationsList
              title="Attendees with updated user profile"
              eventId={eventId}
              registrations={data.updatedRegistrations}
            />
            <RegistrationsList
              title="Deleted attendees"
              eventId={eventId}
              registrations={data.deletedRegistrations}
            />
          </>
        )}
      </Modal>
    </>
  );
}
