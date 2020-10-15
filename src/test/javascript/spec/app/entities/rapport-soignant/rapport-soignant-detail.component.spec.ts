import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { JhiDataUtils } from 'ng-jhipster';

import { FirstCaringTestModule } from '../../../test.module';
import { RapportSoignantDetailComponent } from 'app/entities/rapport-soignant/rapport-soignant-detail.component';
import { RapportSoignant } from 'app/shared/model/rapport-soignant.model';

describe('Component Tests', () => {
  describe('RapportSoignant Management Detail Component', () => {
    let comp: RapportSoignantDetailComponent;
    let fixture: ComponentFixture<RapportSoignantDetailComponent>;
    let dataUtils: JhiDataUtils;
    const route = ({ data: of({ rapportSoignant: new RapportSoignant(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [FirstCaringTestModule],
        declarations: [RapportSoignantDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(RapportSoignantDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(RapportSoignantDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = fixture.debugElement.injector.get(JhiDataUtils);
    });

    describe('OnInit', () => {
      it('Should load rapportSoignant on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.rapportSoignant).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });

    describe('byteSize', () => {
      it('Should call byteSize from JhiDataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'byteSize');
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.byteSize(fakeBase64);

        // THEN
        expect(dataUtils.byteSize).toBeCalledWith(fakeBase64);
      });
    });

    describe('openFile', () => {
      it('Should call openFile from JhiDataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'openFile');
        const fakeContentType = 'fake content type';
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.openFile(fakeContentType, fakeBase64);

        // THEN
        expect(dataUtils.openFile).toBeCalledWith(fakeContentType, fakeBase64);
      });
    });
  });
});
