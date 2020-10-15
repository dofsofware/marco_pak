import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAssureur } from 'app/shared/model/assureur.model';

@Component({
  selector: 'jhi-assureur-detail',
  templateUrl: './assureur-detail.component.html',
})
export class AssureurDetailComponent implements OnInit {
  assureur: IAssureur | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ assureur }) => (this.assureur = assureur));
  }

  previousState(): void {
    window.history.back();
  }
}
