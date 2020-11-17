import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IRapportSoignant, RapportSoignant } from 'app/shared/model/rapport-soignant.model';
import { RapportSoignantService } from './rapport-soignant.service';
import { RapportSoignantComponent } from './rapport-soignant.component';
import { RapportSoignantDetailComponent } from './rapport-soignant-detail.component';
import { RapportSoignantUpdateComponent } from './rapport-soignant-update.component';

@Injectable({ providedIn: 'root' })
export class RapportSoignantResolve implements Resolve<IRapportSoignant> {
  constructor(private service: RapportSoignantService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRapportSoignant> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((rapportSoignant: HttpResponse<RapportSoignant>) => {
          if (rapportSoignant.body) {
            return of(rapportSoignant.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new RapportSoignant());
  }
}

export const rapportSoignantRoute: Routes = [
  {
    path: '',
    component: RapportSoignantComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'firstCaringApp.rapportSoignant.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RapportSoignantDetailComponent,
    resolve: {
      rapportSoignant: RapportSoignantResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'firstCaringApp.rapportSoignant.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RapportSoignantUpdateComponent,
    resolve: {
      rapportSoignant: RapportSoignantResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'firstCaringApp.rapportSoignant.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RapportSoignantUpdateComponent,
    resolve: {
      rapportSoignant: RapportSoignantResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'firstCaringApp.rapportSoignant.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
