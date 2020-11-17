import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAssure } from 'app/shared/model/assure.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { AssureService } from './assure.service';
import { AssureDeleteDialogComponent } from './assure-delete-dialog.component';

@Component({
  selector: 'jhi-assure',
  templateUrl: './assure.component.html',
})
export class AssureComponent implements OnInit, OnDestroy {
  assures: IAssure[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected assureService: AssureService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.assures = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.assureService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<IAssure[]>) => this.paginateAssures(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.assures = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInAssures();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IAssure): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInAssures(): void {
    this.eventSubscriber = this.eventManager.subscribe('assureListModification', () => this.reset());
  }

  delete(assure: IAssure): void {
    const modalRef = this.modalService.open(AssureDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.assure = assure;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateAssures(data: IAssure[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.assures.push(data[i]);
      }
    }
  }
}
