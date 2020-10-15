import { Moment } from 'moment';
import { IUser } from 'app/core/user/user.model';
import { IAssureur } from 'app/shared/model/assureur.model';
import { IPack } from 'app/shared/model/pack.model';
import { IPS } from 'app/shared/model/ps.model';
import { Profil } from 'app/shared/model/enumerations/profil.model';

export interface IAssure {
  id?: number;
  codeAssure?: string;
  profil?: Profil;
  telephone?: string;
  createdAt?: Moment;
  urlPhoto?: string;
  adresse?: string;
  user?: IUser;
  assureur?: IAssureur;
  pack?: IPack;
  assureurs?: IAssureur[];
  ps?: IPS[];
}

export class Assure implements IAssure {
  constructor(
    public id?: number,
    public codeAssure?: string,
    public profil?: Profil,
    public telephone?: string,
    public createdAt?: Moment,
    public urlPhoto?: string,
    public adresse?: string,
    public user?: IUser,
    public assureur?: IAssureur,
    public pack?: IPack,
    public assureurs?: IAssureur[],
    public ps?: IPS[]
  ) {}
}
