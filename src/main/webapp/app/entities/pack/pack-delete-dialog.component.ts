import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPack } from 'app/shared/model/pack.model';
import { PackService } from './pack.service';

@Component({
  templateUrl: './pack-delete-dialog.component.html',
})
export class PackDeleteDialogComponent {
  pack?: IPack;

  constructor(protected packService: PackService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.packService.delete(id).subscribe(() => {
      this.eventManager.broadcast('packListModification');
      this.activeModal.close();
    });
  }
}
