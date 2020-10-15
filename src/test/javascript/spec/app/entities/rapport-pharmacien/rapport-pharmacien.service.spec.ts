import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { RapportPharmacienService } from 'app/entities/rapport-pharmacien/rapport-pharmacien.service';
import { IRapportPharmacien, RapportPharmacien } from 'app/shared/model/rapport-pharmacien.model';

describe('Service Tests', () => {
  describe('RapportPharmacien Service', () => {
    let injector: TestBed;
    let service: RapportPharmacienService;
    let httpMock: HttpTestingController;
    let elemDefault: IRapportPharmacien;
    let expectedResult: IRapportPharmacien | IRapportPharmacien[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(RapportPharmacienService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new RapportPharmacien(0, 'AAAAAAA', 'AAAAAAA', 0, 'AAAAAAA', currentDate);
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

      it('should create a RapportPharmacien', () => {
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

        service.create(new RapportPharmacien()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a RapportPharmacien', () => {
        const returnedFromService = Object.assign(
          {
            codePatient: 'BBBBBB',
            codePS: 'BBBBBB',
            facturation: 1,
            description: 'BBBBBB',
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

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of RapportPharmacien', () => {
        const returnedFromService = Object.assign(
          {
            codePatient: 'BBBBBB',
            codePS: 'BBBBBB',
            facturation: 1,
            description: 'BBBBBB',
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

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a RapportPharmacien', () => {
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
