import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { IRapportSoignant, RapportSoignant } from 'app/shared/model/rapport-soignant.model';
import { RapportSoignantService } from './rapport-soignant.service';
import { AlertError } from 'app/shared/alert/alert-error.model';

@Component({
  selector: 'jhi-rapport-soignant-update',
  templateUrl: './rapport-soignant-update.component.html',
})
export class RapportSoignantUpdateComponent implements OnInit {
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
    protected rapportSoignantService: RapportSoignantService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ rapportSoignant }) => {
      if (!rapportSoignant.id) {
        const today = moment().startOf('day');
        rapportSoignant.createdAt = today;
      }

      this.updateForm(rapportSoignant);
    });
  }

  updateForm(rapportSoignant: IRapportSoignant): void {
    this.editForm.patchValue({
      id: rapportSoignant.id,
      codePatient: rapportSoignant.codePatient,
      codePS: rapportSoignant.codePS,
      facturation: rapportSoignant.facturation,
      description: rapportSoignant.description,
      createdAt: rapportSoignant.createdAt ? rapportSoignant.createdAt.format(DATE_TIME_FORMAT) : null,
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
    const rapportSoignant = this.createFromForm();
    if (rapportSoignant.id !== undefined) {
      this.subscribeToSaveResponse(this.rapportSoignantService.update(rapportSoignant));
    } else {
      this.subscribeToSaveResponse(this.rapportSoignantService.create(rapportSoignant));
    }
  }

  private createFromForm(): IRapportSoignant {
    return {
      ...new RapportSoignant(),
      id: this.editForm.get(['id'])!.value,
      codePatient: this.editForm.get(['codePatient'])!.value,
      codePS: this.editForm.get(['codePS'])!.value,
      facturation: this.editForm.get(['facturation'])!.value,
      description: this.editForm.get(['description'])!.value,
      createdAt: this.editForm.get(['createdAt'])!.value ? moment(this.editForm.get(['createdAt'])!.value, DATE_TIME_FORMAT) : undefined,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRapportSoignant>>): void {
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
