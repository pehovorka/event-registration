import { Event } from "./Event";
import { User } from "./User";

export interface Registration {
  id?: RegistrationKey;
  user: User;
  event: Event;
  registeredAt: Date;
}

export interface RegistrationKey {
  userUid: string;
  eventId: number;
}
