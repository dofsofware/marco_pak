import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAssure } from 'app/shared/model/assure.model';

@Component({
  selector: 'jhi-assure-detail',
  templateUrl: './assure-detail.component.html',
})
export class AssureDetailComponent implements OnInit {
  assure: IAssure | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ assure }) => (this.assure = assure));
  }

  previousState(): void {
    window.history.back();
  }
}
