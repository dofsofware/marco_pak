import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IRapportPharmacien } from 'app/shared/model/rapport-pharmacien.model';

type EntityResponseType = HttpResponse<IRapportPharmacien>;
type EntityArrayResponseType = HttpResponse<IRapportPharmacien[]>;

@Injectable({ providedIn: 'root' })
export class RapportPharmacienService {
  public resourceUrl = SERVER_API_URL + 'api/rapport-pharmaciens';

  constructor(protected http: HttpClient) {}

  create(rapportPharmacien: IRapportPharmacien): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(rapportPharmacien);
    return this.http
      .post<IRapportPharmacien>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(rapportPharmacien: IRapportPharmacien): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(rapportPharmacien);
    return this.http
      .put<IRapportPharmacien>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IRapportPharmacien>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRapportPharmacien[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(rapportPharmacien: IRapportPharmacien): IRapportPharmacien {
    const copy: IRapportPharmacien = Object.assign({}, rapportPharmacien, {
      createdAt: rapportPharmacien.createdAt && rapportPharmacien.createdAt.isValid() ? rapportPharmacien.createdAt.toJSON() : undefined,
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
      res.body.forEach((rapportPharmacien: IRapportPharmacien) => {
        rapportPharmacien.createdAt = rapportPharmacien.createdAt ? moment(rapportPharmacien.createdAt) : undefined;
      });
    }
    return res;
  }
}
