import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { FirstCaringTestModule } from '../../../test.module';
import { RapportSoignantUpdateComponent } from 'app/entities/rapport-soignant/rapport-soignant-update.component';
import { RapportSoignantService } from 'app/entities/rapport-soignant/rapport-soignant.service';
import { RapportSoignant } from 'app/shared/model/rapport-soignant.model';

describe('Component Tests', () => {
  describe('RapportSoignant Management Update Component', () => {
    let comp: RapportSoignantUpdateComponent;
    let fixture: ComponentFixture<RapportSoignantUpdateComponent>;
    let service: RapportSoignantService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [FirstCaringTestModule],
        declarations: [RapportSoignantUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(RapportSoignantUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(RapportSoignantUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(RapportSoignantService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new RapportSoignant(123);
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
        const entity = new RapportSoignant();
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
