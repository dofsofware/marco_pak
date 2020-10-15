import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAssureur } from 'app/shared/model/assureur.model';
import { AssureurService } from './assureur.service';

@Component({
  templateUrl: './assureur-delete-dialog.component.html',
})
export class AssureurDeleteDialogComponent {
  assureur?: IAssureur;

  constructor(protected assureurService: AssureurService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.assureurService.delete(id).subscribe(() => {
      this.eventManager.broadcast('assureurListModification');
      this.activeModal.close();
    });
  }
}
