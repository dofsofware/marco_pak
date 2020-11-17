import { Moment } from 'moment';
import { IPS } from 'app/shared/model/ps.model';
import { IAssureur } from 'app/shared/model/assureur.model';
import { IAssure } from 'app/shared/model/assure.model';

export interface IFacture {
  id?: number;
  date?: Moment;
  prix?: number;
  ps?: IPS;
  assureur?: IAssureur;
  assure?: IAssure;
}

export class Facture implements IFacture {
  constructor(
    public id?: number,
    public date?: Moment,
    public prix?: number,
    public ps?: IPS,
    public assureur?: IAssureur,
    public assure?: IAssure
  ) {}
}
