import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'pack',
        loadChildren: () => import('./pack/pack.module').then(m => m.FirstCaringPackModule),
      },
      {
        path: 'assureur',
        loadChildren: () => import('./assureur/assureur.module').then(m => m.FirstCaringAssureurModule),
      },
      {
        path: 'assure',
        loadChildren: () => import('./assure/assure.module').then(m => m.FirstCaringAssureModule),
      },
      {
        path: 'ps',
        loadChildren: () => import('./ps/ps.module').then(m => m.FirstCaringPSModule),
      },
      {
        path: 'rendez-vous',
        loadChildren: () => import('./rendez-vous/rendez-vous.module').then(m => m.FirstCaringRendezVousModule),
      },
      {
        path: 'rapport-soignant',
        loadChildren: () => import('./rapport-soignant/rapport-soignant.module').then(m => m.FirstCaringRapportSoignantModule),
      },
      {
        path: 'rapport-pharmacien',
        loadChildren: () => import('./rapport-pharmacien/rapport-pharmacien.module').then(m => m.FirstCaringRapportPharmacienModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class FirstCaringEntityModule {}
