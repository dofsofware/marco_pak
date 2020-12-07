import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { PSService } from 'app/entities/ps/ps.service';
import { IPS, PS } from 'app/shared/model/ps.model';
import { Profil } from 'app/shared/model/enumerations/profil.model';
import { Sexe } from 'app/shared/model/enumerations/sexe.model';

describe('Service Tests', () => {
  describe('PS Service', () => {
    let injector: TestBed;
    let service: PSService;
    let httpMock: HttpTestingController;
    let elemDefault: IPS;
    let expectedResult: IPS | IPS[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(PSService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new PS(
        0,
        'AAAAAAA',
        Profil.ASSUREUR,
        Sexe.MASCULIN,
        'AAAAAAA',
        currentDate,
        'AAAAAAA',
        'AAAAAAA',
        0,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA'
      );
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            createdAt: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a PS', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            createdAt: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createdAt: currentDate,
          },
          returnedFromService
        );

        service.create(new PS()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a PS', () => {
        const returnedFromService = Object.assign(
          {
            codePS: 'BBBBBB',
            profil: 'BBBBBB',
            sexe: 'BBBBBB',
            telephone: 'BBBBBB',
            createdAt: currentDate.format(DATE_TIME_FORMAT),
            urlPhoto: 'BBBBBB',
            profession: 'BBBBBB',
            experience: 1,
            nomDeLetablissement: 'BBBBBB',
            telephoneDeLetablissement: 'BBBBBB',
            adresseDeLetablissement: 'BBBBBB',
            lienGoogleMapsDeLetablissement: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createdAt: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of PS', () => {
        const returnedFromService = Object.assign(
          {
            codePS: 'BBBBBB',
            profil: 'BBBBBB',
            sexe: 'BBBBBB',
            telephone: 'BBBBBB',
            createdAt: currentDate.format(DATE_TIME_FORMAT),
            urlPhoto: 'BBBBBB',
            profession: 'BBBBBB',
            experience: 1,
            nomDeLetablissement: 'BBBBBB',
            telephoneDeLetablissement: 'BBBBBB',
            adresseDeLetablissement: 'BBBBBB',
            lienGoogleMapsDeLetablissement: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createdAt: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a PS', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
