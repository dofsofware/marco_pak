import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPS } from 'app/shared/model/ps.model';
import { PSService } from './ps.service';

@Component({
  templateUrl: './ps-delete-dialog.component.html',
})
export class PSDeleteDialogComponent {
  pS?: IPS;

  constructor(protected pSService: PSService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.pSService.delete(id).subscribe(() => {
      this.eventManager.broadcast('pSListModification');
      this.activeModal.close();
    });
  }
}
