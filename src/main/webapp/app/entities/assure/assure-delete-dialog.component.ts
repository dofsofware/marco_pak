import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAssure } from 'app/shared/model/assure.model';
import { AssureService } from './assure.service';

@Component({
  templateUrl: './assure-delete-dialog.component.html',
})
export class AssureDeleteDialogComponent {
  assure?: IAssure;

  constructor(protected assureService: AssureService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.assureService.delete(id).subscribe(() => {
      this.eventManager.broadcast('assureListModification');
      this.activeModal.close();
    });
  }
}
