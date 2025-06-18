import { Box, Typography, Tooltip } from "@mui/material";

interface Props {
  status: number;
  max: number;
}

const StatusIndicator: React.FC<Props> = ({ status, max }) => {
  const percent = (status / max) * 100;

  const getColor = () => {
    if (percent < 33) return "#f44336";
    if (percent < 66) return "#ff9800";
    return "#4CAF50";
  };

  const color = getColor();

  return (
    <Box display="flex" alignItems="center" gap={0.5}>
      <Typography fontSize={10}>Status</Typography>
      <Box
        display="flex"
        alignItems="center"
        px={0.8}
        py={0.2}
        border={`1px solid ${color}`}
        borderRadius="12px"
        color={color}
        fontWeight={500}
        fontSize={10}
        gap={0.5}
      >
        <Typography fontSize={10}>
          {status}/{max}
        </Typography>

        <Tooltip title={`Status is ${status} out of ${max}`}>
          <Box
            sx={{
              width: 16,
              height: 6,
              border: `1px solid ${color}`,
              position: "relative",
              borderRadius: "1px",
              overflow: "hidden",
              cursor: "help",
            }}
          >
            <Box
              sx={{
                position: "absolute",
                top: 0,
                left: 0,
                height: "100%",
                width: `${percent}%`,
                backgroundColor: color,
                transition: "width 0.3s ease",
              }}
            />
          </Box>
        </Tooltip>
      </Box>
    </Box>
  );
};

export default StatusIndicator;
