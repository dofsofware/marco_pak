import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { RapportSoignantService } from 'app/entities/rapport-soignant/rapport-soignant.service';
import { IRapportSoignant, RapportSoignant } from 'app/shared/model/rapport-soignant.model';

describe('Service Tests', () => {
  describe('RapportSoignant Service', () => {
    let injector: TestBed;
    let service: RapportSoignantService;
    let httpMock: HttpTestingController;
    let elemDefault: IRapportSoignant;
    let expectedResult: IRapportSoignant | IRapportSoignant[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(RapportSoignantService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new RapportSoignant(0, 'AAAAAAA', 'AAAAAAA', 0, 'AAAAAAA', currentDate);
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

      it('should create a RapportSoignant', () => {
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

        service.create(new RapportSoignant()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a RapportSoignant', () => {
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

      it('should return a list of RapportSoignant', () => {
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

      it('should delete a RapportSoignant', () => {
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
