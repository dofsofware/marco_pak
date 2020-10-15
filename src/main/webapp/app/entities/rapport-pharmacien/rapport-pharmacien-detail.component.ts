import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IRapportPharmacien } from 'app/shared/model/rapport-pharmacien.model';

@Component({
  selector: 'jhi-rapport-pharmacien-detail',
  templateUrl: './rapport-pharmacien-detail.component.html',
})
export class RapportPharmacienDetailComponent implements OnInit {
  rapportPharmacien: IRapportPharmacien | null = null;

  constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ rapportPharmacien }) => (this.rapportPharmacien = rapportPharmacien));
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType = '', base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  previousState(): void {
    window.history.back();
  }
}
