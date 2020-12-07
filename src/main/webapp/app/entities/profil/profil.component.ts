import { Component, OnInit, OnDestroy } from '@angular/core';
import { JhiEventManager, JhiLanguageService, JhiParseLinks } from 'ng-jhipster';
import { LANGUAGES } from 'app/core/language/language.constants';
import { AbstractControl, FormBuilder, Validators } from '@angular/forms';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/user/account.model';
import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { FactureService } from '../facture/facture.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, Subscription } from 'rxjs';
import { IFacture } from 'app/shared/model/facture.model';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { FactureDeleteDialogComponent } from '../facture/facture-delete-dialog.component';
import { IUser } from 'app/core/user/user.model';
import { Assure, IAssure } from 'app/shared/model/assure.model';
import { ActivatedRoute, Router } from '@angular/router';
import { AssureService } from '../assure/assure.service';
import { UserService } from 'app/core/user/user.service';
import { AssureurService } from '../assureur/assureur.service';
import * as moment from 'moment';
import { Assureur, IAssureur } from 'app/shared/model/assureur.model';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { Profil } from 'app/shared/model/enumerations/profil.model';
import { IPack } from 'app/shared/model/pack.model';
import { PackService } from '../pack/pack.service';

type SelectableEntity = IUser | IAssure;

@Component({
  selector: 'jhi-profil',
  templateUrl: './profil.component.html',
})
export class ProfilComponent implements OnInit, OnDestroy {
  account!: Account;
  success = false;
  languages = LANGUAGES;

  // facture
  factures: IFacture[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  profil: string;
  ascending: boolean;

  // Assureur
  isSaving = false;
  users: IUser[] = [];
  assures: IAssure[] = [];

  // Assuré(e)
  assureurs: IAssureur[] = [];
  packs: IPack[] = [];

  editForm = this.fb.group({
    id: [],
    codeAssureur: [null, [Validators.required, Validators.minLength(4), Validators.maxLength(20), this.codeAssureurValidator.bind(this)]],
    profil: [],
    sexe: [null, [Validators.required]],
    telephone: [null, [Validators.required]],
    createdAt: [],
    urlPhoto: [],
    adresse: [null, [Validators.required, Validators.minLength(4), Validators.maxLength(200)]],
    user: [],
    assures: [],
  });

  editFormAssure = this.fb.group({
    id: [],
    codeAssure: [null, [Validators.required, Validators.minLength(4), Validators.maxLength(20), this.codeAssureValidator.bind(this)]],
    profil: [],
    sexe: [null, [Validators.required]],
    telephone: [null, [Validators.required, Validators.minLength(7)]],
    createdAt: [],
    urlPhoto: [],
    adresse: [null, [Validators.required, Validators.minLength(4), Validators.maxLength(200)]],
    user: [],
    assureur: [],
    pack: [],
  });

  settingsForm = this.fb.group({
    firstName: [undefined, [Validators.required, Validators.minLength(1), Validators.maxLength(50)]],
    lastName: [undefined, [Validators.required, Validators.minLength(1), Validators.maxLength(50)]],
    email: [undefined, [Validators.required, Validators.minLength(5), Validators.maxLength(254), Validators.email]],
    langKey: [undefined],
  });

  constructor(
    protected assureurService: AssureurService,
    protected userService: UserService,
    protected assureService: AssureService,
    protected activatedRoute: ActivatedRoute,
    protected packService: PackService,
    private accountService: AccountService,
    private fb: FormBuilder,
    private languageService: JhiLanguageService,
    protected factureService: FactureService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks,
    private router: Router
  ) {
    this.factures = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.profil = '';
    this.ascending = true;
  }
  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
    // throw new Error('Method not implemented.');
  }

