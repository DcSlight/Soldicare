import {
  Drawer,
  List,
  ListItem,
  ListItemText,
  ListItemButton,
  Box,
  Typography,
  IconButton,
  Divider,
} from "@mui/material";
import MenuIcon from "@mui/icons-material/Menu";
import { useAppContext } from "../context/AppContext";
import { useNavigate } from "react-router-dom";
import logoWhite from "../assets/logo white.svg";
import NotificationsIcon from "@mui/icons-material/Notifications";
import MapIcon from "@mui/icons-material/Map";
import PersonIcon from "@mui/icons-material/Person";
import LoginIcon from "@mui/icons-material/Login";
import LogoutIcon from "@mui/icons-material/Logout";
import ListItemIcon from "@mui/material/ListItemIcon";

interface SideDrawerProps {
  open: boolean;
  onClose: () => void;
}

export default function SideDrawer({ open, onClose }: SideDrawerProps) {
  const { isLoggedIn, logout } = useAppContext();
  const navigate = useNavigate();

  const handleNavigate = (path: string) => {
    navigate(path);
    onClose();
  };

  const handleLogout = () => {
    logout();
    handleNavigate("/");
  };

  const menuItems = [
    {
      label: "Alerts",
      route: "/soldier-alert",
      icon: <NotificationsIcon />,
      showWhenLoggedIn: true,
      showWhenLoggedOut: false,
    },
    {
      label: "Map",
      route: "/map-view",
      icon: <MapIcon />,
      showWhenLoggedIn: true,
      showWhenLoggedOut: false,
    },
    {
      label: "Personal Details",
      route: "/soldier-dashboard",
      icon: <PersonIcon />,
      showWhenLoggedIn: true,
      showWhenLoggedOut: false,
    },
    {
      label: "Login",
      route: "/login",
      icon: <LoginIcon />,
      showWhenLoggedIn: false,
      showWhenLoggedOut: true,
    },
    {
      label: "Logout",
      action: handleLogout,
      icon: <LogoutIcon />,
      showWhenLoggedIn: true,
      showWhenLoggedOut: false,
    },
  ];

  const visibleItems = menuItems.filter((item) =>
    isLoggedIn ? item.showWhenLoggedIn : item.showWhenLoggedOut
  );

  return (
    <Drawer anchor="left" open={open} onClose={onClose}>
      <Box
        sx={{
          width: 280,
          height: "100%",
          backgroundColor: "#41654B", // צבע רקע מהפיגמה
          color: "white", // טקסט לבן
        }}
      >
        {/* Header */}
        <Box
          sx={{
            display: "flex",
            alignItems: "center",
            gap: 1,
            p: 2,
          }}
        >
          <IconButton size="small" disableRipple sx={{ color: "white" }}>
            <MenuIcon />
          </IconButton>
          <img src={logoWhite} alt="logo" style={{ height: 30 }} />
        </Box>

        {/* Menu */}
        <List>
          {visibleItems.map((item, index) => (
            <Box key={index}>
              <ListItem disablePadding>
                <ListItemButton
                  onClick={
                    item.action
                      ? item.action
                      : () => handleNavigate(item.route!)
                  }
                  sx={{ color: "white", px: 3 }}
                >
                  <ListItemIcon sx={{ color: "white", minWidth: 32 }}>
                    {item.icon}
                  </ListItemIcon>
                  <ListItemText
                    primary={
                      <Typography fontWeight={500} color="white">
                        {item.label}
                      </Typography>
                    }
                  />
                </ListItemButton>
              </ListItem>

              <Divider sx={{ backgroundColor: "white", opacity: 0.3 }} />
            </Box>
          ))}
        </List>
      </Box>
    </Drawer>
  );
}
