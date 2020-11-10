import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FirstCaringSharedModule } from 'app/shared/shared.module';
import { PackComponent } from './pack.component';
import { PackDetailComponent } from './pack-detail.component';
import { PackUpdateComponent } from './pack-update.component';
import { PackDeleteDialogComponent } from './pack-delete-dialog.component';
import { packRoute } from './pack.route';

@NgModule({
  imports: [FirstCaringSharedModule, RouterModule.forChild(packRoute)],
  declarations: [PackComponent, PackDetailComponent, PackUpdateComponent, PackDeleteDialogComponent],
  entryComponents: [PackDeleteDialogComponent],
})
export class FirstCaringPackModule {}
