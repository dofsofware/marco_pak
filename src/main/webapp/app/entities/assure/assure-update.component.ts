import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IAssure, Assure } from 'app/shared/model/assure.model';
import { AssureService } from './assure.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';
import { IAssureur } from 'app/shared/model/assureur.model';
import { AssureurService } from 'app/entities/assureur/assureur.service';
import { IPack } from 'app/shared/model/pack.model';
import { PackService } from 'app/entities/pack/pack.service';

type SelectableEntity = IUser | IAssureur | IPack;

@Component({
  selector: 'jhi-assure-update',
  templateUrl: './assure-update.component.html',
})
export class AssureUpdateComponent implements OnInit {
  isSaving = false;
  users: IUser[] = [];
  assureurs: IAssureur[] = [];
  packs: IPack[] = [];

  editForm = this.fb.group({
    id: [],
    codeAssure: [null, [Validators.required, Validators.minLength(4), Validators.maxLength(20)]],
    profil: [null, [Validators.required]],
    telephone: [null, [Validators.required, Validators.minLength(7)]],
    createdAt: [null, [Validators.required]],
    urlPhoto: [],
    adresse: [null, [Validators.required, Validators.minLength(4), Validators.maxLength(200)]],
    user: [],
    assureur: [],
    pack: [],
  });

  constructor(
    protected assureService: AssureService,
    protected userService: UserService,
    protected assureurService: AssureurService,
    protected packService: PackService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ assure }) => {
      if (!assure.id) {
        const today = moment().startOf('day');
        assure.createdAt = today;
      }

      this.updateForm(assure);

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));

      this.assureurService.query().subscribe((res: HttpResponse<IAssureur[]>) => (this.assureurs = res.body || []));

      this.packService.query().subscribe((res: HttpResponse<IPack[]>) => (this.packs = res.body || []));
    });
  }

  updateForm(assure: IAssure): void {
    this.editForm.patchValue({
      id: assure.id,
      codeAssure: assure.codeAssure,
      profil: assure.profil,
      telephone: assure.telephone,
      createdAt: assure.createdAt ? assure.createdAt.format(DATE_TIME_FORMAT) : null,
      urlPhoto: assure.urlPhoto,
      adresse: assure.adresse,
      user: assure.user,
      assureur: assure.assureur,
      pack: assure.pack,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const assure = this.createFromForm();
    if (assure.id !== undefined) {
      this.subscribeToSaveResponse(this.assureService.update(assure));
    } else {
      this.subscribeToSaveResponse(this.assureService.create(assure));
    }
  }

  private createFromForm(): IAssure {
    return {
      ...new Assure(),
      id: this.editForm.get(['id'])!.value,
      codeAssure: this.editForm.get(['codeAssure'])!.value,
      profil: this.editForm.get(['profil'])!.value,
      telephone: this.editForm.get(['telephone'])!.value,
      createdAt: this.editForm.get(['createdAt'])!.value ? moment(this.editForm.get(['createdAt'])!.value, DATE_TIME_FORMAT) : undefined,
      urlPhoto: this.editForm.get(['urlPhoto'])!.value,
      adresse: this.editForm.get(['adresse'])!.value,
      user: this.editForm.get(['user'])!.value,
      assureur: this.editForm.get(['assureur'])!.value,
      pack: this.editForm.get(['pack'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAssure>>): void {
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
