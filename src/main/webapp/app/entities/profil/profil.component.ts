import { Component, OnInit, OnDestroy } from '@angular/core';
import { JhiEventManager, JhiLanguageService, JhiParseLinks } from 'ng-jhipster';
import { LANGUAGES } from 'app/core/language/language.constants';
import { FormBuilder, Validators } from '@angular/forms';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/user/account.model';
import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { FactureService } from '../facture/facture.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Subscription } from 'rxjs';
import { IFacture } from 'app/shared/model/facture.model';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { FactureDeleteDialogComponent } from '../facture/facture-delete-dialog.component';

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

  settingsForm = this.fb.group({
    firstName: [undefined, [Validators.required, Validators.minLength(1), Validators.maxLength(50)]],
    lastName: [undefined, [Validators.required, Validators.minLength(1), Validators.maxLength(50)]],
    email: [undefined, [Validators.required, Validators.minLength(5), Validators.maxLength(254), Validators.email]],
    langKey: [undefined],
  });

  constructor(
    private accountService: AccountService,
    private fb: FormBuilder,
    private languageService: JhiLanguageService,
    protected factureService: FactureService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
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
    this.accountService.identity().subscribe(account => {
      if (account) {
        this.settingsForm.patchValue({
          firstName: account.firstName,
          lastName: account.lastName,
          email: account.email,
          langKey: account.langKey,
        });

        this.account = account;
      }
    });
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
}
