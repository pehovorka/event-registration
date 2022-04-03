import { SelectOutlined } from "@ant-design/icons";
import { Divider } from "antd";
import { NavLink } from "react-router-dom";
import { API_BASE_URL } from "../../config/api";
import { route } from "../../Routes";

interface Props {
  isAdminPath: boolean;
}

function Footer({ isAdminPath }: Props) {
  return (
    <div style={{ textAlign: "center" }}>
      {!isAdminPath ? (
        <NavLink to={route.admin.events}>Switch to admin client</NavLink>
      ) : (
        <NavLink to={route.events}>Switch to attendee client</NavLink>
      )}
      <Divider type="vertical" />
      <a
        href={`${API_BASE_URL}/swagger-ui/index.html`}
        rel="noreferrer"
        target="_blank"
      >
        API docs <SelectOutlined rotate={90} />
      </a>
      <Divider style={{ margin: "1rem" }} />
      <p>
        Client app created for the CSCU9YW - Web Services module assignment.
      </p>
      <p>
        Student number 2944806, University of Stirling, spring semester 2022.
      </p>
    </div>
  );
}

export default Footer;
