import { Moment } from 'moment';
import { IUser } from 'app/core/user/user.model';
import { IAssure } from 'app/shared/model/assure.model';
import { Profil } from 'app/shared/model/enumerations/profil.model';
import { Sexe } from 'app/shared/model/enumerations/sexe.model';

export interface IPS {
  id?: number;
  codePS?: string;
  profil?: Profil;
  sexe?: Sexe;
  telephone?: string;
  createdAt?: Moment;
  urlPhoto?: string;
  profession?: string;
  experience?: number;
  nomDeLetablissement?: string;
  telephoneDeLetablissement?: string;
  adresseDeLetablissement?: string;
  lienGoogleMapsDeLetablissement?: string;
  user?: IUser;
  assures?: IAssure[];
}

export class PS implements IPS {
  constructor(
    public id?: number,
    public codePS?: string,
    public profil?: Profil,
    public sexe?: Sexe,
    public telephone?: string,
    public createdAt?: Moment,
    public urlPhoto?: string,
    public profession?: string,
    public experience?: number,
    public nomDeLetablissement?: string,
    public telephoneDeLetablissement?: string,
    public adresseDeLetablissement?: string,
    public lienGoogleMapsDeLetablissement?: string,
    public user?: IUser,
    public assures?: IAssure[]
  ) {}
}
