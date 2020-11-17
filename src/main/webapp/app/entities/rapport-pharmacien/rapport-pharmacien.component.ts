import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiDataUtils } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IRapportPharmacien } from 'app/shared/model/rapport-pharmacien.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { RapportPharmacienService } from './rapport-pharmacien.service';
import { RapportPharmacienDeleteDialogComponent } from './rapport-pharmacien-delete-dialog.component';

@Component({
  selector: 'jhi-rapport-pharmacien',
  templateUrl: './rapport-pharmacien.component.html',
})
export class RapportPharmacienComponent implements OnInit, OnDestroy {
  rapportPharmaciens: IRapportPharmacien[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected rapportPharmacienService: RapportPharmacienService,
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.rapportPharmaciens = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.rapportPharmacienService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<IRapportPharmacien[]>) => this.paginateRapportPharmaciens(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.rapportPharmaciens = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInRapportPharmaciens();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IRapportPharmacien): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType = '', base64String: string): void {
    return this.dataUtils.openFile(contentType, base64String);
  }

  registerChangeInRapportPharmaciens(): void {
    this.eventSubscriber = this.eventManager.subscribe('rapportPharmacienListModification', () => this.reset());
  }

  delete(rapportPharmacien: IRapportPharmacien): void {
    const modalRef = this.modalService.open(RapportPharmacienDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.rapportPharmacien = rapportPharmacien;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateRapportPharmaciens(data: IRapportPharmacien[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.rapportPharmaciens.push(data[i]);
      }
    }
  }
}
