export const Role = {
  ADMIN: "ADMIN",
  OPERATOR: "OPERATOR",
  END_USER: "END_USER",
} as const;

export type Role = (typeof Role)[keyof typeof Role];
