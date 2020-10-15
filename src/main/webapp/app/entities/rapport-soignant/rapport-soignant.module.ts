import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FirstCaringSharedModule } from 'app/shared/shared.module';
import { RapportSoignantComponent } from './rapport-soignant.component';
import { RapportSoignantDetailComponent } from './rapport-soignant-detail.component';
import { RapportSoignantUpdateComponent } from './rapport-soignant-update.component';
import { RapportSoignantDeleteDialogComponent } from './rapport-soignant-delete-dialog.component';
import { rapportSoignantRoute } from './rapport-soignant.route';

@NgModule({
  imports: [FirstCaringSharedModule, RouterModule.forChild(rapportSoignantRoute)],
  declarations: [
    RapportSoignantComponent,
    RapportSoignantDetailComponent,
    RapportSoignantUpdateComponent,
    RapportSoignantDeleteDialogComponent,
  ],
  entryComponents: [RapportSoignantDeleteDialogComponent],
})
export class FirstCaringRapportSoignantModule {}
