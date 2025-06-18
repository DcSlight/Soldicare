import HeroSection from "./HeroSection";
import ImageDescription from "./ImageDecription";
import { servicesData, YourHealth } from "../../data/HomePageData";
import TeamSection from "./TeamSection";

export default function HomePage() {
  return (
    <>
      <HeroSection />
      <ImageDescription
        id="services"
        header={"Our Services"}
        data={servicesData}
        backgroundGradient="linear-gradient(to bottom, #f5f5f5, #eaffea)"
      />

      <ImageDescription
        id="about"
        header={"Your Health, Your Strength"}
        data={YourHealth}
        backgroundColor="#EAF7E7"
      />

      <TeamSection />
    </>
  );
}
