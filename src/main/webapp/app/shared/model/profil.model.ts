export interface IProfil {
  id?: number;
}

export class Profil implements IProfil {
  constructor(public id?: number) {}
}
