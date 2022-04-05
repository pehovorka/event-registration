import { Registration } from "./Registration";

export interface Event {
  id: number;
  name: string;
  location: string;
  date: Date;
  duration: number;
  capacity: number;
  registered: number;
  registrations?: Registration[];
}
