import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IPS } from 'app/shared/model/ps.model';

type EntityResponseType = HttpResponse<IPS>;
type EntityArrayResponseType = HttpResponse<IPS[]>;

@Injectable({ providedIn: 'root' })
export class PSService {
  public resourceUrl = SERVER_API_URL + 'api/ps';

  constructor(protected http: HttpClient) {}

  create(pS: IPS): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pS);
    return this.http
      .post<IPS>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(pS: IPS): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pS);
    return this.http
      .put<IPS>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPS>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPS[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(pS: IPS): IPS {
    const copy: IPS = Object.assign({}, pS, {
      createdAt: pS.createdAt && pS.createdAt.isValid() ? pS.createdAt.toJSON() : undefined,
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
      res.body.forEach((pS: IPS) => {
        pS.createdAt = pS.createdAt ? moment(pS.createdAt) : undefined;
      });
    }
    return res;
  }
}
