import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FirstCaringSharedModule } from 'app/shared/shared.module';
import { AssureurComponent } from './assureur.component';
import { AssureurDetailComponent } from './assureur-detail.component';
import { AssureurUpdateComponent } from './assureur-update.component';
import { AssureurDeleteDialogComponent } from './assureur-delete-dialog.component';
import { assureurRoute } from './assureur.route';

@NgModule({
  imports: [FirstCaringSharedModule, RouterModule.forChild(assureurRoute)],
  declarations: [AssureurComponent, AssureurDetailComponent, AssureurUpdateComponent, AssureurDeleteDialogComponent],
  entryComponents: [AssureurDeleteDialogComponent],
})
export class FirstCaringAssureurModule {}
