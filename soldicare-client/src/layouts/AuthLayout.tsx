import { Box, Grid, Typography } from "@mui/material";
import { Link } from "react-router-dom";
import logo from "../assets/logo.png";
import backgroundImage from "../assets/green-background-login.svg";

type AuthLayoutProps = {
  title: string;
  children: React.ReactNode;
};

export default function AuthLayout({ title, children }: AuthLayoutProps) {
  return (
    <>
      {/* Clickable Logo */}
      <Link
        to="/"
        style={{
          position: "absolute",
          top: 32,
          left: 32,
          zIndex: 10,
        }}
      >
        <Box
          component="img"
          src={logo}
          alt="SoldiCare Logo"
          sx={{
            height: 50,
            cursor: "pointer",
            transition: "transform 0.2s ease-in-out",
            "&:hover": {
              transform: "scale(1.05)",
            },
          }}
        />
      </Link>

      <Grid
        container
        sx={{ height: "100vh", width: "100%", overflow: "hidden" }}
      >
        {/* Left Side - Form */}
        <Grid
          sx={{
            width: { xs: "100%", md: "50%" },
            display: "flex",
            alignItems: "center",
            justifyContent: "center",
            px: 4,
          }}
        >
          <Box sx={{ width: "100%", maxWidth: 400 }}>
            <Typography
              variant="h1"
              align="center"
              sx={{ fontWeight: 700, fontSize: "2.0rem", mb: 2 }}
            >
              {title}
            </Typography>
            {children}
          </Box>
        </Grid>

        {/* Right Side - Image */}
        <Grid
          sx={{
            width: "50%",
            display: { xs: "none", md: "block" },
            backgroundImage: `url(${backgroundImage})`,
            backgroundSize: "cover",
            backgroundPosition: "center",
          }}
        />
      </Grid>
    </>
  );
}
