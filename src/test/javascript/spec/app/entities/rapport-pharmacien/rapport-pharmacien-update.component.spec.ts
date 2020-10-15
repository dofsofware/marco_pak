import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { FirstCaringTestModule } from '../../../test.module';
import { RapportPharmacienUpdateComponent } from 'app/entities/rapport-pharmacien/rapport-pharmacien-update.component';
import { RapportPharmacienService } from 'app/entities/rapport-pharmacien/rapport-pharmacien.service';
import { RapportPharmacien } from 'app/shared/model/rapport-pharmacien.model';

describe('Component Tests', () => {
  describe('RapportPharmacien Management Update Component', () => {
    let comp: RapportPharmacienUpdateComponent;
    let fixture: ComponentFixture<RapportPharmacienUpdateComponent>;
    let service: RapportPharmacienService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [FirstCaringTestModule],
        declarations: [RapportPharmacienUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(RapportPharmacienUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(RapportPharmacienUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(RapportPharmacienService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new RapportPharmacien(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new RapportPharmacien();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
