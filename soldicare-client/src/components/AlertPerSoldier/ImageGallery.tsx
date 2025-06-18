import {
  Dialog,
  DialogContent,
  DialogTitle,
  IconButton,
  Box,
  Typography,
  CircularProgress,
} from "@mui/material";
import React, { useEffect, useState } from "react";
import CloseIcon from "@mui/icons-material/Close";
import ArrowBackIosNewIcon from "@mui/icons-material/ArrowBackIosNew";
import ArrowForwardIosIcon from "@mui/icons-material/ArrowForwardIos";
import { storage } from "../../firebase";
import { getDownloadURL, listAll, ref } from "firebase/storage";
import { ensureAnonymousAuth } from "../../services/firebaseBaseAuth";

interface IProps {
  openGallery: boolean;
  soldierSerial: string;
  setOpenGallery: React.Dispatch<React.SetStateAction<boolean>>;
}

const ImageGallery: React.FC<IProps> = ({
  openGallery,
  soldierSerial,
  setOpenGallery,
}) => {
  const [images, setImages] = useState<string[]>([]);
  const [currentIndex, setCurrentIndex] = useState(0);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const fetchImages = async () => {
      try {
        setLoading(true);
        await ensureAnonymousAuth();

        const folderRef = ref(storage, "soldier_images/");
        const result = await listAll(folderRef);
        const urls: string[] = [];

        for (const itemRef of result.items) {
          const fileName = itemRef.name; // e.g. "soldier_7654321.1750080304448.jpg"
          const match = fileName.match(/^soldier_(\d+)_/);
          const idFromFile = match ? match[1] : null;
          if (idFromFile === soldierSerial) {
            const url = await getDownloadURL(itemRef);
            urls.push(url);
          }
        }

        setImages(urls);
        setCurrentIndex(0);
      } catch (err) {
        console.error("Error fetching images:", err);
      } finally {
        setLoading(false);
      }
    };

    if (openGallery) {
      fetchImages();
    }
  }, [openGallery, soldierSerial]);

  const handlePrev = () => {
    setCurrentIndex((prev) => (prev - 1 + images.length) % images.length);
  };

  const handleNext = () => {
    setCurrentIndex((prev) => (prev + 1) % images.length);
  };

  return (
    <Dialog
      open={openGallery}
      onClose={() => setOpenGallery(false)}
      maxWidth="md"
      fullWidth
    >
      <DialogTitle
        sx={{
          display: "flex",
          justifyContent: "space-between",
          alignItems: "center",
        }}
      >
        <Typography variant="h6">Gallery</Typography>
        <IconButton onClick={() => setOpenGallery(false)}>
          <CloseIcon />
        </IconButton>
      </DialogTitle>

      <DialogContent>
        {loading ? (
          <Box
            display="flex"
            justifyContent="center"
            alignItems="center"
            minHeight="300px"
          >
            <CircularProgress />
          </Box>
        ) : images.length > 0 ? (
          <Box
            display="flex"
            flexDirection="column"
            alignItems="center"
            justifyContent="center"
            position="relative"
            minHeight="400px"
          >
            <img
              src={images[currentIndex]}
              alt={`Image ${currentIndex + 1}`}
              style={{
                maxWidth: "100%",
                maxHeight: "400px",
                borderRadius: "8px",
                boxShadow: "0 4px 12px rgba(0,0,0,0.2)",
              }}
            />

            <IconButton
              onClick={handlePrev}
              sx={{
                position: "absolute",
                left: 0,
                top: "50%",
                transform: "translateY(-50%)",
              }}
            >
              <ArrowBackIosNewIcon />
            </IconButton>
            <IconButton
              onClick={handleNext}
              sx={{
                position: "absolute",
                right: 0,
                top: "50%",
                transform: "translateY(-50%)",
              }}
            >
              <ArrowForwardIosIcon />
            </IconButton>

            <Box mt={2} display="flex" gap={1}>
              {images.map((_, index) => (
                <Box
                  key={index}
                  width={10}
                  height={10}
                  borderRadius="50%"
                  bgcolor={index === currentIndex ? "primary.main" : "grey.400"}
                  sx={{ transition: "background-color 0.3s" }}
                />
              ))}
            </Box>
          </Box>
        ) : (
          <Typography align="center" color="text.secondary">
            No images found for this soldier.
          </Typography>
        )}
      </DialogContent>
    </Dialog>
  );
};

export default ImageGallery;
