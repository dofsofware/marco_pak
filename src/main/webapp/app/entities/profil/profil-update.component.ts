import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IProfil, Profil } from 'app/shared/model/profil.model';
import { ProfilService } from './profil.service';

@Component({
  selector: 'jhi-profil-update',
  templateUrl: './profil-update.component.html',
})
export class ProfilUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
  });

  constructor(protected profilService: ProfilService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ profil }) => {
      this.updateForm(profil);
    });
  }

  updateForm(profil: IProfil): void {
    this.editForm.patchValue({
      id: profil.id,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const profil = this.createFromForm();
    if (profil.id !== undefined) {
      this.subscribeToSaveResponse(this.profilService.update(profil));
    } else {
      this.subscribeToSaveResponse(this.profilService.create(profil));
    }
  }

  private createFromForm(): IProfil {
    return {
      ...new Profil(),
      id: this.editForm.get(['id'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProfil>>): void {
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
