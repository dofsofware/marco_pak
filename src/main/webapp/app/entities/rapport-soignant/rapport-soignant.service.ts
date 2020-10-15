import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IRapportSoignant } from 'app/shared/model/rapport-soignant.model';

type EntityResponseType = HttpResponse<IRapportSoignant>;
type EntityArrayResponseType = HttpResponse<IRapportSoignant[]>;

@Injectable({ providedIn: 'root' })
export class RapportSoignantService {
  public resourceUrl = SERVER_API_URL + 'api/rapport-soignants';

  constructor(protected http: HttpClient) {}

  create(rapportSoignant: IRapportSoignant): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(rapportSoignant);
    return this.http
      .post<IRapportSoignant>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(rapportSoignant: IRapportSoignant): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(rapportSoignant);
    return this.http
      .put<IRapportSoignant>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IRapportSoignant>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRapportSoignant[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(rapportSoignant: IRapportSoignant): IRapportSoignant {
    const copy: IRapportSoignant = Object.assign({}, rapportSoignant, {
      createdAt: rapportSoignant.createdAt && rapportSoignant.createdAt.isValid() ? rapportSoignant.createdAt.toJSON() : undefined,
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
      res.body.forEach((rapportSoignant: IRapportSoignant) => {
        rapportSoignant.createdAt = rapportSoignant.createdAt ? moment(rapportSoignant.createdAt) : undefined;
      });
    }
    return res;
  }
}
