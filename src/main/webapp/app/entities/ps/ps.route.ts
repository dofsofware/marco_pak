import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IPS, PS } from 'app/shared/model/ps.model';
import { PSService } from './ps.service';
import { PSComponent } from './ps.component';
import { PSDetailComponent } from './ps-detail.component';
import { PSUpdateComponent } from './ps-update.component';

@Injectable({ providedIn: 'root' })
export class PSResolve implements Resolve<IPS> {
  constructor(private service: PSService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPS> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((pS: HttpResponse<PS>) => {
          if (pS.body) {
            return of(pS.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PS());
  }
}

export const pSRoute: Routes = [
  {
    path: '',
    component: PSComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'firstCaringApp.pS.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PSDetailComponent,
    resolve: {
      pS: PSResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'firstCaringApp.pS.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PSUpdateComponent,
    resolve: {
      pS: PSResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'firstCaringApp.pS.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PSUpdateComponent,
    resolve: {
      pS: PSResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'firstCaringApp.pS.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
