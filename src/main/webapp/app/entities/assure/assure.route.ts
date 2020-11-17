import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IAssure, Assure } from 'app/shared/model/assure.model';
import { AssureService } from './assure.service';
import { AssureComponent } from './assure.component';
import { AssureDetailComponent } from './assure-detail.component';
import { AssureUpdateComponent } from './assure-update.component';

@Injectable({ providedIn: 'root' })
export class AssureResolve implements Resolve<IAssure> {
  constructor(private service: AssureService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAssure> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((assure: HttpResponse<Assure>) => {
          if (assure.body) {
            return of(assure.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Assure());
  }
}

export const assureRoute: Routes = [
  {
    path: '',
    component: AssureComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'firstCaringApp.assure.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AssureDetailComponent,
    resolve: {
      assure: AssureResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'firstCaringApp.assure.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AssureUpdateComponent,
    resolve: {
      assure: AssureResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'firstCaringApp.assure.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AssureUpdateComponent,
    resolve: {
      assure: AssureResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'firstCaringApp.assure.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
