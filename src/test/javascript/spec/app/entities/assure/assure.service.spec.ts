import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { AssureService } from 'app/entities/assure/assure.service';
import { IAssure, Assure } from 'app/shared/model/assure.model';
import { Profil } from 'app/shared/model/enumerations/profil.model';

describe('Service Tests', () => {
  describe('Assure Service', () => {
    let injector: TestBed;
    let service: AssureService;
    let httpMock: HttpTestingController;
    let elemDefault: IAssure;
    let expectedResult: IAssure | IAssure[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(AssureService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Assure(0, 'AAAAAAA', Profil.ASSUREUR, 'AAAAAAA', currentDate, 'AAAAAAA', 'AAAAAAA');
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

      it('should create a Assure', () => {
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

        service.create(new Assure()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Assure', () => {
        const returnedFromService = Object.assign(
          {
            codeAssure: 'BBBBBB',
            profil: 'BBBBBB',
            telephone: 'BBBBBB',
            createdAt: currentDate.format(DATE_TIME_FORMAT),
            urlPhoto: 'BBBBBB',
            adresse: 'BBBBBB',
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

      it('should return a list of Assure', () => {
        const returnedFromService = Object.assign(
          {
            codeAssure: 'BBBBBB',
            profil: 'BBBBBB',
            telephone: 'BBBBBB',
            createdAt: currentDate.format(DATE_TIME_FORMAT),
            urlPhoto: 'BBBBBB',
            adresse: 'BBBBBB',
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

      it('should delete a Assure', () => {
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
