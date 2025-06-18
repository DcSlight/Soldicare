import {
  AppBar,
  Toolbar,
  Typography,
  IconButton,
  Box,
  Avatar,
  Button,
} from "@mui/material";
import MenuIcon from "@mui/icons-material/Menu";
import SearchIcon from "@mui/icons-material/Search";
import SettingsIcon from "@mui/icons-material/Settings";
import { useAppContext } from "../context/AppContext";
import logo from "../assets/logo.png";
import { useNavigate } from "react-router-dom";
import soldierImg from "../assets/profile_soldier.png";
import { useState } from "react";
import SettingsPopper from "./SettingsPopper";

interface TopNavProps {
  onMenuClick: () => void;
}

export default function TopNav({ onMenuClick }: TopNavProps) {
  const { username } = useAppContext();
  const navigate = useNavigate();

  const navigateHome = () => navigate("/");
  const navigateLogin = () => navigate("/login");
  const navigateSignup = () => navigate("/sign-up");

  const [settingsAnchor, setSettingsAnchor] = useState<null | HTMLElement>(
    null
  );
  const settingsOpen = Boolean(settingsAnchor);

  const handleSettingsClick = (event: React.MouseEvent<HTMLElement>) => {
    setSettingsAnchor(settingsOpen ? null : event.currentTarget);
  };

  const handleSettingsClose = () => {
    setSettingsAnchor(null);
  };

  return (
    <AppBar
      position="static"
      sx={{ backgroundColor: "white", color: "#3D5943", boxShadow: "none" }}
    >
      <Toolbar sx={{ justifyContent: "space-between" }}>
        {/* Left: Menu + Logo */}
        <Box display="flex" alignItems="center" gap={1}>
          <IconButton edge="start" onClick={onMenuClick}>
            <MenuIcon sx={{ color: "#3D5943" }} />
          </IconButton>
          <img
            src={logo}
            alt="logo"
            style={{ height: 40, cursor: "pointer" }}
            onClick={navigateHome}
          />
        </Box>

        <Box display="flex" alignItems="center" gap={2}>
          {username ? (
            <>
              <Button
                variant="contained"
                startIcon={<SearchIcon />}
                sx={{
                  backgroundColor: "#3D5943",
                  borderRadius: "20px",
                  textTransform: "none",
                  minWidth: {
                    xs: "120px",
                    sm: "160px",
                    md: "200px",
                  },
                  justifyContent: "flex-start",
                  pl: 2,
                  display: { xs: "none", md: "flex" },
                  "&:hover": { backgroundColor: "#2f4433" },
                }}
              >
                Search
              </Button>

              <Avatar alt="User Avatar" src={soldierImg} />
              <Typography
                variant="body1"
                fontWeight={600}
                sx={{ display: { xs: "none", md: "block" } }}
              >
                {username}
              </Typography>
              <IconButton
                sx={{ display: { xs: "none", md: "inline-flex" } }}
                onClick={handleSettingsClick}
              >
                <SettingsIcon sx={{ color: "#3D5943" }} />
              </IconButton>

              <SettingsPopper
                anchorEl={settingsAnchor}
                open={settingsOpen}
                onClose={handleSettingsClose}
              />
            </>
          ) : (
            <>
              <Button
                color="inherit"
                sx={{ fontWeight: 700 }}
                onClick={() => {
                  const element = document.getElementById("services");
                  if (element) {
                    element.scrollIntoView({ behavior: "smooth" });
                  } else {
                    navigate("/#services");
                  }
                }}
              >
                Services
              </Button>
              <Button
                color="inherit"
                sx={{ fontWeight: 700 }}
                onClick={() => {
                  const element = document.getElementById("about");
                  if (element) {
                    element.scrollIntoView({ behavior: "smooth" });
                  } else {
                    navigate("/#about");
                  }
                }}
              >
                About
              </Button>
              <Button
                color="inherit"
                sx={{ fontWeight: 700 }}
                onClick={navigateLogin}
              >
                Log in
              </Button>
              <Button
                variant="contained"
                onClick={navigateSignup}
                sx={{
                  backgroundColor: "#3D5943",
                  borderRadius: "20px",
                  textTransform: "none",
                  "&:hover": { backgroundColor: "#2f4433" },
                }}
              >
                Sign up
              </Button>
            </>
          )}
        </Box>
      </Toolbar>
    </AppBar>
  );
}
