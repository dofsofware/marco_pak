import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IFacture, Facture } from 'app/shared/model/facture.model';
import { FactureService } from './facture.service';
import { IPS } from 'app/shared/model/ps.model';
import { PSService } from 'app/entities/ps/ps.service';
import { IAssureur } from 'app/shared/model/assureur.model';
import { AssureurService } from 'app/entities/assureur/assureur.service';
import { IAssure } from 'app/shared/model/assure.model';
import { AssureService } from 'app/entities/assure/assure.service';

type SelectableEntity = IPS | IAssureur | IAssure;

@Component({
  selector: 'jhi-facture-update',
  templateUrl: './facture-update.component.html',
})
export class FactureUpdateComponent implements OnInit {
  isSaving = false;
  ps: IPS[] = [];
  assureurs: IAssureur[] = [];
  assures: IAssure[] = [];

  editForm = this.fb.group({
    id: [],
    date: [null, [Validators.required]],
    prix: [null, [Validators.required]],
    ps: [],
    assureur: [],
    assure: [],
  });

  constructor(
    protected factureService: FactureService,
    protected pSService: PSService,
    protected assureurService: AssureurService,
    protected assureService: AssureService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ facture }) => {
      if (!facture.id) {
        const today = moment().startOf('day');
        facture.date = today;
      }

      this.updateForm(facture);

      this.pSService.query().subscribe((res: HttpResponse<IPS[]>) => (this.ps = res.body || []));

      this.assureurService.query().subscribe((res: HttpResponse<IAssureur[]>) => (this.assureurs = res.body || []));

      this.assureService.query().subscribe((res: HttpResponse<IAssure[]>) => (this.assures = res.body || []));
    });
  }

  updateForm(facture: IFacture): void {
    this.editForm.patchValue({
      id: facture.id,
      date: facture.date ? facture.date.format(DATE_TIME_FORMAT) : null,
      prix: facture.prix,
      ps: facture.ps,
      assureur: facture.assureur,
      assure: facture.assure,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const facture = this.createFromForm();
    if (facture.id !== undefined) {
      this.subscribeToSaveResponse(this.factureService.update(facture));
    } else {
      this.subscribeToSaveResponse(this.factureService.create(facture));
    }
  }

  private createFromForm(): IFacture {
    return {
      ...new Facture(),
      id: this.editForm.get(['id'])!.value,
      date: this.editForm.get(['date'])!.value ? moment(this.editForm.get(['date'])!.value, DATE_TIME_FORMAT) : undefined,
      prix: this.editForm.get(['prix'])!.value,
      ps: this.editForm.get(['ps'])!.value,
      assureur: this.editForm.get(['assureur'])!.value,
      assure: this.editForm.get(['assure'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFacture>>): void {
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

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
