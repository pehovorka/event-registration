import { Registration } from "./Registration";

export interface User {
  uid: string;
  name: string;
  interests: { name: string }[];
  registrations?: Registration[];
}
