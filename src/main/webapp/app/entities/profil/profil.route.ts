import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IProfil, Profil } from 'app/shared/model/profil.model';
import { ProfilService } from './profil.service';
import { ProfilComponent } from './profil.component';

@Injectable({ providedIn: 'root' })
export class ProfilResolve implements Resolve<IProfil> {
  constructor(private service: ProfilService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IProfil> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((profil: HttpResponse<Profil>) => {
          if (profil.body) {
            return of(profil.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Profil());
  }
}

export const profilRoute: Routes = [
  {
    path: '',
    component: ProfilComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'firstCaringApp.profil.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
