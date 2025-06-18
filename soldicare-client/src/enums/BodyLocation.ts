const BodyLocation = {
    HEAD: 'head',
    TORSO: 'torso',
    LEFT_ARM: 'left_arm',
    RIGHT_ARM: 'right_arm',
    LEFT_LEG: 'left_leg',
    RIGHT_LEG: 'right_leg',
    } as const;

type BodyLocation = typeof BodyLocation[keyof typeof BodyLocation];
  
export { BodyLocation, BodyLocation as default };

