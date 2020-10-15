import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IAssureur, Assureur } from 'app/shared/model/assureur.model';
import { AssureurService } from './assureur.service';
import { AssureurComponent } from './assureur.component';
import { AssureurDetailComponent } from './assureur-detail.component';
import { AssureurUpdateComponent } from './assureur-update.component';

@Injectable({ providedIn: 'root' })
export class AssureurResolve implements Resolve<IAssureur> {
  constructor(private service: AssureurService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAssureur> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((assureur: HttpResponse<Assureur>) => {
          if (assureur.body) {
            return of(assureur.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Assureur());
  }
}

export const assureurRoute: Routes = [
  {
    path: '',
    component: AssureurComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'firstCaringApp.assureur.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AssureurDetailComponent,
    resolve: {
      assureur: AssureurResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'firstCaringApp.assureur.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AssureurUpdateComponent,
    resolve: {
      assureur: AssureurResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'firstCaringApp.assureur.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AssureurUpdateComponent,
    resolve: {
      assureur: AssureurResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'firstCaringApp.assureur.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
