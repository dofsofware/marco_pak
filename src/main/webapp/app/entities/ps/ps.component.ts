import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPS } from 'app/shared/model/ps.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { PSService } from './ps.service';
import { PSDeleteDialogComponent } from './ps-delete-dialog.component';

@Component({
  selector: 'jhi-ps',
  templateUrl: './ps.component.html',
})
export class PSComponent implements OnInit, OnDestroy {
  pS: IPS[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected pSService: PSService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.pS = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.pSService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<IPS[]>) => this.paginatePS(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.pS = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInPS();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IPS): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInPS(): void {
    this.eventSubscriber = this.eventManager.subscribe('pSListModification', () => this.reset());
  }

  delete(pS: IPS): void {
    const modalRef = this.modalService.open(PSDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.pS = pS;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginatePS(data: IPS[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.pS.push(data[i]);
      }
    }
  }
}
