import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IProfil } from 'app/shared/model/profil.model';
import { ProfilService } from './profil.service';

@Component({
  templateUrl: './profil-delete-dialog.component.html',
})
export class ProfilDeleteDialogComponent {
  profil?: IProfil;

  constructor(protected profilService: ProfilService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.profilService.delete(id).subscribe(() => {
      this.eventManager.broadcast('profilListModification');
      this.activeModal.close();
    });
  }
}
