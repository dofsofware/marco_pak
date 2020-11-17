import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IRendezVous } from 'app/shared/model/rendez-vous.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { RendezVousService } from './rendez-vous.service';
import { RendezVousDeleteDialogComponent } from './rendez-vous-delete-dialog.component';

@Component({
  selector: 'jhi-rendez-vous',
  templateUrl: './rendez-vous.component.html',
})
export class RendezVousComponent implements OnInit, OnDestroy {
  rendezVous: IRendezVous[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected rendezVousService: RendezVousService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.rendezVous = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.rendezVousService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<IRendezVous[]>) => this.paginateRendezVous(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.rendezVous = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInRendezVous();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IRendezVous): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInRendezVous(): void {
    this.eventSubscriber = this.eventManager.subscribe('rendezVousListModification', () => this.reset());
  }

  delete(rendezVous: IRendezVous): void {
    const modalRef = this.modalService.open(RendezVousDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.rendezVous = rendezVous;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateRendezVous(data: IRendezVous[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.rendezVous.push(data[i]);
      }
    }
  }
}
