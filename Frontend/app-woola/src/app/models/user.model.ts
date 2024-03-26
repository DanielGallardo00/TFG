export interface User {
  id: number;
  email: string;
  username: string;
  encodedPassword: string;
  rol: string;
  favoritos: string;
  validated: boolean;
  checkToken: string;
}
