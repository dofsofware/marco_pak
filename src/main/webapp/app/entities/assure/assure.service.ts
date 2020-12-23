import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IAssure } from 'app/shared/model/assure.model';

type EntityResponseType = HttpResponse<IAssure>;
type EntityArrayResponseType = HttpResponse<IAssure[]>;

@Injectable({ providedIn: 'root' })
export class AssureService {
  public resourceUrl = SERVER_API_URL + 'api/assures';

  constructor(protected http: HttpClient) {}

  create(assure: IAssure): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(assure);
    return this.http
      .post<IAssure>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(assure: IAssure): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(assure);
    return this.http
      .put<IAssure>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAssure>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  findAllByIdAssureur(id: number): Observable<EntityArrayResponseType> {
    return this.http
      .get<IAssure[]>(`${this.resourceUrl}/findAllByIdAssureur/${id}`, { observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  findAllByCode(codeAssure: string): Observable<EntityArrayResponseType> {
    return this.http
      .get<IAssure[]>(`${this.resourceUrl}/getAllAssuresByCode/${codeAssure}`, { observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAssure[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  getAllAssuresByCurrentUser(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAssure[]>(`${this.resourceUrl}/getAllAssuresByCurrentUser`, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(assure: IAssure): IAssure {
    const copy: IAssure = Object.assign({}, assure, {
      createdAt: assure.createdAt && assure.createdAt.isValid() ? assure.createdAt.toJSON() : undefined,
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
      res.body.forEach((assure: IAssure) => {
        assure.createdAt = assure.createdAt ? moment(assure.createdAt) : undefined;
      });
    }
    return res;
  }
}
