import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { FirstCaringTestModule } from '../../../test.module';
import { MockEventManager } from '../../../helpers/mock-event-manager.service';
import { MockActiveModal } from '../../../helpers/mock-active-modal.service';
import { RapportPharmacienDeleteDialogComponent } from 'app/entities/rapport-pharmacien/rapport-pharmacien-delete-dialog.component';
import { RapportPharmacienService } from 'app/entities/rapport-pharmacien/rapport-pharmacien.service';

describe('Component Tests', () => {
  describe('RapportPharmacien Management Delete Component', () => {
    let comp: RapportPharmacienDeleteDialogComponent;
    let fixture: ComponentFixture<RapportPharmacienDeleteDialogComponent>;
    let service: RapportPharmacienService;
    let mockEventManager: MockEventManager;
    let mockActiveModal: MockActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [FirstCaringTestModule],
        declarations: [RapportPharmacienDeleteDialogComponent],
      })
        .overrideTemplate(RapportPharmacienDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(RapportPharmacienDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(RapportPharmacienService);
      mockEventManager = TestBed.get(JhiEventManager);
      mockActiveModal = TestBed.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.closeSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
      });
    });
  });
});
