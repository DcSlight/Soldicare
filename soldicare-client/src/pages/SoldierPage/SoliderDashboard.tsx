import {
  Box,
  Typography,
  Avatar,
  TextField,
  InputAdornment,
} from "@mui/material";
import SearchIcon from "@mui/icons-material/Search";
import VitalsSection from "../../components/AlertPerSoldier/VitalsSection";
import ChartsGrid from "../../components/AlertPerSoldier/ChartsGrid";
import soldierAvater from "../../assets/profile_soldier.png";
import SearchSoldier from "../../components/PersonalDetails/SearchSoldier";
import InfoCards from "../../components/PersonalDetails/InfoCards";
import MedicalInfoCards from "../../components/PersonalDetails/MedicalInfoCards";
import type { Soldier } from "../../interface/soldier.interface";
import { useState } from "react";
import { useAppContext } from "../../context/AppContext";
import { useSoldierData } from "../../hooks/useSoldierData";
import type { Snapshot } from "../../interface/alert.interface";

const EMPTY_SNAPSHOT: Snapshot = {
  id: { objectId: "", systemID: "" },
  type: "sensor",
  alias: "allSensor",
  status: "INACTIVE",
  active: false,
  creationTimestamp: new Date().toISOString(),
  createdBy: {
    userId: {
      email: "",
      systemID: "",
    },
  },
  objectDetails: {
    Temperature: "",
    HeartRate: "",
    BloodOxygen: "",
    BloodPressure: "",
    Hydration: "",
    ECG: "",
    EEG: "",
    location: {
      lat: "0",
      lng: "0",
    },
  },
};

const SoliderDashboard = () => {
  const { email, systemID } = useAppContext();
  const [soldiers, setSoldiers] = useState<Soldier[]>([]);
  const [selectedId, setSelectedId] = useState<string | null>(null);

  const { loading, activeSoldier, soldierUnit, snapshotHistory } =
    useSoldierData(soldiers, selectedId, systemID, email);

  return (
    <Box display="grid" gridTemplateColumns="250px 1fr" height="100%">
      {/* Sidebar */}
      <SearchSoldier
        soldiers={soldiers}
        setSoldiers={setSoldiers}
        selectedId={selectedId}
        setSelectedId={setSelectedId}
      />
      {!loading && activeSoldier && (
        <Box display="flex" flexDirection="column">
          {/* Header */}
          <Box bgcolor="#D3D9D2" display="flex" alignItems="center" padding={5}>
            <Avatar
              sx={{ width: 120, height: 120, mr: 2 }}
              src={soldierAvater}
            />
            <Box>
              <Typography>{activeSoldier?.objectDetails.name}</Typography>
              <Typography>{soldierUnit?.name}</Typography>
            </Box>
          </Box>

          {/* Content: Main Grid + Bottom Search */}
          <Box padding={2}>
            {/* Main Grid */}
            <Box display="grid" gridTemplateColumns="1fr 2fr" gap={2}>
              {/* Left Section */}
              <Box>
                <VitalsSection
                  relatedSoldier={{
                    id: {
                      objectId: String(activeSoldier.id.objectId),
                      systemID: activeSoldier.id.systemID,
                    },
                    name: activeSoldier.objectDetails.name,
                    unitName: String(soldierUnit?.id.objectId),
                    serial: activeSoldier.objectDetails.serial ?? "Unknown",
                  }}
                  lastSnapshot={snapshotHistory[0] ?? EMPTY_SNAPSHOT}
                />
                <InfoCards soldier={activeSoldier} />
              </Box>

              {/* Right Section */}
              <ChartsGrid snapshotHistory={snapshotHistory} />
            </Box>
            {/* Bottom Search */}
            <Box mt={4} display="flex" justifyContent="flex-end">
              <TextField
                placeholder="Search e.g allergy"
                variant="outlined"
                size="small"
                sx={{ width: 250 }}
                InputProps={{
                  startAdornment: (
                    <InputAdornment position="start">
                      <SearchIcon />
                    </InputAdornment>
                  ),
                }}
              />
            </Box>
            <MedicalInfoCards />
          </Box>
        </Box>
      )}
    </Box>
  );
};

export default SoliderDashboard;
