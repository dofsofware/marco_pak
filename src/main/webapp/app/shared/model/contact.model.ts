export interface IContact {
  id?: number;
}

export class Contact implements IContact {
  constructor(public id?: number) {}
}
