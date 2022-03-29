import { Registration } from "./Registration";

export interface Event {
  id: number;
  name: string;
  date: Date;
  duration: number;
  capacity: number;
  registered: number;
  registrations?: Registration[];
}
