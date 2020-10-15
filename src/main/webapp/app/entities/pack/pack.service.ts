import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IPack } from 'app/shared/model/pack.model';

type EntityResponseType = HttpResponse<IPack>;
type EntityArrayResponseType = HttpResponse<IPack[]>;

@Injectable({ providedIn: 'root' })
export class PackService {
  public resourceUrl = SERVER_API_URL + 'api/packs';

  constructor(protected http: HttpClient) {}

  create(pack: IPack): Observable<EntityResponseType> {
    return this.http.post<IPack>(this.resourceUrl, pack, { observe: 'response' });
  }

  update(pack: IPack): Observable<EntityResponseType> {
    return this.http.put<IPack>(this.resourceUrl, pack, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPack>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPack[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
