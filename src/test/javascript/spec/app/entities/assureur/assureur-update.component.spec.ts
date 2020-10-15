import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { FirstCaringTestModule } from '../../../test.module';
import { AssureurUpdateComponent } from 'app/entities/assureur/assureur-update.component';
import { AssureurService } from 'app/entities/assureur/assureur.service';
import { Assureur } from 'app/shared/model/assureur.model';

describe('Component Tests', () => {
  describe('Assureur Management Update Component', () => {
    let comp: AssureurUpdateComponent;
    let fixture: ComponentFixture<AssureurUpdateComponent>;
    let service: AssureurService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [FirstCaringTestModule],
        declarations: [AssureurUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(AssureurUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AssureurUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(AssureurService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Assureur(123);
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
        const entity = new Assureur();
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
