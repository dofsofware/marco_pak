import { Moment } from 'moment';

export interface IRapportSoignant {
  id?: number;
  codePatient?: string;
  codePS?: string;
  facturation?: number;
  description?: any;
  createdAt?: Moment;
}

export class RapportSoignant implements IRapportSoignant {
  constructor(
    public id?: number,
    public codePatient?: string,
    public codePS?: string,
    public facturation?: number,
    public description?: any,
    public createdAt?: Moment
  ) {}
}
