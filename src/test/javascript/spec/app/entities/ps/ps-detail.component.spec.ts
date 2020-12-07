import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FirstCaringTestModule } from '../../../test.module';
import { PSDetailComponent } from 'app/entities/ps/ps-detail.component';
import { PS } from 'app/shared/model/ps.model';

describe('Component Tests', () => {
  describe('PS Management Detail Component', () => {
    let comp: PSDetailComponent;
    let fixture: ComponentFixture<PSDetailComponent>;
    const route = ({ data: of({ pS: new PS(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [FirstCaringTestModule],
        declarations: [PSDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(PSDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PSDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load pS on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.pS).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
