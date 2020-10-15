import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { FirstCaringTestModule } from '../../../test.module';
import { AssureUpdateComponent } from 'app/entities/assure/assure-update.component';
import { AssureService } from 'app/entities/assure/assure.service';
import { Assure } from 'app/shared/model/assure.model';

describe('Component Tests', () => {
  describe('Assure Management Update Component', () => {
    let comp: AssureUpdateComponent;
    let fixture: ComponentFixture<AssureUpdateComponent>;
    let service: AssureService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [FirstCaringTestModule],
        declarations: [AssureUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(AssureUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AssureUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(AssureService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Assure(123);
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
        const entity = new Assure();
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
