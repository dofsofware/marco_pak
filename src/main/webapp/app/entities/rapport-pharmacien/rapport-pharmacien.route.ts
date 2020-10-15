import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IRapportPharmacien, RapportPharmacien } from 'app/shared/model/rapport-pharmacien.model';
import { RapportPharmacienService } from './rapport-pharmacien.service';
import { RapportPharmacienComponent } from './rapport-pharmacien.component';
import { RapportPharmacienDetailComponent } from './rapport-pharmacien-detail.component';
import { RapportPharmacienUpdateComponent } from './rapport-pharmacien-update.component';

@Injectable({ providedIn: 'root' })
export class RapportPharmacienResolve implements Resolve<IRapportPharmacien> {
  constructor(private service: RapportPharmacienService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRapportPharmacien> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((rapportPharmacien: HttpResponse<RapportPharmacien>) => {
          if (rapportPharmacien.body) {
            return of(rapportPharmacien.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new RapportPharmacien());
  }
}

export const rapportPharmacienRoute: Routes = [
  {
    path: '',
    component: RapportPharmacienComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'firstCaringApp.rapportPharmacien.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RapportPharmacienDetailComponent,
    resolve: {
      rapportPharmacien: RapportPharmacienResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'firstCaringApp.rapportPharmacien.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RapportPharmacienUpdateComponent,
    resolve: {
      rapportPharmacien: RapportPharmacienResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'firstCaringApp.rapportPharmacien.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RapportPharmacienUpdateComponent,
    resolve: {
      rapportPharmacien: RapportPharmacienResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'firstCaringApp.rapportPharmacien.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
