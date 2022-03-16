import { User } from "./User";

export interface Registration {
  id: { userUid: string; eventId: number };
  user?: User;
  event: Event;
  registeredAt: Date;
}
