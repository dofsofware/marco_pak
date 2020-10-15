import { Moment } from 'moment';
import { IUser } from 'app/core/user/user.model';
import { IAssure } from 'app/shared/model/assure.model';
import { Profil } from 'app/shared/model/enumerations/profil.model';

export interface IAssureur {
  id?: number;
  codeAssureur?: string;
  profil?: Profil;
  telephone?: string;
  createdAt?: Moment;
  urlPhoto?: string;
  adresse?: string;
  user?: IUser;
  assures?: IAssure[];
}

export class Assureur implements IAssureur {
  constructor(
    public id?: number,
    public codeAssureur?: string,
    public profil?: Profil,
    public telephone?: string,
    public createdAt?: Moment,
    public urlPhoto?: string,
    public adresse?: string,
    public user?: IUser,
    public assures?: IAssure[]
  ) {}
}
