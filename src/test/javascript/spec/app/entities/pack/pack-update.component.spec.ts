import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { FirstCaringTestModule } from '../../../test.module';
import { PackUpdateComponent } from 'app/entities/pack/pack-update.component';
import { PackService } from 'app/entities/pack/pack.service';
import { Pack } from 'app/shared/model/pack.model';

describe('Component Tests', () => {
  describe('Pack Management Update Component', () => {
    let comp: PackUpdateComponent;
    let fixture: ComponentFixture<PackUpdateComponent>;
    let service: PackService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [FirstCaringTestModule],
        declarations: [PackUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(PackUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PackUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(PackService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Pack(123);
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
        const entity = new Pack();
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
