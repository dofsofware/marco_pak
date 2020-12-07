import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FirstCaringTestModule } from '../../../test.module';
import { AssureurDetailComponent } from 'app/entities/assureur/assureur-detail.component';
import { Assureur } from 'app/shared/model/assureur.model';

describe('Component Tests', () => {
  describe('Assureur Management Detail Component', () => {
    let comp: AssureurDetailComponent;
    let fixture: ComponentFixture<AssureurDetailComponent>;
    const route = ({ data: of({ assureur: new Assureur(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [FirstCaringTestModule],
        declarations: [AssureurDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(AssureurDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AssureurDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load assureur on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.assureur).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
