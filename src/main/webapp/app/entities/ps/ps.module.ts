import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FirstCaringSharedModule } from 'app/shared/shared.module';
import { PSComponent } from './ps.component';
import { PSDetailComponent } from './ps-detail.component';
import { PSUpdateComponent } from './ps-update.component';
import { PSDeleteDialogComponent } from './ps-delete-dialog.component';
import { pSRoute } from './ps.route';

@NgModule({
  imports: [FirstCaringSharedModule, RouterModule.forChild(pSRoute)],
  declarations: [PSComponent, PSDetailComponent, PSUpdateComponent, PSDeleteDialogComponent],
  entryComponents: [PSDeleteDialogComponent],
})
export class FirstCaringPSModule {}
