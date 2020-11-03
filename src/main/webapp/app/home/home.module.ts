import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { FirstCaringSharedModule } from 'app/shared/shared.module';
import { HOME_ROUTE } from './home.route';
import { HomeComponent } from './home.component';
import { PackComponent } from 'app/entities/pack/pack.component';
// import { FirstCaringPackModule } from 'app/entities/pack/pack.module';

@NgModule({
  imports: [FirstCaringSharedModule, RouterModule.forChild([HOME_ROUTE])],
  declarations: [HomeComponent],
  entryComponents: [PackComponent],
})
export class FirstCaringHomeModule {}
