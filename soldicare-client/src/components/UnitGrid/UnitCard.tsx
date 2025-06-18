import React from "react";
import {
  Item,
  CustomAccordion,
  CustomAccordionSummary,
  CustomAccordionDetails,
} from "./UnitCard.styles";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import UnitAccordionHeader from "./UnitAccordionHeader";
import UnitAccordionDetails from "./UnitAccordionDetails";
import type { UnitData } from "../../interface/UnitData.interface";

interface Props {
  unit: UnitData;
  isExpanded: boolean;
  onToggle: () => void;
  displaySpecificUnit: (unitId: string) => void;
}

const UnitCard: React.FC<Props> = ({
  unit,
  isExpanded,
  onToggle,
  displaySpecificUnit,
}) => {
  return (
    <Item>
      <CustomAccordion expanded={isExpanded} onChange={onToggle}>
        <CustomAccordionSummary expandIcon={<ExpandMoreIcon />}>
          <UnitAccordionHeader
            unit={unit}
            displaySpecificUnit={displaySpecificUnit}
          />
        </CustomAccordionSummary>
        <CustomAccordionDetails>
          <UnitAccordionDetails unit={unit} />
        </CustomAccordionDetails>
      </CustomAccordion>
    </Item>
  );
};

export default UnitCard;
