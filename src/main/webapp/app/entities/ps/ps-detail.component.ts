import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPS } from 'app/shared/model/ps.model';

@Component({
  selector: 'jhi-ps-detail',
  templateUrl: './ps-detail.component.html',
})
export class PSDetailComponent implements OnInit {
  pS: IPS | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pS }) => (this.pS = pS));
  }

  previousState(): void {
    window.history.back();
  }
}
