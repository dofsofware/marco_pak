import { Moment } from 'moment';

export interface IRendezVous {
  id?: number;
  codePatient?: string;
  codePS?: string;
  debutRV?: Moment;
  finRV?: Moment;
  createdAt?: Moment;
}

export class RendezVous implements IRendezVous {
  constructor(
    public id?: number,
    public codePatient?: string,
    public codePS?: string,
    public debutRV?: Moment,
    public finRV?: Moment,
    public createdAt?: Moment
  ) {}
}
