import { styled } from "@mui/material/styles";
import {
  Paper,
  Accordion,
  AccordionSummary,
  AccordionDetails,
} from "@mui/material";

export const Item = styled(Paper)(() => ({
  backgroundColor: "#fff",
  borderRadius: "16px",
  boxShadow: "0 4px 12px rgba(0, 0, 0, 0.08)",
  overflow: "hidden",
}));

export const CustomAccordion = styled(Accordion)(() => ({
  boxShadow: "none",
  borderRadius: "inherit",
  "&:before": {
    display: "none",
  },
}));

export const CustomAccordionSummary = styled(AccordionSummary)(() => ({
  padding: "16px",
  minHeight: "64px",
  marginBottom: 0,
  "& .MuiAccordionSummary-content": {
    margin: 0,
  },
}));

export const CustomAccordionDetails = styled(AccordionDetails)(() => ({
  padding: "16px",
  marginTop: 0,
}));
