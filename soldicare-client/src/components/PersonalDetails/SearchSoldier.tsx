import { useEffect, useState, type Dispatch, type SetStateAction } from "react";
import {
  Box,
  CircularProgress,
  Typography,
  Avatar,
  InputAdornment,
  TextField,
} from "@mui/material";
import SearchIcon from "@mui/icons-material/Search";
import soldierAvatar from "../../assets/profile_soldier.png";
import { useAppContext } from "../../context/AppContext";
import { getSoldiers } from "../../services/SoldierService";
import type { Soldier } from "../../interface/soldier.interface";

interface SoldierListProps {
  soldiers: Soldier[];
  setSoldiers: Dispatch<SetStateAction<Soldier[]>>;
  selectedId: string | null;
  setSelectedId: Dispatch<SetStateAction<string | null>>;
}

const SearchSoldier: React.FC<SoldierListProps> = ({
  soldiers,
  setSoldiers,
  selectedId,
  setSelectedId,
}) => {
  const { email, systemID } = useAppContext();

  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchSoldiers = async () => {
      setLoading(true);
      try {
        const data = await getSoldiers(systemID, email);
        setSoldiers(data);
      } catch (error) {
        console.error("Failed to fetch soldiers:", error);
      } finally {
        setLoading(false);
      }
    };

    if (systemID && email) {
      fetchSoldiers();
    }
  }, [systemID, email]);

  const handleSelect = (id: string) => {
    setSelectedId(id);
    console.log("Selected soldier:", id);
  };

  return (
    <Box
      bgcolor="#f5f5f5"
      p={2}
      display="flex"
      flexDirection="column"
      height="100%"
    >
      <TextField
        placeholder="Search for soldier name or ID"
        variant="outlined"
        size="small"
        fullWidth
        sx={{
          mb: 2,
          "& .MuiOutlinedInput-root": {
            borderRadius: "999px",
            backgroundColor: "#ffffff",
          },
        }}
        InputProps={{
          startAdornment: (
            <InputAdornment position="start">
              <SearchIcon />
            </InputAdornment>
          ),
        }}
      />

      {loading ? (
        <Box
          display="flex"
          justifyContent="center"
          alignItems="center"
          flex={1}
        >
          <CircularProgress />
        </Box>
      ) : (
        soldiers.map((soldier) => {
          const isSelected = selectedId === soldier.id.objectId;

          return (
            <Box
              key={soldier.id.objectId}
              display="flex"
              alignItems="center"
              gap={1}
              py={1}
              px={1}
              borderBottom="1px solid #ccc"
              onClick={() => handleSelect(soldier.id.objectId)}
              sx={{
                cursor: "pointer",
                backgroundColor: isSelected ? "#ffffff" : "transparent",
                transition: "background-color 0.2s ease-in-out",
                "&:hover": {
                  backgroundColor: "#ffffff",
                },
              }}
            >
              <Avatar src={soldierAvatar} sx={{ width: 36, height: 36 }} />
              <Box>
                <Typography fontWeight="bold" fontSize="14px">
                  {soldier.objectDetails.name}
                </Typography>
                <Typography fontSize="13px" color="text.secondary">
                  {soldier.alias}
                </Typography>
              </Box>
            </Box>
          );
        })
      )}
    </Box>
  );
};

export default SearchSoldier;
