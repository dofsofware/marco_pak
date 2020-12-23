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
import { PSService } from '../ps/ps.service';
import { IPS, PS } from 'app/shared/model/ps.model';
import { AssureDeleteDialogComponent } from '../assure/assure-delete-dialog.component';

type SelectableEntity = IUser | IAssure;

@Component({
  selector: 'jhi-profil',
  templateUrl: './profil.component.html',
})
export class ProfilComponent implements OnInit, OnDestroy {
  account!: Account;
  success = false;
  languages = LANGUAGES;
  profilEncours: string;
  profilIncomplet: boolean;
  recherche: boolean;

  // facture
  factures: IFacture[];
  facturesByAssureurEncours: IFacture[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  profil: string;
  ascending: boolean;
  selectedOption: number;

  // Assureur
  isSaving = false;
  users: IUser[] = [];
  assures: IAssure[] = [];
  assureursByCurrentUser: IAssureur[] = [];

  // Assuré(e)
  assureurs: IAssureur[] = [];
  packs: IPack[] = [];
  assuresByCurrentUser: IAssure[] = [];
  assuresByAssureurEncours: IAssure[] = [];
  Assuresrecherche: IAssure[] = [];
  codeAssureRecherche: string;

  // PERSONNEL DE SANTE
  PS: IPS[] = [];
  PSByCurrentUser: IPS[] = [];

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

  editFormPS = this.fb.group({
    id: [],
    codePS: [null, [Validators.required, Validators.minLength(4), Validators.maxLength(20), this.codePSValidator.bind(this)]],
    profil: [],
    sexe: [null, [Validators.required]],
    telephone: [null, [Validators.required]],
    createdAt: [],
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
    protected pSService: PSService,
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
    this.profilIncomplet = false;
    this.recherche = false;
    this.profilEncours = '';
    this.codeAssureRecherche = '';
    this.selectedOption = 1;
    this.factures = [];
    this.facturesByAssureurEncours = [];
    this.assuresByAssureurEncours = [];
    this.assures = [];
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
        this.assureurService
          .getAllAssureursByCurrentUser({ page: this.page, size: this.itemsPerPage, sort: this.sort() })
          .subscribe((res: HttpResponse<IAssureur[]>) => this.paginateAssureurs(res.body, res.headers));
        // this.assureurService.getAllAssureursByCurrentUser().subscribe((res: HttpResponse<IAssureur[]>) => (this.assureursByCurrentUser = res.body || []));
        this.pSService
          .getAllPSByCurrentUser()
          .subscribe(
            (res: HttpResponse<IPS[]>) => ((this.profilIncomplet = this.gestionProfilEncore()), (this.PSByCurrentUser = res.body || []))
          );
        this.assureService
          .getAllAssuresByCurrentUser()
          .subscribe(
            (res: HttpResponse<IAssure[]>) => (
              (this.profilIncomplet = this.gestionProfilEncore()), (this.assuresByCurrentUser = res.body || [])
            )
          );
        this.account = account;
        this.userService
          .query()
          .subscribe((res: HttpResponse<IUser[]>) => ((this.profilIncomplet = this.gestionProfilEncore()), (this.users = res.body || [])));
        this.assureService
          .query()
          .subscribe(
            (res: HttpResponse<IAssure[]>) => ((this.profilIncomplet = this.gestionProfilEncore()), (this.assures = res.body || []))
          );
        this.userService
          .query()
          .subscribe((res: HttpResponse<IUser[]>) => ((this.profilIncomplet = this.gestionProfilEncore()), (this.users = res.body || [])));
        this.assureurService
          .query()
          .subscribe(
            (res: HttpResponse<IAssureur[]>) => ((this.profilIncomplet = this.gestionProfilEncore()), (this.assureurs = res.body || []))
          );
        this.packService
          .query()
          .subscribe((res: HttpResponse<IPack[]>) => ((this.profilIncomplet = this.gestionProfilEncore()), (this.packs = res.body || [])));
        this.pSService
          .query()
          .subscribe((res: HttpResponse<IPS[]>) => ((this.profilIncomplet = this.gestionProfilEncore()), (this.PS = res.body || [])));
        // this.profilComplet = this.gestionProfilEncore();
      }
    });

    this.loadAllFacture();
    this.registerChangeInFactures();
    this.registerChangeInAssures();

