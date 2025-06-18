const SoldierRoleType = {
    INFANTRY: 'INFANTRY',
    MEDIC: 'MEDIC',
    SNIPER: 'SNIPER',
  } as const;
  
  type SoldierRoleType = typeof SoldierRoleType[keyof typeof SoldierRoleType];
  
  export { SoldierRoleType, SoldierRoleType as default };
  


