import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IRendezVous, RendezVous } from 'app/shared/model/rendez-vous.model';
import { RendezVousService } from './rendez-vous.service';

@Component({
  selector: 'jhi-rendez-vous-update',
  templateUrl: './rendez-vous-update.component.html',
})
export class RendezVousUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    codePatient: [null, [Validators.required, Validators.minLength(4), Validators.maxLength(20)]],
    codePS: [null, [Validators.required, Validators.minLength(4), Validators.maxLength(20)]],
    debutRV: [null, [Validators.required]],
    finRV: [null, [Validators.required]],
    createdAt: [null, [Validators.required]],
  });

  constructor(protected rendezVousService: RendezVousService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ rendezVous }) => {
      if (!rendezVous.id) {
        const today = moment().startOf('day');
        rendezVous.debutRV = today;
        rendezVous.finRV = today;
        rendezVous.createdAt = today;
      }

      this.updateForm(rendezVous);
    });
  }

  updateForm(rendezVous: IRendezVous): void {
    this.editForm.patchValue({
      id: rendezVous.id,
      codePatient: rendezVous.codePatient,
      codePS: rendezVous.codePS,
      debutRV: rendezVous.debutRV ? rendezVous.debutRV.format(DATE_TIME_FORMAT) : null,
      finRV: rendezVous.finRV ? rendezVous.finRV.format(DATE_TIME_FORMAT) : null,
      createdAt: rendezVous.createdAt ? rendezVous.createdAt.format(DATE_TIME_FORMAT) : null,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const rendezVous = this.createFromForm();
    if (rendezVous.id !== undefined) {
      this.subscribeToSaveResponse(this.rendezVousService.update(rendezVous));
    } else {
      this.subscribeToSaveResponse(this.rendezVousService.create(rendezVous));
    }
  }

  private createFromForm(): IRendezVous {
    return {
      ...new RendezVous(),
      id: this.editForm.get(['id'])!.value,
      codePatient: this.editForm.get(['codePatient'])!.value,
      codePS: this.editForm.get(['codePS'])!.value,
      debutRV: this.editForm.get(['debutRV'])!.value ? moment(this.editForm.get(['debutRV'])!.value, DATE_TIME_FORMAT) : undefined,
      finRV: this.editForm.get(['finRV'])!.value ? moment(this.editForm.get(['finRV'])!.value, DATE_TIME_FORMAT) : undefined,
      createdAt: this.editForm.get(['createdAt'])!.value ? moment(this.editForm.get(['createdAt'])!.value, DATE_TIME_FORMAT) : undefined,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRendezVous>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