    //
  }

  gestionProfilEncore(): boolean {
    this.loadAllAssuresByIdAssureur();
    this.loadAllFactureByIdAssureur();
    if (this.assuresByCurrentUser?.length === 1) {
      this.profilEncours = 'ASSURE';
      return false;
    } else if (this.assureursByCurrentUser?.length === 1) {
      this.profilEncours = 'ASSUREUR';
      return false;
    } else if (this.PSByCurrentUser?.length === 1) {
      this.profilEncours = 'PS';
      return false;
    }
    return true;
  }

  updateFormPS(pS: IPS): void {
    this.editFormPS.patchValue({
      id: pS.id,
      codePS: pS.codePS,
      profil: pS.profil,
      sexe: pS.sexe,
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

  savePS(): void {
    this.isSaving = true;
    const pS = this.createFromFormPS();
    if (pS.id !== undefined) {
      this.subscribeToSaveResponsePS(this.pSService.update(pS));
    } else {
      this.subscribeToSaveResponsePS(this.pSService.create(pS));
    }
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

  private createFromFormPS(): IPS {
    return {
      ...new PS(),
      id: undefined,
      codePS: this.editFormPS.get(['codePS'])!.value,
      profil: Profil.PS,
      sexe: this.editFormPS.get(['sexe'])!.value,
      telephone: this.editFormPS.get(['telephone'])!.value,
      createdAt: moment(moment().format(DATE_TIME_FORMAT)),
      urlPhoto: this.editFormPS.get(['urlPhoto'])!.value,
      profession: this.editFormPS.get(['profession'])!.value,
      experience: this.editFormPS.get(['experience'])!.value,
      nomDeLetablissement: this.editFormPS.get(['nomDeLetablissement'])!.value,
      telephoneDeLetablissement: this.editFormPS.get(['telephoneDeLetablissement'])!.value,
      adresseDeLetablissement: this.editFormPS.get(['adresseDeLetablissement'])!.value,
      lienGoogleMapsDeLetablissement: this.editFormPS.get(['lienGoogleMapsDeLetablissement'])!.value,
      user: this.editFormPS.get(['user'])!.value,
      assures: this.editFormPS.get(['assures'])!.value,
    };
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

  protected subscribeToSaveResponseAssure(result: Observable<HttpResponse<IAssure>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected subscribeToSaveResponsePS(result: Observable<HttpResponse<IPS>>): void {
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

  deleteAssure(assure: IAssure): void {
    const modalRef = this.modalService.open(AssureDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.assure = assure;
  }

  sortAssure(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
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

  loadAllFacture(): void {
    this.factureService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<IFacture[]>) => this.paginateFactures(res.body, res.headers));
  }

  loadAllAssuresByIdAssureur(): void {
    if (this.assureursByCurrentUser[0]?.id !== undefined) {
      this.assureService
        .findAllByIdAssureur(this.assureursByCurrentUser[0]?.id)
        .subscribe((res: HttpResponse<IAssure[]>) => this.paginateAssures(res.body, res.headers));
    }
  }

  resetAssures(): void {
    this.page = 0;
    this.assures = [];
    this.loadAllAssuresByIdAssureur();
  }

  loadAllFactureByIdAssureur(): void {
    if (this.assureursByCurrentUser[0]?.id !== undefined) {
      this.factureService
        .findAllByIdAssureur(this.assureursByCurrentUser[0]?.id)
        .subscribe((res: HttpResponse<IFacture[]>) => this.paginateFacturesByAssureurEncours(res.body, res.headers));
    }
  }

  registerChangeInAssures(): void {
    this.eventSubscriber = this.eventManager.subscribe('assureListModification', () => this.reset());
  }

  // ASSURE
  // getAllAssuresByCurrentUser(): void {
  //   this.assureService
  //     .getAllAssuresByCurrentUser({
  //       page: this.page,
  //       size: this.itemsPerPage,
  //       sort: this.sort(),
  //     })
  //     .subscribe((res: HttpResponse<IAssure[]>) => this.paginateAssures(res.body, res.headers));
  // }

  reset(): void {
    this.page = 0;
    this.factures = [];
    this.facturesByAssureurEncours = [];
    this.assuresByAssureurEncours = [];
    this.loadAllFacture();
    this.loadAllFactureByIdAssureur();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAllFacture();
    this.loadAllFactureByIdAssureur();
    this.loadAllAssuresByIdAssureur();
  }

  trackIdAssure(index: number, item: IAssure): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
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

  protected paginateFacturesByAssureurEncours(data: IFacture[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.facturesByAssureurEncours = [];
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.facturesByAssureurEncours.push(data[i]);
      }
    }
  }

  protected paginateAssures(data: IAssure[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.assuresByAssureurEncours = [];
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.assuresByAssureurEncours.push(data[i]);
      }
    }
  }
  protected paginateAssuresrecherche(data: IAssure[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.Assuresrecherche = [];
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.Assuresrecherche.push(data[i]);
      }
    }
  }

  protected paginateAssureurs(data: IAssureur[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    this.profilIncomplet = this.gestionProfilEncore();
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.assureursByCurrentUser.push(data[i]);
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

  rechercheAssure(): void {
    if (this.codeAssureRecherche !== undefined) {
      this.Assuresrecherche = [];
      if (this.codeAssureRecherche.length > 0) {
        this.recherche = true;
      }

      this.assureService
        .findAllByCode(this.codeAssureRecherche)
        .subscribe((res: HttpResponse<IAssure[]>) => this.paginateAssuresrecherche(res.body, res.headers));
    }
  }

  OptionSelectionne(event: any): void {
    this.selectedOption = event.target.value;
  }

  EnregistrerAssureRecherche(): void {
    if (this.Assuresrecherche[0]?.pack?.id !== undefined) {
      this.Assuresrecherche[0].pack.id = this.selectedOption;
    } else {
      this.Assuresrecherche[0].pack = this.packs[this.selectedOption - 1];
    }
    this.Assuresrecherche[0].assureur = this.assureursByCurrentUser[0];
    this.subscribeToSaveResponseAssure(this.assureService.update(this.Assuresrecherche[0]));
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

  codePSValidator(control: AbstractControl): { [key: string]: any } | null {
    const codePS: string = control.value;
    if (codePS !== null) {
      if (codePS !== '' && codePS.length >= 4) {
        for (let i = 0; i < this.PS.length; i++) {
          if (this.PS[i].codePS === codePS) {
            return { codePS: true };
          }
        }
      }
    }
    return null;
  }
}
