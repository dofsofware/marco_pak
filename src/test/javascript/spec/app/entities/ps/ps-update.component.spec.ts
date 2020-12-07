import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { FirstCaringTestModule } from '../../../test.module';
import { PSUpdateComponent } from 'app/entities/ps/ps-update.component';
import { PSService } from 'app/entities/ps/ps.service';
import { PS } from 'app/shared/model/ps.model';

describe('Component Tests', () => {
  describe('PS Management Update Component', () => {
    let comp: PSUpdateComponent;
    let fixture: ComponentFixture<PSUpdateComponent>;
    let service: PSService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [FirstCaringTestModule],
        declarations: [PSUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(PSUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PSUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PSService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new PS(123);
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
        const entity = new PS();
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
