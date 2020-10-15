import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IRapportSoignant } from 'app/shared/model/rapport-soignant.model';

@Component({
  selector: 'jhi-rapport-soignant-detail',
  templateUrl: './rapport-soignant-detail.component.html',
})
export class RapportSoignantDetailComponent implements OnInit {
  rapportSoignant: IRapportSoignant | null = null;

  constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ rapportSoignant }) => (this.rapportSoignant = rapportSoignant));
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
