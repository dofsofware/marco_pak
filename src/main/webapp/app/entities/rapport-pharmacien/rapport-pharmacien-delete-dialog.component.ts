import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IRapportPharmacien } from 'app/shared/model/rapport-pharmacien.model';
import { RapportPharmacienService } from './rapport-pharmacien.service';

@Component({
  templateUrl: './rapport-pharmacien-delete-dialog.component.html',
})
export class RapportPharmacienDeleteDialogComponent {
  rapportPharmacien?: IRapportPharmacien;

  constructor(
    protected rapportPharmacienService: RapportPharmacienService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.rapportPharmacienService.delete(id).subscribe(() => {
      this.eventManager.broadcast('rapportPharmacienListModification');
      this.activeModal.close();
    });
  }
}
