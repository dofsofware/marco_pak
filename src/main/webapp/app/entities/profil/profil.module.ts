import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FirstCaringSharedModule } from 'app/shared/shared.module';
import { ProfilComponent } from './profil.component';
import { profilRoute } from './profil.route';

@NgModule({
  imports: [FirstCaringSharedModule, RouterModule.forChild(profilRoute)],
  declarations: [ProfilComponent],
  entryComponents: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class FirstCaringProfilModule {}
