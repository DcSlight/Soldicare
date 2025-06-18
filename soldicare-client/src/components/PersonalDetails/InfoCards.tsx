import { Box, Typography, Paper } from "@mui/material";
import type { Soldier } from "../../interface/soldier.interface";

interface IProps {
  soldier: Soldier;
}

const InfoCards: React.FC<IProps> = ({ soldier }) => {
  return (
    <Box mt={2} display="flex" justifyContent="space-between">
      {[
        { label: "AGE", value: soldier.objectDetails.age },
        { label: "Blood Type", value: soldier.objectDetails.bloodType },
        {
          label: "Medical Condition",
          value: soldier.objectDetails.MedicalCondition,
        },
      ].map((item) => (
        <Paper
          key={item.label}
          sx={{
            p: 2,
            width: "30%",
            textAlign: "center",
            transition: "0.2s",
            "&:hover": {
              backgroundColor: "#f5f5f5",
              transform: "scale(1.02)",
            },
          }}
        >
          <Typography variant="body2" color="text.secondary">
            {item.label}
          </Typography>
          <Typography
            variant="h6"
            sx={{
              fontSize: "15px",
              wordBreak: "break-word", // allows breaking long strings
              whiteSpace: "pre-wrap", // allows newlines if added
            }}
          >
            {Array.isArray(item.value)
              ? item.value.join(", ")
              : item.value ?? "N/A"}
          </Typography>
        </Paper>
      ))}
    </Box>
  );
};

export default InfoCards;
