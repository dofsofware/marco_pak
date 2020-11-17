import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiDataUtils } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IRapportSoignant } from 'app/shared/model/rapport-soignant.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { RapportSoignantService } from './rapport-soignant.service';
import { RapportSoignantDeleteDialogComponent } from './rapport-soignant-delete-dialog.component';

@Component({
  selector: 'jhi-rapport-soignant',
  templateUrl: './rapport-soignant.component.html',
})
export class RapportSoignantComponent implements OnInit, OnDestroy {
  rapportSoignants: IRapportSoignant[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected rapportSoignantService: RapportSoignantService,
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.rapportSoignants = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.rapportSoignantService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<IRapportSoignant[]>) => this.paginateRapportSoignants(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.rapportSoignants = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInRapportSoignants();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IRapportSoignant): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType = '', base64String: string): void {
    return this.dataUtils.openFile(contentType, base64String);
  }

  registerChangeInRapportSoignants(): void {
    this.eventSubscriber = this.eventManager.subscribe('rapportSoignantListModification', () => this.reset());
  }

  delete(rapportSoignant: IRapportSoignant): void {
    const modalRef = this.modalService.open(RapportSoignantDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.rapportSoignant = rapportSoignant;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateRapportSoignants(data: IRapportSoignant[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.rapportSoignants.push(data[i]);
      }
    }
  }
}
