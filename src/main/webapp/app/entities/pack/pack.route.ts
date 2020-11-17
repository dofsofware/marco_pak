import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IPack, Pack } from 'app/shared/model/pack.model';
import { PackService } from './pack.service';
import { PackComponent } from './pack.component';
import { PackDetailComponent } from './pack-detail.component';
import { PackUpdateComponent } from './pack-update.component';

@Injectable({ providedIn: 'root' })
export class PackResolve implements Resolve<IPack> {
  constructor(private service: PackService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPack> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((pack: HttpResponse<Pack>) => {
          if (pack.body) {
            return of(pack.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Pack());
  }
}

export const packRoute: Routes = [
  {
    path: '',
    component: PackComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'firstCaringApp.pack.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PackDetailComponent,
    resolve: {
      pack: PackResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'firstCaringApp.pack.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PackUpdateComponent,
    resolve: {
      pack: PackResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'firstCaringApp.pack.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PackUpdateComponent,
    resolve: {
      pack: PackResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'firstCaringApp.pack.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
