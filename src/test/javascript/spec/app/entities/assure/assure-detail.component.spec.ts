import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FirstCaringTestModule } from '../../../test.module';
import { AssureDetailComponent } from 'app/entities/assure/assure-detail.component';
import { Assure } from 'app/shared/model/assure.model';

describe('Component Tests', () => {
  describe('Assure Management Detail Component', () => {
    let comp: AssureDetailComponent;
    let fixture: ComponentFixture<AssureDetailComponent>;
    const route = ({ data: of({ assure: new Assure(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [FirstCaringTestModule],
        declarations: [AssureDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(AssureDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AssureDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load assure on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.assure).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
