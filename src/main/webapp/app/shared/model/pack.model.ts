export interface IPack {
  id?: number;
  denommination?: string;
  prix?: number;
  description?: any;
  nmbDePers?: number;
}

export class Pack implements IPack {
  constructor(
    public id?: number,
    public denommination?: string,
    public prix?: number,
    public description?: any,
    public nmbDePers?: number
  ) {}
}
