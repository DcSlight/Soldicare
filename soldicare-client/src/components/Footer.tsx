import { Box, Typography } from "@mui/material";

export default function Footer() {
  return (
    <Box
      component="footer"
      sx={{
        py: 2,
        textAlign: "center",
        borderTop: "1px solid #ddd",
        color: "text.secondary",
        fontSize: "0.9rem",
      }}
    >
      <Typography variant="body2">
        © {new Date().getFullYear()} Soldicare. All rights reserved.
      </Typography>
    </Box>
  );
}
