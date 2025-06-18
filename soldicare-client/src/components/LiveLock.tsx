import { useEffect, useState } from "react";
import { Typography } from "@mui/material";

interface LiveClockProps {
  /**
   * Optional timestamp in ISO format or anything parsable by `new Date()`.
   * If not provided, the clock will start from a random time between 00:00 and 04:00.
   */
  initialTimestamp?: string;
}

const LiveClock: React.FC<LiveClockProps> = ({ initialTimestamp }) => {
  // Return a time between 00:00:00 and 04:00:00
  const getRandomStartTime = (): Date => {
    const today = new Date();
    today.setHours(0, 0, 0, 0); // 00:00:00
    const randomSeconds = Math.floor(Math.random() * 4 * 60 * 60); // up to 4 hours
    today.setSeconds(today.getSeconds() + randomSeconds);
    return today;
  };

  // Determine initial time
  const getStartTime = (): Date => {
    if (initialTimestamp) {
      const parsed = new Date(initialTimestamp);
      if (!isNaN(parsed.getTime())) return parsed;
    }
    return getRandomStartTime();
  };

  const [time, setTime] = useState<Date>(getStartTime());

  useEffect(() => {
    const interval = setInterval(() => {
      setTime((prev) => new Date(prev.getTime() + 1000)); // Add 1 second
    }, 1000);

    return () => clearInterval(interval);
  }, []);

  return (
    <Typography
      sx={{
        fontSize: "0.875rem",
        fontWeight: "medium",
        fontFamily: "monospace",
      }}
    >
      {time.toTimeString().split(" ")[0]} {/* Format: HH:MM:SS */}
    </Typography>
  );
};

export default LiveClock;
