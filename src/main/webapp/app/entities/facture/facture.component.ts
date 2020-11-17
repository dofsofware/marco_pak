import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IFacture } from 'app/shared/model/facture.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { FactureService } from './facture.service';
import { FactureDeleteDialogComponent } from './facture-delete-dialog.component';

@Component({
  selector: 'jhi-facture',
  templateUrl: './facture.component.html',
})
export class FactureComponent implements OnInit, OnDestroy {
  factures: IFacture[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
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
    this.ascending = true;
  }

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

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInFactures();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
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
