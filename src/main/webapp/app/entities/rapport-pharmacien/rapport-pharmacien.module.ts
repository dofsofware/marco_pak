import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FirstCaringSharedModule } from 'app/shared/shared.module';
import { RapportPharmacienComponent } from './rapport-pharmacien.component';
import { RapportPharmacienDetailComponent } from './rapport-pharmacien-detail.component';
import { RapportPharmacienUpdateComponent } from './rapport-pharmacien-update.component';
import { RapportPharmacienDeleteDialogComponent } from './rapport-pharmacien-delete-dialog.component';
import { rapportPharmacienRoute } from './rapport-pharmacien.route';

@NgModule({
  imports: [FirstCaringSharedModule, RouterModule.forChild(rapportPharmacienRoute)],
  declarations: [
    RapportPharmacienComponent,
    RapportPharmacienDetailComponent,
    RapportPharmacienUpdateComponent,
    RapportPharmacienDeleteDialogComponent,
  ],
  entryComponents: [RapportPharmacienDeleteDialogComponent],
})
export class FirstCaringRapportPharmacienModule {}
