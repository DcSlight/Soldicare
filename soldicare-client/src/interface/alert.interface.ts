export interface AlertResponse {
  id: {
    objectId: string;
    systemID: string;
  };
  type: string; // "alert"
  alias: string; // "critical"
  status: string; // "ACTIVE"
  active: boolean;
  creationTimestamp: string;
  createdBy: {
    userId: {
      email: string;
      systemID: string;
    };
  };
  objectDetails: {
    command: {
      id: {
        commandId: string;
        systemID: string;
      };
      command: string; // e.g. "ALERT_CRITICAL_VALUES"
      targetObject: {
        id: {
          objectId: string;
          systemID: string;
        };
      };
      invocationTimestamp: string;
      invokedBy: {
        userId: {
          email: string;
          systemID: string;
        };
      };
      commandAttributes: {
        [key: string]: string | RelatedSoldier; // like "BloodPressure": "136.0"
      };
    };
    snapshotHistory: Snapshot[];
  };
}

export interface RelatedSoldier {
  id: {
    objectId: string;
    systemID: string;
  };
  name: string;
  unitName: string;
  serial: string;
}

export interface Snapshot {
  id: {
    objectId: string;
    systemID: string;
  };
  type: string;
  alias: string;
  status: string;
  active: boolean;
  creationTimestamp: string;
  createdBy: {
    userId: {
      email: string;
      systemID: string;
    };
  };
  objectDetails: SensorDetails;
}

export interface SensorDetails {
  Temperature?: string;
  HeartRate?: string;
  BloodOxygen?: string;
  BloodPressure?: string;
  Hydration?: string;
  ECG?: string;
  EEG?: string;
  location: {
    lat: string;
    lng: string;
  };
  [key: string]: string | { lat: string; lng: string } | undefined;
}
