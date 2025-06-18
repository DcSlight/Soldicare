import { useEffect, useState } from "react";
import { CircularProgress, Typography, Box } from "@mui/material";
import AlertAccordion from "../../components/AlertPerSoldier/AlertAccordion";
import { getAlerts } from "../../services/AlertService";
import avatarImg from "../../assets/profile_soldier.png";
import SoldierAlertsHeader from "../../components/AlertPerSoldier/SoldierAlertsHeader";
import type {
  AlertResponse,
  RelatedSoldier,
} from "../../interface/alert.interface";

const SoldierAlert = () => {
  const [loading, setLoading] = useState(true);
  const [alerts, setAlerts] = useState<AlertResponse[]>([]);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchAlerts = async () => {
      try {
        setLoading(true);
        const response = await getAlerts(
          "2025b.Itay.Chabra",
          "joanna@demo.org"
        );
        setAlerts(response);
        console.log(response);
      } catch (err: any) {
        console.error("Failed to fetch alerts:", err);
        setError("Failed to load alerts.");
      } finally {
        setLoading(false);
      }
    };
    fetchAlerts();
  }, []);

  if (loading) {
    return (
      <Box
        display="flex"
        justifyContent="center"
        alignItems="center"
        height="200px"
      >
        <CircularProgress />
      </Box>
    );
  }

  if (error) {
    return (
      <Typography color="error" textAlign="center">
        {error}
      </Typography>
    );
  }

  return (
    <Box
      paddingLeft={5}
      paddingRight={5}
      paddingTop={2}
      paddingBottom={5}
      sx={{ backgroundColor: "#F4F3F2" }}
    >
      {/* HEADER */}
      <SoldierAlertsHeader />

      {/* ALERT ACCORDIONS */}

      {alerts.map((alert) => {
        const command = alert.objectDetails.command;
        const snapshotHistory = alert.objectDetails.snapshotHistory;
        const relatedSoldier = command.commandAttributes
          .relatedSoldier as RelatedSoldier;

        return (
          <AlertAccordion
            key={alert.id.objectId}
            soldierName={relatedSoldier?.name ?? "Unknown"}
            unitName={relatedSoldier?.unitName ?? "Unknown"}
            soldierSerial={relatedSoldier?.serial ?? "Unknown"}
            timestamp={new Date(alert.creationTimestamp).toLocaleTimeString()}
            status={alert.status}
            severity={alert.alias}
            profileImage={avatarImg}
            commandAttributes={command.commandAttributes}
            snapshotHistory={snapshotHistory}
            relatedSoldier={relatedSoldier}
          />
        );
      })}
    </Box>
  );
};

export default SoldierAlert;
