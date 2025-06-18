package com.example.soldicare;

import android.Manifest;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.exifinterface.media.ExifInterface;
import android.graphics.Matrix;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageException;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "SoldicareCam";

    private ImageButton  shutterBtn;
    private ProgressBar  spinner;
    private View         flashView;
    private TextView     nfcStatusText;
    private ImageCapture imageCapture;

    // NFC related
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private IntentFilter[] intentFilters;
    private String[][] techLists;
    private String scannedNfcId = null;
    private boolean nfcSetupComplete = false;
    private boolean nfcForegroundDispatchEnabled = false;

    // Compression settings
    private static final int COMPRESSION_QUALITY = 75; // 0-100 (95% gives good balance)

    private final ActivityResultLauncher<String> permLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                    granted -> {
                        if (granted) startCamera();
                        else Toast.makeText(this,
                                "Camera permission required", Toast.LENGTH_LONG).show();
                    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);

        // Debug Firebase authentication
        debugFirebaseAuth();

        setContentView(R.layout.activity_main);

        shutterBtn = findViewById(R.id.captureButton);
        spinner    = findViewById(R.id.uploadSpinner);
        flashView  = findViewById(R.id.flashOverlay);
        nfcStatusText = findViewById(R.id.nfcStatusText);

        shutterBtn.setEnabled(false);
        shutterBtn.setOnClickListener(v -> takePhoto());

        if (spinner != null) spinner.setVisibility(View.GONE);
        if (flashView != null) flashView.setVisibility(View.INVISIBLE);

        // Initialize NFC with delay to ensure activity is fully created
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            initializeNFC();
            updateNfcStatus();
        }, 100);

        permLauncher.launch(Manifest.permission.CAMERA);

        // Handle NFC intent if app was launched via NFC
        handleNfcIntent(getIntent());
    }

    /* ───────────────────  FIREBASE AUTHENTICATION DEBUG  ─────────────────── */

    private void debugFirebaseAuth() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        Log.d(TAG, "Firebase Auth Debug:");
        Log.d(TAG, "User: " + (user != null ? user.getUid() : "null"));
        Log.d(TAG, "Anonymous: " + (user != null ? user.isAnonymous() : "n/a"));

        if (user == null) {
            Log.w(TAG, "No authenticated user - signing in anonymously");
            auth.signInAnonymously()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Anonymous sign-in successful");
                            FirebaseUser newUser = auth.getCurrentUser();
                            Log.d(TAG, "New user UID: " + (newUser != null ? newUser.getUid() : "null"));
                        } else {
                            Log.e(TAG, "Anonymous sign-in failed", task.getException());
                        }
                    });
        }
    }

    /* ───────────────────  FIREBASE UPLOAD METHODS  ─────────────────── */

    // Single method to handle authentication checks
    private void ensureAuthenticatedThenUpload(Runnable uploadAction) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user == null) {
            Log.w(TAG, "No authenticated user, signing in anonymously first");
            auth.signInAnonymously()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Anonymous auth successful, proceeding with upload");
                            uploadAction.run();
                        } else {
                            Log.e(TAG, "Anonymous auth failed", task.getException());
                            runOnUiThread(() -> {
                                Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show();
                            });
                        }
                    });
            return;
        }

        // User is authenticated, proceed with upload
        uploadAction.run();
    }

    // Create metadata for camera uploads
    private StorageMetadata createCameraUploadMetadata(String soldierId) {
        return new StorageMetadata.Builder()
                .setContentType("image/jpeg")
                .setCustomMetadata("SoldierID", soldierId)
                .setCustomMetadata("SystemID", "2025b.Itay.Chabra")
                .setCustomMetadata("captureDate", new java.text.SimpleDateFormat(
                        "yyyy-MM-dd", java.util.Locale.getDefault()).format(new java.util.Date()))
                .setCustomMetadata("captureTime", new java.text.SimpleDateFormat(
                        "HH:mm:ss", java.util.Locale.getDefault()).format(new java.util.Date()))
                .setCustomMetadata("captureDateTime", new java.text.SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault()).format(new java.util.Date()))
                .setCustomMetadata("UserID", "1234567")
                .setCustomMetadata("deviceModel", android.os.Build.MODEL)
                .setCustomMetadata("captureMethod", "NFC_SCAN")
                .build();
    }

    // Upload camera photo to Firebase Storage
    private void uploadToFirebaseStorage(byte[] imageBytes, String soldierId, Runnable onSuccess, Runnable onFailure) {
        String firebaseFilename = "soldier_" + soldierId + "_" + System.currentTimeMillis() + ".jpg";
        StorageReference storageRef = FirebaseStorage.getInstance()
                .getReference("soldier_images")
                .child(firebaseFilename);

        StorageMetadata metadata = createCameraUploadMetadata(soldierId);

        Log.d(TAG, "Starting upload for soldier " + soldierId + " to path: " + storageRef.getPath());

        storageRef.putBytes(imageBytes, metadata)
                .addOnProgressListener(taskSnapshot -> {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    Log.d(TAG, "Upload progress for " + soldierId + ": " + String.format("%.1f%%", progress));
                })
                .addOnSuccessListener(taskSnapshot -> {
                    Log.d(TAG, "Successfully uploaded photo for soldier " + soldierId);
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Photo uploaded with NFC ID: " + soldierId, Toast.LENGTH_SHORT).show();
                    });

                    if (onSuccess != null) onSuccess.run();
                })
                .addOnFailureListener(exception -> {
                    Log.e(TAG, "Failed to upload photo for soldier " + soldierId, exception);

                    // Detailed error logging
                    if (exception instanceof StorageException) {
                        StorageException storageException = (StorageException) exception;
                        Log.e(TAG, "Storage error code: " + storageException.getErrorCode());
                        Log.e(TAG, "Storage error message: " + storageException.getMessage());
                        Log.e(TAG, "HTTP result code: " + storageException.getHttpResultCode());
                    }

                    runOnUiThread(() -> {
                        Toast.makeText(this, "Upload failed: " + exception.getMessage(), Toast.LENGTH_LONG).show();
                    });

                    if (onFailure != null) onFailure.run();
                });
    }

    /* ───────────────────  NFC METHODS  ─────────────────── */

    private void initializeNFC() {
        Log.d(TAG, "Initializing NFC...");

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (nfcAdapter == null) {
            Log.d(TAG, "NFC not supported on this device");
            return;
        }

        if (!nfcAdapter.isEnabled()) {
            Log.d(TAG, "NFC is disabled");
            Toast.makeText(this, "Please enable NFC in settings", Toast.LENGTH_LONG).show();
            return;
        }

        // Create pending intent for NFC - CRITICAL: Use proper flags
        Intent intent = new Intent(this, getClass());
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Use FLAG_IMMUTABLE for Android 12+ compatibility, FLAG_MUTABLE for older versions
        int flags = PendingIntent.FLAG_UPDATE_CURRENT;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            flags |= PendingIntent.FLAG_MUTABLE;
        }

        pendingIntent = PendingIntent.getActivity(this, 0, intent, flags);

        // Setup intent filters - ONLY use the ones we need
        IntentFilter ndefFilter = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        IntentFilter tagFilter = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);

        intentFilters = new IntentFilter[]{tagFilter, ndefFilter};

        // Simplified tech lists - focus on what we actually need
        techLists = new String[][]{
                new String[]{Ndef.class.getName()},
                new String[]{NfcA.class.getName()},
                new String[]{NfcB.class.getName()},
                new String[]{NfcF.class.getName()},
                new String[]{NfcV.class.getName()}
        };

        nfcSetupComplete = true;
        Log.d(TAG, "NFC initialization complete");
    }

    private void updateNfcStatus() {
        if (nfcStatusText == null) return;

        if (nfcAdapter == null) {
            nfcStatusText.setText("NFC not supported");
            nfcStatusText.setTextColor(Color.RED);
        } else if (!nfcAdapter.isEnabled()) {
            nfcStatusText.setText("NFC disabled - Please enable in settings");
            nfcStatusText.setTextColor(Color.YELLOW);
        } else if (scannedNfcId != null) {
            nfcStatusText.setText("NFC ID: " + scannedNfcId);
            nfcStatusText.setTextColor(Color.GREEN);
        } else {
            nfcStatusText.setText("Ready to scan NFC tag");
            nfcStatusText.setTextColor(Color.WHITE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume called");

        // Add delay to ensure everything is ready
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            enableNfcForegroundDispatch();
        }, 200);

        updateNfcStatus();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause called");
        disableNfcForegroundDispatch();
    }

    private void enableNfcForegroundDispatch() {
        if (nfcAdapter != null && nfcAdapter.isEnabled() && nfcSetupComplete) {
            try {
                // Only disable if we know it's enabled to avoid IllegalStateException
                if (nfcForegroundDispatchEnabled) {
                    nfcAdapter.disableForegroundDispatch(this);
                    nfcForegroundDispatchEnabled = false;
                }

                // Small delay before re-enabling
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    try {
                        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, techLists);
                        nfcForegroundDispatchEnabled = true;
                        Log.d(TAG, "NFC foreground dispatch enabled successfully");
                    } catch (Exception e) {
                        Log.e(TAG, "Error enabling NFC foreground dispatch (delayed)", e);
                    }
                }, 50);

            } catch (Exception e) {
                Log.e(TAG, "Error in NFC foreground dispatch setup", e);
            }
        } else {
            Log.w(TAG, "Cannot enable NFC foreground dispatch - adapter=" +
                    (nfcAdapter != null) + ", enabled=" +
                    (nfcAdapter != null && nfcAdapter.isEnabled()) +
                    ", setup=" + nfcSetupComplete);
        }
    }

    private void disableNfcForegroundDispatch() {
        if (nfcAdapter != null && nfcForegroundDispatchEnabled) {
            try {
                nfcAdapter.disableForegroundDispatch(this);
                nfcForegroundDispatchEnabled = false;
                Log.d(TAG, "NFC foreground dispatch disabled");
            } catch (Exception e) {
                Log.e(TAG, "Error disabling NFC foreground dispatch", e);
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "onNewIntent called with action: " + intent.getAction());
        setIntent(intent);

        // Handle NFC intent immediately
        handleNfcIntent(intent);
    }

    private void handleNfcIntent(Intent intent) {
        if (intent == null) {
            Log.w(TAG, "Intent is null");
            return;
        }

        String action = intent.getAction();
        Log.d(TAG, "Handling NFC intent with action: " + action);

        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action) ||
                NfcAdapter.ACTION_TAG_DISCOVERED.equals(action) ||
                NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {

            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            if (tag != null) {
                Log.d(TAG, "NFC tag detected, attempting to read...");

                // Process in background thread to avoid blocking UI
                new Thread(() -> {
                    String nfcData = readNfcTag(tag);

                    // Update UI on main thread
                    runOnUiThread(() -> {
                        if (nfcData != null && !nfcData.isEmpty()) {
                            scannedNfcId = nfcData.trim();
                            Log.d(TAG, "NFC tag scanned successfully: " + scannedNfcId);
                            Toast.makeText(this, "NFC ID scanned: " + scannedNfcId, Toast.LENGTH_SHORT).show();
                            updateNfcStatus();
                        } else {
                            Log.w(TAG, "Could not read NFC tag data");
                            Toast.makeText(this, "Could not read NFC tag data", Toast.LENGTH_SHORT).show();
                        }
                    });
                }).start();

            } else {
                Log.w(TAG, "No NFC tag found in intent");
            }
        } else {
            Log.d(TAG, "Intent action not related to NFC: " + action);
        }
    }

    private String readNfcTag(Tag tag) {
        Log.d(TAG, "Reading NFC tag...");

        try {
            // First try to read NDEF data
            Ndef ndef = Ndef.get(tag);
            if (ndef != null) {
                Log.d(TAG, "NDEF tag detected");
                ndef.connect();

                if (ndef.isConnected()) {
                    NdefMessage ndefMessage = ndef.getNdefMessage();
                    if (ndefMessage != null) {
                        NdefRecord[] records = ndefMessage.getRecords();
                        Log.d(TAG, "Found " + records.length + " NDEF records");

                        for (NdefRecord record : records) {
                            Log.d(TAG, "Processing NDEF record, TNF: " + record.getTnf());

                            if (record.getTnf() == NdefRecord.TNF_WELL_KNOWN &&
                                    java.util.Arrays.equals(record.getType(), NdefRecord.RTD_TEXT)) {

                                byte[] payload = record.getPayload();
                                if (payload.length > 1) {
                                    // First byte contains the language code length
                                    int languageCodeLength = payload[0] & 0x3F;
                                    // Extract the text after the language code
                                    String text = new String(payload, languageCodeLength + 1,
                                            payload.length - languageCodeLength - 1, StandardCharsets.UTF_8);
                                    Log.d(TAG, "Successfully read NDEF text: " + text);
                                    ndef.close();
                                    return text;
                                }
                            }
                            // Also try reading as UTF-8 string directly for some tag types
                            else if (record.getTnf() == NdefRecord.TNF_WELL_KNOWN) {
                                try {
                                    String directText = new String(record.getPayload(), StandardCharsets.UTF_8);
                                    if (directText.length() > 0 && !directText.contains("\u0000")) {
                                        Log.d(TAG, "Read as direct UTF-8: " + directText);
                                        ndef.close();
                                        return directText;
                                    }
                                } catch (Exception e) {
                                    Log.d(TAG, "Could not read as direct UTF-8");
                                }
                            }
                        }
                    }
                }
                ndef.close();
                Log.d(TAG, "No readable text records found in NDEF message");
            } else {
                Log.d(TAG, "Not an NDEF tag, trying to read tag ID");
            }

            // Fallback: return tag ID as hex string
            byte[] tagId = tag.getId();
            if (tagId != null && tagId.length > 0) {
                String hexId = bytesToHex(tagId);
                Log.d(TAG, "Using tag ID as fallback: " + hexId);
                return hexId;
            }

        } catch (Exception e) {
            Log.e(TAG, "Error reading NFC tag", e);
        }

        Log.w(TAG, "Could not read any data from NFC tag");
        return null;
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }

    /* ───────────────────  CAMERA METHODS  ─────────────────── */

    private void startCamera() {
        PreviewView previewView = findViewById(R.id.previewView);

        ListenableFuture<ProcessCameraProvider> providerFuture =
                ProcessCameraProvider.getInstance(this);

        providerFuture.addListener(() -> {
            try {
                ProcessCameraProvider provider = providerFuture.get();

                Preview preview = new Preview.Builder().build();
                imageCapture   = new ImageCapture.Builder()
                        .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                        .setTargetRotation(previewView.getDisplay().getRotation())
                        .build();

                preview.setSurfaceProvider(previewView.getSurfaceProvider());

                provider.unbindAll();
                provider.bindToLifecycle(this,
                        CameraSelector.DEFAULT_BACK_CAMERA,
                        preview, imageCapture);

                shutterBtn.setEnabled(true);
                Log.d(TAG, "Camera bound and ready");

            } catch (ExecutionException | InterruptedException e) {
                Log.e(TAG, "Camera init failed", e);
                Toast.makeText(this, "Camera error", Toast.LENGTH_LONG).show();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void takePhoto() {
        if (imageCapture == null) {
            Toast.makeText(this, "Camera not ready", Toast.LENGTH_SHORT).show();
            return;
        }

        if (scannedNfcId == null || scannedNfcId.isEmpty()) {
            Toast.makeText(this, "Please scan an NFC tag first", Toast.LENGTH_LONG).show();
            return;
        }

        shutterBtn.setEnabled(false);

        playFlash(() -> {
            if (spinner != null) spinner.setVisibility(View.VISIBLE);
        });

        File dir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "photos");
        if (!dir.exists() && !dir.mkdirs()) {
            Log.e(TAG, "Failed to create photo directory");
            resetUI();
            Toast.makeText(this, "Failed to create photo directory", Toast.LENGTH_LONG).show();
            return;
        }

        File photoFile = new File(dir, "photo_" + System.currentTimeMillis() + ".jpg");
        ImageCapture.OutputFileOptions opts =
                new ImageCapture.OutputFileOptions.Builder(photoFile).build();

        imageCapture.takePicture(opts,
                ContextCompat.getMainExecutor(this),
                new ImageCapture.OnImageSavedCallback() {

                    @Override
                    public void onError(@NonNull ImageCaptureException exc) {
                        Log.e(TAG, "Photo capture failed", exc);
                        resetUI();
                        Toast.makeText(MainActivity.this,
                                "Photo capture failed: " + exc.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults result) {
                        Log.d(TAG, "Photo saved: " + photoFile.getAbsolutePath());
                        uploadToFirebase(photoFile);
                    }
                });
    }

    // Simplified camera photo upload
    private void uploadToFirebase(File photo) {
        ensureAuthenticatedThenUpload(() -> {
            try {
                File compressedPhoto = compressImage(photo);
                byte[] imageBytes = java.nio.file.Files.readAllBytes(compressedPhoto.toPath());

                uploadToFirebaseStorage(imageBytes, scannedNfcId,
                        () -> {
                            // Success callback
                            resetUI();
                            photo.delete();
                            compressedPhoto.delete();
                        },
                        () -> {
                            // Failure callback
                            resetUI();
                            compressedPhoto.delete();
                        }
                );

            } catch (Exception e) {
                Log.e(TAG, "Error processing camera image", e);
                Toast.makeText(this, "Error processing image for upload", Toast.LENGTH_LONG).show();
                resetUI();
            }
        });
    }

    private File compressImage(File originalFile) {
        try {
            // Get the original image orientation
            ExifInterface exif = new ExifInterface(originalFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            Log.d(TAG, "Original image orientation: " + orientation);

            // Load the image
            Bitmap bitmap = BitmapFactory.decodeFile(originalFile.getAbsolutePath());

            if (bitmap == null) {
                Log.e(TAG, "Failed to decode image file");
                return originalFile;
            }

            // Rotate the bitmap if needed
            Bitmap rotatedBitmap = rotateBitmapIfNeeded(bitmap, orientation);

            // If rotation created a new bitmap, recycle the original
            if (rotatedBitmap != bitmap) {
                bitmap.recycle();
            }

            // Create compressed file
            File compressedDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "compressed");
            if (!compressedDir.exists() && !compressedDir.mkdirs()) {
                Log.e(TAG, "Failed to create compressed directory");
                rotatedBitmap.recycle();
                return originalFile;
            }

            File compressedFile = new File(compressedDir, "compressed_" + originalFile.getName());

            // Compress and save
            FileOutputStream fos = new FileOutputStream(compressedFile);

            // Compress with 95% quality
            boolean success = rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, COMPRESSION_QUALITY, fos);

            fos.flush();
            fos.close();
            rotatedBitmap.recycle(); // Free memory

            if (!success) {
                Log.e(TAG, "Failed to compress image");
                return originalFile;
            }

            // Set the correct orientation in the compressed file's EXIF data
            ExifInterface compressedExif = new ExifInterface(compressedFile.getAbsolutePath());
            compressedExif.setAttribute(ExifInterface.TAG_ORIENTATION, String.valueOf(ExifInterface.ORIENTATION_NORMAL));
            compressedExif.saveAttributes();

            Log.d(TAG, "Image compressed successfully with correct orientation");
            return compressedFile;

        } catch (Exception e) {
            Log.e(TAG, "Error compressing image", e);
            return originalFile; // Return original if compression fails
        }
    }

    // Handle bitmap rotation
    private Bitmap rotateBitmapIfNeeded(Bitmap bitmap, int orientation) {
        Matrix matrix = new Matrix();

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                Log.d(TAG, "Rotating image 90 degrees");
                matrix.postRotate(90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                Log.d(TAG, "Rotating image 180 degrees");
                matrix.postRotate(180);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                Log.d(TAG, "Rotating image 270 degrees");
                matrix.postRotate(270);
                break;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                Log.d(TAG, "Flipping image horizontally");
                matrix.preScale(-1.0f, 1.0f);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                Log.d(TAG, "Flipping image vertically");
                matrix.preScale(1.0f, -1.0f);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                Log.d(TAG, "Transposing image");
                matrix.postRotate(90);
                matrix.preScale(-1.0f, 1.0f);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                Log.d(TAG, "Transversing image");
                matrix.postRotate(-90);
                matrix.preScale(-1.0f, 1.0f);
                break;
            case ExifInterface.ORIENTATION_NORMAL:
            default:
                Log.d(TAG, "Image orientation is normal, no rotation needed");
                return bitmap; // No rotation needed
        }

        try {
            Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            return rotatedBitmap;
        } catch (OutOfMemoryError e) {
            Log.e(TAG, "Out of memory error while rotating bitmap", e);
            return bitmap; // Return original if out of memory
        }
    }

    /* ───────────────────  UI HELPER METHODS  ─────────────────── */

    private void resetUI() {
        runOnUiThread(() -> {
            if (spinner != null) spinner.setVisibility(View.GONE);
            shutterBtn.setEnabled(true);
        });
    }

    private void playFlash(Runnable onFlashComplete) {
        if (flashView == null) {
            if (onFlashComplete != null) onFlashComplete.run();
            return;
        }

        flashView.setVisibility(View.VISIBLE);

        ValueAnimator anim = ValueAnimator.ofObject(
                new ArgbEvaluator(), Color.WHITE, Color.TRANSPARENT);

        anim.setDuration(150);
        anim.addUpdateListener(animator ->
                flashView.setBackgroundColor((int) animator.getAnimatedValue()));

        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                flashView.setVisibility(View.INVISIBLE);
                if (onFlashComplete != null) {
                    onFlashComplete.run();
                }
            }
        });

        anim.start();
    }
}