import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAssureur } from 'app/shared/model/assureur.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { AssureurService } from './assureur.service';
import { AssureurDeleteDialogComponent } from './assureur-delete-dialog.component';

@Component({
  selector: 'jhi-assureur',
  templateUrl: './assureur.component.html',
})
export class AssureurComponent implements OnInit, OnDestroy {
  assureurs: IAssureur[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected assureurService: AssureurService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.assureurs = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.assureurService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<IAssureur[]>) => this.paginateAssureurs(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.assureurs = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInAssureurs();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IAssureur): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInAssureurs(): void {
    this.eventSubscriber = this.eventManager.subscribe('assureurListModification', () => this.reset());
  }

  delete(assureur: IAssureur): void {
    const modalRef = this.modalService.open(AssureurDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.assureur = assureur;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateAssureurs(data: IAssureur[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.assureurs.push(data[i]);
      }
    }
  }
}
