import {
  Box,
  Paper,
  Popper,
  Typography,
  ClickAwayListener,
  Divider,
  Button,
  Avatar,
  IconButton,
} from "@mui/material";
import CloseIcon from "@mui/icons-material/Close";
import soldierImg from "../assets/profile_soldier.png";
import { useAppContext } from "../context/AppContext";

interface Props {
  anchorEl: null | HTMLElement;
  open: boolean;
  onClose: () => void;
}

export default function SettingsPopper({ anchorEl, open, onClose }: Props) {
  const { username, email } = useAppContext();
  return (
    <Popper
      open={open}
      anchorEl={anchorEl}
      placement="bottom-end"
      disablePortal
      modifiers={[
        {
          name: "zIndex",
          enabled: true,
          phase: "write",
          fn: ({ state }) => {
            state.elements.popper.style.zIndex = "1500";
          },
        },
      ]}
    >
      <ClickAwayListener onClickAway={onClose}>
        <Paper
          elevation={4}
          sx={{
            mt: 1,
            mr: 1,
            minWidth: 260,
            borderRadius: 4,
            p: 2.5,
            bgcolor: "#fff",
            position: "relative",
          }}
        >
          {/* Close Button */}
          <IconButton
            size="small"
            onClick={onClose}
            sx={{
              position: "absolute",
              top: 8,
              left: 8,
              backgroundColor: "#f0f0f0",
              "&:hover": { backgroundColor: "#e0e0e0" },
            }}
          >
            <CloseIcon fontSize="small" />
          </IconButton>

          {/* Avatar + Name + Email */}
          <Box display="flex" flexDirection="column" alignItems="center" mt={1}>
            <Avatar
              src={soldierImg}
              alt="User"
              sx={{ width: 80, height: 80, mb: 1 }}
            />
            <Typography fontWeight={600}>{username}</Typography>
            <Typography variant="body2" color="success.main">
              {email}
            </Typography>
          </Box>

          {/* Preferences box */}
          <Box
            mt={3}
            px={2}
            py={2}
            sx={{
              border: "1px solid #ddd",
              borderRadius: 3,
              backgroundColor: "#fafafa",
            }}
          >
            <Box display="flex" justifyContent="space-between" mb={1}>
              <Typography fontWeight={600}>Language</Typography>
              <Typography>EN</Typography>
            </Box>
            <Box display="flex" justifyContent="space-between">
              <Typography fontWeight={600}>Theme</Typography>
              <Typography>WHITE</Typography>
            </Box>
            <Divider sx={{ my: 1 }} />
            <Box display="flex" justifyContent="space-around">
              <Typography variant="body2" color="text.secondary">
                Policy
              </Typography>
              <Typography variant="body2" color="text.secondary">
                FAQ
              </Typography>
            </Box>
          </Box>

          {/* Log Out button */}
          <Button
            variant="outlined"
            fullWidth
            sx={{
              mt: 2,
              borderRadius: 3,
              textTransform: "none",
              fontWeight: 600,
              color: "#3D5943",
              borderColor: "#3D5943",
              "&:hover": {
                backgroundColor: "#f4f7f5",
                borderColor: "#3D5943",
              },
            }}
            onClick={() => alert("Logged out")}
          >
            Log Out
          </Button>
        </Paper>
      </ClickAwayListener>
    </Popper>
  );
}
