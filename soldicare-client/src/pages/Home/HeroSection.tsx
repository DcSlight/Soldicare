import { Box, Button, Typography } from "@mui/material";
import { useNavigate } from "react-router-dom";
import heroImage from "../../assets/soldiers.jpg";
export default function HeroSection() {
  const navigate = useNavigate();

  return (
    <Box
      sx={{
        display: "flex",
        flexDirection: { xs: "column", md: "row" },
        height: "100vh",
        overflow: "hidden",
        backgroundColor: "#f5f5f5",
      }}
    >
      {/* Text Side */}
      <Box
        sx={{
          width: { xs: "100%", md: "50%" },
          display: "flex",
          flexDirection: "column",
          justifyContent: "center",
          px: { xs: 4, md: 8 },
          mt: { xs: 4, md: 0 },
        }}
      >
        <Typography
          variant="h2"
          sx={{ color: "#3D5943", fontWeight: "bold", mb: 2 }}
        >
          Real-time Health Monitoring for Soldiers
        </Typography>

        <Typography variant="h5" sx={{ mb: 2 }}>
          Anytime, Anywhere
        </Typography>

        <Typography sx={{ mb: 4 }}>
          SoldiCare provides commanders and medics with real-time health
          tracking, critical alerts, and actionable insights to protect soldiers
          in the field.
        </Typography>

        <Button
          variant="contained"
          onClick={() => navigate("/login")}
          sx={{
            width: "fit-content",
            backgroundColor: "#3D5943",
            "&:hover": { backgroundColor: "#2f4634" },
            px: 4,
            py: 1.5,
            fontSize: "1rem",
            fontWeight: "bold",
            borderRadius: 999,
          }}
        >
          Get Started
        </Button>
      </Box>

      {/* Image Side - Half Circle Style */}
      <Box
        sx={{
          display: { xs: "none", md: "block" },
          width: { xs: "100%", md: "50%" },
          height: "100%",
          backgroundImage: `url(${heroImage})`,
          backgroundSize: "cover",
          backgroundPosition: "center",
          clipPath: {
            md: "ellipse(90% 90% at 90% 50%)",
            xs: "none",
          },
        }}
      />
    </Box>
  );
}
