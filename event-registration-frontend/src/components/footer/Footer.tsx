import { NavLink } from "react-router-dom";
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
      <br />
      Client app created for the CSCU9YW - Web Services module assignment.
      <br />
      Student number 2944806, University of Stirling, spring semester 2022.
    </div>
  );
}

export default Footer;
