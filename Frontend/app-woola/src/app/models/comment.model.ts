import { Route } from "./route.model";

export interface Comment {
  id: number;
  route: Route;
  comment_user: string;
  description: string;
  time: string;
  totalLikes:number;
}
