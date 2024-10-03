export class LoginData {
  username!: string;
  password!: string;
}

export class AuthResponse {
  token!: string;
}

export interface Thing {
  a: number,
  b: number,
  c: number
}

export class HumanBeing {
  id!: number;
  name!: String;
  coordinates!: Coordinates;
  realHero!: Boolean;
  hasToothpick!: Boolean;
  car!: Car;
  mood!: string;
  impactSpeed!: number;
  minutesOfWaiting!: number;
  weaponType!: string;
  creationDate!: string;
}

export class Coordinates {
  x!: number;
  y!: number;
}

export class Car {
  name!: String;
}
