import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { IRapportPharmacien, RapportPharmacien } from 'app/shared/model/rapport-pharmacien.model';
import { RapportPharmacienService } from './rapport-pharmacien.service';
import { AlertError } from 'app/shared/alert/alert-error.model';

@Component({
  selector: 'jhi-rapport-pharmacien-update',
  templateUrl: './rapport-pharmacien-update.component.html',
})
export class RapportPharmacienUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    codePatient: [null, [Validators.required, Validators.minLength(4), Validators.maxLength(20)]],
    codePS: [null, [Validators.required, Validators.minLength(4), Validators.maxLength(20)]],
    facturation: [null, [Validators.required]],
    description: [],
    createdAt: [null, [Validators.required]],
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected rapportPharmacienService: RapportPharmacienService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ rapportPharmacien }) => {
      if (!rapportPharmacien.id) {
        const today = moment().startOf('day');
        rapportPharmacien.createdAt = today;
      }

      this.updateForm(rapportPharmacien);
    });
  }

  updateForm(rapportPharmacien: IRapportPharmacien): void {
    this.editForm.patchValue({
      id: rapportPharmacien.id,
      codePatient: rapportPharmacien.codePatient,
      codePS: rapportPharmacien.codePS,
      facturation: rapportPharmacien.facturation,
      description: rapportPharmacien.description,
      createdAt: rapportPharmacien.createdAt ? rapportPharmacien.createdAt.format(DATE_TIME_FORMAT) : null,
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType: string, base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  setFileData(event: any, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe(null, (err: JhiFileLoadError) => {
      this.eventManager.broadcast(
        new JhiEventWithContent<AlertError>('firstCaringApp.error', { ...err, key: 'error.file.' + err.key })
      );
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const rapportPharmacien = this.createFromForm();
    if (rapportPharmacien.id !== undefined) {
      this.subscribeToSaveResponse(this.rapportPharmacienService.update(rapportPharmacien));
    } else {
      this.subscribeToSaveResponse(this.rapportPharmacienService.create(rapportPharmacien));
    }
  }

  private createFromForm(): IRapportPharmacien {
    return {
      ...new RapportPharmacien(),
      id: this.editForm.get(['id'])!.value,
      codePatient: this.editForm.get(['codePatient'])!.value,
      codePS: this.editForm.get(['codePS'])!.value,
      facturation: this.editForm.get(['facturation'])!.value,
      description: this.editForm.get(['description'])!.value,
      createdAt: this.editForm.get(['createdAt'])!.value ? moment(this.editForm.get(['createdAt'])!.value, DATE_TIME_FORMAT) : undefined,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRapportPharmacien>>): void {
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
