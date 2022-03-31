import { Registration } from "./Registration";

export interface ValidationResult {
  unchangedRegistrations: Registration[];
  updatedRegistrations: Registration[];
  deletedRegistrations: Registration[];
}
