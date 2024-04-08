import { Route } from "./route.model";

export interface User {
  id: number;
  email: string;
  username: string;
  encodedPassword: string;
  rol: string;
  favoritos: Route[];
  validated: boolean;
  checkToken: string;
}
