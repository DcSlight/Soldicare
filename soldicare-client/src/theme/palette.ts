// src/theme/palette.ts
import type { PaletteOptions } from "@mui/material";

declare module "@mui/material/styles" {
  interface Palette {
    severity: {
      critical: string;
      warning: string;
      minor: string;
      disabled: string;
      fallen: string;
      regular: string;
    };
  }

  interface PaletteOptions {
    severity?: {
      critical?: string;
      warning?: string;
      minor?: string;
      disabled?: string;
      fallen?: string;
      regular?: string;
      medical?: string;
    };
  }
}

export const palette: PaletteOptions = {
  severity: {
    critical: "#f44336", // red
    warning: "#ff9800", // orange
    minor: "#ffeb3b", // yellow
    disabled: "#2196f3", // blue
    fallen: "#9e9e9e", // gray
    regular: "#4caf50", // green
    medical: "#c39696",
  },
};
