import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IRapportSoignant } from 'app/shared/model/rapport-soignant.model';
import { RapportSoignantService } from './rapport-soignant.service';

@Component({
  templateUrl: './rapport-soignant-delete-dialog.component.html',
})
export class RapportSoignantDeleteDialogComponent {
  rapportSoignant?: IRapportSoignant;

  constructor(
    protected rapportSoignantService: RapportSoignantService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.rapportSoignantService.delete(id).subscribe(() => {
      this.eventManager.broadcast('rapportSoignantListModification');
      this.activeModal.close();
    });
  }
}
