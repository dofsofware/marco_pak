import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { AssureurService } from 'app/entities/assureur/assureur.service';
import { IAssureur, Assureur } from 'app/shared/model/assureur.model';
import { Profil } from 'app/shared/model/enumerations/profil.model';

describe('Service Tests', () => {
  describe('Assureur Service', () => {
    let injector: TestBed;
    let service: AssureurService;
    let httpMock: HttpTestingController;
    let elemDefault: IAssureur;
    let expectedResult: IAssureur | IAssureur[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(AssureurService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Assureur(0, 'AAAAAAA', Profil.ASSUREUR, 'AAAAAAA', currentDate, 'AAAAAAA', 'AAAAAAA');
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

      it('should create a Assureur', () => {
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

        service.create(new Assureur()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Assureur', () => {
        const returnedFromService = Object.assign(
          {
            codeAssureur: 'BBBBBB',
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

      it('should return a list of Assureur', () => {
        const returnedFromService = Object.assign(
          {
            codeAssureur: 'BBBBBB',
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

      it('should delete a Assureur', () => {
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
