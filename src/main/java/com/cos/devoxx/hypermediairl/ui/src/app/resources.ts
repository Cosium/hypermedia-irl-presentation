import {Resource, State} from "ketting";

export class Resources<T> {
  constructor(private readonly resources: Resource<T>[]) {}

  toStates(): Promise<State<T>[]> {
    return Promise.all(this.resources.map(resource => resource.get()));
  }
}
