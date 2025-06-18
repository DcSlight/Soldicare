export interface Soldier {
  id: {
    objectId: string;
    systemID: string;
  };
  type: string;
  alias: string;
  status: string;
  objectDetails: {
    serial?: string;
    name: string;
    bloodType?: string;
    age?: string;
    alergies?: string[];
    MedicalCondition?: string[];
    Medications?: string[];
  };
}