  changeProfil(profil: string): void {
    this.profil = profil;
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInFactures();
    // this.activatedRoute.data.subscribe(({ assure }) => {
    //   if (!assure.id) {
    //     const today = moment().startOf('day');
    //     assure.createdAt = today;
    //   }

    //   this.updateForm(assure);

    //   this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));

    //   this.assureurService.query().subscribe((res: HttpResponse<IAssureur[]>) => (this.assureurs = res.body || []));

    //   this.packService.query().subscribe((res: HttpResponse<IPack[]>) => (this.packs = res.body || []));
    // });

    // this.activatedRoute.data.subscribe(({ assureur }) => {
    //   if (!assureur.id) {
    //     const today = moment().startOf('day');
    //     assureur.createdAt = today;
    //   }

    //   this.updateForm(assureur);

    //   this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));

    //   this.assureService.query().subscribe((res: HttpResponse<IAssure[]>) => (this.assures = res.body || []));
    // });
    this.accountService.identity().subscribe(account => {
      if (account) {
        this.settingsForm.patchValue({
          firstName: account.firstName,
          lastName: account.lastName,
          email: account.email,
          langKey: account.langKey,
        });

        this.account = account;
        this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));
        this.assureService.query().subscribe((res: HttpResponse<IAssure[]>) => (this.assures = res.body || []));
        this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));
        this.assureurService.query().subscribe((res: HttpResponse<IAssureur[]>) => (this.assureurs = res.body || []));
        this.packService.query().subscribe((res: HttpResponse<IPack[]>) => (this.packs = res.body || []));
      }
    });
  }

  updateFormAssure(assure: IAssure): void {
    this.editFormAssure.patchValue({
      id: assure.id,
      codeAssure: assure.codeAssure,
      profil: assure.profil,
      sexe: assure.sexe,
      telephone: assure.telephone,
      createdAt: assure.createdAt ? assure.createdAt.format(DATE_TIME_FORMAT) : null,
      urlPhoto: assure.urlPhoto,
      adresse: assure.adresse,
      user: assure.user,
      assureur: assure.assureur,
      pack: assure.pack,
    });
  }

  saveAssure(): void {
    this.isSaving = true;
    const assure = this.createFromFormAssure();
    if (assure.id !== undefined) {
      this.subscribeToSaveResponseAssure(this.assureService.update(assure));
    } else {
      this.subscribeToSaveResponseAssure(this.assureService.create(assure));
    }
  }

  private createFromFormAssure(): IAssure {
    return {
      ...new Assure(),
      id: undefined,
      codeAssure: this.editFormAssure.get(['codeAssure'])!.value,
      profil: Profil.ASSURE,
      sexe: this.editFormAssure.get(['sexe'])!.value,
      telephone: this.editFormAssure.get(['telephone'])!.value,
      createdAt: moment(moment().format(DATE_TIME_FORMAT)),
      urlPhoto: this.editFormAssure.get(['urlPhoto'])!.value,
      adresse: this.editFormAssure.get(['adresse'])!.value,
      user: this.editFormAssure.get(['user'])!.value,
      assureur: this.editFormAssure.get(['assureur'])!.value,
      pack: this.editFormAssure.get(['pack'])!.value,
    };
  }

  updateForm(assureur: IAssureur): void {
    this.editForm.patchValue({
      id: assureur.id,
      codeAssureur: assureur.codeAssureur,
      profil: assureur.profil,
      sexe: assureur.sexe,
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

  saveAssureur(): void {
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
      id: undefined,
      codeAssureur: this.editForm.get(['codeAssureur'])!.value,
      profil: Profil.ASSUREUR,
      sexe: this.editForm.get(['sexe'])!.value,
      telephone: this.editForm.get(['telephone'])!.value,
      createdAt: moment(moment().format(DATE_TIME_FORMAT)),
      urlPhoto: this.editForm.get(['urlPhoto'])!.value,
      adresse: this.editForm.get(['adresse'])!.value,
      user: this.editForm.get(['user'])!.value,
      assures: this.editForm.get(['assures'])!.value,
    };
  }

  protected subscribeToSaveResponseAssure(result: Observable<HttpResponse<IAssure>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAssureur>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }
  protected onSaveSuccess(): void {
    this.isSaving = false;
    // this.previousState();
    window.location.reload();
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

  save(): void {
    this.success = false;

    this.account.firstName = this.settingsForm.get('firstName')!.value;
    this.account.lastName = this.settingsForm.get('lastName')!.value;
    this.account.email = this.settingsForm.get('email')!.value;
    this.account.langKey = this.settingsForm.get('langKey')!.value;

    this.accountService.save(this.account).subscribe(() => {
      this.success = true;

      this.accountService.authenticate(this.account);

      if (this.account.langKey !== this.languageService.getCurrentLanguage()) {
        this.languageService.changeLanguage(this.account.langKey);
      }
    });
  }

  // facture

  loadAll(): void {
    this.factureService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<IFacture[]>) => this.paginateFactures(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.factures = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  trackId(index: number, item: IFacture): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInFactures(): void {
    this.eventSubscriber = this.eventManager.subscribe('factureListModification', () => this.reset());
  }

  delete(facture: IFacture): void {
    const modalRef = this.modalService.open(FactureDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.facture = facture;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateFactures(data: IFacture[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.factures.push(data[i]);
      }
    }
  }
  codeAssureValidator(control: AbstractControl): { [key: string]: any } | null {
    const codeAssure: string = control.value;
    if (codeAssure !== null) {
      if (codeAssure !== '' && codeAssure.length >= 4) {
        for (let i = 0; i < this.assures.length; i++) {
          if (this.assures[i].codeAssure === codeAssure) {
            return { codeAssure: true };
          }
        }
      }
    }
    return null;
  }

  codeAssureurValidator(control: AbstractControl): { [key: string]: any } | null {
    const codeAssureur: string = control.value;
    if (codeAssureur !== null) {
      if (codeAssureur !== '' && codeAssureur.length >= 4) {
        for (let i = 0; i < this.assureurs.length; i++) {
          if (this.assureurs[i].codeAssureur === codeAssureur) {
            return { codeAssureur: true };
          }
        }
      }
    }
    return null;
  }
}
