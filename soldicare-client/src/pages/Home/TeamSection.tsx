import { Box, Typography, Avatar } from "@mui/material";
import { motion } from "framer-motion";

import idanAvatar from "../../assets/idan_avatar.png";
import itayAvatar from "../../assets/itay_avatar.png";
import sapirAvatar from "../../assets/sapir_avatar.png";
import netaAvatar from "../../assets/neta_avatar.png";
import roiAvatar from "../../assets/roi_avatar.png";

const teamMembers = [
  {
    name: "Itay Chabra",
    role: "Team Lead & DBA",
    img: itayAvatar,
  },
  {
    name: "Sapir Gilany",
    role: "Scrum Master & Tech Writer & System Architecture & UI/UX",
    img: sapirAvatar,
  },
  {
    name: "Idan Noyshul",
    role: "Devops  & System Architecture & UI/UX",
    img: idanAvatar,
  },
  {
    name: "Neta Ben Mordechai",
    role: "Product Owner",
    img: netaAvatar,
  },
  {
    name: "Roi Dor",
    role: "QA Engineer ",
    img: roiAvatar,
  },
];

export default function TeamSection() {
  return (
    <Box
      id="team"
      textAlign="center"
      px={3}
      py={10}
      sx={{ backgroundColor: "white" }}
    >
      <motion.div
        initial={{ opacity: 0, y: 50 }}
        whileInView={{ opacity: 1, y: 0 }}
        viewport={{ once: true, amount: 0.3 }}
        transition={{ duration: 0.6, ease: "easeOut" }}
      >
        <Typography variant="h3" fontWeight={700} gutterBottom>
          Who We Are
        </Typography>
        <Typography variant="h6" gutterBottom>
          Meet the Team Behind SoldiCare
        </Typography>

        <Box
          display="grid"
          gridAutoFlow="column"
          gap={6}
          justifyContent="center"
          mt={6}
          sx={{
            width: "100%",
            maxWidth: "100%",
            paddingBottom: 2,
            overflowX: "auto",
            scrollbarWidth: "none", // Firefox
            "&::-webkit-scrollbar": {
              display: "none", // Chrome/Safari
            },
          }}
        >
          {teamMembers.map((member, index) => (
            <motion.div
              key={member.name}
              initial={{ opacity: 0, y: 30 }}
              whileInView={{ opacity: 1, y: 0 }}
              viewport={{ once: true, amount: 0.3 }}
              transition={{ duration: 0.5, delay: index * 0.1 }}
            >
              <Box minWidth="160px">
                <Avatar
                  src={member.img}
                  alt={member.name}
                  sx={{ width: 100, height: 100, margin: "0 auto" }}
                />
                <Typography variant="subtitle1" fontWeight="bold" mt={1}>
                  {member.name}
                </Typography>
                <Typography variant="body2" color="text.secondary">
                  {member.role}
                </Typography>
              </Box>
            </motion.div>
          ))}
        </Box>

        <Typography
          variant="body1"
          maxWidth="md"
          mx="auto"
          mt={6}
          fontSize="1rem"
        >
          We are a team of software engineering students driven by a shared
          vision: to harness technology for real-time military health
          monitoring...
        </Typography>

        <Typography
          variant="body1"
          fontWeight="bold"
          maxWidth="md"
          mx="auto"
          mt={4}
          fontSize="1rem"
        >
          From classroom concepts to real-world solutions...
        </Typography>
      </motion.div>
    </Box>
  );
}
