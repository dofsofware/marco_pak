import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IAssureur } from 'app/shared/model/assureur.model';

type EntityResponseType = HttpResponse<IAssureur>;
type EntityArrayResponseType = HttpResponse<IAssureur[]>;

@Injectable({ providedIn: 'root' })
export class AssureurService {
  public resourceUrl = SERVER_API_URL + 'api/assureurs';

  constructor(protected http: HttpClient) {}

  create(assureur: IAssureur): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(assureur);
    return this.http
      .post<IAssureur>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(assureur: IAssureur): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(assureur);
    return this.http
      .put<IAssureur>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAssureur>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAssureur[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  getAllAssureursByCurrentUser(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAssureur[]>(`${this.resourceUrl}/getAllAssureursByCurrentUser`, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(assureur: IAssureur): IAssureur {
    const copy: IAssureur = Object.assign({}, assureur, {
      createdAt: assureur.createdAt && assureur.createdAt.isValid() ? assureur.createdAt.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createdAt = res.body.createdAt ? moment(res.body.createdAt) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((assureur: IAssureur) => {
        assureur.createdAt = assureur.createdAt ? moment(assureur.createdAt) : undefined;
      });
    }
    return res;
  }
}
