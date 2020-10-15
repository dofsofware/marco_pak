import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IAssureur, Assureur } from 'app/shared/model/assureur.model';
import { AssureurService } from './assureur.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';
import { IAssure } from 'app/shared/model/assure.model';
import { AssureService } from 'app/entities/assure/assure.service';

type SelectableEntity = IUser | IAssure;

@Component({
  selector: 'jhi-assureur-update',
  templateUrl: './assureur-update.component.html',
})
export class AssureurUpdateComponent implements OnInit {
  isSaving = false;
  users: IUser[] = [];
  assures: IAssure[] = [];

  editForm = this.fb.group({
    id: [],
    codeAssureur: [null, [Validators.required, Validators.minLength(4), Validators.maxLength(20)]],
    profil: [null, [Validators.required]],
    telephone: [null, [Validators.required]],
    createdAt: [null, [Validators.required]],
    urlPhoto: [],
    adresse: [null, [Validators.required, Validators.minLength(4), Validators.maxLength(200)]],
    user: [],
    assures: [],
  });

  constructor(
    protected assureurService: AssureurService,
    protected userService: UserService,
    protected assureService: AssureService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ assureur }) => {
      if (!assureur.id) {
        const today = moment().startOf('day');
        assureur.createdAt = today;
      }

      this.updateForm(assureur);

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));

      this.assureService.query().subscribe((res: HttpResponse<IAssure[]>) => (this.assures = res.body || []));
    });
  }

  updateForm(assureur: IAssureur): void {
    this.editForm.patchValue({
      id: assureur.id,
      codeAssureur: assureur.codeAssureur,
      profil: assureur.profil,
      telephone: assureur.telephone,
      createdAt: assureur.createdAt ? assureur.createdAt.format(DATE_TIME_FORMAT) : null,
      urlPhoto: assureur.urlPhoto,
      adresse: assureur.adresse,
      user: assureur.user,
      assures: assureur.assures,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const assureur = this.createFromForm();
    if (assureur.id !== undefined) {
      this.subscribeToSaveResponse(this.assureurService.update(assureur));
    } else {
      this.subscribeToSaveResponse(this.assureurService.create(assureur));
    }
  }

  private createFromForm(): IAssureur {
    return {
      ...new Assureur(),
      id: this.editForm.get(['id'])!.value,
      codeAssureur: this.editForm.get(['codeAssureur'])!.value,
      profil: this.editForm.get(['profil'])!.value,
      telephone: this.editForm.get(['telephone'])!.value,
      createdAt: this.editForm.get(['createdAt'])!.value ? moment(this.editForm.get(['createdAt'])!.value, DATE_TIME_FORMAT) : undefined,
      urlPhoto: this.editForm.get(['urlPhoto'])!.value,
      adresse: this.editForm.get(['adresse'])!.value,
      user: this.editForm.get(['user'])!.value,
      assures: this.editForm.get(['assures'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAssureur>>): void {
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

  getSelected(selectedVals: IAssure[], option: IAssure): IAssure {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
