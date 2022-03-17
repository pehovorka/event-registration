import { Event } from "./Event";
import { User } from "./User";

export interface Registration {
  user?: User;
  event: Event;
  registeredAt: Date;
}
