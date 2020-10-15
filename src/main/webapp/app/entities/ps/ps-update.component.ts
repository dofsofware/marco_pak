import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IPS, PS } from 'app/shared/model/ps.model';
import { PSService } from './ps.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';
import { IAssure } from 'app/shared/model/assure.model';
import { AssureService } from 'app/entities/assure/assure.service';

type SelectableEntity = IUser | IAssure;

@Component({
  selector: 'jhi-ps-update',
  templateUrl: './ps-update.component.html',
})
export class PSUpdateComponent implements OnInit {
  isSaving = false;
  users: IUser[] = [];
  assures: IAssure[] = [];

  editForm = this.fb.group({
    id: [],
    codePS: [null, [Validators.required, Validators.minLength(4), Validators.maxLength(20)]],
    profil: [null, [Validators.required]],
    telephone: [null, [Validators.required]],
    createdAt: [null, [Validators.required]],
    urlPhoto: [],
    profession: [null, [Validators.required, Validators.minLength(4)]],
    experience: [null, [Validators.required, Validators.max(30)]],
    nomDeLetablissement: [null, [Validators.required, Validators.minLength(4), Validators.maxLength(200)]],
    telephoneDeLetablissement: [null, [Validators.required]],
    adresseDeLetablissement: [null, [Validators.required, Validators.minLength(4), Validators.maxLength(200)]],
    lienGoogleMapsDeLetablissement: [null, [Validators.minLength(100), Validators.maxLength(600)]],
    user: [],
    assures: [],
  });

  constructor(
    protected pSService: PSService,
    protected userService: UserService,
    protected assureService: AssureService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pS }) => {
      if (!pS.id) {
        const today = moment().startOf('day');
        pS.createdAt = today;
      }

      this.updateForm(pS);

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));

      this.assureService.query().subscribe((res: HttpResponse<IAssure[]>) => (this.assures = res.body || []));
    });
  }

  updateForm(pS: IPS): void {
    this.editForm.patchValue({
      id: pS.id,
      codePS: pS.codePS,
      profil: pS.profil,
      telephone: pS.telephone,
      createdAt: pS.createdAt ? pS.createdAt.format(DATE_TIME_FORMAT) : null,
      urlPhoto: pS.urlPhoto,
      profession: pS.profession,
      experience: pS.experience,
      nomDeLetablissement: pS.nomDeLetablissement,
      telephoneDeLetablissement: pS.telephoneDeLetablissement,
      adresseDeLetablissement: pS.adresseDeLetablissement,
      lienGoogleMapsDeLetablissement: pS.lienGoogleMapsDeLetablissement,
      user: pS.user,
      assures: pS.assures,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pS = this.createFromForm();
    if (pS.id !== undefined) {
      this.subscribeToSaveResponse(this.pSService.update(pS));
    } else {
      this.subscribeToSaveResponse(this.pSService.create(pS));
    }
  }

  private createFromForm(): IPS {
    return {
      ...new PS(),
      id: this.editForm.get(['id'])!.value,
      codePS: this.editForm.get(['codePS'])!.value,
      profil: this.editForm.get(['profil'])!.value,
      telephone: this.editForm.get(['telephone'])!.value,
      createdAt: this.editForm.get(['createdAt'])!.value ? moment(this.editForm.get(['createdAt'])!.value, DATE_TIME_FORMAT) : undefined,
      urlPhoto: this.editForm.get(['urlPhoto'])!.value,
      profession: this.editForm.get(['profession'])!.value,
      experience: this.editForm.get(['experience'])!.value,
      nomDeLetablissement: this.editForm.get(['nomDeLetablissement'])!.value,
      telephoneDeLetablissement: this.editForm.get(['telephoneDeLetablissement'])!.value,
      adresseDeLetablissement: this.editForm.get(['adresseDeLetablissement'])!.value,
      lienGoogleMapsDeLetablissement: this.editForm.get(['lienGoogleMapsDeLetablissement'])!.value,
      user: this.editForm.get(['user'])!.value,
      assures: this.editForm.get(['assures'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPS>>): void {
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
