import { Box, Typography } from "@mui/material";
import { motion } from "framer-motion";
import type { ImageDes } from "../../interface/ImageDes.interface";

interface IProps {
  id: string;
  header: string;
  data: ImageDes[];
  backgroundGradient?: string;
  backgroundColor?: string;
}

const ImageDescription: React.FC<IProps> = ({
  header,
  data,
  backgroundGradient,
  backgroundColor,
  id,
}) => {
  return (
    <Box
      id={id}
      sx={{
        py: 8,
        px: 4,
        textAlign: "center",
        background: backgroundGradient || undefined,
        backgroundColor: backgroundColor || undefined,
      }}
    >
      <Typography
        variant="h4"
        fontSize={50}
        sx={{ color: "#3D5943" }}
        gutterBottom
      >
        {header}
      </Typography>

      <Box
        sx={{
          display: "grid",
          gap: 4,
          gridTemplateColumns: {
            xs: "1fr",
            sm: "1fr 1fr",
            md: "1fr 1fr 1fr",
          },
          mt: 4,
        }}
      >
        {data.map((service, index) => (
          <motion.div
            key={index}
            initial={{ opacity: 0, y: 30 }}
            whileInView={{ opacity: 1, y: 0 }}
            viewport={{ once: true, amount: 0.2 }}
            transition={{ duration: 0.6, delay: index * 0.2 }}
          >
            <Box sx={{ maxWidth: 360, mx: "auto" }}>
              <Box
                component="img"
                src={service.imageSrc}
                alt={service.title}
                sx={{
                  width: "80%",
                  height: "70%",
                  objectFit: "cover",
                  borderRadius: 4,
                  mb: 2,
                }}
              />
              <Typography variant="h6" fontWeight={700} gutterBottom>
                {service.title}
              </Typography>
              <Typography variant="body2" fontSize={15} color="text.secondary">
                {service.description}
              </Typography>
            </Box>
          </motion.div>
        ))}
      </Box>
    </Box>
  );
};

export default ImageDescription;
