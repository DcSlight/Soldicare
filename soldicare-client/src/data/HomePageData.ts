import SoldierMonitorImg from "../assets/soldier with monitor.png";
import SoldierMedicImg from "../assets/soldierMedic.jpg";
import MilitarySoldierImg from "../assets/military-soldiers.jpg";
import DataHistorySVG from "../assets/health-data-history.svg";
import HealthMonitoringSVG from "../assets/health monitoring on a map.svg";
import DashboardSVG from "../assets/dashboard.svg";
import type { ImageDes } from "../interface/ImageDes.interface";

export const servicesData: ImageDes[] = [
  {
    imageSrc: SoldierMedicImg,
    title: "Real-Time Health Monitoring",
    description:
      "Continuous medical monitoring of soldiers using Ambient Intelligence Invisible (AII) sensors to detect early signs of health issues",
  },
  {
    imageSrc: SoldierMonitorImg,
    title: "Smart Emergency Alerts",
    description:
      "Intelligent emergency alerts that enable commanders and medical teams to identify and prioritize critical cases",
  },
  {
    imageSrc: MilitarySoldierImg,
    title: "Combat Readiness Assessment",
    description:
      "Analysis of soldiers’ physical readiness based on medical and physiological data to support operational decisions",
  },
];

export const YourHealth: ImageDes[] = [
  {
    imageSrc: DataHistorySVG,
    title: "Health Data History & Trends",
    description:
      "Access to historical health data, tracking soldiers’ conditions over time and identifying critical trends",
  },
  {
    imageSrc: HealthMonitoringSVG,
    title: "Event Monitoring on a Map",
    description:
      "Real-time monitoring of medical incidents on a unit-wide map, displaying soldier locations and alerts by severity",
  },
  {
    imageSrc: DashboardSVG,
    title: "Customizable Data Dashboard",
    description:
      "Advanced dashboard with smart filters, allowing commanders to tailor data visualization to their specific needs",
  },
];
