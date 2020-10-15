import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { IPack, Pack } from 'app/shared/model/pack.model';
import { PackService } from './pack.service';
import { AlertError } from 'app/shared/alert/alert-error.model';

@Component({
  selector: 'jhi-pack-update',
  templateUrl: './pack-update.component.html',
})
export class PackUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    denommination: [null, [Validators.required]],
    prix: [null, [Validators.required]],
    description: [],
    nmbDePers: [null, [Validators.required]],
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected packService: PackService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pack }) => {
      this.updateForm(pack);
    });
  }

  updateForm(pack: IPack): void {
    this.editForm.patchValue({
      id: pack.id,
      denommination: pack.denommination,
      prix: pack.prix,
      description: pack.description,
      nmbDePers: pack.nmbDePers,
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
    const pack = this.createFromForm();
    if (pack.id !== undefined) {
      this.subscribeToSaveResponse(this.packService.update(pack));
    } else {
      this.subscribeToSaveResponse(this.packService.create(pack));
    }
  }

  private createFromForm(): IPack {
    return {
      ...new Pack(),
      id: this.editForm.get(['id'])!.value,
      denommination: this.editForm.get(['denommination'])!.value,
      prix: this.editForm.get(['prix'])!.value,
      description: this.editForm.get(['description'])!.value,
      nmbDePers: this.editForm.get(['nmbDePers'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPack>>): void {
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
