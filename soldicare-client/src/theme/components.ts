import type { Components } from "@mui/material/styles";

const components: Components = {
  MuiButton: {
    styleOverrides: {
      root: {
        borderRadius: "8px",
        textTransform: "none",
      },
    },
  },
};

export default components;
