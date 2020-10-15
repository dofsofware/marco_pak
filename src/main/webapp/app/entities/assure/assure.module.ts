import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FirstCaringSharedModule } from 'app/shared/shared.module';
import { AssureComponent } from './assure.component';
import { AssureDetailComponent } from './assure-detail.component';
import { AssureUpdateComponent } from './assure-update.component';
import { AssureDeleteDialogComponent } from './assure-delete-dialog.component';
import { assureRoute } from './assure.route';

@NgModule({
  imports: [FirstCaringSharedModule, RouterModule.forChild(assureRoute)],
  declarations: [AssureComponent, AssureDetailComponent, AssureUpdateComponent, AssureDeleteDialogComponent],
  entryComponents: [AssureDeleteDialogComponent],
})
export class FirstCaringAssureModule {}
